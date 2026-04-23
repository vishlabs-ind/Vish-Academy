package com.rach.co.homescreen.data.Remote


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {



    suspend fun addCourseToUser(courseId: String) {

        val uid = auth.currentUser?.uid
            ?: throw Exception("User not logged in")

        val email = auth.currentUser?.email


        firestore
            .collection("users")
            .document(uid)
            .set(
                mapOf(
                    "email" to email,
                    "coursePurchased"
                            to FieldValue.arrayUnion(courseId)
                ),
                com.google.firebase.firestore.SetOptions.merge()
            )
            .await()
    }


    suspend fun addpremiumtouser(){
        val uid = auth.currentUser?.uid
            ?: throw Exception("User not logged in")
        val email = auth.currentUser?.email

        firestore
            .collection("users")
            .document(uid)
            .set(
                mapOf(
                    "email" to email,
                    "premium" to true
                ),
                com.google.firebase.firestore.SetOptions.merge()
            )
            .await()


    }
}