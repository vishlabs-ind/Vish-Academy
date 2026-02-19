package com.rach.co.auth.presentation.home

import androidx.lifecycle.ViewModel
import com.rach.co.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    fun logout() {
        repo.logout()
    }
}
