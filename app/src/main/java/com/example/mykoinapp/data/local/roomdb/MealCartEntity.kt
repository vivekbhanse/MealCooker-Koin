package com.example.mykoinapp.data.local.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "meals_cart")
data class MealCartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val restaurantName: String,
    val isAvailable: Boolean = true
)

