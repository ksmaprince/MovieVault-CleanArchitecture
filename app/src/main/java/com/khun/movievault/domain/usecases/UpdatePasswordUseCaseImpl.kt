package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.model.UpdatePasswordResponse
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdatePasswordUseCaseImpl(private val profileRepository: ProfileRepository) :
    UpdatePasswordUseCase {
    override suspend fun execute(passwordRequest: UpdatePasswordRequest): Flow<DataResult<UpdatePasswordResponse>> =
        flow<DataResult<UpdatePasswordResponse>> {
            profileRepository.updatePassword(passwordRequest).collect {
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