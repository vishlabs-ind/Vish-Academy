package com.rach.co.homescreen.domain.Repo


import com.rach.co.homescreen.data.DataClass.NotesItems

interface NoteRepository {

    suspend fun getNotePdf():List<NotesItems>
    suspend fun getHindiNotePdf():List<NotesItems>
}