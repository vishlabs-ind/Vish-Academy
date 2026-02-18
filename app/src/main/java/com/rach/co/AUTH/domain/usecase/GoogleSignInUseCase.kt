package com.rach.co.AUTH.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.rach.co.AUTH.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(credential: AuthCredential) =
        repo.googleSignIn(credential)
}
