package com.example.mykoinapp.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay


@Composable
fun ShowMealListWithSwipeRefresh(navController: NavController) {
    var refreshing by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            refreshTrigger = !refreshTrigger // Toggle to trigger recomposition
            delay(3000) // Simulate API call
            refreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true }
    ) {
        MealScreen(navController = navController, refreshTrigger = refreshTrigger)
    }
}