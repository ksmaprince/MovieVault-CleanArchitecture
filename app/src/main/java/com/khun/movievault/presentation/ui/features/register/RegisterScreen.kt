package com.khun.movievault.presentation.ui.features.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khun.movievault.R
import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.model.User
import com.khun.movievault.presentation.ui.components.AppTopAppBar
import com.khun.movievault.presentation.ui.components.ShowLoading
import com.khun.movievault.presentation.ui.components.showToastMessage
import com.khun.movievault.presentation.ui.features.login.ConfirmPasswordState
import com.khun.movievault.presentation.ui.features.login.Email
import com.khun.movievault.presentation.ui.features.login.EmailState
import com.khun.movievault.presentation.ui.features.login.Password
import com.khun.movievault.presentation.ui.features.login.PasswordState
import com.khun.movievault.presentation.ui.theme.stronglyDeemphasizedAlpha
import com.khun.movievault.utils.ROLE_USER
import com.khun.movievault.utils.SecretKey
import com.khun.movievault.utils.encryptPassword

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    popBack: () -> Unit
) {
    val context = LocalContext.current
    val registerUserState by registerViewModel.registerUserState.collectAsStateWithLifecycle()
    var isLoading by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(registerUserState) {
        if (registerUserState is RegisterScreenUIState.RegisterSuccess) {
            showToastMessage(context, "Register User Success.")
            popBack()
        }
    }

    when (val currentState = registerUserState) {
        is RegisterScreenUIState.Loading -> isLoading = true
        is RegisterScreenUIState.Error -> {
            isLoading = false
            showToastMessage(context = context, message = currentState.message)
        }
        else -> isLoading = false

    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                topAppBarText = stringResource(id = R.string.create_account),
                onNavUp = {
                    popBack()
                },
            )
        },
        content = { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                RegisterContent { user ->
                    registerViewModel.registerUser(user = user)
                }
            }
            if (isLoading) ShowLoading()
        }
    )

}

@Composable
fun RegisterContent(
    registerSubmit: (user: User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var fullName by remember {
            mutableStateOf("")
        }
        var contactNo by remember {
            mutableStateOf("")
        }
        val passwordFocusRequest = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }
        val emailState = remember { EmailState("") }
        val passwordState = remember { PasswordState() }
        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }

        val onSubmit = {
            val profile = Profile(0, fullName, contactNo, "")
            val email = emailState.text
            val password = encryptPassword(SecretKey, passwordState.text)
            registerSubmit(User(0, email, password, profile, ROLE_USER))
        }

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
            },
            label = {
                Text(
                    text = stringResource(R.string.full_name),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contactNo,
            onValueChange = {
                contactNo = it
            },
            label = {
                Text(
                    text = stringResource(R.string.mobile_number),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        Email(emailState, onImeAction = { passwordFocusRequest.requestFocus() })

        Spacer(modifier = Modifier.height(8.dp))


        Password(
            label = stringResource(R.string.password),
            passwordState = passwordState,
            imeAction = ImeAction.Next,
            onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(passwordFocusRequest)
        )


        Spacer(modifier = Modifier.height(8.dp))


        Password(
            label = stringResource(R.string.confirm_password),
            passwordState = confirmPasswordState,
            onImeAction = { onSubmit() },
            modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.terms_and_conditions),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSubmit()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid &&
                    passwordState.isValid && confirmPasswordState.isValid
        ) {
            Text(text = stringResource(R.string.create_account))
        }
    }
}



