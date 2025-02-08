package com.example.mykoinapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.ui.theme.DeepBlue
import com.example.mykoinapp.ui.theme.SlateGray
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = koinViewModel(),
    refreshTrigger: Boolean = false
) {
    val mealState by homeViewModel.mealState.collectAsState()
    val mealStateCategory by homeViewModel.mealStateCategory.collectAsState()

    LaunchedEffect(Unit,refreshTrigger) {
        homeViewModel.getMealByLetter("s") // Fetch meals when screen loads
    }

    when (mealState) {
        is ApiResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiResult.Success -> {
            Column {
                ShowMealHorizontal((mealStateCategory as ApiResult.Success<CategoryResponse>).data)
                ShowMealList((mealState as ApiResult.Success<MealResponse>).data,navController)
            }

        }

        is ApiResult.Error -> {

        }
    }
}

@Composable
fun ShowMealList(data: MealResponse,navController: NavController) {
    LazyColumn {
        val mealData = data.meals
        mealData?.let {
            items(it.size) { index ->
                val backgroundColor = if (index % 2 == 0) DeepBlue else SlateGray
                Card(modifier = Modifier.padding(8.dp).clickable {
                    navController.navigate("mealDetail/${mealData[index].idMeal}")
                }) { // Add padding for spacing
                    Column(modifier = Modifier.background(backgroundColor)) {
                        EnhancedImageFromUrl(mealData[index].strMealThumb, 250) // Pass image URL
                        Text(
                            text = "${index + 1}. Meal : " + mealData[index].strMeal,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Area : " + mealData[index].strArea,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
