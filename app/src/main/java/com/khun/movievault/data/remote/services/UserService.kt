package com.khun.movievault.data.remote.services

import com.khun.movievault.data.model.RegisterUserResponse
import com.khun.movievault.data.model.User
import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.data.model.UserLoginResponse
import com.khun.movievault.data.model.CommonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/validateToken")
    suspend fun validateToken(): Response<CommonResponse>

    @POST("auth/addUser")
    suspend fun registerUser(@Body user: User): Response<RegisterUserResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: UserLoginRequest): Response<UserLoginResponse>

    @POST("auth/logout")
    suspend fun logoutUser(): Response<CommonResponse>
}