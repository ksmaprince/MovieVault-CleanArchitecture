package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetProfileByIdUseCaseImpl(private val profileRepository: ProfileRepository) :
    GetProfileByIdUseCase {
    override suspend fun execute(profileId: Long): Flow<DataResult<Profile>> =
        flow<DataResult<Profile>> {
            profileRepository.getProfileById(profileId).collect {
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