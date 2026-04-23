package com.rach.co.homescreen.data.RepoImpl


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.rach.co.homescreen.data.DataClass.NotesItems
import com.rach.co.homescreen.domain.Repo.NoteRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRepoImplemtation @Inject constructor(val firestore: FirebaseFirestore) : NoteRepository {
    override suspend fun getNotePdf(
        folderName: String
    ): List<NotesItems> {

        val result =
            FirebaseFirestore.getInstance()
                .collection("notes")
                .document(folderName)
                .get()
                .await()

        val list = mutableListOf<NotesItems>()


//        Log.d("FIRESTORE_DATA", doc.data.toString())


        val chapterName = result.getString("chapterName") ?: ""
        val link = result.getString("pdflink") ?: ""
//        Log.d("FIRESTORE_DATA_NAME", chapterName)
//        Log.d("FIRESTORE_DATA_LINK", link)
        val item = NotesItems(
            chapterName = chapterName,
            pdflinkchapterName = link
        )


        list.add(item)


    return list

}}