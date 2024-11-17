package com.khun.movievault.presentation.ui.features.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khun.movievault.R
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.EditProfileContract
import com.khun.movievault.presentation.ui.components.ShowLoadingDialog
import com.khun.movievault.presentation.ui.components.showToastMessage
import com.khun.movievault.presentation.ui.features.login.ConfirmPasswordState
import com.khun.movievault.presentation.ui.features.login.Password
import com.khun.movievault.presentation.ui.features.login.PasswordState
import com.khun.movievault.utils.SecretKey
import com.khun.movievault.utils.encryptPassword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun ChangePasswordScreen(
    state: EditProfileContract.State,
    effectFlow: Flow<BaseContract.Effect>?,
    navController: NavController,
    onSubmit: (currentPassword: String, newPassword: String) -> Unit,
    onChangePasswordSuccess: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(effectFlow) {
        effectFlow?.onEach {
            if (it is BaseContract.Effect.DataWasLoaded) {
                showToastMessage(context, "Change Password Success, Please login again")
                onChangePasswordSuccess()
            }
        }?.collect {
            if (it is BaseContract.Effect.Error) {
                showToastMessage(context, it.errorMessage)
            }
        }
    }
    Scaffold(topBar = {
        EditProfileTopAppBar(
            topAppBarText = stringResource(id = R.string.change_password),
            onNavUp = {
                navController.popBackStack()
            },
        )
    }, content = { contentPadding ->
        Column(Modifier.padding(contentPadding)) {

            val passwordFocusRequest = remember { FocusRequester() }
            val currentPasswordState = remember {
                PasswordState()
            }
            val confirmationPasswordFocusRequest = remember { FocusRequester() }
            val passwordState = remember { PasswordState() }
            val confirmPasswordState =
                remember { ConfirmPasswordState(passwordState = passwordState) }

            val submit = {
                val currentPassword = encryptPassword(SecretKey, currentPasswordState.text)
                val newPassword = encryptPassword(SecretKey, passwordState.text)
                onSubmit(currentPassword, newPassword)
            }


            Password(
                label = stringResource(R.string.current_password),
                passwordState = currentPasswordState,
                imeAction = ImeAction.Next,
                onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
                modifier = Modifier
                    .focusRequester(passwordFocusRequest)
                    .padding(10.dp)
            )


            Password(
                label = stringResource(R.string.new_password),
                passwordState = passwordState,
                imeAction = ImeAction.Next,
                onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
                modifier = Modifier
                    .focusRequester(passwordFocusRequest)
                    .padding(10.dp)
            )

            Password(
                label = stringResource(R.string.confirm_new_password),
                passwordState = confirmPasswordState,
                onImeAction = {
                    submit()
                },
                modifier = Modifier
                    .focusRequester(confirmationPasswordFocusRequest)
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    submit()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                enabled = passwordState.isValid && confirmPasswordState.isValid
            ) {
                Text(text = stringResource(R.string.change_password))
            }

            if (state.isLoading) {
                ShowLoadingDialog(message = "Password Changing ...") {
                    // Do Something
                }
            }
        }
    })
}