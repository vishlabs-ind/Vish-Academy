package com.rach.co.quiz.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.quiz.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _course = mutableStateOf<Course?>(null)
    val course: State<Course?> = _course

    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    private val _selectedAnswers = mutableStateMapOf<Int, Int>()
    val selectedAnswers: Map<Int, Int> = _selectedAnswers


    fun loadCourse(courseId: String) {
        _course.value = repository.getCourseById(courseId)
    }

    fun nextQuestion() {
        val total = _course.value?.questions?.size ?: 0
        if (_currentQuestionIndex.value < total - 1) {
            _currentQuestionIndex.value++
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value = currentQuestionIndex.value - 1
        }
    }

    fun isLastQuestion(): Boolean {
        val total = _course.value?.questions?.size ?: 0
        return _currentQuestionIndex.value == total - 1
    }
    fun checkAnswer(selectedIndex: Int) {
        val questionIndex = currentQuestionIndex.value
        _selectedAnswers[questionIndex] = selectedIndex
    }

    fun calculateScore() {
        val questions = _course.value?.questions ?: return
        var count = 0

        questions.forEachIndexed { index, question ->
            val selectedAnswer = _selectedAnswers[index]  // Int?
            if (selectedAnswer == question.correctAnswerIndex) {
                count++
            }
        }
        _score.value = count
    }
}