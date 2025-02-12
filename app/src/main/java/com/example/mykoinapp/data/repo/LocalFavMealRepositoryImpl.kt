package com.example.mykoinapp.data.repo

import com.example.mykoinapp.data.dto.MealDB
import com.example.mykoinapp.data.local.roomdb.MealDao
import com.example.mykoinapp.data.local.roomdb.MealEntity
import com.example.mykoinapp.domain.repository.LocalFavMealRepository

class LocalFavMealRepositoryImpl(private val mealDao: MealDao) : LocalFavMealRepository {

    override suspend fun getMeals(): List<MealEntity> {
        return mealDao.getAllFavoriteMeals()
    }

    override suspend fun insertMeal(meal: MealEntity) {
        mealDao.insert(meal)
    }

    override suspend fun existFavoriteMeal(meal: Int): Boolean {
        return mealDao.doesMealExist(meal)
    }

    override suspend fun existsDeleteMealById(mealId: Int):Int {
        return mealDao.deleteMealById(mealId)
    }

    private fun MealEntity.toDomainModel() =
        MealDB(id, name, imgThumb, instructions, area, category)
    //  private fun Meal.toEntity() = MealEntity(id = idMeal, name, description)
}