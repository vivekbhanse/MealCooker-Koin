package com.example.mykoinapp.presentation.fav_meal

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.usecases.CheckFavoriteMealUseCase
import com.example.mykoinapp.domain.usecases.FavoriteMealsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteMealViewModel(
    private val favoriteMealsUseCase: FavoriteMealsUseCase,
    private val checkFavoriteMealUseCase: CheckFavoriteMealUseCase
) : ViewModel() {
    private var _mealFavorite =
        MutableStateFlow<List<MealEntity>>(emptyList())
    val mealFavorite = _mealFavorite.asStateFlow()

    private var _mealUpdateFlag = MutableSharedFlow<Int>(0)
    val mealUpdateFlag = _mealUpdateFlag.asSharedFlow()

    init {
        getMeals()
    }

    fun getMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            _mealFavorite.value = favoriteMealsUseCase.invoke()
        }
    }

    fun deleteMealById(mealId: Int): Boolean {
        return viewModelScope.launch {
           val rowsDeleted = checkFavoriteMealUseCase.invoke2(mealId)
            if (rowsDeleted > 0) {
                // Reload the list of meals after deletion
                _mealFavorite.value = favoriteMealsUseCase.invoke()
                true // Deletion was successful
            } else {
                false // No rows affected, deletion failed
            }
        }.isCompleted

    }
}