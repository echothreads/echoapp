package com.echo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.feature.auth.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoTheme {
                Scaffold() { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        LoginScreen({ handle, password ->
                            println("Logging in with $handle")
                        })
                    }
                }
            }
        }
    }
}