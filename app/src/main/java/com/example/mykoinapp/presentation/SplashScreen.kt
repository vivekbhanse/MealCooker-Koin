package com.example.mykoinapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(navController: NavController) {
    // Use a delay to show the splash screen for a few seconds
    LaunchedEffect(Unit) {
        delay(2000) // Show splash screen for 2 seconds (adjust as needed)
        navController.navigate(Screen.BiometricAuthScreen.route) // Navigate to home screen after delay
    }

    // Content of the splash screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Change the color as needed
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to MyApp",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
