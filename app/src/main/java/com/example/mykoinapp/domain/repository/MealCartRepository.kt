package com.example.mykoinapp.domain.repository

import com.example.mykoinapp.data.local.roomdb.MealCartDao
import com.example.mykoinapp.data.local.roomdb.MealCartEntity
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow

interface MealCartRepository {

    suspend fun getMealsCart(): Flow<ApiResult<List<MealCartEntity>>>
    suspend fun insertMealCart(mealCart: MealCartEntity)
    suspend fun deleteAllMealCart()
    suspend fun deleteCartMealById(mealId:String)

}