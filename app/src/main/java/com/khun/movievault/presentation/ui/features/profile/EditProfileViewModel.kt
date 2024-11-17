package com.khun.movievault.presentation.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.mappers.CallSuccessModel
import com.khun.movievault.domain.usecases.UpdatePasswordUseCase
import com.khun.movievault.domain.usecases.UpdateProfileByIdUseCase
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.EditProfileContract
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.userEmail
import com.khun.movievault.utils.userProfileId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileByIdUseCase: UpdateProfileByIdUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) :
    ViewModel() {

    private val _editProfileState = MutableStateFlow(
        EditProfileContract.State(
            updatedProfile = null,
            updatePassword = null,
            isLoading = false
        )
    )
    val editProfileState: StateFlow<EditProfileContract.State> get() = _editProfileState
    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)

    fun updateUserProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            updateProfileByIdUseCase.execute(profile).collect {
                when (it) {
                    is DataResult.Success -> {
                        _editProfileState.value = editProfileState.value.copy(
                            updatedProfile = CallSuccessModel(
                                "Update Profile Success",
                                it.data?.profileId
                            ),
                            isLoading = false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _editProfileState.value = editProfileState.value.copy(
                            isLoading = false
                        )
                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        if (!editProfileState.value.isLoading) {
                            _editProfileState.value = editProfileState.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun updatePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePasswordUseCase.execute(
                UpdatePasswordRequest(userEmail, currentPassword, newPassword)
            ).collect {
                when (it) {
                    is DataResult.Success -> {
                        _editProfileState.value = editProfileState.value.copy(
                            updatePassword = CallSuccessModel(it.data!!.message, userProfileId),
                            isLoading = false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _editProfileState.value = editProfileState.value.copy(
                            isLoading = false
                        )
                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        if (!editProfileState.value.isLoading) {
                            _editProfileState.value = editProfileState.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }


}