package com.khun.movievault.data.local

import android.content.Context
import android.content.SharedPreferences
import com.khun.movievault.utils.Constants.Companion.EMAIL_KEY
import com.khun.movievault.utils.Constants.Companion.PREF_NAME
import com.khun.movievault.utils.Constants.Companion.PROFILE_KEY
import com.khun.movievault.utils.Constants.Companion.TOKEN_KEY

class PreferencesManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveAccessToken(token: String?) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAccessToken(): String? {
        return preferences.getString(TOKEN_KEY, null);
    }

    fun saveProfileId(profileId: Long) {
        preferences.edit().putLong(PROFILE_KEY, profileId).apply()

    }

    fun getProfileId(): Long {
        return preferences.getLong(PROFILE_KEY, 0)
    }

    fun saveEmailId(email: String?) {
        preferences.edit().putString(EMAIL_KEY, email).apply()
    }

    fun getEmailId(): String? {
        return preferences.getString(EMAIL_KEY, null)
    }
}