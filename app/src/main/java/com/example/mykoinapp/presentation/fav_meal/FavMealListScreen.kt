package com.example.mykoinapp.presentation.fav_meal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.mykoinapp.presentation.home.EnhancedImageFromUrl
import com.example.mykoinapp.presentation.home.getRandomColor
import com.example.mykoinapp.ui.theme.DeepBlue
import com.example.mykoinapp.ui.theme.SlateGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun FavMealListScreen() {
    val viewModel: FavoriteMealViewModel = getViewModel()
    val favMeals by viewModel.mealFavorite.collectAsState()
    if (favMeals.isEmpty()) {
        // If the list is empty, show the text in the center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Optional padding for some space around
            contentAlignment = Alignment.Center // Center the content
        ) {
            Text(
                text = "No favorite meals yet!",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn {
            val mealData = favMeals
            mealData.let {
                items(it.size) { index ->
                    val backgroundColor = getRandomColor()
                    Card(modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            //  navController.navigate("mealDetail/${mealData[index].id}")
                        }) { // Add padding for spacing
                        Column(modifier = Modifier.background(backgroundColor)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                EnhancedImageFromUrl(
                                    mealData[index].imgThumb,
                                    250
                                ) // Pass image URL

                                Icon(
                                    imageVector = Icons.Default.Favorite, // You can use any heart icon or custom one
                                    contentDescription = "Favorite",
                                    modifier = Modifier
                                        .align(Alignment.TopEnd) // Position it at the top right
                                        .size(60.dp) // Adjust size of the icon
                                        .padding(8.dp)
                                        .clickable {

                                            CoroutineScope(Dispatchers.IO).launch {
                                                viewModel.deleteMealById(mealData[index].id)
                                                                                           }
                                        }
                                        .border(
                                            width = 2.dp, // Border thickness
                                            color = Color.Black,
                                        ),
                                    tint = Color.Red
                                )
                            }
                            Text(
                                text = "${index + 1}. Meal : " + mealData[index].name,
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(
                                text = "Area : " + mealData[index].area,
                                modifier = Modifier.padding(8.dp)
                            )


                        }
                    }
                }
            }
        }
    }
}

