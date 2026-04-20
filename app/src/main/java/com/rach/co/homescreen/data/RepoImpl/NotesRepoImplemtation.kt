package com.rach.co.homescreen.data.RepoImpl


import com.google.firebase.firestore.FirebaseFirestore
import com.rach.co.homescreen.data.DataClass.NotesItems
import com.rach.co.homescreen.domain.Repo.NoteRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRepoImplemtation @Inject constructor( val firestore: FirebaseFirestore): NoteRepository {
    override suspend fun getNotePdf(): List<NotesItems> {

        return try {

            val snap = firestore.collection("notes").document("Maths Youtube 1").get().await()
            val list = mutableListOf<NotesItems>()
//            list = snap.toObject(NotesItems::class.java)
            val data1 = snap.getString("chapterName") ?: "Character name is empty"
            val data2 = snap.getString("pdflink") ?: "link name is empty"
//            data
            list.add(NotesItems(data1,data2))
            list
        } catch (e: Exception) {
            emptyList<NotesItems>()
        }
    }

    override suspend fun getHindiNotePdf(): List<NotesItems> {
        return try {
            val snap = firestore.collection("notes").document("Hindi PYQ").get().await()
            val list = mutableListOf<NotesItems>()
//            list = snap.toObject(NotesItems::class.java)
            val data1 = snap.getString("chapterName") ?: "Character name is empty"
            val data2 = snap.getString("pdflink") ?: "link name is empty"
//            data
            list.add(NotesItems(data1,data2))
            list
        } catch (e: Exception) {
            emptyList<NotesItems>()
        }
    }

}