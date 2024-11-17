package com.khun.movievault.presentation.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.UserLoginRequest
import com.khun.movievault.domain.usecases.RetrieveLoginUserInfoUseCase
import com.khun.movievault.domain.usecases.UserLoginUseCase
import com.khun.movievault.domain.usecases.ValidateTokenUseCase
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.LoginContract
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.loginToken
import com.khun.movievault.utils.userEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: UserLoginUseCase,
    private val retrieveLoginUserInfoUseCase: RetrieveLoginUserInfoUseCase,
    private val validateTokenUseCase: ValidateTokenUseCase
) : ViewModel() {

    private var _loginState = MutableStateFlow(
        LoginContract.State(
            isUserLoginSuccess = false,
            isLoading = false
        )
    )
    val loginState: StateFlow<LoginContract.State> get() = _loginState
    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)

    init {
        retrieveUserInfo()
    }

    fun login(userLoginRequest: UserLoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.execute(userLoginRequest).collect {
                when (it) {
                    is DataResult.Success -> {
                        _loginState.value = loginState.value.copy(
                            isLoading = false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _loginState.value = loginState.value.copy(
                            isLoading = false
                        )
                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        if (!loginState.value.isLoading) {
                            _loginState.value = loginState.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun validateToken() {
        viewModelScope.launch() {
            validateTokenUseCase.execute().collect {
                when (it) {
                    is DataResult.Success -> {
                        _loginState.value = loginState.value.copy(
                            isLoading = false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _loginState.value = loginState.value.copy(
                            isLoading = false
                        )
                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        if (!loginState.value.isLoading) {
                            _loginState.value = loginState.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun retrieveUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            retrieveLoginUserInfoUseCase.execute().collect {
                when (it) {
                    is DataResult.Success -> {
                        _loginState.value = loginState.value.copy(
                            isLoading = false
                        )
                        if (userEmail.isNotBlank() && loginToken.isNotBlank()) {
                            validateToken()
                        }
                        // effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _loginState.value = loginState.value.copy(
                            isLoading = false
                        )

                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        if (!loginState.value.isLoading) {
                            _loginState.value = loginState.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }
}