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


    fun viewAllNotes() {

        viewModelScope.launch {

            val folderList =
                listOf(
                    "Hindi PYQ",
                    "Maths Youtube 1"
                )

            val allPdfList =
                mutableListOf<NotesItems>()

            for(folder in folderList){

                val data =
                    noteRepository.getNotePdf(folder)

                allPdfList.addAll(data)

            }

            _notesPdf.value =
                allPdfList

        }

    }

//    // single function for all folders
//    fun viewNotes(folderName: String) {
//
//        viewModelScope.launch {
//
//            val data = noteRepository.getNotePdf(folderName)
//
//            _notesPdf.value = data
//
//            Log.d("PDF_DATA", data.toString())
//        }
//    }
}