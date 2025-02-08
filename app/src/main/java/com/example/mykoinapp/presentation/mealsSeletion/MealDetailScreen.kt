package com.example.mykoinapp.presentation.mealsSeletion

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.presentation.home.EnhancedImageFromUrl
import com.example.mykoinapp.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.koin.androidx.compose.koinViewModel
import timber.log.Timber


@Composable
fun MealDetailScreen(navController: NavController, mealId: String, mealDetailsViewModel: MealDetailsViewModel = koinViewModel()) {
  //  val mealDetailsViewModel = koinViewModel<MealDetailsViewModel>()
    val mealByIdState by mealDetailsViewModel.mealIdState.collectAsState()

    LaunchedEffect(Unit) {
        mealDetailsViewModel.getMealById(mealId)
    }
    when (mealByIdState) {
        is ApiResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiResult.Success -> {

            val mealData = (mealByIdState as ApiResult.Success<MealResponse>).data
            mealData.meals?.firstOrNull()?.let { meal ->
                MealDetailScreenView(data = mealData)
            }

        }

        is ApiResult.Error -> {

        }
    }
}

@Composable
fun MealDetailScreenView(data: MealResponse) {
    var isFavorite by remember { mutableStateOf(false) }
    val context = LocalContext.current

//    val viewmodel : MealDetailsViewModel = koinViewModel()
    data.meals?.let { meal ->
        val mealEntity = MealEntity(
            name = meal[0].strMeal,
            area = meal[0].strArea,
            category = meal[0].strCategory,
            instructions = meal[0].strInstructions,
            imgThumb = meal[0].strMealThumb
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                EnhancedImageFromUrl(meal[0].strMealThumb, 250) // Display meal image
                Icon(
                    imageVector = Icons.Default.Favorite, // You can use any heart icon or custom one
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .align(Alignment.TopEnd) // Position it at the top right
                        .size(60.dp) // Adjust size of the icon
                        .padding(8.dp)
                        .clickable {
                            isFavorite = !isFavorite

                            // Save to database
//                            CoroutineScope(Dispatchers.IO).launch {
////                                viewmodel.saveFavMealDB(mealEntity)
//                                Timber.d("MealDetailScreenView","Added to favorites!")
//                            }
                        }
                        .border(
                            width = 2.dp, // Border thickness
                            color = Color.Black,
                        ),
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
            Text(
                text = "Meal: ${meal[0].strMeal}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "Area: ${meal[0].strArea}", modifier = Modifier.padding(top = 8.dp))
            Text(text = "Category: ${meal[0].strCategory}", modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Instructions: ${meal[0].strInstructions}",
                modifier = Modifier.padding(top = 8.dp)
            )
            // More details if needed
        }
    } ?: run {
        Text("Meal not found", style = MaterialTheme.typography.bodyLarge)
    }
}

