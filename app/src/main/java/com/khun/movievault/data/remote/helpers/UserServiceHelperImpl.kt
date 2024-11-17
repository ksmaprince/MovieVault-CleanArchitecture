package com.khun.movievault.data.remote.helpers

import com.khun.movievault.data.model.CommonResponse
import com.khun.movievault.data.model.RegisterUserResponse
import com.khun.movievault.data.model.User
import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.data.model.UserLoginResponse
import com.khun.movievault.data.remote.services.UserService
import retrofit2.Response

class UserServiceHelperImpl(private val userService: UserService) : UserServiceHelper {
    override suspend fun validateToken(): Response<CommonResponse> =
        userService.validateToken()

    override suspend fun registerUser(user: User): Response<RegisterUserResponse> =
        userService.registerUser(user)

    override suspend fun loginUser(loginRequest: UserLoginRequest): Response<UserLoginResponse> =
        userService.loginUser(loginRequest)

    override suspend fun logoutUser(): Response<CommonResponse> =
        userService.logoutUser()
}