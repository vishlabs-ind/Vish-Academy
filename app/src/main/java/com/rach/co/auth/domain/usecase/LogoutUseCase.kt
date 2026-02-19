package com.rach.co.auth.domain.usecase

import com.rach.co.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    operator fun invoke() {
        repo.logout()
    }
}
