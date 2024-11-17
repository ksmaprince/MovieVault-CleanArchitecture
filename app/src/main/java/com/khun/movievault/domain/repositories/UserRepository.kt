package com.khun.movievault.domain.repositories

import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.CommonResponse
import com.khun.movievault.data.model.RegisterUserResponse
import com.khun.movievault.data.model.User
import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.data.model.UserLoginResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun validateToken(): Flow<DataResult<CommonResponse>>
    suspend fun createUser(user: User): Flow<DataResult<RegisterUserResponse>>
    suspend fun userLogin(userLoginRequest: UserLoginRequest): Flow<DataResult<UserLoginResponse>>
    suspend fun retrieveLoginUserInfo(): Flow<DataResult<UserLoginResponse>>
    suspend fun logoutUser(): Flow<DataResult<CommonResponse>>

}

