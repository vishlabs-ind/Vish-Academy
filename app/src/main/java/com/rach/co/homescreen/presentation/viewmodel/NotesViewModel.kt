package com.rach.co.homescreen.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.co.homescreen.data.DataClass.NotesItems
import com.rach.co.homescreen.domain.Repo.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor( val noteRepository: NoteRepository): ViewModel(){


    private val _notesPdf = MutableStateFlow<List<NotesItems>>(emptyList())

    val notesPdf: StateFlow<List<NotesItems>> = _notesPdf

    private val _pdfSearch = MutableStateFlow<List<NotesItems>>(emptyList())

    val pdfSearch: StateFlow<List<NotesItems>> = _pdfSearch


    fun viewAllNotes() {

        viewModelScope.launch {

            try {

                val data = noteRepository.getNotePdf()

                _notesPdf.value = data

            } catch (e: Exception) {

                Log.e("VIEWMODEL_ERROR", "Error fetching notes", e)

                _notesPdf.value = emptyList() // fallback

                // Optional: error state bhi bana sakte ho
                // _errorState.value = "Something went wrong"
            }
        }
    }


    fun pdfSearchBar(query: String) {

        val searchText = query.trim()

        _pdfSearch.value = if (searchText.isEmpty()) {
            _notesPdf.value
        } else {
            _notesPdf.value.filter { item ->
                item.chapterName
                    ?.contains(searchText, ignoreCase = true) == true
            }
        }
    }}