package com.example.mykoinapp.presentation.navhost


import BiometricAuthScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mykoinapp.presentation.Screen
import com.example.mykoinapp.presentation.SplashScreen
import com.example.mykoinapp.presentation.home.MealScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "biometricAuthScreen") {
        composable("biometricAuthScreen") {
            BiometricAuthScreen(navController = navController)
        }
        composable("mealScreen") {
            MealScreen(navController = navController)
        }
    }
}

