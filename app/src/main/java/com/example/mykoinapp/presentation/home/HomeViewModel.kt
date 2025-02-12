package com.example.mykoinapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.dto.Category
import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.domain.usecases.CategoryApiUseCase
import com.example.mykoinapp.domain.usecases.LetterApiUseCase
import com.example.mykoinapp.domain.usecases.MealsByCategoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val letterApiUseCase: LetterApiUseCase,
    private val categoryApiUseCase: CategoryApiUseCase,
    private val mealsByCategoriesUseCase: MealsByCategoriesUseCase
) : ViewModel() {
    val TAG = "HomeViewModel"
    private var _mealState = MutableStateFlow<ApiResult<MealResponse>>(ApiResult.Loading)
    val mealState = _mealState.asStateFlow()

    private var _mealStateCategory =
        MutableStateFlow<ApiResult<CategoryResponse>>(ApiResult.Loading)
    val mealStateCategory = _mealStateCategory.asStateFlow()

    private var _mealStateCategoryNamed =
        MutableStateFlow<ApiResult<MealResponse>>(ApiResult.Loading)
    val mealStateCategoryNamed = _mealStateCategoryNamed.asStateFlow()

    init {
        getMealByCategories()
    }

    fun getMealByLetter(letter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                letterApiUseCase(letter).collect { mealState ->
                    _mealState.value = mealState
                }

            } catch (e: Exception) {
                Timber.d(TAG, e.message)
            }
        }

    }

    private fun getMealByCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                categoryApiUseCase().collect { mealStateCategory ->
                    _mealStateCategory.value = mealStateCategory
                }

            } catch (e: Exception) {
                Timber.d(TAG, e.message)
            }
        }

    }

    fun getMealsByCategoriesNamed(selectedCategory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mealsByCategoriesUseCase(selectedCategory).collect { mealStateCategory ->
                    _mealStateCategoryNamed.value = mealStateCategory
                }

            } catch (e: Exception) {
                Timber.d(TAG, e.message)
            }
        }

    }
}