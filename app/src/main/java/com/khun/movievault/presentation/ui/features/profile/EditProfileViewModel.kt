package com.khun.movievault.presentation.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.Profile
import com.khun.movievault.domain.usecases.UpdateProfileByIdUseCase
import com.khun.movievault.utils.ErrorsMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileByIdUseCase: UpdateProfileByIdUseCase
) : ViewModel() {

    private var _editProfileState = MutableStateFlow<EditProfileUiState>(
        EditProfileUiState.Idle
    )
    val editProfileUiState get() = _editProfileState

    fun updateUserProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            updateProfileByIdUseCase.execute(profile).collect {
                when (it) {
                    is DataResult.Success -> _editProfileState.value = EditProfileUiState.Done
                    is DataResult.Error -> _editProfileState.value =
                        EditProfileUiState.Error(it.message ?: ErrorsMessage.gotApiCallError)

                    is DataResult.Loading -> _editProfileState.value = EditProfileUiState.Loading
                }
            }
        }
    }
}

sealed interface EditProfileUiState {
    data object Idle : EditProfileUiState
    data object Loading : EditProfileUiState
    data class Error(val message: String) : EditProfileUiState
    data object Done : EditProfileUiState
}