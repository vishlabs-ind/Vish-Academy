package com.rach.co.homescreen.data.Model

import android.content.Context

class ProfileManager(context: Context) {
    private val prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    fun isProfileCreated(): Boolean = prefs.getBoolean("is_profile_created", false)

    fun setProfileCreated() = prefs.edit().putBoolean("is_profile_created", true).apply()
}