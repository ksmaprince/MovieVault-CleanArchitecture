package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.RegisterUserResponse
import com.khun.movievault.data.model.User
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface CreateUserUseCase {
    suspend fun execute(user: User): Flow<DataResult<RegisterUserResponse>>
}