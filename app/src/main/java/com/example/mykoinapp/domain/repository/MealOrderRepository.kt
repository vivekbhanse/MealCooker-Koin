package com.example.mykoinapp.domain.repository

import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.data.local.roomdb.OrderEntity

interface MealOrderRepository {
   suspend fun getAllOrders():List<OrderEntity>
   suspend fun saveAllOrders(order: List<OrderEntity>)
   suspend fun clearAllOrders()

}