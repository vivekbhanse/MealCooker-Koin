package com.example.mykoinapp.presentation.home

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.ui.theme.DeepBlue
import com.example.mykoinapp.ui.theme.SlateGray
import com.example.mykoinapp.ui.theme.colorArray
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import kotlin.random.Random

@Composable
fun MealScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = koinViewModel(),
    refreshTrigger: Boolean = false
) {

    val mealStateCategoryNamed by homeViewModel.mealStateCategoryNamed.collectAsState()
    var selectedCategory by remember { mutableStateOf("Beef") }

    LaunchedEffect(key1 = Unit, refreshTrigger) {
        //homeViewModel.getMealsByCategoriesNamed(selectedCategory) // Fetch meals when screen loads
    }

    when (mealStateCategoryNamed) {
        is ApiResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiResult.Success -> {

            ShowMealList(
                (mealStateCategoryNamed as ApiResult.Success<MealResponse>).data,
                navController,
                selectedCategory
            )


        }

        is ApiResult.Error -> {

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowMealList(data: MealResponse, navController: NavController, selectedCategory: String) {
    val isClicked = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(1000) // Prevent clicks for 1 second (adjust delay as needed)
        isClicked.value = false // Re-enable clicking after the delay
    }
    LazyColumn {
        val mealData = data.meals
        mealData?.let {
            items(it.size) { index ->
//                val backgroundColor = if (index % 2 == 0) DeepBlue else SlateGray
                val backgroundColor = getRandomColor()
                Card(modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        if (!isClicked.value) {
                            isClicked.value = true
                            Timber.d("Clicked Item ${mealData[index].idMeal}")
                            navController.navigate("mealDetail/${mealData[index].idMeal}")
                        }


                    }) { // Add padding for spacing
                    Column(modifier = Modifier.background(backgroundColor)) {
                        EnhancedImageFromUrl(mealData[index].strMealThumb, 250) // Pass image URL
                        Text(
                            text = "Meal ID : " + mealData[index].idMeal,
                            modifier = Modifier.padding(8.dp),
                            androidx.compose.ui.graphics.Color.Black
                        )

                        Text(
                            text = "${index + 1}. Meal : " + mealData[index].strMeal,
                            modifier = Modifier.padding(8.dp),
                            androidx.compose.ui.graphics.Color.Black
                        )

                    }
                }
            }
        }
    }

}
fun getRandomColor(): androidx.compose.ui.graphics.Color {
    return colorArray[Random.nextInt(colorArray.size)]
}