package com.example.mykoinapp.data.dto

import com.google.gson.annotations.SerializedName

data class MealResponse(
    @SerializedName("meals") val meals: List<Meal>?=null
)