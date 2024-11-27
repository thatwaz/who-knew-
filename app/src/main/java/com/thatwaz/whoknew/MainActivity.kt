package com.thatwaz.whoknew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thatwaz.whoknew.ui.viewmodels.TriviaViewModel
import com.thatwaz.whoknew.ui.screens.ChooseCategoryScreen
import com.thatwaz.whoknew.ui.screens.CreateProfileScreen
import com.thatwaz.whoknew.ui.screens.TriviaScreen

import com.thatwaz.whoknew.ui.theme.WhoKnewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhoKnewTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "profile_screen") {
        // Profile Screen
        composable("profile_screen") {
            CreateProfileScreen(onNavigateToCategory = {
                navController.navigate("choose_category_screen")
            })
        }

        // Choose Category Screen
        composable("choose_category_screen") {
            ChooseCategoryScreen(onCategorySelected = { selectedCategory ->
                navController.currentBackStackEntry?.savedStateHandle?.set("selectedCategory", selectedCategory)
                navController.navigate("trivia_screen")
            })
        }

        // Trivia Screen
        composable("trivia_screen") {
            val viewModel: TriviaViewModel = hiltViewModel()
            val selectedCategory = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selectedCategory") ?: "General Knowledge"

            TriviaScreen(viewModel, selectedCategory)
        }
    }
}


