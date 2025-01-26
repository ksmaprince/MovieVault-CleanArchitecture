package com.khun.movievault.presentation.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.domain.usecases.UpdatePasswordUseCase
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.userEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase
) :
    ViewModel() {

    private val _changePasswordUiState = MutableStateFlow<ChangePasswordUiState>(
        ChangePasswordUiState.Idle
    )
    val changePasswordUiState get() = _changePasswordUiState

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePasswordUseCase.execute(
                UpdatePasswordRequest(userEmail, currentPassword, newPassword)
            ).collect {
                when (it) {
                    is DataResult.Success -> _changePasswordUiState.value =
                        ChangePasswordUiState.Done

                    is DataResult.Error -> _changePasswordUiState.value =
                        ChangePasswordUiState.Error(it.message ?: ErrorsMessage.gotApiCallError)

                    is DataResult.Loading -> _changePasswordUiState.value =
                        ChangePasswordUiState.Loading
                }
            }
        }
    }

}

sealed interface ChangePasswordUiState {
    data object Idle : ChangePasswordUiState
    data object Loading : ChangePasswordUiState
    data class Error(val message: String) : ChangePasswordUiState
    data object Done : ChangePasswordUiState
}