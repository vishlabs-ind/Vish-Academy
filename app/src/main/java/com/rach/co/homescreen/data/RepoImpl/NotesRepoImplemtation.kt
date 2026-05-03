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

    }

    override suspend fun pdfSearchBar(query: String): List<NotesItems> {
        val list = mutableListOf<NotesItems>()

        try {
            val documents = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("chapterName", query.trim())
                .get()
                .await()

            if (documents.isEmpty) {
                Log.d("EMPTY", "No documents found")
                return emptyList()
            }

            Log.d("FIREBASE", "docs size = ${documents.size()}")

            for (doc in documents) {
                val data = doc.toObject(NotesItems::class.java)
                Log.d("FIREBASE", "doc data = ${doc.data}")
                list.add(data)
            }

        } catch (e: Exception) {
            Log.d("ERROR", e.message.toString())
        }

        return list
    }
}