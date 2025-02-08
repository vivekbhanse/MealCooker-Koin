package com.example.mykoinapp.domain.usecases

import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent


class LetterApiUseCase(private val mealRepository: MealRepository):KoinComponent {
    suspend operator fun invoke(letter: String): Flow<ApiResult<MealResponse>> =
        mealRepository.getMealsByLetter(letter)
}
