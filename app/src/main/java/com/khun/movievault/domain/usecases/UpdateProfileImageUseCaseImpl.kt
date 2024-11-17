package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class UpdateProfileImageUseCaseImpl(private val profileRepository: ProfileRepository) :
    UpdateProfileImageUseCase {
    override suspend fun execute(
        file: MultipartBody.Part,
        profileId: Long
    ): Flow<DataResult<Profile>> =
        flow<DataResult<Profile>> {
            profileRepository.updateProfileImage(file, profileId).collect {
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