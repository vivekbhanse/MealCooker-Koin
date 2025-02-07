package com.example.mykoinapp.domain.repository

import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    suspend fun getMealsByLetter(letter:String):Flow<ApiResult<MealResponse>>
    suspend fun getMealsByCategory():Flow<ApiResult<CategoryResponse>>
    suspend fun getMealsById(mealId:String):Flow<ApiResult<MealResponse>>
}