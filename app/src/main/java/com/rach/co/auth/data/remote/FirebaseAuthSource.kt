package com.rach.co.auth.data.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthSource(private val auth: FirebaseAuth) {

    suspend fun login(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun signup(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password).await()

    suspend fun forgotPassword(email: String) =
        auth.sendPasswordResetEmail(email).await()

    suspend fun googleSignIn(credential: AuthCredential) =
        auth.signInWithCredential(credential).await()

    fun logout() = auth.signOut()

    fun currentUser() = auth.currentUser

    fun hasUser() = auth.currentUser != null
}
