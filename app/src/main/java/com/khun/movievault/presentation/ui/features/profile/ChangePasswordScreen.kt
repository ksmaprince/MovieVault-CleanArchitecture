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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khun.movievault.R
import com.khun.movievault.presentation.ui.components.ShowLoading
import com.khun.movievault.presentation.ui.components.showToastMessage
import com.khun.movievault.presentation.ui.features.login.ConfirmPasswordState
import com.khun.movievault.presentation.ui.features.login.Password
import com.khun.movievault.presentation.ui.features.login.PasswordState
import com.khun.movievault.utils.SecretKey
import com.khun.movievault.utils.encryptPassword

@Composable
fun ChangePasswordScreen(
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel(),
    popBack: () -> Unit,
) {
    val context = LocalContext.current
    val changePasswordUiState by changePasswordViewModel.changePasswordUiState.collectAsStateWithLifecycle()
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(changePasswordUiState) {
        if (changePasswordUiState is ChangePasswordUiState.Done) {
            isLoading = false
            showToastMessage(context = context, "Change Password Success")
            popBack()
        }
    }
    when (val currentSate = changePasswordUiState) {
        is ChangePasswordUiState.Loading -> {
            isLoading = true
        }

        is ChangePasswordUiState.Error -> {
            isLoading = false
            showToastMessage(context = context, currentSate.message)
        }

        else -> isLoading = false
    }

    Scaffold(topBar = {
        EditProfileTopAppBar(
            topAppBarText = stringResource(id = R.string.change_password),
            onNavUp = popBack,
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
                changePasswordViewModel.changePassword(currentPassword, newPassword)
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

            if (isLoading) ShowLoading()
        }
    })

}
