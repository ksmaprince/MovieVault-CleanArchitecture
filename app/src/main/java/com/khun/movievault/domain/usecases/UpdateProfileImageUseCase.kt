package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface UpdateProfileImageUseCase {
    suspend fun execute(
        file: MultipartBody.Part,
        profileId: Long
    ): Flow<DataResult<Profile>>
}