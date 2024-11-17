package com.khun.movievault.data.remote.services

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.model.UpdatePasswordResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET("profile/{profileId}")
    suspend fun getProfileById(
        @Path("profileId") profileId: Long
    ): Response<Profile>

    @PATCH("profile/addFavourite")
    suspend fun addFavouriteMovie(
        @Query("profileId") profileId: Long,
        @Query("movieId") movieId: Long
    ): Response<Long>

    @GET("profile/favourites/{profileId}")
    suspend fun getAllFavouriteMovies(
        @Path("profileId") profileId: Long
    ): Response<List<Movie>>

    @PUT("profile/{profileId}")
    suspend fun updateProfileById(
        @Path("profileId") profileId: Long,
        @Body profile: Profile
    ): Response<Profile>

    @PATCH("auth/changePassword")
    suspend fun changePassword(
        @Body passwordRequest: UpdatePasswordRequest
    ): Response<UpdatePasswordResponse>

    @Multipart
    @POST("profile/uploadImage/{profileId}")
    suspend fun updateProfileImage(
        @Part file: MultipartBody.Part,
        @Path("profileId") profileId: Long
    ): Response<Profile>
}