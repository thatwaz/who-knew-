package com.thatwaz.whoknew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CreateProfileScreen(onNavigateToCategory: () -> Unit) {
    var username by remember { mutableStateOf("") }
    val maxCharacters = 6
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Your Profile",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display underscores with a tappable Box
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until maxCharacters) {
                val char = username.getOrNull(i)?.toString() ?: "_"
                Box(
                    modifier = Modifier
                        .size(48.dp) // Adjust size for better touch targets
                        .background(
                            if (char == "_") MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            else Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 36.sp
                        ),
                        color = if (char == "_") MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Invisible TextField for keyboard input
        BasicTextField(
            value = username,
            onValueChange = { newValue ->
                if (newValue.length <= maxCharacters) {
                    username = newValue
                }
            },
            textStyle = TextStyle(color = Color.Transparent),
            cursorBrush = SolidColor(Color.Transparent),
            decorationBox = { },
            modifier = Modifier.focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to proceed to category selection
        Button(
            onClick = {
                if (username.isBlank()) {
                    username = "Guest" // Default to "Guest" if no input
                }
                onNavigateToCategory()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Profile and Continue")
        }
    }
}



//@Composable
//fun CreateProfileScreen(onNavigateToTrivia: () -> Unit) {
//    var username by remember { mutableStateOf("") }
//    val maxCharacters = 6
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Title
//        Text(
//            text = "Create Your Profile",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Underscore-based input visualization
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            for (i in 0 until maxCharacters) {
//                val char = username.getOrNull(i)?.toString() ?: "_"
//                Text(
//                    text = char,
//                    style = MaterialTheme.typography.titleLarge.copy(
//                        fontSize = 48.sp
//                    ),
//                    color = if (char == "_") MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary,
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp) // Add spacing between underscores
//                        .weight(1f, fill = false) // Equal spacing between letters
//                )
//            }
//        }
//
//        // Hidden TextField for capturing user input
//
//        TextField(
//            value = username,
//            onValueChange = {
//                if (it.length <= maxCharacters) {
//                    username = it
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(0.dp), // Hide the TextField
//            colors = TextFieldDefaults.colors(
//                focusedTextColor = Color.Transparent,
//                unfocusedTextColor = Color.Transparent,
//                disabledTextColor = Color.Transparent,
//                cursorColor = Color.Transparent,
//                focusedContainerColor = Color.Transparent,
//                unfocusedContainerColor = Color.Transparent,
//                disabledContainerColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            ),
//            singleLine = true
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Create Profile and Play Button
//        Button(
//            onClick = {
//                if (username.isBlank()) {
//                    username = "Guest" // Default to "Guest" if no input
//                }
//                onNavigateToTrivia()
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Create Profile and Play")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Play as Guest Button
//        TextButton(onClick = onNavigateToTrivia) {
//            Text("Play as Guest", style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}







//@Composable
//fun CreateProfileScreen(onNavigateToTrivia: () -> Unit) {
//    var username by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Create Your Profile",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Fun, playful username input with underscores
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp)
//                .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                for (i in 0 until 12) { // Max 12 characters
//                    val char = username.getOrNull(i)?.toString() ?: "_"
//                    Text(
//                        text = char,
//                        style = MaterialTheme.typography.bodyLarge.copy(
//                            fontFamily = FontFamily.SansSerif // Replace with a "bar trivia" font
//                        ),
//                        color = if (char == "_") MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.padding(horizontal = 2.dp)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Actual system keyboard input
//        OutlinedTextField(
//            value = username,
//            onValueChange = { input ->
//                if (input.length <= 12) {
//                    username = input
//                    errorMessage = ""
//                }
//            },
//            label = { Text("Enter your username") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
//            textStyle = MaterialTheme.typography.bodyLarge
//        )
//
//        if (errorMessage.isNotBlank()) {
//            Text(
//                text = errorMessage,
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                if (username.isBlank()) {
//                    errorMessage = "Username cannot be empty"
//                } else {
//                    onNavigateToTrivia()
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Create Profile and Play")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextButton(onClick = onNavigateToTrivia) {
//            Text("Play as Guest", style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}
//


//@Composable
//fun CreateProfileScreen(onNavigateToTrivia: () -> Unit) {
//    var username by remember { mutableStateOf("") }
//    var isGuest by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Welcome to Who Knew?",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 16.dp),
//            textAlign = TextAlign.Center
//        )
//
//        if (!isGuest) {
//            OutlinedTextField(
//                value = username,
//                onValueChange = {
//                    username = it
//                    errorMessage = ""
//                },
//                label = { Text("Enter your username") },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true
//            )
//
//            if (errorMessage.isNotBlank()) {
//                Text(
//                    text = errorMessage,
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                if (isGuest || username.isNotBlank()) {
//                    errorMessage = ""
//                    onNavigateToTrivia()
//                } else {
//                    errorMessage = "Please enter a username or select 'Play as Guest'"
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = if (isGuest) "Continue as Guest" else "Create Profile and Play")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextButton(onClick = { isGuest = !isGuest }) {
//            Text(
//                text = if (isGuest) "Switch to Create Profile" else "Play as Guest",
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//}
//
