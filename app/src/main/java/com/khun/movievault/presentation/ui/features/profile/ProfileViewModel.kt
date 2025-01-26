package com.khun.movievault.presentation.ui.features.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.Profile
import com.khun.movievault.domain.usecases.GetProfileByIdUseCase
import com.khun.movievault.domain.usecases.UpdateProfileImageUseCase
import com.khun.movievault.domain.usecases.UserLogoutUseCase
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.UserProfileContract
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.userProfileId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileByIdUseCase: GetProfileByIdUseCase,
    private val updateImageUseCase: UpdateProfileImageUseCase,
    private val userLogoutUseCase: UserLogoutUseCase
) :
    ViewModel() {

//    private val _profileState = MutableStateFlow(
//        UserProfileContract.State(
//            userProfile = null,
//            updateImage = null,
//            isLogout = false,
//            logoutSuccessMessage = "",
//            isLoading = false
//        )
//    )
//    val profileState: StateFlow<UserProfileContract.State> get() = _profileState
//    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)

    private var _profileUiState = MutableStateFlow<ProfileUiState>(
        ProfileUiState.Loading
    )
    val profileUiState get() = _profileUiState

    init {
        getUserProfile()
    }

    fun getUserProfile() {
        viewModelScope.launch {
            getProfileByIdUseCase.execute(userProfileId).collect {
                when (it) {
                    is DataResult.Success -> {
                        if (it.data != null)
                            _profileUiState.value = ProfileUiState.LoadProfileSuccess(it.data)
                        else _profileUiState.value =
                            ProfileUiState.Error(it.message ?: ErrorsMessage.gotApiCallError)
                    }

                    is DataResult.Error -> _profileUiState.value =
                        ProfileUiState.Error(it.message ?: ErrorsMessage.gotApiCallError)

                    is DataResult.Loading -> _profileUiState.value = ProfileUiState.Loading
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadeImage(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val requestFile: RequestBody = RequestBody.create(
                "image/jpg".toMediaType(),
                file
            )
            val currentTime = LocalDateTime.now()
            val multiparFile = MultipartBody.Part.createFormData(
                name = "file",
                filename = "${userProfileId}_${currentTime}_${file.name}",
                body = requestFile
            )
            updateImageUseCase.execute(file = multiparFile, profileId = userProfileId).collect {
                when (it) {
                    is DataResult.Success -> _profileUiState.value =
                        ProfileUiState.UploadImageSuccess

                    is DataResult.Error -> _profileUiState.value =
                        ProfileUiState.Error(it.message ?: ErrorsMessage.gotApiCallError)

                    is DataResult.Loading -> _profileUiState.value = ProfileUiState.Loading
                }
            }
        }
    }

    fun logoutCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userLogoutUseCase.execute().collect {
                when (it) {
                    is DataResult.Success -> _profileUiState.value = ProfileUiState.LogoutSuccess

                    is DataResult.Error -> _profileUiState.value =
                        ProfileUiState.Error(it.message ?: ErrorsMessage.gotApiCallError)

                    is DataResult.Loading -> _profileUiState.value = ProfileUiState.Loading
                }
            }
        }
    }


    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    fun setProfile(profile: Profile) {
        _profile.value = profile
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data class LoadProfileSuccess(val profile: Profile?) : ProfileUiState
    data object UploadImageSuccess : ProfileUiState
    data object LogoutSuccess : ProfileUiState
}