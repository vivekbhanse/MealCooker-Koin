package com.example.mykoinapp.data.local.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val mealId: Int = 0,
    val mealName: String = "",
    val quantity: Int = 0,
    val totalPrice: Double = 0.0,
    val sellerName: String = "",
    val paymentId: String = "",
    val mealThumb: String = "",
    val isDelivered: Boolean = false,
    val orderTime: Long = 0L
) {
    constructor() : this(0, 0, "", 0, 0.0, "", "", "", false, 0L) // No-arg constructor
}

