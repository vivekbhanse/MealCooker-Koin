package com.example.mykoinapp.domain.repository


import com.example.mykoinapp.data.dto.MealDB
import com.example.mykoinapp.data.local.roomdb.MealEntity

interface FavMealRepository {
    suspend fun getMeals(): List<MealEntity>
    suspend fun insertMeal(meal: MealEntity)
}