package com.example.mykoinapp.data.local.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite-meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imgThumb: String,
    val instructions: String,
    val area: String,
    val category: String,
)