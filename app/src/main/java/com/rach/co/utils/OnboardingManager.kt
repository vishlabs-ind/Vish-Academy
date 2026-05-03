package com.rach.co.utils

import android.content.Context

class OnboardingManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean =
        prefs.getBoolean("is_first_launch", true)

    fun setFirstLaunchCompleted() {
        prefs.edit().putBoolean("is_first_launch", false).apply()
    }
}