package com.example.mykoinapp.data.repo

import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.data.local.roomdb.MealCartDao
import com.example.mykoinapp.data.local.roomdb.MealCartEntity
import com.example.mykoinapp.domain.repository.MealCartRepository
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class MealCartDaoImpl(private val mealCartDao: MealCartDao) : MealCartRepository {

    override suspend fun insertMealCart(mealCart: MealCartEntity) {
        mealCartDao.insertOrder(mealCart)
    }

    override suspend fun deleteAllMealCart() {
        val response = mealCartDao.deleteAllOrders()
    }

    override suspend fun deleteCartMealById(mealId: String) {
        val response = mealCartDao.deleteMealById(mealId.toInt())
    }

    override suspend fun getMealsCart(): Flow<ApiResult<List<MealCartEntity>>> = flow {
        emit(ApiResult.Loading)
        val response = mealCartDao.getOrderAll()
        emit(ApiResult.Success(response))
    }.onStart { emit(ApiResult.Loading) }
        .catch { e -> emit(ApiResult.Error(e.message ?: "Error")) } // Handle errors



}