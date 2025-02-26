package com.example.mykoinapp.presentation.orders

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mykoinapp.R
import com.example.mykoinapp.data.local.roomdb.MealCartEntity
import com.example.mykoinapp.data.local.roomdb.OrderEntity
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.presentation.home.EnhancedImageFromUrlWidth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
@Preview
fun CartManagedState(modifier: Modifier = Modifier) {
    val cartViewModel: CartViewModel = koinViewModel()
    var savedMeals by remember { mutableStateOf<ApiResult<List<MealCartEntity>>>(ApiResult.Loading) }
    LaunchedEffect(Unit) {
        cartViewModel.cartMeals.collect { meals ->
            savedMeals = meals
        }
    }

    when (savedMeals) {
        is ApiResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiResult.Success -> {
            val items = (savedMeals as ApiResult.Success<List<MealCartEntity>>).data
            CartOrdersScreen(items.toMutableStateList())
        }

        is ApiResult.Error -> {
        }
    }


}

@Composable
fun CartManagedView(items: MutableList<MealCartEntity>) {
    val itemQuantities = remember { mutableStateMapOf<Int, Int>() }
    val cartViewModel: CartViewModel = koinViewModel()
    val ordersViewModel: OrdersViewModel = koinViewModel()
    val totalPrice = items.sumOf { item ->
        val quantity = itemQuantities[item.id] ?: 1
        item.price * quantity
    } + if (items.isNotEmpty()) 50 else 0 //delivery charges
    val context = LocalContext.current
    val paymentStatus = remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val paymentId = result.data?.getStringExtra("PAYMENT_ID")
            paymentStatus.value = "$paymentId"
        } else {
            val errorMsg = result.data?.getStringExtra("ERROR_MESSAGE")
            paymentStatus.value = "Payment Failed: $errorMsg"
        }
    }


    Scaffold(


        floatingActionButton = {
            if (items.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, PaymentActivity::class.java).apply {
                            putExtra("AMOUNT", totalPrice)
                        }
                        launcher.launch(intent)
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
                            text = "Rs. $totalPrice",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Optional padding for some space around
                    contentAlignment = Alignment.Center // Center the content
                ) {
                    Text(
                        text = "No meals in Cart",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(items.size) { index ->
                val item = items[index]
                val quantity = itemQuantities[item.id] ?: 1
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .clickable { /* Handle click */ },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        items[index].imageUrl?.let { imageUrl ->
                            EnhancedImageFromUrlWidth(imageUrl, 80, 70)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = items[index].name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Rs. ${items[index].price}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    if (quantity > 1) itemQuantities[item.id] = quantity - 1
                                }) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_minus_circle_24),
                                        contentDescription = "Delivered Success",
                                        modifier = Modifier.size(24.dp),
                                    )
                                }

                                Text(
                                    text = "Quantity: $quantity",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                IconButton(onClick = {
                                    if (quantity < 10) itemQuantities[item.id] = quantity + 1
                                }) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_add_circle_24),
                                        contentDescription = "Increase quantity"
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Total: Rs. ${items[index].price * quantity} ${
                                    if (items.size == 1) "(including Rs. 50 Delivery Charge)" else ""
                                }",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hotel : ${items[index].restaurantName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(onClick = {
                                    cartViewModel.deleteMealsByID("${items[index].id}")
                                    items.remove(item)
                                }) {
                                    Text("Remove")
                                }
                                TextButton(onClick = { /* TODO: Move to Wishlist */ }) {
                                    Text("Add Wishlist")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Show Payment Status
        paymentStatus.value?.let {
            val currentTime = System.currentTimeMillis()
            val orders = items.mapNotNull { item ->
                item.restaurantName?.let { restaurantName ->
                    OrderEntity(
                        mealId = item.id,
                        mealName = item.name,
                        totalPrice = item.price,
                        quantity = itemQuantities[item.id] ?: 1,
                        sellerName = restaurantName,
                        paymentId = paymentStatus.toString(), // Replace with actual payment ID if available
                        orderTime = currentTime,
                        mealThumb = item.imageUrl
                    )
                }
            }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    // Save all orders
                    ordersViewModel.saveAllOrder(orders)
                    //save on firebase
//                    ordersViewModel.saveOrdersToFirestore(orders)
                    // Clear cart after order success
                    cartViewModel.deleteAllMealsAfterOrderSuccess()
                }
            }
            Text(
                text = it,
                modifier = Modifier.padding(16.dp),
                color = if (it.contains("Successful")) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
