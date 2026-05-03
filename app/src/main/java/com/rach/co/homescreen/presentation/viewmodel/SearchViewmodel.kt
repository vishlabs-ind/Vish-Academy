package com.rach.co.homescreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.domain.Repo.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CourseRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query

    private val _results = MutableStateFlow<List<Course>>(emptyList())
    val results: StateFlow<List<Course>> = _results

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    init {
        observeSearch()
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _query
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { text ->

                    if (text.isBlank()) {
                        _isSearching.value = false
                        _results.value = emptyList()
                    } else {
                        _isSearching.value = true

                        val result = repository.searchCourses(text)

                        _results.value = result
                    }
                }
        }
    }

    fun updateQuery(text: String) {
        _query.value = text
    }
}