package com.example.mykoinapp.data.repo

import com.example.mykoinapp.data.local.roomdb.MealOrderDao
import com.example.mykoinapp.data.local.roomdb.OrderEntity
import com.example.mykoinapp.domain.repository.MealOrderRepository

class MealOrderDaoImpl(private val mealOrderDao: MealOrderDao) : MealOrderRepository {
    override suspend fun getAllOrders(): List<OrderEntity> {
       return mealOrderDao.getOrders()
    }

    override suspend fun saveAllOrders(order: List<OrderEntity>) {
        return mealOrderDao.insertOrder(order)
    }


}