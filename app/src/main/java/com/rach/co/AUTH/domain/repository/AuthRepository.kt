package com.rach.co.AUTH.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.rach.co.AUTH.utils.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(email: String, password: String): Flow<Response<AuthResult?>>

    suspend fun signup(email: String, password: String): Flow<Response<AuthResult?>>

    suspend fun forgotPassword(email: String): Flow<Response<Boolean>>

    suspend fun googleSignIn(credential: AuthCredential): Flow<Response<AuthResult?>>

    fun logout()

    fun hasUser(): Boolean

    fun sendEmailVerification()

    suspend fun isEmailVerified(): Boolean


}
