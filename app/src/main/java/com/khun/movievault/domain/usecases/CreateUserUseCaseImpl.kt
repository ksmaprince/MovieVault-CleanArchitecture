package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.RegisterUserResponse
import com.khun.movievault.data.model.User
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreateUserUseCaseImpl(private val userRepository: UserRepository) : CreateUserUseCase {
    override suspend fun execute(user: User): Flow<DataResult<RegisterUserResponse>> =
        flow<DataResult<RegisterUserResponse>> {
            userRepository.createUser(user).collect {
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