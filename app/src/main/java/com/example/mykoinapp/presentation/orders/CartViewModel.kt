package com.example.mykoinapp.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.local.roomdb.MealCartEntity
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.domain.usecases.meal_orders.MealCartUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val mealCartUseCase: MealCartUseCase) : ViewModel() {
    private var _cartMeals = MutableStateFlow<ApiResult<List<MealCartEntity>>>(ApiResult.Loading)
    val cartMeals = _cartMeals.asStateFlow()

    init {
        getCartMeals()
    }

    fun saveCartMeals(mealCartEntity: MealCartEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mealCartUseCase.saveCartMeals(mealCartEntity)
            delay(200)
            getCartMeals()
        }
    }

    fun deleteAllMealsAfterOrderSuccess() {
        viewModelScope.launch(Dispatchers.IO) {
            mealCartUseCase.deleteMeals()
            delay(200)
            getCartMeals()
        }
    }

    fun deleteMealsByID(mealId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            mealCartUseCase.deleteMealsById(mealId)
//            delay(200)
//            getCartMeals()
        }
    }

    fun getCartMeals() = viewModelScope.launch {
        mealCartUseCase.getAllCartMeals()
            .collect { result ->
                _cartMeals.value = result
            }
    }


}