package com.example.mykoinapp.presentation.mealsSeletion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.domain.usecases.CheckFavoriteMealUseCase
import com.example.mykoinapp.domain.usecases.FavoriteMealsUseCase
import com.example.mykoinapp.domain.usecases.MealByIdUseCase
import com.example.mykoinapp.domain.usecases.SaveUnSaveMealUseCase
import com.example.mykoinapp.presentation.fav_meal.FavoriteMealViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MealDetailsViewModel(
    private val mealByIdUseCase: MealByIdUseCase,
    private val saveUnSaveMealUseCase: SaveUnSaveMealUseCase,
    private val checkFavoriteMealUseCase: CheckFavoriteMealUseCase
) : ViewModel() {
    val TAG = "MealDetailsViewModel"
    private var _mealIdState = MutableStateFlow<ApiResult<MealResponse>>(ApiResult.Loading)
    val mealIdState = _mealIdState.asStateFlow()

    private var _isMealExist = MutableStateFlow(false)
    val isMealExist = _isMealExist.asStateFlow()

    fun getMealById(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mealByIdUseCase(mealId).collect { mealIdState ->
                    _mealIdState.value = mealIdState
                }
            } catch (e: Exception) {
                Timber.d(TAG, e.message)
            }
        }

    }

     fun saveFavMealDB(mealEntity: MealEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveUnSaveMealUseCase.invoke(mealEntity)
            } catch (e: Exception) {
                Timber.d(TAG, e.message)
            }
        }

    }

    suspend fun existMealInDB(mealId: Int): Boolean {
        return try {
            checkFavoriteMealUseCase.invoke1(mealId)
        } catch (e: Exception) {
            Timber.d(TAG, "Error checking meal existence: ${e.message}")
            false // Return false in case of an error
        }
    }

    fun favoriteDeleteMeal(mealId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // If it exists, delete the meal from the DB
            checkFavoriteMealUseCase.invoke2(mealId)
        }
    }

}