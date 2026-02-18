package com.rach.co.AUTH.domain.usecase

import com.rach.co.AUTH.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repo.login(email, password)
}

