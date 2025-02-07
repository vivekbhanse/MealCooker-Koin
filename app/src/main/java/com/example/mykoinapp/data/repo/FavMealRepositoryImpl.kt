package com.example.mykoinapp.data.repo

import com.example.mykoinapp.data.dto.Meal
import com.example.mykoinapp.data.dto.MealDB
import com.example.mykoinapp.data.local.roomdb.MealDao
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.repository.FavMealRepository

class FavMealRepositoryImpl(private val favMealRepository: FavMealRepository) : FavMealRepository {

    override suspend fun getMeals(): List<MealEntity> {
        return favMealRepository.getMeals()
    }

    override suspend fun insertMeal(meal: MealEntity) {
        favMealRepository.insertMeal(meal)
    }

    private fun MealEntity.toDomainModel() =
        MealDB(id, name, imgThumb, instructions, area, category)
    //  private fun Meal.toEntity() = MealEntity(id = idMeal, name, description)
}