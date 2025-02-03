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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.ui.theme.DarkBlue
import com.example.mykoinapp.ui.theme.DeepBlue
import com.example.mykoinapp.ui.theme.SlateGray
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
fun EnhancedImageFromUrl(strMeal: String,height:Int) {
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
fun ShowMealHorizontal(data: CategoryResponse) {
    // Use LazyRow for horizontal sliding
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val mealData = data.categories
        mealData?.let {
            items(it.size) { index ->
                val backgroundColor = if (index % 2 == 0) DeepBlue else SlateGray
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentHeight()
                        .width(300.dp) // Adjust width for horizontal scrolling
                ) {
                    Column(modifier = Modifier.background(backgroundColor)) {
                        EnhancedImageFromUrl(mealData[index].strCategoryThumb,100) // Pass image URL
                        Text(
                            text = "${index+1}. Category : ${mealData[index].strCategory}",
                            modifier = Modifier.padding(8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis

                        )

                    }
                }
            }
        }
    }
}
