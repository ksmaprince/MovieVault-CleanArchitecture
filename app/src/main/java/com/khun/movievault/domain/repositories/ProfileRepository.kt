package com.khun.movievault.domain.repositories

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.model.UpdatePasswordResponse
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ProfileRepository {
    suspend fun getProfileById(profileId: Long): Flow<DataResult<Profile>>
    suspend fun addFavouriteMovie(profileId: Long, movieId: Long): Flow<DataResult<Long>>
    suspend fun getAllFavouriteMovie(profileId: Long): Flow<DataResult<List<Movie>>>
    suspend fun updateProfileById(profile: Profile): Flow<DataResult<Profile>>
    suspend fun updatePassword(passwordRequest: UpdatePasswordRequest): Flow<DataResult<UpdatePasswordResponse>>
    suspend fun updateProfileImage(
        file: MultipartBody.Part,
        profileId: Long
    ): Flow<DataResult<Profile>>
}