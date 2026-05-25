package com.rach.co.homescreen.data.RepoImpl

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.rach.co.homescreen.data.DataClass.NotesItems
import com.rach.co.homescreen.domain.Repo.NoteRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRepoImplemtation @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {

    private var lastDocument: DocumentSnapshot? = null

    override suspend fun getNotePdf(): List<NotesItems> {

        return try {

            val result = firestore
                .collection("notes")
                .limit(10)
                .get()
                .await()

            lastDocument = result.documents.lastOrNull()

            result.documents.mapNotNull { doc ->

                val chapterName = doc.getString("chapterName") ?: return@mapNotNull null
                val link = doc.getString("pdflink") ?: return@mapNotNull null

                NotesItems(
                    chapterName = chapterName,
                    pdflinkchapterName = link
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun loadNextPage(): List<NotesItems> {

        return try {

            val lastDoc = lastDocument ?: return emptyList()

            val result = firestore
                .collection("notes")
                .startAfter(lastDoc)
                .limit(10)
                .get()
                .await()

            lastDocument = result.documents.lastOrNull()

            result.documents.mapNotNull { doc ->

                val chapterName = doc.getString("chapterName") ?: return@mapNotNull null
                val link = doc.getString("pdflink") ?: return@mapNotNull null

                NotesItems(
                    chapterName = chapterName,
                    pdflinkchapterName = link
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }
}