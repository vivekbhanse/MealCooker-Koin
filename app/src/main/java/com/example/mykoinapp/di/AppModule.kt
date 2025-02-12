package com.example.mykoinapp.di

import androidx.room.Room
import com.example.mykoinapp.data.local.roomdb.AppDatabase
import com.example.mykoinapp.data.remote.ApiService
import com.example.mykoinapp.data.repo.LocalFavMealRepositoryImpl
import com.example.mykoinapp.data.repo.MealRepositoryImpl
import com.example.mykoinapp.domain.repository.LocalFavMealRepository
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.usecases.CategoryApiUseCase
import com.example.mykoinapp.domain.usecases.CheckFavoriteMealUseCase
import com.example.mykoinapp.domain.usecases.FavoriteMealsUseCase
import com.example.mykoinapp.domain.usecases.LetterApiUseCase
import com.example.mykoinapp.domain.usecases.MealByIdUseCase
import com.example.mykoinapp.domain.usecases.MealsByCategoriesUseCase
import com.example.mykoinapp.domain.usecases.SaveUnSaveMealUseCase
import com.example.mykoinapp.presentation.fav_meal.FavoriteMealViewModel
import com.example.mykoinapp.presentation.home.HomeViewModel
import com.example.mykoinapp.presentation.mealsSeletion.MealDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Data Module (for Retrofit, Room, and DAOs)
val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "meal_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().mealDao() }
}

// Repository Module
val repositoryModule = module {
    factory<MealRepository> { MealRepositoryImpl(get()) }
    factory<LocalFavMealRepository> { LocalFavMealRepositoryImpl(get()) }
}

// UseCase Module
val useCaseModule = module {
    factory<LetterApiUseCase> { LetterApiUseCase(get()) }
    factory<CategoryApiUseCase> { CategoryApiUseCase(get()) }
    factory { MealByIdUseCase(get()) }
    factory { FavoriteMealsUseCase(get()) }
    factory { SaveUnSaveMealUseCase(get()) }
    factory { CheckFavoriteMealUseCase(get()) }
    factory { MealsByCategoriesUseCase(get()) }
}

// ViewModel Module
val viewModelModule = module {
    viewModel { HomeViewModel(get(), get() ,get()) }
    viewModel { MealDetailsViewModel(get(), get(), get()) }
    viewModel { FavoriteMealViewModel(get()) }
}

// App Module (combining all other modules)
val appModule = module {
    includes(dataModule, repositoryModule, useCaseModule, viewModelModule)
}


