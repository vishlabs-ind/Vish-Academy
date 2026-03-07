package com.rach.co.homescreen.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.rach.co.homescreen.data.DataClass.Course
import androidx.compose.runtime.State
import com.rach.co.homescreen.data.Repository.QuizRepository
import com.rach.co.homescreen.data.local.QuizLocalDataSource

class QuizCategoryViewModel(application: Application) : AndroidViewModel(application) {

    // Create Repository manually (no Hilt for now)
    private val localDataSource = QuizLocalDataSource(application)
    private val repository = QuizRepository(localDataSource)

    // -------------------------
    // UI States
    // -------------------------

    private val _courseList = mutableStateOf<List<Course>>(emptyList())
    val courseList: State<List<Course>> = _courseList

    private val _isDialogOpen = mutableStateOf(false)
    val isDialogOpen: State<Boolean> = _isDialogOpen

    private val _selectedCourse = mutableStateOf<Course?>(null)
    val selectedCourse: State<Course?> = _selectedCourse

    // -------------------------
    // Load Courses
    // -------------------------

    fun loadCourses() {
        _courseList.value = repository.getAllCourses()
    }

    // -------------------------
    // Dialog Controls
    // -------------------------

    fun openDialog() {
        _isDialogOpen.value = true
    }

    fun closeDialog() {
        _isDialogOpen.value = false
    }

    // -------------------------
    // Course Selection
    // -------------------------

    fun selectCourse(course: Course) {
        _selectedCourse.value = course
        closeDialog()
    }
}