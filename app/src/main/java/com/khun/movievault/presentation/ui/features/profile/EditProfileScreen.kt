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
import androidx.navigation.NavController
import com.khun.movievault.R
import com.khun.movievault.data.model.Profile
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.EditProfileContract
import com.khun.movievault.presentation.ui.components.ShowLoadingDialog
import com.khun.movievault.presentation.ui.components.showToastMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun EditProfileScreen(
    state: EditProfileContract.State,
    effectFlow: Flow<BaseContract.Effect>?,
    profile: Profile,
    navController: NavController,
    onSubmit: (Profile) -> Unit,
    onProfileUpdated: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(effectFlow) {
        effectFlow?.onEach {
            if (it is BaseContract.Effect.DataWasLoaded) {
                onProfileUpdated()
                showToastMessage(context, "Profile updated successfully")
                navController.popBackStack()
            }
        }?.collect {
            if (it is BaseContract.Effect.Error) {
                showToastMessage(context, it.errorMessage)
            }
        }
    }
    Scaffold(topBar = {
        EditProfileTopAppBar(
            topAppBarText = stringResource(id = R.string.edit_profile),
            onNavUp = {
                navController.popBackStack()
            },
        )
    }, content = { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            EditProfileContent(profile = profile, onSubmit)
        }
        if (state.isLoading) {
            ShowLoadingDialog(message = "Profile updating ...") {
                // Do Something
            }
        }
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
