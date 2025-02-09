package com.example.mykoinapp.domain.usecases

import com.example.mykoinapp.domain.repository.LocalFavMealRepository

class CheckFavoriteMealUseCase (private val localFavMealRepository: LocalFavMealRepository) {
    suspend  fun invoke1(mealId: Int): Boolean  {
        return localFavMealRepository.existFavoriteMeal(mealId)
    }
    suspend  fun invoke2(mealId: Int)  {
        return localFavMealRepository.existsDeleteMealById(mealId)
    }
}