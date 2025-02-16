package com.example.mykoinapp.domain.model

data class MealResponseDomain (
    val id: String,
    val area: String,
    val category: String,
    val ingredients: List<String>,
    val instructions: String,
    val mealName: String,
    val mealThumb: String
)