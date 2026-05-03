package com.rach.co.auth.presentation.signup

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
