package com.rach.co.homescreen.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rach.co.homescreen.data.Model.AppDatabase
import com.rach.co.homescreen.domain.Repo.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val database: AppDatabase, // Hilt injects this automatically
    private val repository: CourseRepository // Or your repo
) : ViewModel() {

//    fun logout(onComplete: () -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            // logic for deleting Room data
//            database.clearAllTables()
//
//            // Firebase sign out
//            FirebaseAuth.getInstance().signOut()
//
//            withContext(Dispatchers.Main) {
//                onComplete()
//            }
//        }
//    }
fun logout(context: Context, onComplete: () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        // 1. Clear Room database
        database.clearAllTables()

        // 2. Clear SharedPreferences
        context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
            .edit().clear().apply()

        // Clear any other prefs your app uses
        context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
            .edit().clear().apply()

        // 3. Firebase sign out
        FirebaseAuth.getInstance().signOut()

        withContext(Dispatchers.Main) {
            onComplete()
        }
    }
}
}