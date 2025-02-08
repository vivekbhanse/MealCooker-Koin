package com.example.mykoinapp.di

import androidx.room.Room
import com.example.mykoinapp.data.local.roomdb.AppDatabase
import com.example.mykoinapp.data.remote.ApiService
import com.example.mykoinapp.data.repo.LocalFavMealRepositoryImpl
import com.example.mykoinapp.data.repo.MealRepositoryImpl
import com.example.mykoinapp.domain.repository.LocalFavMealRepository
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.usecases.CategoryApiUseCase
import com.example.mykoinapp.domain.usecases.FavoriteMealsUseCase
import com.example.mykoinapp.domain.usecases.LetterApiUseCase
import com.example.mykoinapp.domain.usecases.MealByIdUseCase
import com.example.mykoinapp.domain.usecases.SaveUnSaveMealUseCase
import com.example.mykoinapp.presentation.fav_meal.FavoriteMealViewModel
import com.example.mykoinapp.presentation.home.HomeViewModel
import com.example.mykoinapp.presentation.mealsSeletion.MealDetailsViewModel

import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule: Module = module {

    // Retrofit instance (singleton)
    single {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Room Database (singleton)
    single {
        Room.databaseBuilder(
            get(), // Context is injected automatically by Koin
            AppDatabase::class.java,
            "meal_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // Provide MealDao instance (singleton)
    single { get<AppDatabase>().mealDao() }

    // Repositories
    factory<MealRepository> { MealRepositoryImpl(get()) }
    factory<LocalFavMealRepository> { LocalFavMealRepositoryImpl(get()) }

    // UseCase instances
    factory<LetterApiUseCase> { LetterApiUseCase(get()) }
    factory<CategoryApiUseCase> { CategoryApiUseCase(get()) }
    factory { MealByIdUseCase(get()) }
    factory { FavoriteMealsUseCase(get()) }
    factory { SaveUnSaveMealUseCase(get()) }

    // ViewModel definitions (with factory for new instance each time)
    factory { HomeViewModel(get(), get()) }

    // For parameterized ViewModels, use parametersOf()
    factory {
        MealDetailsViewModel(get(), get())
    }

    factory {
        FavoriteMealViewModel(get())
    }
}
