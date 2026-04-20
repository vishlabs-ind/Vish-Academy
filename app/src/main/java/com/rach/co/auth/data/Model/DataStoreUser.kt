package com.rach.co.auth.data.Model


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extension (only once in project)
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // 🔑 Keys
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val PREMIUM_KEY = booleanPreferencesKey("is_premium")

    // ✅ Save Email
    suspend fun saveEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
        }
    }

    // ✅ Save Premium
    suspend fun savePremium(isPremium: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PREMIUM_KEY] = isPremium
        }
    }

    // ✅ Get Email
    val email: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[EMAIL_KEY]
    }

    // ✅ Get Premium
    val isPremium: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PREMIUM_KEY] ?: false
    }

    // ✅ Clear (logout)
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}