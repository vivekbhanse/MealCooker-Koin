package com.example.mykoinapp.data.repo

import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.data.remote.ApiService
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.states.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.onStart


class MealRepositoryImpl(private val apiService: ApiService) : MealRepository {
    override suspend fun getMealsByLetter(letter: String): Flow<ApiResult<MealResponse>> = flow {
        emit(ApiResult.Loading)  // Emit loading state
        val response = apiService.getMealsByLetter(letter) // Make the API call
        emit(ApiResult.Success(response)) // Emit success with data
    }.onStart { emit(ApiResult.Loading) } // Ensure loading state at start
        .catch { e -> emit(ApiResult.Error(e.message ?: "Error")) } // Handle errors

    override suspend fun getMealsByCategory(): Flow<ApiResult<CategoryResponse>> = flow {
        emit(ApiResult.Loading)  // Emit loading state
        val response = apiService.getMealsByCategory() // Make the API call
        emit(ApiResult.Success(response)) // Emit success with data
    }.onStart { emit(ApiResult.Loading) } // Ensure loading state at start
        .catch { e -> emit(ApiResult.Error(e.message ?: "Error")) } //
}

