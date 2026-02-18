package com.rach.co.AUTH.data.repository


import com.google.firebase.auth.AuthCredential
import com.rach.co.AUTH.data.remote.FirebaseAuthSource
import com.rach.co.AUTH.domain.repository.AuthRepository
import com.rach.co.AUTH.utils.Response
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val source: FirebaseAuthSource
) : AuthRepository {

    override suspend fun login(email: String, password: String) = flow {
        emit(Response.Loading())
        try {
            emit(Response.Success(source.login(email, password)))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

    override suspend fun signup(email: String, password: String) = flow {
        emit(Response.Loading())
        try {
            emit(Response.Success(source.signup(email, password)))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

    override suspend fun forgotPassword(email: String) = flow {
        emit(Response.Loading())
        try {
            source.forgotPassword(email)
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

    override suspend fun googleSignIn(credential: AuthCredential) = flow {
        emit(Response.Loading())
        try {
            emit(Response.Success(source.googleSignIn(credential)))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

    override fun logout() = source.logout()

    override fun hasUser(): Boolean = source.hasUser()

    override fun sendEmailVerification() {
        source.currentUser()?.sendEmailVerification()
    }

    override suspend fun isEmailVerified(): Boolean {
        val user = source.currentUser()
        user?.reload()?.await()
        return user?.isEmailVerified ?: false
    }

}
