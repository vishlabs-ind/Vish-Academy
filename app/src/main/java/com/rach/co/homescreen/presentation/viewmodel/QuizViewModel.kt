package com.rach.co.homescreen.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.data.Repository.QuizRepository
import com.rach.co.homescreen.data.local.QuizLocalDataSource

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val localDataSource = QuizLocalDataSource(application)
    private val repository = QuizRepository(localDataSource)

    private val _course = mutableStateOf<Course?>(null)
    val course: State<Course?> = _course

    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    fun loadCourse(courseId: String) {
        _course.value = repository.getCourseById(courseId)
    }

    fun nextQuestion() {
        val total = _course.value?.questions?.size ?: 0
        if (_currentQuestionIndex.value < total - 1) {
            _currentQuestionIndex.value++
        }
    }

    fun isLastQuestion(): Boolean {
        val total = _course.value?.questions?.size ?: 0
        return _currentQuestionIndex.value == total - 1
    }
    fun checkAnswer(selectedIndex: Int) {

        val question = _course.value?.questions?.get(_currentQuestionIndex.value)
        if (question?.correctAnswerIndex == selectedIndex) {
            _score.value++
        }
    }
}