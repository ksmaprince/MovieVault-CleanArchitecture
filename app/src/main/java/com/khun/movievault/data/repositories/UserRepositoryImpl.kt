package com.khun.movievault.data.repositories

import com.google.gson.Gson
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.local.PreferencesManager
import com.khun.movievault.data.model.CommonResponse
import com.khun.movievault.data.model.RegisterUserResponse
import com.khun.movievault.data.model.ResponseException
import com.khun.movievault.data.model.User
import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.data.model.UserLoginResponse
import com.khun.movievault.data.remote.helpers.UserServiceHelper
import com.khun.movievault.domain.repositories.UserRepository
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.loginToken
import com.khun.movievault.utils.userEmail
import com.khun.movievault.utils.userProfileId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val preferencesManager: PreferencesManager,
    private val userServiceHelper: UserServiceHelper
) : UserRepository {
    override suspend fun validateToken(): Flow<DataResult<CommonResponse>> =
        flow<DataResult<CommonResponse>> {
            emit(DataResult.Loading())
            with(userServiceHelper.validateToken()) {
                if (isSuccessful) {
                    emit(DataResult.Success(this.body()))
                } else {
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
                    clearCache()
                }
            }
        }.catch {
            clearCache()
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun createUser(user: User): Flow<DataResult<RegisterUserResponse>> =
        flow<DataResult<RegisterUserResponse>> {
            emit(DataResult.Loading())
            with(userServiceHelper.registerUser(user)) {
                if (isSuccessful) emit(DataResult.Success(this.body()))
                else {
                    val error = Gson().fromJson(
                        this.errorBody()?.charStream(), ResponseException::class.java
                    )
                    emit(DataResult.Error(error.ErrorMessage))
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun userLogin(userLoginRequest: UserLoginRequest): Flow<DataResult<UserLoginResponse>> =
        flow<DataResult<UserLoginResponse>> {
            emit(DataResult.Loading())
            with(userServiceHelper.loginUser(userLoginRequest)) {
                if (isSuccessful) {
                    this.body()?.let {
                        preferencesManager.saveAccessToken(it.jwtToken)
                        preferencesManager.saveEmailId(it.email)
                        preferencesManager.saveProfileId(it.profileId)
                        //Load the device info into memory
                        loadUserInfoToCache()
                    }
                    emit(DataResult.Success(this.body()))
                } else {
                    val error = Gson().fromJson(
                        this.errorBody()?.charStream(), ResponseException::class.java
                    )
                    emit(DataResult.Error(error.ErrorMessage))
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }

    override suspend fun logoutUser(): Flow<DataResult<CommonResponse>> =
        flow<DataResult<CommonResponse>> {
            emit(DataResult.Loading())
            with(userServiceHelper.logoutUser()) {
                if (isSuccessful) {
                    clearCache()
                    emit(DataResult.Success(this.body()))
                } else {
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


    override suspend fun retrieveLoginUserInfo(): Flow<DataResult<UserLoginResponse>> = flow {
        emit(DataResult.Loading())
        preferencesManager.getEmailId()?.let {
            userEmail = it
        }
        preferencesManager.getProfileId().let {
            userProfileId = it
        }
        preferencesManager.getAccessToken()?.let {
            loginToken = it
        }
        emit(
            DataResult.Success(
                UserLoginResponse(
                    profileId = userProfileId, email = userEmail, jwtToken = loginToken
                )
            )
        )
    }.catch {
        emit(DataResult.Error(it.localizedMessage))
    }

    private fun loadUserInfoToCache() {
        preferencesManager.getEmailId()?.let {
            userEmail = it
        }
        preferencesManager.getProfileId().let {
            userProfileId = it
        }
        preferencesManager.getAccessToken()?.let {
            loginToken = it
        }
    }

    private fun clearCache() {
        preferencesManager.saveEmailId(null)
        preferencesManager.saveAccessToken(null)
        preferencesManager.saveProfileId(0)
        userProfileId = 0
        userEmail = ""
        loginToken = ""
    }
}