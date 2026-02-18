package com.rach.co.AUTH.domain.usecase

import com.rach.co.AUTH.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repo.signup(email, password)
}
