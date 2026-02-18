package com.rach.co.AUTH.presentation.signup


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.co.AUTH.domain.usecase.LogoutUseCase
import com.rach.co.AUTH.domain.usecase.SendVerificationUseCase
import com.rach.co.AUTH.domain.usecase.SignUpUseCase
import com.rach.co.AUTH.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val sendVerificationUseCase: SendVerificationUseCase,
    private val logoutUseCase: LogoutUseCase


) : ViewModel() {

    var state by mutableStateOf(SignUpUiState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword)
    }

    fun signup() {

        if (state.email.isBlank() ||
            state.password.isBlank() ||
            state.confirmPassword.isBlank()
        ) {
            state = state.copy(error = "Fields cannot be empty")
            return
        }

        if (state.password != state.confirmPassword) {
            state = state.copy(error = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            signUpUseCase(state.email, state.password)
                .collect { result ->

                    state = when (result) {

                        is Response.Loading ->
                            state.copy(loading = true, error = null)

                        is Response.Success -> {

                            sendVerificationUseCase()

                            logoutUseCase()

                            state.copy(
                                loading = false,
                                success = true
                            )
                        }


                        is Response.Error ->
                            state.copy(
                                loading = false,
                                error = result.error?.message
                            )
                    }
                }
        }
    }
}
