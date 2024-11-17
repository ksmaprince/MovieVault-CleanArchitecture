package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.data.model.UserLoginResponse
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface UserLoginUseCase {
    suspend fun execute(userLoginRequest: UserLoginRequest): Flow<DataResult<UserLoginResponse>>
}