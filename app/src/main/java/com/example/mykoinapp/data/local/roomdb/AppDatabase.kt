package com.example.mykoinapp.data.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MealEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}