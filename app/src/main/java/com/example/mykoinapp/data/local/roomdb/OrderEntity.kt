package com.example.mykoinapp.data.local.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val mealId: Int,
    val mealName: String,
    val quantity: Int,
    val totalPrice: Double,
    val sellerName: String,
    val paymentId: String,
    val mealThumb: String,
    val isDelivered :Boolean =false,
    val orderTime: Long
)
