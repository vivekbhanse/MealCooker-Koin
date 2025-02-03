package com.example.mykoinapp.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Search : Screen("search", Icons.Default.Search, "Search")
    object Favorites : Screen("favorites", Icons.Default.Favorite, "Favorites")
    object Profile : Screen("profile", Icons.Default.Person, "Profile")
}
