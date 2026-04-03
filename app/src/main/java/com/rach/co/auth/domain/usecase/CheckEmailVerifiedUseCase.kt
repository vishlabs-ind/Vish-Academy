package com.rach.co.auth.domain.usecase

import com.rach.co.auth.domain.repository.AuthRepository
import javax.inject.Inject

class CheckEmailVerifiedUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return repo.isEmailVerified()
    }
}

