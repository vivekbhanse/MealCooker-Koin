package com.example.mykoinapp.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.local.roomdb.OrderEntity
import com.example.mykoinapp.domain.usecases.meal_orders.MealOrderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val mealOrderUseCase: MealOrderUseCase) : ViewModel() {
    private val _ordersState = MutableStateFlow<List<OrderEntity>>(emptyList())
    val ordersState: StateFlow<List<OrderEntity>> = _ordersState

    fun getAllOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val orders = mealOrderUseCase.getAllOrders()
            _ordersState.value = orders
        }
    }

    fun saveAllOrder(orders:List<OrderEntity>){
        viewModelScope.launch (Dispatchers.IO){
            val result = async {
                mealOrderUseCase.saveAllOrders(orders)
            }.await()

        }

    }
}