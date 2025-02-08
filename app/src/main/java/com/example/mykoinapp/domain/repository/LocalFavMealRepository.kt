package com.example.mykoinapp.domain.repository


import com.example.mykoinapp.data.local.roomdb.MealEntity

interface LocalFavMealRepository {
    suspend fun getMeals(): List<MealEntity>
    suspend fun insertMeal(meal: MealEntity)
}