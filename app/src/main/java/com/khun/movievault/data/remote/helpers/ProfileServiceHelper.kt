package com.khun.movievault.data.remote.helpers

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.model.UpdatePasswordResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface ProfileServiceHelper {
    suspend fun getProfileById(profileId: Long): Response<Profile>
    suspend fun addFavouriteMovie(profileId: Long, movieId: Long): Response<Long>
    suspend fun getAllFavouriteMovies(profileId: Long): Response<List<Movie>>
    suspend fun updateProfileById(profileId: Long, profile: Profile): Response<Profile>
    suspend fun changePassword(passwordRequest: UpdatePasswordRequest): Response<UpdatePasswordResponse>
    suspend fun updateProfileImage(
        file: MultipartBody.Part, profileId: Long
    ): Response<Profile>
}