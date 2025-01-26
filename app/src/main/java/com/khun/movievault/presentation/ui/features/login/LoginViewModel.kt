package com.khun.movievault.presentation.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.domain.usecases.RetrieveLoginUserInfoUseCase
import com.khun.movievault.domain.usecases.UserLoginUseCase
import com.khun.movievault.domain.usecases.ValidateTokenUseCase
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.loginToken
import com.khun.movievault.utils.userEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: UserLoginUseCase,
    private val retrieveLoginUserInfoUseCase: RetrieveLoginUserInfoUseCase,
    private val validateTokenUseCase: ValidateTokenUseCase
) : ViewModel() {
    private var _loginScreenUiState = MutableStateFlow<LoginScreenUIState>(
        LoginScreenUIState.Idle
    )
    val loginScreenUIState get() = _loginScreenUiState

    init {
        retrieveUserInfo()
    }

    fun login(userLoginRequest: UserLoginRequest) {
        viewModelScope.launch {
            loginUseCase.execute(userLoginRequest).collect {
                when (it) {
                    is DataResult.Loading -> {
                        _loginScreenUiState.value = LoginScreenUIState.Loading
                    }

                    is DataResult.Success -> _loginScreenUiState.value =
                        LoginScreenUIState.LoginSuccess

                    is DataResult.Error -> _loginScreenUiState.value =
                        LoginScreenUIState.Error(it.message ?: ErrorsMessage.gotApiCallError)
                }
            }
        }
    }

    private fun validateToken() {
        viewModelScope.launch() {
            validateTokenUseCase.execute().collect {
                when (it) {
                    is DataResult.Success -> {
                        _loginScreenUiState.value = LoginScreenUIState.LoginSuccess
                    }

                    is DataResult.Error -> _loginScreenUiState.value =
                        LoginScreenUIState.Error(it.message ?: ErrorsMessage.gotApiCallError)


                    is DataResult.Loading -> _loginScreenUiState.value = LoginScreenUIState.Loading

                }
            }
        }
    }

    fun retrieveUserInfo() {
        viewModelScope.launch {
            retrieveLoginUserInfoUseCase.execute().collect {
                when (it) {
                    is DataResult.Success -> {
                        if (userEmail.isNotBlank() && loginToken.isNotBlank()) {
                            validateToken()
                        } else {
                            _loginScreenUiState.value = LoginScreenUIState.Idle
                        }
                    }

                    is DataResult.Error -> _loginScreenUiState.value =
                        LoginScreenUIState.Error(it.message ?: ErrorsMessage.gotApiCallError)


                    is DataResult.Loading -> _loginScreenUiState.value = LoginScreenUIState.Loading
                }
            }
        }
    }
}

sealed interface LoginScreenUIState {
    data object Idle : LoginScreenUIState
    data object Loading : LoginScreenUIState
    data object LoginSuccess : LoginScreenUIState
    data class Error(val message: String) : LoginScreenUIState
}