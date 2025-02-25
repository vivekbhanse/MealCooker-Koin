package com.example.mykoinapp.domain.usecases.meal_orders

import com.example.mykoinapp.data.local.roomdb.OrderEntity
import com.example.mykoinapp.domain.repository.MealOrderRepository

class MealOrderUseCase(private val mealOrderRepository: MealOrderRepository) {
    suspend fun getAllOrders():List<OrderEntity>{
        return mealOrderRepository.getAllOrders()
    }

    suspend fun saveAllOrders(order: List<OrderEntity>){
        return mealOrderRepository.saveAllOrders(order)
    }
}