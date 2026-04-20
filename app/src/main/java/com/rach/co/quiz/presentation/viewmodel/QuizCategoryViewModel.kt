package com.rach.co.quiz.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.quiz.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizCategoryViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    // UI States
    private val _courseList = mutableStateOf<List<Course>>(emptyList())
    val courseList: State<List<Course>> = _courseList

    private val _isDialogOpen = mutableStateOf(false)
    val isDialogOpen: State<Boolean> = _isDialogOpen

    private val _selectedCourse = mutableStateOf<Course?>(null)
    val selectedCourse: State<Course?> = _selectedCourse

    // Load Courses

    fun loadCourses() {
        _courseList.value = repository.getAllCourses()
    }

    // Dialog Controls

    fun openDialog() {
        _isDialogOpen.value = true
    }

    fun closeDialog() {
        _isDialogOpen.value = false
    }

    // Course Selection

    fun selectCourse(course: Course) {
        _selectedCourse.value = course
        closeDialog()
    }
}