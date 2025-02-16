package com.example.mykoinapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.mykoinapp.R
import com.example.mykoinapp.data.dto.MealResponse
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.ui.theme.DarkBlue
import com.example.mykoinapp.ui.theme.DeepBlue
import com.example.mykoinapp.ui.theme.SlateGray
import com.example.mykoinapp.ui.theme.color1
import com.example.mykoinapp.ui.theme.color2
import com.neo.wave.WaveSpeed
import com.neo.wave.WaveView


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AppTopBar() {
//    WaveView(
//        modifier = Modifier
//            .size(150.dp)
//            .clip(RoundedCornerShape(50)),
//        waveColor = MaterialTheme.colorScheme.primary.copy(0.4f),
//        wavePointCount = 10,
//        waveSpeed = WaveSpeed.FAST,
//        progress = 50f,
//        dragEnabled = true,
//        isDebugMode = false,
//        onProgressUpdated = {
////            progress = it
//        }
//    )
    TopAppBar(
        title = {
            Text(text = "Meal Cooker", style = MaterialTheme.typography.headlineSmall)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = DarkBlue // Example background color (Teal)
        ),
        actions = {
            // You can add actions like icons here if needed
        }
    )
}


@Composable
fun EnhancedImageFromUrl(strMeal: String, height: Int) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(strMeal)
            .placeholder(R.drawable.dessert) // Placeholder while loading
            .error(R.drawable.dessert) // Error image if loading fails
//            .crossfade(true).size(Size.ORIGINAL)
            .transformations(RoundedCornersTransformation(20f)) // Optional: Rounded corners
            .build()
    )

    Image(
        painter = painter,
        contentDescription = "Enhanced Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
    )
}

@Composable
fun ShowMealHorizontal(data: CategoryResponse, categoryCallback: (String) -> Unit) {
    // Single state to track selected item index
    var selectedIndex by rememberSaveable { mutableStateOf<Int>(0) }

    // Use LazyRow for horizontal sliding
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val mealData = data.categories
        items(mealData.size) { index ->
            val isSelected = selectedIndex == index // Only the selected index is true

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentHeight()
                    .width(300.dp)
                    .clickable {
                        // Update the selected category and index
                        categoryCallback(mealData[index].strCategory)
                        selectedIndex = if (isSelected) 0 else index
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(if (index % 2 == 0) color1 else color2)
                        .padding(8.dp)
                ) {
                    // Show the selection button (checkmark or circle)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            EnhancedImageFromUrl(
                                mealData[index].strCategoryThumb,
                                100
                            ) // Display image
                            Text(
                                text = "${index + 1}. Category: ${mealData[index].strCategory}",
                                modifier = Modifier.padding(8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Black
                            )
                        }

                        // Selection button (Checkmark or Circle)
                        Icon(
                            imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.AddCircle,
                            tint = if (isSelected) Color.Green else Color.LightGray,
                            contentDescription = if (isSelected) "Selected" else "Unselected",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

