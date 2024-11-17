package com.khun.movievault.data.repositories

import com.google.gson.Gson
import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.model.ResponseException
import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.model.UpdatePasswordResponse
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.remote.helpers.ProfileServiceHelper
import com.khun.movievault.domain.repositories.ProfileRepository
import com.khun.movievault.utils.ErrorsMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class ProfileRepositoryImpl(private val profileServiceHelper: ProfileServiceHelper) :
    ProfileRepository {
    override suspend fun getProfileById(profileId: Long): Flow<DataResult<Profile>> =
        flow<DataResult<Profile>> {
            emit(DataResult.Loading())
            with(profileServiceHelper.getProfileById(profileId = profileId)) {
                if (isSuccessful) emit(DataResult.Success(this.body()))
                else {
                    if (this.code() in 400..499) {
                        emit(DataResult.Error(ErrorsMessage.sessionExpiredError))
                    } else if (this.code() in 500..599) {
                        emit(DataResult.Error(ErrorsMessage.serverError))
                    } else {
                        val error = Gson().fromJson(
                            this.errorBody()?.charStream(),
                            ResponseException::class.java
                        )
                        emit(DataResult.Error(error.ErrorMessage))
                    }
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun addFavouriteMovie(
        profileId: Long,
        movieId: Long
    ): Flow<DataResult<Long>> =
        flow<DataResult<Long>> {
            emit(DataResult.Loading())
            with(profileServiceHelper.addFavouriteMovie(profileId, movieId)) {
                if (isSuccessful) emit(DataResult.Success(this.body()))
                else {
                    if (this.code() in 400..499) {
                        emit(DataResult.Error(ErrorsMessage.sessionExpiredError))
                    } else if (this.code() in 500..599) {
                        emit(DataResult.Error(ErrorsMessage.serverError))
                    } else {
                        val error = Gson().fromJson(
                            this.errorBody()?.charStream(),
                            ResponseException::class.java
                        )
                        emit(DataResult.Error(error.ErrorMessage))
                    }
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun getAllFavouriteMovie(profileId: Long): Flow<DataResult<List<Movie>>> =
        flow<DataResult<List<Movie>>> {
            emit(DataResult.Loading())
            with(profileServiceHelper.getAllFavouriteMovies(profileId = profileId)) {
                if (isSuccessful) emit(DataResult.Success(this.body()))
                else {
                    if (this.code() in 400..499) {
                        emit(DataResult.Error(ErrorsMessage.sessionExpiredError))
                    } else if (this.code() in 500..599) {
                        emit(DataResult.Error(ErrorsMessage.serverError))
                    } else {
                        val error = Gson().fromJson(
                            this.errorBody()?.charStream(),
                            ResponseException::class.java
                        )
                        emit(DataResult.Error(error.ErrorMessage))
                    }
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun updateProfileById(profile: Profile): Flow<DataResult<Profile>> =
        flow<DataResult<Profile>> {
            emit(DataResult.Loading())
            with(profileServiceHelper.updateProfileById(profile.profileId, profile)) {
                if (isSuccessful) emit(DataResult.Success(this.body()))
                else {
                    if (this.code() in 400..499) {
                        emit(DataResult.Error(ErrorsMessage.sessionExpiredError))
                    } else if (this.code() in 500..599) {
                        emit(DataResult.Error(ErrorsMessage.serverError))
                    } else {
                        val error = Gson().fromJson(
                            this.errorBody()?.charStream(),
                            ResponseException::class.java
                        )
                        emit(DataResult.Error(error.ErrorMessage))
                    }
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun updatePassword(passwordRequest: UpdatePasswordRequest): Flow<DataResult<UpdatePasswordResponse>> =
        flow<DataResult<UpdatePasswordResponse>> {
            emit(DataResult.Loading())
            with(profileServiceHelper.changePassword(passwordRequest)) {
                if (isSuccessful) emit(DataResult.Success(this.body()))
                else {
                    if (this.code() in 400..499) {
                        emit(DataResult.Error(ErrorsMessage.sessionExpiredError))
                    } else if (this.code() in 500..599) {
                        emit(DataResult.Error(ErrorsMessage.serverError))
                    } else {
                        val error = Gson().fromJson(
                            this.errorBody()?.charStream(),
                            ResponseException::class.java
                        )
                        emit(DataResult.Error(error.ErrorMessage))
                    }
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun updateProfileImage(
        file: MultipartBody.Part,
        profileId: Long
    ): Flow<DataResult<Profile>> =
        flow<DataResult<Profile>> {
            emit(DataResult.Loading())
            with(profileServiceHelper.updateProfileImage(file, profileId)) {
                if (isSuccessful) emit(DataResult.Success(this.body()))
                else {
                    if (this.code() in 400..499) {
                        emit(DataResult.Error(ErrorsMessage.sessionExpiredError))
                    } else if (this.code() in 500..599) {
                        emit(DataResult.Error(ErrorsMessage.serverError))
                    } else {
                        val error = Gson().fromJson(
                            this.errorBody()?.charStream(),
                            ResponseException::class.java
                        )
                        emit(DataResult.Error(error.ErrorMessage))
                    }
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }
}