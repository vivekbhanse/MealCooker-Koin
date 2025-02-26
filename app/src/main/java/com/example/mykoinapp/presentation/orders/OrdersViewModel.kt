package com.example.mykoinapp.presentation.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.local.roomdb.OrderEntity
import com.example.mykoinapp.data.remote.UserOrders
import com.example.mykoinapp.domain.usecases.meal_orders.MealOrderUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrdersViewModel(
    private val mealOrderUseCase: MealOrderUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _ordersState = MutableStateFlow<List<OrderEntity>>(emptyList())
    val ordersState: StateFlow<List<OrderEntity>> = _ordersState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    fun getAllOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val orders = mealOrderUseCase.getAllOrders()
            _ordersState.value = orders
        }
    }

    fun saveAllOrder(orders: List<OrderEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                mealOrderUseCase.saveAllOrders(orders)
            }.await()

        }

    }
    fun clearAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                mealOrderUseCase.clearAllOrders()
            }.await()

        }

    }

    fun saveOrdersToFirestore(orders: List<OrderEntity>) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val ordersCollectionRef =
            firestore.collection("users").document(userId).collection("orders")

        viewModelScope.launch {
            try {
                orders.forEach { order ->
                    val orderDocRef = ordersCollectionRef.document(
                        "${
                            order.mealId.toString().plus(order.paymentId)
                        }"
                    ) // Use unique ID
                    orderDocRef.set(order).await()
                }
                Log.d("Firestore", "Orders added as separate documents")
            } catch (e: Exception) {
                Log.e("Firestore", "Error saving orders", e)
            }
        }
    }


//    fun getOrdersFromFirestore() {
//        val userId = firebaseAuth.currentUser?.uid ?: return
//        val userDocRef = firestore.collection("users").document(userId)
//
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                val snapshot = userDocRef.get().await()
//                if (snapshot.exists()) {
//                    val orders = snapshot.get("orders") as? List<Map<String, Any>> ?: emptyList()
//                    val orderList = orders.map { map ->
//                        OrderEntity(
//                            mealId = map["mealId"] as? Int ?: 0,
//                            orderId = map["orderId"] as? Int ?: 0,
//                            mealName = map["mealName"] as? String ?: "",
//                            paymentId = map["paymentId"] as? String ?: "",
//                            mealThumb = map["mealThumb"] as? String ?: "",
//                            sellerName = map["sellerName"] as? String ?: "",
//                            quantity = (map["quantity"] as? Long)?.toInt() ?: 0,
//                            totalPrice = (map["totalPrice"] as? Double) ?: 0.0,
//                            orderTime = (map["orderTime"] as? Long) ?: 0,
//                        )
//                    }
//                    _ordersState.value = orderList
//                } else {
//                    _ordersState.value = emptyList()
//                }
//            } catch (e: Exception) {
//                Log.e("Firestore", "Error fetching orders", e)
//                _ordersState.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun getOrdersFromFirestore() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val ordersCollectionRef = firestore.collection("users").document(userId).collection("orders")

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val snapshot = ordersCollectionRef.get().await()
                val orders = snapshot.documents.mapNotNull { it.toObject(OrderEntity::class.java) }
                _ordersState.value = orders
                Log.d("Firestore", "Orders fetched successfully")
            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching orders", e)
                _ordersState.value = emptyList()
            }finally {

                _isLoading.value = false
            }
        }
    }

    fun signout() {
        firebaseAuth.signOut()
    }


}