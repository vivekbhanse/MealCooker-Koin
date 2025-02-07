package com.example.mykoinapp.domain.usecases

import com.example.mykoinapp.data.dto.MealDB
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.repository.FavMealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteMealsUseCase(private val favMealRepository: FavMealRepository) {
    suspend operator fun invoke(): List<MealEntity>  {
        return favMealRepository.getMeals()
    }

}