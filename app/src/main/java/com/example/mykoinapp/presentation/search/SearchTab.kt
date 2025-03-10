package com.example.mykoinapp.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mykoinapp.data.dto.Meal
import com.example.mykoinapp.data.dto.MealResponse
import com.example.mykoinapp.domain.states.ApiResult
import com.example.mykoinapp.presentation.home.EnhancedImageFromUrl
import com.example.mykoinapp.presentation.home.getRandomColor
import com.example.mykoinapp.ui.theme.color4
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchableList(navController: NavController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val viewModelSearch: SearchableListViewModel = getViewModel()
    val searchResults by viewModelSearch.searchResults.collectAsState()
    LaunchedEffect(searchQuery.text) {
        if(searchQuery.text.isNotEmpty()) {
            viewModelSearch.getMealByLetter(searchQuery.text)
        }
        else{
            viewModelSearch.getMealByLetter("b")
        }
    }


    Column(modifier = Modifier.padding(16.dp)) {
        SearchBar(searchQuery) {
            if (it.text.length <= 1) {
                searchQuery = it
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Meal By Letter : ${searchQuery.text.uppercase()}")
        Spacer(modifier = Modifier.height(8.dp))
        when (searchResults) {
            is ApiResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiResult.Success -> { val items = (searchResults as ApiResult.Success<MealResponse>).data.meals
                if (items != null) {
                    ItemList(items,navController)
                }else{
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp), // Optional padding for some space around
                        contentAlignment = Alignment.Center // Center the content
                    ) {
                        Text(
                            text = "No meals yet!",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                Text(text = "Error: ${(searchResults as ApiResult.Error).message}",
                   )
            }
        }
    }
}


@Composable
fun SearchBar(searchQuery: TextFieldValue, onSearchQueryChange: (TextFieldValue) -> Unit) {
    BasicTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, color4, MaterialTheme.shapes.small)
            .padding(16.dp),
        singleLine = true,
        textStyle = TextStyle(color = color4),
        decorationBox = { innerTextField ->
            if (searchQuery.text.isEmpty()) {
                Text(
                    text = "Search...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = color4)
                )
            }
            innerTextField()
        }
    )
}

@Composable
fun ItemList(items: List<Meal>, navController: NavController) {

    if (items.isEmpty()) {
        // If the list is empty, show the text in the center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Optional padding for some space around
            contentAlignment = Alignment.Center // Center the content
        ) {
            Text(
                text = "No meals yet!",
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.Black
            )
        }
    } else {
        LazyColumn {
            val mealData = items
            mealData.let {
                items(it.size) { index ->
                    val backgroundColor = getRandomColor()
                    Card(modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                              navController.navigate("mealDetail/${mealData[index].idMeal}")
                        }) { // Add padding for spacing
                        Column(modifier = Modifier.background(backgroundColor)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                EnhancedImageFromUrl(
                                    mealData[index].strMealThumb,
                                    250
                                ) // Pass image URL

                            }
                            Text(
                                text = "${index + 1}. Meal : " + mealData[index].strMeal,
                                modifier = Modifier.padding(8.dp),
                                color = androidx.compose.ui.graphics.Color.Black
                            )
                            Text(
                                text = "Area : " + mealData[index].strArea,
                                modifier = Modifier.padding(8.dp),
                                color = androidx.compose.ui.graphics.Color.Black
                            )


                        }
                    }
                }
            }
        }
    }
}