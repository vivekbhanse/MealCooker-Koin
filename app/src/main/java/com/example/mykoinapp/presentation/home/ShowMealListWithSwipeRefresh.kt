package com.example.mykoinapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.states.ApiResult
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun ShowMealListWithSwipeRefresh(
    navController: NavController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    var refreshing by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(false) }
    val mealStateCategory by homeViewModel.mealStateCategory.collectAsState()
    var selectedCategory by rememberSaveable { mutableStateOf("Beef") }
    LaunchedEffect(refreshing, key2 = selectedCategory) {
        if (refreshing) {
            refreshTrigger = !refreshTrigger // Toggle to trigger recomposition
            delay(1000) // Simulate API call
            refreshing = false
        }
        homeViewModel.getMealsByCategoriesNamed(selectedCategory)
    }

    when (mealStateCategory) {
        is ApiResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiResult.Success -> {
            Column {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = { refreshing = true }
                ) {
                    Column {
                        ShowMealHorizontal(
                            (mealStateCategory as ApiResult.Success<CategoryResponse>).data,
                            categoryCallback = { selectedCategorys ->
                                selectedCategory = selectedCategorys
                            })
                        MealScreen(navController = navController, refreshTrigger = refreshTrigger)
                    }
                }
            }
        }

        is ApiResult.Error -> {

        }
    }


}