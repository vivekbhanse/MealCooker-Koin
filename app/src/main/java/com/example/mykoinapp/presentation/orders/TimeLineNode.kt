package com.example.mykoinapp.presentation.orders

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mykoinapp.R
import com.example.mykoinapp.data.local.roomdb.OrderEntity
import com.example.mykoinapp.presentation.home.EnhancedImageFromUrlWidth
import com.example.mykoinapp.ui.theme.DarkBlue
import com.example.mykoinapp.ui.theme.DarkGreen
import com.pushpal.jetlime.EventPointType
import com.pushpal.jetlime.HorizontalAlignment
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeRow


@Composable
fun TimelineWithJetLime( orderedItems: List<OrderEntity>) {
    val items = listOf("Preparing\n food", "Assign to\nDelivery\npartner", "Delivered")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
    ) {
        items(orderedItems.size) { index ->
            val order = orderedItems[index]
            val completedStages = getOrderTimeRange(order.orderTime)
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        EnhancedImageFromUrlWidth(order.mealThumb, 50, 120)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = order.mealName,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Rs. ${order.totalPrice * order.quantity}",
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )

                                if (completedStages.isEmpty()) {
                                    Image(
                                        painter = painterResource(id = R.drawable.delivered_stamp),
                                        contentDescription = "Delivered Success",
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }
                        }
                    }
                    if (completedStages.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center // Center the content horizontally and vertically
                        ) {
                            JetLimeRow(
                                modifier = Modifier.padding(16.dp),
                                itemsList = ItemsList(items),
                                key = { _, item -> item },
                            ) { index, item, position ->
                                val isCompleted = completedStages.contains(index)
                                val pointColor by animateColorAsState(
                                    targetValue = if (isCompleted) DarkGreen else Color.Red,
                                    animationSpec = tween(durationMillis = 1000)
                                )
                                val pointFillColor by animateColorAsState(
                                    targetValue = if (isCompleted) Color.White else Color(0xFFD5F2FF),
                                    animationSpec = tween(durationMillis = 1000)
                                )

                                JetLimeEvent(
                                    style = JetLimeEventDefaults.eventStyle(
                                        position = position,
                                        pointColor = pointColor,
                                        pointFillColor = pointFillColor,
                                        pointRadius = 14.dp,
                                        pointAnimation = JetLimeEventDefaults.pointAnimation(),
                                        pointType = EventPointType.filled(0.5f),
                                        pointStrokeWidth = 2.dp,
                                        pointStrokeColor = MaterialTheme.colorScheme.onBackground,
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        if (isCompleted) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_check_circle_outline_24),
                                                contentDescription = "Green Tick",
                                                tint = DarkGreen,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_await_24),
                                                contentDescription = "Red Tick",
                                                tint = Color.Red,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = item, fontSize = 12.sp) // Display the item name
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
fun getOrderTimeStatus(orderTime: Long): Int {
    val currentTime = System.currentTimeMillis()

    val fifteenMinutesAgo = currentTime - (15 * 60 * 1000) // 15 minutes in milliseconds
    val thirtyMinutesAgo = currentTime - (30 * 60 * 1000)  // 30 minutes in milliseconds
    val oneHourAgo = currentTime - (60 * 60 * 1000)        // 1 hour in milliseconds

    return when {
        orderTime >= fifteenMinutesAgo -> 0
        orderTime >= thirtyMinutesAgo -> 1
        orderTime >= oneHourAgo -> 2
        else -> -1 // More than 1 hour ago
    }
}

fun getOrderTimeRange(orderTime: Long): List<Int> {
    return when (val status = getOrderTimeStatus(orderTime)) {
        2 -> listOf(0, 1, 2)
        1 -> listOf(0, 1)
        0 -> listOf(0)
        else -> emptyList() // If more than 1 hour ago
    }
}



