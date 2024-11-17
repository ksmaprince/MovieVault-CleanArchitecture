package com.khun.movievault.domain.usecases

import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.UserLoginResponse
import com.khun.movievault.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrieveLoginUserInfoUseCaseImpl(private val userRepository: UserRepository) :
    RetrieveLoginUserInfoUseCase {
    override suspend fun execute() : Flow<DataResult<UserLoginResponse>> =
        flow {
            userRepository.retrieveLoginUserInfo().collect{
                when(it){
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