package com.example.mykoinapp.presentation.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrdersManagedScreen() {
    val ordersViewModel: OrdersViewModel = koinViewModel()
    val placedOrders by ordersViewModel.ordersState.collectAsState(emptyList())
    LaunchedEffect(Unit) {
        ordersViewModel.getAllOrder()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Orders", fontWeight = FontWeight.Bold)

        if (placedOrders.isEmpty()) {
            Text(text = "No orders found", modifier = Modifier.padding(top = 16.dp))
        } else {
            TimelineWithJetLime(placedOrders)
        }
    }
}