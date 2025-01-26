package com.khun.movievault.presentation.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowDialog(title: String, message: String, onDimissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDimissRequest,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = {
                    onDimissRequest()
                }
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun ShowLoading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Center,
        modifier = modifier.size(100.dp)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


fun showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}