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

    private val _notesPdf= MutableStateFlow<List<NotesItems>>(emptyList())

    val notesPdf: StateFlow<List<NotesItems>> =_notesPdf

    private val _notesPdfHindi= MutableStateFlow<List<NotesItems>>(emptyList())

    val notesPdfHindi: StateFlow<List<NotesItems>> =_notesPdfHindi

    fun viewNotes(){
        viewModelScope.launch {
            val data=  noteRepository.getNotePdf()
            _notesPdf.value=data


        }
    }

    fun viewHindiNotes(){
        viewModelScope.launch {
            val data=  noteRepository.getHindiNotePdf()
            _notesPdfHindi.value=data


        }
    }
}