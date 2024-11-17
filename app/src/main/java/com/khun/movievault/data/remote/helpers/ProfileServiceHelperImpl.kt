package com.khun.movievault.data.remote.helpers

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.model.UpdatePasswordResponse
import com.khun.movievault.data.remote.services.ProfileService
import okhttp3.MultipartBody
import retrofit2.Response

class ProfileServiceHelperImpl(private val profileService: ProfileService) : ProfileServiceHelper {
    override suspend fun getProfileById(profileId: Long): Response<Profile> =
        profileService.getProfileById(profileId)

    override suspend fun addFavouriteMovie(profileId: Long, movieId: Long): Response<Long> =
        profileService.addFavouriteMovie(profileId, movieId)

    override suspend fun getAllFavouriteMovies(profileId: Long): Response<List<Movie>> =
        profileService.getAllFavouriteMovies(profileId)

    override suspend fun updateProfileById(profileId: Long, profile: Profile): Response<Profile> =
        profileService.updateProfileById(profileId, profile)

    override suspend fun changePassword(passwordRequest: UpdatePasswordRequest): Response<UpdatePasswordResponse> =
        profileService.changePassword(passwordRequest)

    override suspend fun updateProfileImage(
        file: MultipartBody.Part,
        profileId: Long
    ): Response<Profile> =
        profileService.updateProfileImage(file, profileId)
}