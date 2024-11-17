package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.data.model.UserLoginResponse
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserLoginUseCaseImpl(private val userRepository: UserRepository) : UserLoginUseCase {
    override suspend fun execute(userLoginRequest: UserLoginRequest): Flow<DataResult<UserLoginResponse>> =
        flow<DataResult<UserLoginResponse>> {
            userRepository.userLogin(userLoginRequest).collect {
                when (it) {
                    is DataResult.Success -> {
                        emit(DataResult.Success(it.data))
                    }

                    is DataResult.Error -> {
                        emit(DataResult.Error(it.message))
                    }

                    is DataResult.Loading -> {
                        emit(DataResult.Loading())
                    }
                }
            }
        }
}