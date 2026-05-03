package com.rach.co.exam.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.rach.co.exam.data.dataClass.ExamCourse
import com.rach.co.exam.data.dataClass.ExamResult
import com.rach.co.exam.data.dataClass.Subject
import com.rach.co.exam.data.repository.ExamFirestoreRepository
import com.rach.co.exam.data.repository.ExamLocalRepository
import com.rach.co.exam.data.repository.ExamRealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

// sealed class for exam access check result
sealed class ExamAccessResult {
    object Allowed : ExamAccessResult()
    data class NotStartedYet(val startTime: String) : ExamAccessResult()
    data class Ended(val endTime: String) : ExamAccessResult()
    object AlreadyAttempted : ExamAccessResult()
}

// sealed class for screen UI state
sealed class ExamScreenState {
    object Loading : ExamScreenState()
    object Success : ExamScreenState()
    data class Error(val message: String) : ExamScreenState()
}

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val firestoreRepository: ExamFirestoreRepository,
    private val realtimeRepository: ExamRealtimeRepository,
    private val localRepository: ExamLocalRepository
) : ViewModel() {

    // --- SubjectSelectionScreen States ---
    private val _examCourses = mutableStateListOf<ExamCourse>()
    val examCourses: List<ExamCourse> = _examCourses

    private val _selectionScreenState = mutableStateOf<ExamScreenState>(ExamScreenState.Loading)
    val selectionScreenState: State<ExamScreenState> = _selectionScreenState

    // stores courseIds that user already attempted
    private val _attemptedCourseIds = mutableStateOf<Set<String>>(emptySet())
    val attemptedCourseIds: State<Set<String>> = _attemptedCourseIds

    // --- ExamScreen States ---
    private val _subject = mutableStateOf<Subject?>(null)
    val subject: State<Subject?> = _subject

    private val _examLoadState = mutableStateOf<ExamScreenState>(ExamScreenState.Loading)
    val examLoadState: State<ExamScreenState> = _examLoadState

    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    private val _timeLeftSeconds = mutableStateOf(0L)
    val timeLeftSeconds: State<Long> = _timeLeftSeconds

    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting

    private val _isSubmitted = mutableStateOf(false)
    val isSubmitted: State<Boolean> = _isSubmitted

    private val _submitError = mutableStateOf<String?>(null)
    val submitError: State<String?> = _submitError

    // stores selected answer per question
    private val _selectedAnswers = mutableStateMapOf<Int, Int>()
    val selectedAnswers: Map<Int, Int> = _selectedAnswers

    // timer warning shown flag
    private var warningShown = false
    private val _showWarningDialog = mutableStateOf(false)
    val showWarningDialog: State<Boolean> = _showWarningDialog

    private var timerJob: Job? = null

    // ─────────────────────────────────────────
    // SUBJECT SELECTION SCREEN FUNCTIONS
    // ─────────────────────────────────────────

    // Step 1 — fetch enabled courses from Firestore
    // Step 2 — check already attempted for each course
    fun loadEnabledCourses() {
        viewModelScope.launch {
            _selectionScreenState.value = ExamScreenState.Loading

            val result = firestoreRepository.getEnabledExamCourses()

            result.fold(
                onSuccess = { courses ->
                    if (courses.isEmpty()) {
                        _selectionScreenState.value = ExamScreenState.Error("No exams available right now")
                        return@launch
                    }
                    _examCourses.clear()
                    _examCourses.addAll(courses)

                    // after courses loaded check already attempted
                    checkAlreadyAttempted(courses)
                },
                onFailure = {
                    _selectionScreenState.value = ExamScreenState.Error("Something went wrong")
                }
            )
        }
    }

    // check all courses at once
    private suspend fun checkAlreadyAttempted(courses: List<ExamCourse>) {
        val attemptedIds = mutableSetOf<String>()

        courses.forEach { course ->
            val attempted = realtimeRepository.hasAlreadyAttempted(course.courseId)
            if (attempted) {
                attemptedIds.add(course.courseId)
                Log.d("ExamViewModel", "Already attempted: ${course.courseId}")
            }
        }

        _attemptedCourseIds.value = attemptedIds
        // only set success after both checks complete
        _selectionScreenState.value = ExamScreenState.Success
    }

    // check time when user taps Start Exam
    fun checkExamAccess(course: ExamCourse): ExamAccessResult {
        val now = Timestamp.now()
        val start = course.startTime
        val end = course.endTime
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        if (start == null || end == null) return ExamAccessResult.NotStartedYet("N/A")

        return when {
            now < start -> ExamAccessResult.NotStartedYet(dateFormat.format(start.toDate()))
            now > end -> ExamAccessResult.Ended(dateFormat.format(end.toDate()))
            else -> ExamAccessResult.Allowed
        }
    }

    // ─────────────────────────────────────────
    // EXAM SCREEN FUNCTIONS
    // ─────────────────────────────────────────

    // load questions from local JSON
    fun loadSubject(subjectId: String) {
        viewModelScope.launch {
            _examLoadState.value = ExamScreenState.Loading

            val subject = localRepository.getSubjectById(subjectId)

            if (subject == null) {
                _examLoadState.value = ExamScreenState.Error("Failed to load exam")
                return@launch
            }

            _subject.value = subject
            _examLoadState.value = ExamScreenState.Success
            Log.d("ExamViewModel", "✅ Subject loaded: ${subject.subjectTitle}")
            startTimer()
        }
    }

    // single select — overwrite previous answer
    fun selectAnswer(optionIndex: Int) {
        _selectedAnswers[_currentIndex.value] = optionIndex
        Log.d("ExamViewModel", "Answer selected — Q${_currentIndex.value}: $optionIndex")
    }

    // question navigation
    fun nextQuestion() {
        val total = _subject.value?.questions?.size ?: 0
        if (_currentIndex.value < total - 1) _currentIndex.value++
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) _currentIndex.value--
    }

    fun isLastQuestion(): Boolean {
        val total = _subject.value?.questions?.size ?: 0
        return _currentIndex.value == total - 1
    }

    // countdown timer
    private fun startTimer() {
        val durationSeconds = (_subject.value?.examDurationMinutes ?: 0) * 60L
        _timeLeftSeconds.value = durationSeconds
        warningShown = false

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeftSeconds.value > 0) {
                delay(1000L)
                _timeLeftSeconds.value--

                // show one time warning at 30 seconds
                if (_timeLeftSeconds.value == 30L && !warningShown) {
                    warningShown = true
                    _showWarningDialog.value = true
                }
            }
            // time up — auto submit
            Log.d("ExamViewModel", "⏱ Time up — auto submitting")
            submitExam()
        }
    }

    fun dismissWarningDialog() {
        _showWarningDialog.value = false
    }

    // calculate score
    fun calculateScore(): Int {
        val questions = _subject.value?.questions ?: return 0
        return questions.filterIndexed { index, question ->
            _selectedAnswers[index] == question.correctAnswerIndex
        }.size
    }

    // submit exam — await Firebase before navigating
    suspend fun submitExam(): Result<ExamResult> {
        if (_isSubmitted.value) {
            Log.d("ExamViewModel", "Already submitted — skipping")
            return Result.failure(Exception("Already submitted"))
        }

        timerJob?.cancel()
        _isSubmitting.value = true
        _submitError.value = null

        val score = calculateScore()
        val result = ExamResult(
            subjectId = _subject.value?.subjectId ?: "",
            subjectTitle = _subject.value?.subjectTitle ?: "",
            score = score,
            totalQuestions = _subject.value?.questions?.size ?: 0,
            submittedAt = System.currentTimeMillis()
        )

        val saveResult = realtimeRepository.saveResult(result)

        return saveResult.fold(
            onSuccess = {
                _isSubmitted.value = true
                _isSubmitting.value = false
                Log.d("ExamViewModel", "✅ Exam submitted — score: $score")
                Result.success(result)
            },
            onFailure = {
                _isSubmitting.value = false
                _submitError.value = "Failed to submit. Please try again"
                Log.e("ExamViewModel", "❌ Submit failed: ${it.message}")
                Result.failure(it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        Log.d("ExamViewModel", "ViewModel cleared")
    }
}