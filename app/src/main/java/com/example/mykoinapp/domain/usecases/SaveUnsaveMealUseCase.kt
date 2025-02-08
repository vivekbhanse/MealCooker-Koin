package com.example.mykoinapp.domain.usecases

import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.repository.LocalFavMealRepository

class SaveUnSaveMealUseCase(private val localFavMealRepository: LocalFavMealRepository) {
    suspend operator fun invoke(meal: MealEntity) {
        return localFavMealRepository.insertMeal(meal)
    }
}