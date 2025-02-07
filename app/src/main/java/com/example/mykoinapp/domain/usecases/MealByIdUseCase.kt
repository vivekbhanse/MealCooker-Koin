package com.example.mykoinapp.domain.usecases

import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow

class MealByIdUseCase(private val mealRepository: MealRepository) {
    suspend operator fun invoke(mealId: String): Flow<ApiResult<MealResponse>> =
        mealRepository.getMealsById(mealId)
}
