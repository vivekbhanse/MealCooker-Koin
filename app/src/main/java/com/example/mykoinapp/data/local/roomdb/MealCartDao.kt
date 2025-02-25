package com.example.mykoinapp.data.local.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: MealCartEntity)

    @Query("SELECT * FROM meals_cart")
    suspend fun getOrderAll(): List<MealCartEntity>

    @Query("DELETE FROM meals_cart")
    suspend fun deleteAllOrders()

    @Query("DELETE FROM meals_cart WHERE id = :mealId")
    suspend fun deleteMealById(mealId:Int)
}