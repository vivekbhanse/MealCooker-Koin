package com.example.mykoinapp.data.local.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "encrypted_preferences")

    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    // Save User's Name
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = encrypt(name)
        }
    }

    // Save User Credentials and Login Status
    suspend fun saveUser(email: String, password: String, isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = encrypt(email)
            preferences[USER_PASSWORD] = encrypt(password)
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    // Get User Name
    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME]?.let { decrypt(it) }
    }

    // Get Email
    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]?.let { decrypt(it) }
    }

    // Get Login Status
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    // Clear User Data (Logout)
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_NAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_PASSWORD)
            preferences[IS_LOGGED_IN] = false
        }
    }

    // Encryption
    private fun encrypt(data: String): String {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encryptedPrefs = EncryptedSharedPreferences.create(
            "Encrypted_Prefs",
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        encryptedPrefs.edit().putString("temp", data).apply()
        return encryptedPrefs.getString("temp", "") ?: ""
    }

    // Decryption
    private fun decrypt(data: String): String {
        return encrypt(data) // Symmetric encryption
    }
}

