package com.rach.co.mock.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.co.mock.data.AnswerUtils
import com.rach.co.mock.data.dataClass.Question
import com.rach.co.mock.data.repository.MockLocalRepository
import com.rach.co.mock.data.room.MockRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MockReviewViewModel @Inject constructor(
    private val roomRepository: MockRoomRepository,
    private val localRepository: MockLocalRepository
) : ViewModel() {

    private val _questions = mutableStateOf<List<Question>>(emptyList())
    val questions: State<List<Question>> = _questions

    // key: questionIndex, value: selectedOptionIndex (-1 = skipped)
    private val _userAnswers = mutableStateOf<Map<Int, Int>>(emptyMap())
    val userAnswers: State<Map<Int, Int>> = _userAnswers

    private val _subjectTitle = mutableStateOf("")
    val subjectTitle: State<String> = _subjectTitle

    private val _screenState = mutableStateOf<MockScreenState>(MockScreenState.Loading)
    val screenState: State<MockScreenState> = _screenState

    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    fun loadReview(resultId: Int) {
        viewModelScope.launch {
            _screenState.value = MockScreenState.Loading

            // Step 1 — fetch result from Room
            val resultData = roomRepository.getResultById(resultId)

            if (resultData.isFailure) {
                Log.e("MockReview", "❌ Failed to fetch result")
                _screenState.value = MockScreenState.Error("Failed to load review")
                return@launch
            }

            val result = resultData.getOrNull()!!
            Log.d("MockReview", "✅ Result fetched — subjectId: ${result.subjectId}")

            // Step 2 — load questions from JSON
            val subject = localRepository.getSubjectById(result.subjectId)

            if (subject == null) {
                Log.e("MockReview", "❌ Subject not found: ${result.subjectId}")
                _screenState.value = MockScreenState.Error("Failed to load questions")
                return@launch
            }

            // Step 3 — decode answers
            val decodedAnswers = AnswerUtils.decodeAnswers(result.answers)

            _questions.value = subject.questions
            _userAnswers.value = decodedAnswers
            _subjectTitle.value = result.subjectTitle
            _screenState.value = MockScreenState.Success

            Log.d("MockReview", "✅ Questions: ${subject.questions.size}")
            Log.d("MockReview", "✅ Answers: $decodedAnswers")
        }
    }


    fun nextQuestion() {
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value++
        }
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
        }
    }
}