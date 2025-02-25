package com.example.mykoinapp.presentation.mealsSeletion

import android.media.MediaPlayer
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.data.local.roomdb.MealCartEntity
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.presentation.home.EnhancedImageFromUrl
import com.example.mykoinapp.presentation.orders.CartManagedState
import com.example.mykoinapp.presentation.orders.CartViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import java.io.IOException


@Composable
fun MealDetailScreen(
    navController: NavController,
    mealId: String,
    showCartState: (Boolean) -> Unit
) {
    val mealDetailsViewModel: MealDetailsViewModel = getViewModel()
    val mealByIdState by mealDetailsViewModel.mealIdState.collectAsState()
    var showCartState by remember { mutableStateOf(false) }
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
                MealDetailScreenView(data = mealData) {
                    showCartState(it)
                }
            }

        }

        is ApiResult.Error -> {

        }
    }
}


@Composable
fun MealDetailScreenView(data: MealResponse, showCartState: (Boolean) -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }

    val viewModel: MealDetailsViewModel = koinViewModel()
    val cartViewModel: CartViewModel = koinViewModel()

    var savedMeals by remember { mutableStateOf<ApiResult<List<MealCartEntity>>>(ApiResult.Loading) }

    data.meals?.let { meal ->
        val mealEntity = MealEntity(
            id = meal[0].idMeal.toInt(),
            name = meal[0].strMeal,
            area = meal[0].strArea,
            category = meal[0].strCategory,
            instructions = meal[0].strInstructions,
            imgThumb = meal[0].strMealThumb
        )

        LaunchedEffect(Unit) {
            isFavorite = viewModel.existMealInDB(meal[0].idMeal.toInt())
            cartViewModel.cartMeals.collect { meals ->
                savedMeals = meals
            }
        }

        when (savedMeals) {
            is ApiResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ApiResult.Success -> {
                val items = (savedMeals as ApiResult.Success<List<MealCartEntity>>).data
                ShowMealsViewList(
                    mealEntity,
                    cartViewModel,
                    viewModel,
                    isFavorite,
                    items
                ) { showCartStatex ->

                    showCartState( showCartStatex)

                }

            }

            is ApiResult.Error -> {
                Text(text = "Error loading meals", modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ShowMealsViewList(
    meal: MealEntity,
    cartViewModel: CartViewModel,
    viewModel: MealDetailsViewModel,
    isFavoriteState: Boolean,
    items: List<MealCartEntity>,
    showCartState: (Boolean) -> Unit
) {
    var isFavorite by remember { mutableStateOf(isFavoriteState) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current
    var isButtonEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    // Ensure media player is released when the composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        floatingActionButton = {
            if (items.size > 0) {
                FloatingActionButton(
                    onClick = {
                        showCartState(true)
                    },
                    containerColor = Color(0xFFFF9800), // Orange color
                    contentColor = Color.White,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Checkout"
                        )
                        Text(
                            text = "Checkout Order",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        // Play sound when button is clicked
                        if (!isButtonEnabled) return@Button // Prevent multiple clicks

                        isButtonEnabled = false // Disable button
                        coroutineScope.launch {
                            delay(3000) // Wait for 3 seconds
                            isButtonEnabled = true // Re-enable button
                        }

                        // Play sound when button is clicked
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer()
                            try {
                                val assetFileDescriptor = context.assets.openFd("cart_bell.mp3")
                                mediaPlayer?.apply {
                                    setDataSource(
                                        assetFileDescriptor.fileDescriptor,
                                        assetFileDescriptor.startOffset,
                                        assetFileDescriptor.length
                                    )
                                    prepare()
                                    start()
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        } else {
                            mediaPlayer?.start()
                        }

                        // Add meal to cart
                        val mealCartEntity = MealCartEntity(
                            id = meal.id,
                            name = meal.name,
                            description = meal.instructions,
                            price = getRandomPrice(),
                            imageUrl = meal.imgThumb,
                            category = meal.category,
                            restaurantName = getRandomRestaurantName()
                        )
                        cartViewModel.saveCartMeals(mealCartEntity)

                        Toast.makeText(context, "Added to cart!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isButtonEnabled, // Disable button when false
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isButtonEnabled) Color(0xFFFF9800) else Color.Gray, // Change color when disabled
                        contentColor = Color.White
                    )
                ) {
                    Text("Add To Cart")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                EnhancedImageFromUrl(meal.imgThumb, 250)
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(60.dp)
                        .padding(8.dp)
                        .clickable {
                            isFavorite = !isFavorite
                            if (isFavorite) {
                                viewModel.saveFavMealDB(meal)
                                Timber.d("Meal added to favorites!")
                            } else {
                                viewModel.favoriteDeleteMeal(meal.id)
                                Timber.d("Meal removed from favorites!")
                            }
                        }
                        .border(width = 2.dp, color = Color.Black),
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
            Text(
                text = "Meal: ${meal.name}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "Area: ${meal.area}", modifier = Modifier.padding(top = 8.dp))
            Text(text = "Category: ${meal.category}", modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Instructions: ${meal.instructions}",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


fun getRandomPrice(): Double {
    val hundreds = (1..9).random()
    val finalPrice = (hundreds * 100) + 99
    return finalPrice.toDouble()
}

val restaurantNames = listOf(
    "Spice Junction",
    "The Tandoori House",
    "Golden Spoon",
    "Biryani Bliss",
    "Urban Diner",
    "The Royal Feast",
    "Street Food Hub",
    "Curry Delight",
    "Grill & Chill",
    "Flavors of India",
    "Saffron Bistro",
    "The Hungry Bowl",
    "Ocean’s Catch",
    "Fusion Fiesta",
    "Taste Paradise",
    "Zesty Zing",
    "The Secret Kitchen",
    "Tandoor Treats",
    "Gourmet Express",
    "Heritage Eats"
)

fun getRandomRestaurantName(): String {
    return restaurantNames.random()
}


