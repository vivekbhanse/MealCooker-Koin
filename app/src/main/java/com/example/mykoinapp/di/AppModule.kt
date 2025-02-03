package com.example.mykoinapp.di

import com.example.mykoinapp.data.remote.ApiService
import com.example.mykoinapp.data.repo.MealRepositoryImpl
import com.example.mykoinapp.domain.repository.MealRepository
import com.example.mykoinapp.domain.usecases.CategoryApiUseCase
import com.example.mykoinapp.domain.usecases.LetterApiUseCase
import com.example.mykoinapp.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule: Module = module {
    single {
        Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    single<LetterApiUseCase> {
        LetterApiUseCase(get())
    }

    single<CategoryApiUseCase> {
        CategoryApiUseCase(get())
    }

    // Provide Repository
    single<MealRepository> { MealRepositoryImpl(get()) }

    // Provide ViewModel
    viewModel<HomeViewModel> { HomeViewModel(get(), get()) }
}