package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateProfileByIdUseCaseImpl(private val profileRepository: ProfileRepository) :
    UpdateProfileByIdUseCase {
    override suspend fun execute(profile: Profile): Flow<DataResult<Profile>> =
        flow<DataResult<Profile>> {
            profileRepository.updateProfileById(profile).collect {
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