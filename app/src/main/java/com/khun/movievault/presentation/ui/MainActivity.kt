package com.khun.movievault.presentation.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.khun.movievault.presentation.ui.components.nav.MovieVaultNavHost
import com.khun.movievault.presentation.ui.theme.MovieVaultCleanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieVaultCleanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieVaultNavHost {
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
    }
}
