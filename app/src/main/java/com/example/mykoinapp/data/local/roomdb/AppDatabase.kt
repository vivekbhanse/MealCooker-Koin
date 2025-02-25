package com.example.mykoinapp.data.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MealEntity::class,MealCartEntity::class,OrderEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun orderDao(): MealOrderDao
    abstract fun cartDao(): MealCartDao
}