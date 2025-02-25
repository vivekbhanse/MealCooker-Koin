package com.example.mykoinapp.domain.usecases.meal_orders

import com.example.mykoinapp.data.local.roomdb.MealCartEntity
import com.example.mykoinapp.domain.repository.MealCartRepository
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow

class MealCartUseCase(private val mealCartRepository: MealCartRepository) {
    suspend fun saveCartMeals(mealCartEntity: MealCartEntity) {
        mealCartRepository.insertMealCart(mealCartEntity)
    }

    suspend fun getAllCartMeals(): Flow<ApiResult<List<MealCartEntity>>> {
        return mealCartRepository.getMealsCart()
    }

    suspend fun deleteMeals() {
        return mealCartRepository.deleteAllMealCart()
    }

    suspend fun deleteMealsById(mealId:String) {
        return mealCartRepository.deleteCartMealById(mealId)
    }
}