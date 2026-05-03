package com.rach.co.auth.presentation.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
