package com.khun.movievault.data.remote.helpers

import com.khun.movievault.data.model.RegisterUserResponse
import com.khun.movievault.data.model.User
import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.data.model.UserLoginResponse
import com.khun.movievault.data.model.CommonResponse
import retrofit2.Response

interface UserServiceHelper {
    suspend fun validateToken(): Response<CommonResponse>
    suspend fun registerUser(user: User): Response<RegisterUserResponse>
    suspend fun loginUser(loginRequest: UserLoginRequest): Response<UserLoginResponse>
    suspend fun logoutUser(): Response<CommonResponse>
}