package com.rach.co.homescreen.data.RepoImpl


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.LazyStringArrayList.emptyList
import com.rach.co.homescreen.data.DataClass.NotesItems
import com.rach.co.homescreen.domain.Repo.NoteRepository
import kotlinx.coroutines.tasks.await
import java.util.Collections.list
import javax.inject.Inject

class NotesRepoImplemtation @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {

    override suspend fun getNotePdf(): List<NotesItems> {

        return try {

            val result = firestore
                .collection("notes")
                .get()
                .await()

            val list = mutableListOf<NotesItems>()

            for (doc in result.documents) {

                val chapterName = doc.getString("chapterName") ?: ""
                val link = doc.getString("pdflink") ?: ""

                // Invalid data skip
                if (chapterName.isBlank() || link.isBlank()) continue

                val item = NotesItems(
                    chapterName = chapterName,
                    pdflinkchapterName = link
                )

                list.add(item)
            }

            list

        } catch (e: Exception) {


        return emptyList<NotesItems>()

    }

    }


}