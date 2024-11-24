package com.thatwaz.whoknew.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ChooseCategoryScreen(onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        "General Knowledge",
        "Entertainment: Film",
        "Entertainment: Music",
        "Sports",
        "History",
        "Science & Nature",
        "Geography",
        "Entertainment: Television"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose a Category",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { category ->
                CategoryCard(category = category) { selectedCategory ->
                    Log.d("ChooseCategoryScreen", "Category selected: $selectedCategory")
                    onCategorySelected(selectedCategory)
                }
            }
        }
    }
}


//@Composable
//fun ChooseCategoryScreen(onCategorySelected: (String) -> Unit) {
//    val categories = listOf(
//        "General Knowledge",
//        "Film",
//        "Music",
//        "Sports",
//        "History",
//        "Science & Nature",
//        "Geography",
//        "Celebrities"
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Choose a Category",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(150.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(categories) { category ->
//                CategoryCard(category = category, onCategorySelected = onCategorySelected)
//            }
//        }
//    }
//}

@Composable
fun CategoryCard(category: String, onCategorySelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .clickable { onCategorySelected(category) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = category,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


