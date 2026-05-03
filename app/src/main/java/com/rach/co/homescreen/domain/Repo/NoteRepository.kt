package com.rach.co.homescreen.domain.Repo


import com.rach.co.homescreen.data.DataClass.NotesItems

interface NoteRepository {

    suspend fun getNotePdf(folderName: String):List<NotesItems>

    suspend fun pdfSearchBar(query: String):List<NotesItems>
}