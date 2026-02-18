package com.rach.co.AUTH.presentation.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.rach.co.AUTH.domain.usecase.CheckEmailVerifiedUseCase
import com.rach.co.AUTH.domain.usecase.ForgotPasswordUseCase
import com.rach.co.AUTH.domain.usecase.GoogleSignInUseCase
import com.rach.co.AUTH.domain.usecase.LoginUseCase
import com.rach.co.AUTH.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collect


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val checkEmailVerifiedUseCase: CheckEmailVerifiedUseCase

) : ViewModel() {

    var state by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String) {
        state = state.copy(email = email, error = null)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password, error = null)
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {

            forgotPasswordUseCase(email)
                .collect { result ->

                    state = when (result) {

                        is Response.Loading ->
                            state.copy(loading = true, error = null)

                        is Response.Success ->
                            state.copy(loading = false)

                        is Response.Error ->
                            state.copy(
                                loading = false,
                                error = result.error?.message
                            )
                    }
                }
        }
    }

    fun googleLogin(credential: AuthCredential) {
        viewModelScope.launch {
            googleSignInUseCase(credential).collect { result ->

                state = when (result) {

                    is Response.Loading ->
                        state.copy(loading = true)

                    is Response.Success ->
                        state.copy(loading = false, success = true)

                    is Response.Error ->
                        state.copy(
                            loading = false,
                            error = result.error?.message
                        )
                }
            }
        }
    }

    fun onError(message: String) {
        state = state.copy(
            error = message,
            loading = false
        )
    }




    fun login() {
        viewModelScope.launch {

            loginUseCase(state.email, state.password)
                .collect { result ->

                    val currentState = state

                    state = when (result) {

                        is Response.Loading ->
                            currentState.copy(
                                loading = true,
                                success = false,
                                error = null
                            )

                        is Response.Success -> {

                            val isVerified = checkEmailVerifiedUseCase()

                            if (!isVerified) {
                                currentState.copy(
                                    loading = false,
                                    success = false,
                                    error = "Please verify your email first"
                                )
                            } else {
                                currentState.copy(
                                    loading = false,
                                    success = true,
                                    error = null
                                )
                            }
                        }

                        is Response.Error ->
                            currentState.copy(
                                loading = false,
                                success = false,
                                error = result.error?.message
                            )
                    }
                }
        }
    }

}
