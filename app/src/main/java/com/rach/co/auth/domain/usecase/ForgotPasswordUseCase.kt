package com.rach.co.auth.domain.usecase

import com.rach.co.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String) =
        repo.forgotPassword(email)
}
