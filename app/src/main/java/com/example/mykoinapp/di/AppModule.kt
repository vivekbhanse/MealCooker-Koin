package com.example.mykoinapp.di


import androidx.room.Room
import com.example.mykoinapp.data.local.roomdb.AppDatabase
import com.example.mykoinapp.data.remote.ApiService
import com.example.mykoinapp.data.repo.FavMealRepositoryImpl
import com.example.mykoinapp.data.repo.MealRepositoryImpl
import com.example.mykoinapp.domain.repository.FavMealRepository
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.usecases.CategoryApiUseCase
import com.example.mykoinapp.domain.usecases.FavoriteMealsUseCase
import com.example.mykoinapp.domain.usecases.LetterApiUseCase
import com.example.mykoinapp.domain.usecases.MealByIdUseCase
import com.example.mykoinapp.presentation.fav_meal.FavoriteMealViewModel
import com.example.mykoinapp.presentation.home.HomeViewModel
import com.example.mykoinapp.presentation.mealsSeletion.MealDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule: Module = module {

    // Retrofit instance
    single {
        Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/") // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

// Room Database
    single {
        Room.databaseBuilder(
            get(), // Context is injected automatically by Koin
            AppDatabase::class.java, // AppDatabase should be used here, not RoomDatabase
            "meal_database"
        ).fallbackToDestructiveMigration() // Useful for development
            .build()
    }

    // Provide MealDao instance
    single { get<AppDatabase>().mealDao() }


    // Repositories
    single<MealRepository> { MealRepositoryImpl(get()) }
    single<FavMealRepository> { FavMealRepositoryImpl(get()) }

    // UseCase instances
    single<LetterApiUseCase> { LetterApiUseCase(get()) }
    single<CategoryApiUseCase> { CategoryApiUseCase(get()) }
    single { MealByIdUseCase(get()) }
    single { FavoriteMealsUseCase(get()) }

    // ViewModel definitions
    viewModel<HomeViewModel> { HomeViewModel(get(), get()) }
    viewModel<MealDetailsViewModel> { (MealDetailsViewModel(get())) }
    viewModel<FavoriteMealViewModel> { (FavoriteMealViewModel(get())) }



}
