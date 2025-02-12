package com.example.mykoinapp.data.local.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: MealEntity)

    @Query("SELECT * FROM `favorite-meals`")
    suspend fun getAllFavoriteMeals(): List<MealEntity>

    @Query("SELECT EXISTS(SELECT * FROM `favorite-meals` WHERE id = :mealId)")
    suspend fun doesMealExist(mealId: Int): Boolean

    @Query("DELETE FROM `favorite-meals` WHERE id = :mealId")
    suspend fun deleteMealById(mealId: Int):Int
}