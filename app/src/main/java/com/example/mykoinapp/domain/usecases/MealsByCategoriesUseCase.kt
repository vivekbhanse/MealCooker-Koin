package com.example.mykoinapp.domain.usecases

import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class MealsByCategoriesUseCase (private val mealRepository: MealRepository){
    suspend operator fun invoke(selectedCategory: String): Flow<ApiResult<MealResponse>> =
        mealRepository.getMealsByCategoryName(selectedCategory)
}