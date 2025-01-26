package com.khun.movievault.presentation.ui.features.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khun.movievault.R
import com.khun.movievault.data.model.Profile
import com.khun.movievault.presentation.ui.components.ShowLoading
import com.khun.movievault.presentation.ui.components.showToastMessage

@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    profile: Profile,
    popBack: () -> Unit,
) {
    val context = LocalContext.current
    val editProfileUiState by editProfileViewModel.editProfileUiState.collectAsStateWithLifecycle()
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(editProfileUiState) {
        if (editProfileUiState is EditProfileUiState.Done) {
            showToastMessage(context = context, message = "Profile Updated")
            popBack()
        }
    }
    when (val currentState = editProfileUiState) {
        is EditProfileUiState.Loading -> isLoading = true
        is EditProfileUiState.Error -> {
            isLoading = false
            showToastMessage(context = context, message = currentState.message)
        }

        else -> isLoading = false
    }

    Scaffold(topBar = {
        EditProfileTopAppBar(
            topAppBarText = stringResource(id = R.string.edit_profile),
            onNavUp = popBack
        )
    }, content = { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            EditProfileContent(profile = profile) {
                editProfileViewModel.updateUserProfile(it)
            }
        }
        if (isLoading) ShowLoading()
    })
}

@Composable
fun EditProfileContent(
    profile: Profile,
    onSubmit: (Profile) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        var fullName by remember {
            mutableStateOf(profile.fullName)
        }
        var contactNo by remember {
            mutableStateOf(profile.contactNo)
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSubmit(
                    Profile(
                        profile.profileId, fullName, contactNo, profile.imageUrl
                    )
                )
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Profile")
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class) // CenterAlignedTopAppBar is experimental in m3
@Composable
fun EditProfileTopAppBar(
    topAppBarText: String, onNavUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = topAppBarText,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        // We need to balance the navigation icon, so we add a spacer.
        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },
    )
}
