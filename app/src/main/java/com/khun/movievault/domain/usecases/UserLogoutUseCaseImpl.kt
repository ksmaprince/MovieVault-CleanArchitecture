package com.khun.movievault.domain.usecases

import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.CommonResponse
import com.khun.movievault.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserLogoutUseCaseImpl(private val userRepository: UserRepository) : UserLogoutUseCase {
    override suspend fun execute(): Flow<DataResult<CommonResponse>> =
        flow {
            userRepository.logoutUser().collect {
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