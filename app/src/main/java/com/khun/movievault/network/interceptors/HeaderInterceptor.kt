package com.khun.movievault.network.interceptors

import android.util.Log
import com.khun.movievault.utils.loginToken
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val mutableHeaders: MutableMap<String, String> =
            chain.request().headers.toMap().toMutableMap()

        /**
         * Constant reoccuring headers to be placed here
         */
        if (!mutableHeaders.containsKey("Accept")) {
            mutableHeaders["Accept"] = "application/json"
        }
        if (!mutableHeaders.containsKey("Content-Type")) {
            mutableHeaders["Content-Type"] = "application/json"
        }

        if (!mutableHeaders.containsKey("x-api-key")) {
            mutableHeaders["x-api-key"] = "DEMO-API-KEY"
        }

        if (!mutableHeaders.containsKey("Authorization")) {
            mutableHeaders["Authorization"] = if (loginToken == "") "" else "Bearer $loginToken"
        }

        val headerBuilder: Headers.Builder = Headers.Builder()
        for ((k, v) in mutableHeaders.entries) {
            Log.i("Header[$k] >>>>", v)
            headerBuilder.add(k, v)
        }

        val request = chain.request().newBuilder()
        request.headers(headerBuilder.build())
        return chain.proceed(request.build())
    }
}