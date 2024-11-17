package com.khun.movievault.utils

import com.khun.movievault.BuildConfig


class Constants {
    companion object {
        internal const val BASE_URL = BuildConfig.BASE_URL
        internal const val PREF_NAME = "MOVIE_VAULT"
        internal const val TOKEN_KEY = "ACCESS_TOKEN"
        internal const val PROFILE_KEY = "PROFILE_ID"
        internal const val EMAIL_KEY = "EMAIL_ID"
    }
}