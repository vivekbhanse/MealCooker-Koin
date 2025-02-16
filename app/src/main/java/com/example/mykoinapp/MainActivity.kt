package com.example.mykoinapp

import BiometricAuthScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mykoinapp.presentation.profile.ProfileScreen
import com.example.mykoinapp.presentation.Screen
import com.example.mykoinapp.presentation.fav_meal.FavMealListScreen
import com.example.mykoinapp.presentation.home.AppTopBar
import com.example.mykoinapp.presentation.home.ShowMealListWithSwipeRefresh
import com.example.mykoinapp.presentation.mealsSeletion.MealDetailScreen
import com.example.mykoinapp.presentation.navhost.AppNavigation
import com.example.mykoinapp.presentation.navhost.BottomNavigationBar
import com.example.mykoinapp.presentation.search.SearchableList
import com.example.mykoinapp.ui.theme.DarkBlue
import com.example.mykoinapp.ui.theme.MyKoinAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AppNavigation()

            MyKoinAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(DarkBlue)
    val context = LocalContext.current
    Scaffold(
        topBar = { AppTopBar() },  // ✅ Add top bar
        bottomBar = {

            if (shouldShowBottomBar(navController)) {
                BottomNavigationBar(navController)
            }
        }  // ✅ Add bottom bar
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = Screen.Home.route) {

                composable(Screen.BiometricAuthScreen.route) { BiometricAuthScreen(navController) }
                composable(Screen.Home.route) { ShowMealListWithSwipeRefresh(navController) }
                composable(Screen.MealDetailsPage.route) { backStackEntry ->
                    val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
                    MealDetailScreen(
                        navController = navController,
                        mealId = mealId
                    ) // Pass the data
                }
                composable(Screen.Favorites.route) { FavMealListScreen() }
                composable(Screen.Profile.route) { ProfileScreen() }
                composable(Screen.Search.route) {
                    SearchableList()
                }
            }
        }
    }
}

@Composable
fun shouldShowBottomBar(navController: NavController): Boolean {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return currentRoute != Screen.MealDetailsPage.route
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyKoinAppTheme {
        Greeting("Android")
    }
}