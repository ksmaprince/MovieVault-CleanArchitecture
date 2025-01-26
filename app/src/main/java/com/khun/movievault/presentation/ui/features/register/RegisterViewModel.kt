package com.khun.movievault.presentation.ui.features.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.User
import com.khun.movievault.domain.usecases.CreateUserUseCase
import com.khun.movievault.utils.ErrorsMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val createUserUseCase: CreateUserUseCase) :
    ViewModel() {

    private val _registerUserState = MutableStateFlow<RegisterScreenUIState>(
        RegisterScreenUIState.Idle
    )
    val registerUserState get() = _registerUserState

    fun registerUser(user: User) {
        viewModelScope.launch {
            createUserUseCase.execute(user).collect {
                when (it) {
                    is DataResult.Success -> _registerUserState.value =
                        RegisterScreenUIState.RegisterSuccess

                    is DataResult.Loading -> _registerUserState.value =
                        RegisterScreenUIState.Loading

                    is DataResult.Error -> _registerUserState.value =
                        RegisterScreenUIState.Error(it.message ?: ErrorsMessage.gotApiCallError)
                }
            }
        }
    }
}

sealed interface RegisterScreenUIState {
    data object Idle : RegisterScreenUIState
    data object Loading : RegisterScreenUIState
    data class Error(val message: String) : RegisterScreenUIState
    data object RegisterSuccess : RegisterScreenUIState
}