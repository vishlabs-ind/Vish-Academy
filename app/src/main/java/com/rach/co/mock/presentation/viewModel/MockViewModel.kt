package com.rach.co.mock.presentation.viewModel


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.co.mock.data.AnswerUtils
import com.rach.co.mock.data.dataClass.Subject
import com.rach.co.mock.data.repository.MockLocalRepository
import com.rach.co.mock.data.repository.MockResultEntity
import com.rach.co.mock.data.room.MockRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state sealed class
sealed class MockScreenState {
    object Loading : MockScreenState()
    object Success : MockScreenState()
    data class Error(val message: String) : MockScreenState()
}

@HiltViewModel
class MockViewModel @Inject constructor(
    private val localRepository: MockLocalRepository,
    private val roomRepository: MockRoomRepository
) : ViewModel() {

    // --- Subject Selection States ---
    private val _subjects = mutableStateOf<List<Subject>>(emptyList())
    val subjects: State<List<Subject>> = _subjects

    private val _subjectLoadState = mutableStateOf<MockScreenState>(MockScreenState.Loading)
    val subjectLoadState: State<MockScreenState> = _subjectLoadState

    // --- Mock Exam States ---
    private val _subject = mutableStateOf<Subject?>(null)
    val subject: State<Subject?> = _subject

    private val _examLoadState = mutableStateOf<MockScreenState>(MockScreenState.Loading)
    val examLoadState: State<MockScreenState> = _examLoadState

    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    // timer
    private val _timeLeftSeconds = mutableStateOf(0L)
    val timeLeftSeconds: State<Long> = _timeLeftSeconds

    // total elapsed time for result screen
    private var totalDurationSeconds = 0L
    private val _timeTakenSeconds = mutableStateOf(0L)
    val timeTakenSeconds: State<Long> = _timeTakenSeconds

    // submit states
    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting

    private val _isSubmitted = mutableStateOf(false)
    val isSubmitted: State<Boolean> = _isSubmitted

    private val _submitError = mutableStateOf<String?>(null)
    val submitError: State<String?> = _submitError

    // result id from Room after save — used for review navigation
    private val _savedResultId = mutableStateOf<Int?>(null)
    val savedResultId: State<Int?> = _savedResultId

    // selected answers — key: questionIndex, value: selectedOptionIndex
    private val _selectedAnswers = mutableStateMapOf<Int, Int>()
    val selectedAnswers: Map<Int, Int> = _selectedAnswers

    // warning dialog at 30 seconds
    private var warningShown = false
    private val _showWarningDialog = mutableStateOf(false)
    val showWarningDialog: State<Boolean> = _showWarningDialog

    private var timerJob: Job? = null

    private var allSubjects: List<Subject> = emptyList()

    // search query
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    // SUBJECT SELECTION SCREEN

    fun loadAllSubjects() {
        viewModelScope.launch {

            _subjectLoadState.value = MockScreenState.Loading

            val subjects = localRepository.getAllSubjects()

            if (subjects.isEmpty()) {
                _subjectLoadState.value =
                    MockScreenState.Error("No subjects found")
                return@launch
            }

            // store original list
            allSubjects = subjects

            // show all initially
            _subjects.value = subjects

            _subjectLoadState.value = MockScreenState.Success

            Log.d("MockViewModel", "✅ Loaded ${subjects.size} subjects")
        }
    }

    // MOCK EXAM SCREEN

    fun loadSubject(subjectId: String) {
        viewModelScope.launch {
            _examLoadState.value = MockScreenState.Loading

            val subject = localRepository.getSubjectById(subjectId)

            if (subject == null) {
                _examLoadState.value = MockScreenState.Error("Failed to load subject")
                return@launch
            }

            _subject.value = subject
            _examLoadState.value = MockScreenState.Success
            Log.d("MockViewModel", "✅ Subject loaded: ${subject.subjectTitle}")
            startTimer()
        }
    }

    // single select — overwrite previous answer
    fun selectAnswer(optionIndex: Int) {
        _selectedAnswers[_currentIndex.value] = optionIndex
        Log.d("MockViewModel", "Answer — Q${_currentIndex.value}: $optionIndex")
    }

    // navigation
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
        val durationSeconds = (_subject.value?.examDurationMinutes ?: 120) * 60L
        totalDurationSeconds = durationSeconds
        _timeLeftSeconds.value = durationSeconds
        warningShown = false

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeftSeconds.value > 0) {
                delay(1000L)
                _timeLeftSeconds.value--

                // one time warning at 30 seconds
                if (_timeLeftSeconds.value == 30L && !warningShown) {
                    warningShown = true
                    _showWarningDialog.value = true
                }
            }
            // time up — auto save
            Log.d("MockViewModel", "⏱ Time up — auto saving")
            submitMock()
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

    // submit mock — save to Room
    suspend fun submitMock(): Result<MockResultEntity> {
        if (_isSubmitted.value) {
            Log.d("MockViewModel", "Already submitted — skipping")
            return Result.failure(Exception("Already submitted"))
        }

        timerJob?.cancel()
        _isSubmitting.value = true
        _submitError.value = null

        // calculate time taken
        val timeTaken = totalDurationSeconds - _timeLeftSeconds.value
        _timeTakenSeconds.value = timeTaken

        val score = calculateScore()
        val totalQuestions = _subject.value?.questions?.size ?: 0

        // encode answers — -1 for skipped
        val encodedAnswers = AnswerUtils.encodeAnswers(_selectedAnswers, totalQuestions)

        val entity = MockResultEntity(
            subjectId = _subject.value?.subjectId ?: "",
            subjectTitle = _subject.value?.subjectTitle ?: "",
            score = score,
            timeTakenSeconds = timeTaken,
            answers = encodedAnswers
        )

        val saveResult = roomRepository.saveResult(entity)

        return saveResult.fold(
            onSuccess = { id ->
                _savedResultId.value = id
                _isSubmitted.value = true
                _isSubmitting.value = false
                Log.d("MockViewModel", "✅ Mock saved — id: $id, score: $score")
                Result.success(entity.copy(id = id))
            },
            onFailure = {
                _isSubmitting.value = false
                _submitError.value = "Failed to save. Please try again"
                Log.e("MockViewModel", "❌ Save failed: ${it.message}")
                Result.failure(it)
            }
        )
    }

    fun onSearchQueryChanged(query: String) {

        _searchQuery.value = query

        _subjects.value =
            if (query.isBlank()) {

                allSubjects

            } else {

                allSubjects.filter { subject ->

                    subject.subjectTitle.contains(
                        query,
                        ignoreCase = true
                    )
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        Log.d("MockViewModel", "ViewModel cleared")
    }
}