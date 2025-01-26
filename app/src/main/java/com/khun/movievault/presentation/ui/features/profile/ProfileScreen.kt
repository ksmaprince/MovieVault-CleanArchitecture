package com.khun.movievault.presentation.ui.features.profile

import android.content.Context
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.khun.movievault.R
import com.khun.movievault.data.model.Profile
import com.khun.movievault.presentation.ui.components.ShowLoading
import com.khun.movievault.presentation.ui.components.showToastMessage
import com.khun.movievault.utils.userEmail
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToEditPassword: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val context = LocalContext.current

    var imageUri: Uri? by remember {
        mutableStateOf(null)
    }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                imageUri = it
                val file = fileFromContentUri(context, it)
                profileViewModel.uploadeImage(file)
            }
        }
    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val currentState = profileUiState) {
            is ProfileUiState.Loading -> ShowLoading()
            is ProfileUiState.Error -> showToastMessage(context = context, currentState.message)
            is ProfileUiState.UploadImageSuccess -> {
                profileViewModel.getUserProfile()
            }
            is ProfileUiState.LogoutSuccess -> onLogoutSuccess()
            is ProfileUiState.LoadProfileSuccess -> {
                ProfileScreenItem(
                    userProfile = currentState.profile,
                    onNavigateToEditProfile = {
                        it?.let {
                            profileViewModel.setProfile(it)
                            onNavigateToEditProfile()
                        }
                    },
                    onNavigateToEditPassword = onNavigateToEditPassword,
                    onLogoutSubmit = {
                        profileViewModel.logoutCurrentUser()
                    },
                    pickImage = {
                        launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )
            }
        }
    }

}

@Composable
private fun ProfileScreenItem(
    userProfile: Profile?,
    onNavigateToEditProfile: (profile: Profile?) -> Unit,
    onNavigateToEditPassword: () -> Unit,
    onLogoutSubmit: () -> Unit,
    pickImage: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        SubcomposeAsyncImage(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .clickable {
                    pickImage()
                },
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(
                    userProfile?.imageUrl
                        ?: "https://t3.ftcdn.net/jpg/05/16/27/58/360_F_516275801_f3Fsp17x6HQK0xQgDQEELoTuERO4SsWV.jpg"
                )
                .build(),
            loading = {
                CircularProgressIndicator(modifier = Modifier.requiredSize(24.dp))
            },
            contentDescription = userProfile?.fullName ?: "NAME",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = userProfile?.fullName ?: "NAME",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userProfile?.contactNo ?: "",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userEmail,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = {
                onNavigateToEditProfile(userProfile)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.edit_profile))
        }

        TextButton(
            onClick = {
                onNavigateToEditPassword()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.change_password))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onLogoutSubmit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = true
        ) {
            Text(
                text = stringResource(R.string.logout),
            )
        }

    }
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "temp_image")
    inputStream?.let { input ->
        val outputStream = FileOutputStream(file)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024)
            var byteCount: Int
            while (input.read(buffer).also { byteCount = it } != -1) {
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
    return file
}

private fun fileFromContentUri(context: Context, contentUri: Uri): File {
    val fileExtension = getFileExtension(context, contentUri)
    val fileName = "temporary_file" + if (fileExtension != null) ".$fileExtension" else ""

    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()

    try {
        val oStream = FileOutputStream(tempFile)
        val inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let {
            copy(inputStream, oStream)
        }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}


