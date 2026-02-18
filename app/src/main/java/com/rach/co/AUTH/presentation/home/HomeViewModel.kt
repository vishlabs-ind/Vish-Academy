package com.rach.co.AUTH.presentation.home

import androidx.lifecycle.ViewModel
import com.rach.co.AUTH.domain.repository.AuthRepository
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
