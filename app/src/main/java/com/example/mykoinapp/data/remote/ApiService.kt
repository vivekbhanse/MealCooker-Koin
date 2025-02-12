package com.example.mykoinapp.data.remote

import com.example.mykoinapp.data.dto.CategoryResponse
import com.example.mykoinapp.data.dto.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("search.php")
    suspend fun getMealsByLetter(@Query("f") letter: String): MealResponse

    @GET("categories.php")
    suspend fun getMealsByCategory(): CategoryResponse

    @GET("lookup.php")
    suspend fun getMealsById(@Query("i") mealId:String):MealResponse

    @GET("filter.php")
    suspend fun getMealsByCategoryName(@Query("c") selectedCategory: String): MealResponse

}