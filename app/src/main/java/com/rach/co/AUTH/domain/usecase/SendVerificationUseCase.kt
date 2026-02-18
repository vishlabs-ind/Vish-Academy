package com.rach.co.AUTH.domain.usecase

import com.rach.co.AUTH.domain.repository.AuthRepository
import javax.inject.Inject

class SendVerificationUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    operator fun invoke() = repo.sendEmailVerification()
}
