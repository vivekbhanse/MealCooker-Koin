package com.example.mykoinapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.domain.usecases.LetterApiUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchableListViewModel(private val letterApiUseCase: LetterApiUseCase) : ViewModel() {
    val TAG = "SearchableListViewModel"
    private var _searchResults = MutableStateFlow<ApiResult<MealResponse>>(ApiResult.Loading)
    val searchResults = _searchResults.asStateFlow()



    fun getMealByLetter(letter: String = "b") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                letterApiUseCase(letter).collect { mealState ->
                    _searchResults.value = mealState
                }

            } catch (e: Exception) {
                Timber.d(TAG, e.message)
            }
        }

    }
}