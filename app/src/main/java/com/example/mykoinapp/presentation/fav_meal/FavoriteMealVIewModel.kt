package com.example.mykoinapp.presentation.fav_meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealDB
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.domain.usecases.FavoriteMealsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteMealViewModel(private val favoriteMealsUseCase: FavoriteMealsUseCase) : ViewModel() {
    private var _mealFavorite =
        MutableStateFlow<List<MealEntity>>(emptyList())
    val mealFavorite = _mealFavorite.asStateFlow()

     init {
         getMeals()
     }
    fun getMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            _mealFavorite.value = favoriteMealsUseCase.invoke()
        }
    }
}