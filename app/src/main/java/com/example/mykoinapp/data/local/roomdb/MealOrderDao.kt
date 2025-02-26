package com.example.mykoinapp.data.local.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealOrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: List<OrderEntity>)

    @Query("SELECT * FROM orders ORDER BY orderId DESC")
    suspend fun getOrders(): List<OrderEntity>

    @Query("DELETE FROM orders")
    suspend fun clearOrders()
}