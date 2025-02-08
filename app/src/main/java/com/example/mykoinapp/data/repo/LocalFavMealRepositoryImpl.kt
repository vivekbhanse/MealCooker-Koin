package com.example.mykoinapp.data.repo

import com.example.mykoinapp.data.dto.MealDB
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.repository.LocalFavMealRepository

class LocalFavMealRepositoryImpl(private val localFavMealRepository: LocalFavMealRepository) : LocalFavMealRepository {

    override suspend fun getMeals(): List<MealEntity> {
        return localFavMealRepository.getMeals()
    }

    override suspend fun insertMeal(meal: MealEntity) {
        localFavMealRepository.insertMeal(meal)
    }

    private fun MealEntity.toDomainModel() =
        MealDB(id, name, imgThumb, instructions, area, category)
    //  private fun Meal.toEntity() = MealEntity(id = idMeal, name, description)
}