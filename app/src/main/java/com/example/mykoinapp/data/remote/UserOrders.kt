package com.example.mykoinapp.data.remote

import com.example.mykoinapp.data.local.roomdb.OrderEntity

data class UserOrders(
    val orders: List<OrderEntity> = emptyList()
)