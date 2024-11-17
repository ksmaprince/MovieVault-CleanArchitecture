package com.khun.movievault.presentation.ui.features.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.model.User
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.usecases.CreateUserUseCase
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.RegisterUserContract
import com.khun.movievault.utils.ErrorsMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val createUserUseCase: CreateUserUseCase) :
    ViewModel() {

    private val _registerUserState = MutableStateFlow(
        RegisterUserContract.State(
            user = null, isLoading = false
        )
    )
    val registerUserState: StateFlow<RegisterUserContract.State> get() = _registerUserState
    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)

    fun registerUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.execute(user).collect {
                when (it) {
                    is DataResult.Success -> {
                        _registerUserState.value = registerUserState.value.copy(
                            user = it.data, false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Loading -> {
                        if (!registerUserState.value.isLoading) _registerUserState.value =
                            registerUserState.value.copy(
                                isLoading = true
                            )
                    }

                    is DataResult.Error -> {
                        _registerUserState.value = registerUserState.value.copy(
                            isLoading = false
                        )
                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }
                }
            }
        }
    }
}