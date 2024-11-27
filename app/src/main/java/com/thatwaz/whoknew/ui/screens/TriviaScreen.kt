package com.thatwaz.whoknew.ui.screens


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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thatwaz.whoknew.ui.viewmodels.TriviaViewModel

@Composable
fun TriviaScreen(viewModel: TriviaViewModel, category: String) {
    LaunchedEffect(category) {
        viewModel.setCategory(category)
    }

    val questions by viewModel.questions.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    val nextQuestion = questions.getOrNull(currentQuestionIndex + 1)
    val points by viewModel.points.collectAsState()
    val isGameOver = remember { mutableStateOf(false) }

    // State to track whether the first wager has been placed
    var hasPlacedFirstWager by remember { mutableStateOf(false) }

    // State to control wager buttons visibility
    var showWagerButtons by remember { mutableStateOf(true) }
    var answerFeedback by remember { mutableStateOf<String?>(null) }

    if (isGameOver.value) {
        // Game Over Screen
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Over!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Final Score: $points",
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        // Main Game Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Points Display
            Text(
                text = "Points: $points",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Topic Display for Next Question
            if (showWagerButtons && nextQuestion != null) {
                val nextTopic = viewModel.getTopicForQuestion(nextQuestion.decodedQuestion)
                Text(
                    text = "Your topic is: $nextTopic",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Conditionally Render Question and Answers
            if (hasPlacedFirstWager && currentQuestion != null) {
                Spacer(modifier = Modifier.height(16.dp))
                QuestionCard(questionText = currentQuestion.decodedQuestion)

                Spacer(modifier = Modifier.height(16.dp))
                OptionsCard(
                    options = currentQuestion.options,
                    selectedAnswer = null,
                    onOptionSelected = { answer ->
                        val isCorrect = viewModel.answerQuestion(answer)
                        answerFeedback = if (isCorrect) "Correct!" else "Wrong!"

                        if (viewModel.isGameOver()) {
                            isGameOver.value = true
                        } else {
                            showWagerButtons = true
                        }
                    }
                )

                // Feedback Text
                answerFeedback?.let { feedback ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = feedback,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (feedback == "Correct!") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else if (!hasPlacedFirstWager) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Place your wager to reveal the question!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            }

            // Wager Buttons
            if (showWagerButtons) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Place Your Wager",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        viewModel.setWager((points * 0.25).toInt())
                        viewModel.moveToNextQuestion()
                        showWagerButtons = false
                        answerFeedback = null
                        hasPlacedFirstWager = true // Mark first wager as placed
                    }) {
                        Text(text = "Bet 1/4")
                    }
                    Button(onClick = {
                        viewModel.setWager((points * 0.5).toInt())
                        viewModel.moveToNextQuestion()
                        showWagerButtons = false
                        answerFeedback = null
                        hasPlacedFirstWager = true // Mark first wager as placed
                    }) {
                        Text(text = "Bet 1/2")
                    }
                    Button(onClick = {
                        viewModel.setWager(points)
                        viewModel.moveToNextQuestion()
                        showWagerButtons = false
                        answerFeedback = null
                        hasPlacedFirstWager = true // Mark first wager as placed
                    }) {
                        Text(text = "Bet Max")
                    }
                }
            }
        }
    }
}

//@Composable
//fun TriviaScreen(viewModel: TriviaViewModel, category: String) {
//    LaunchedEffect(category) {
//        viewModel.setCategory(category)
//    }
//
//    val questions by viewModel.questions.collectAsState()
//    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
//    val currentQuestion = questions.getOrNull(currentQuestionIndex)
//    val nextQuestion = questions.getOrNull(currentQuestionIndex + 1)
//    val points by viewModel.points.collectAsState()
//    val isGameOver = remember { mutableStateOf(false) }
//
//    var showWagerButtons by remember { mutableStateOf(true) }
//    var answerFeedback by remember { mutableStateOf<String?>(null) }
//
//    if (isGameOver.value) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Game Over!",
//                style = MaterialTheme.typography.titleLarge,
//                color = MaterialTheme.colorScheme.error,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "Final Score: $points",
//                style = MaterialTheme.typography.titleMedium
//            )
//        }
//    } else {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Points: $points",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            if (showWagerButtons && nextQuestion != null) {
//                val nextTopic = viewModel.getTopicForQuestion(nextQuestion.decodedQuestion)
//                Text(
//                    text = "Your topic is: $nextTopic",
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    textAlign = TextAlign.Center
//                )
//            }
//
//            currentQuestion?.let { question ->
//                Spacer(modifier = Modifier.height(16.dp))
//                QuestionCard(questionText = question.decodedQuestion)
//
//                Spacer(modifier = Modifier.height(16.dp))
//                OptionsCard(
//                    options = question.options,
//                    selectedAnswer = null,
//                    onOptionSelected = { answer ->
//                        val isCorrect = viewModel.answerQuestion(answer)
//                        answerFeedback = if (isCorrect) "Correct!" else "Wrong!"
//
//                        if (viewModel.isGameOver()) {
//                            isGameOver.value = true
//                        } else {
//                            showWagerButtons = true
//                        }
//                    }
//                )
//
//                answerFeedback?.let { feedback ->
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = feedback,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = if (feedback == "Correct!") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//            }
//
//            if (showWagerButtons) {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "Place Your Wager",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Button(onClick = {
//                        viewModel.setWager((points * 0.25).toInt())
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet 1/4")
//                    }
//                    Button(onClick = {
//                        viewModel.setWager((points * 0.5).toInt())
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet 1/2")
//                    }
//                    Button(onClick = {
//                        viewModel.setWager(points)
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet Max")
//                    }
//                }
//            }
//        }
//    }
//}

//@Composable
//fun TriviaScreen(viewModel: TriviaViewModel, category: String) {
//    LaunchedEffect(category) {
//        viewModel.setCategory(category)
//    }
//
//    val questions by viewModel.questions.collectAsState()
//    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
//    val currentQuestion = questions.getOrNull(currentQuestionIndex)
//    val nextQuestionTopic = questions.getOrNull(currentQuestionIndex + 1)?.category // Next question's topic
//
//    val points by viewModel.points.collectAsState()
//    val isGameOver = remember { mutableStateOf(false) }
//
//    var showWagerButtons by remember { mutableStateOf(true) }
//    var answerFeedback by remember { mutableStateOf<String?>(null) }
//
//    if (isGameOver.value) {
//        // Game Over Screen
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Game Over!",
//                style = MaterialTheme.typography.titleLarge,
//                color = MaterialTheme.colorScheme.error,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "Final Score: $points",
//                style = MaterialTheme.typography.titleMedium
//            )
//        }
//    } else {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Points Display
//            Text(
//                text = "Points: $points",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // Display the next topic before wager buttons
//            if (showWagerButtons) {
//                Text(
//                    text = "Your topic is: ${nextQuestionTopic ?: "Unknown"}",
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    textAlign = TextAlign.Center
//                )
//            }
//
//            // Trivia Question UI
//            currentQuestion?.let { question ->
//                Spacer(modifier = Modifier.height(16.dp))
//                QuestionCard(questionText = question.decodedQuestion)
//
//                Spacer(modifier = Modifier.height(16.dp))
//                OptionsCard(
//                    options = question.options,
//                    selectedAnswer = null,
//                    onOptionSelected = { answer ->
//                        val isCorrect = viewModel.answerQuestion(answer)
//                        answerFeedback = if (isCorrect) "Correct!" else "Wrong!"
//
//                        // Handle game logic
//                        if (viewModel.isGameOver()) {
//                            isGameOver.value = true
//                        } else {
//                            showWagerButtons = true // Show wager buttons for the next question
//                        }
//                    }
//                )
//
//                // Feedback Text
//                answerFeedback?.let { feedback ->
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = feedback,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = if (feedback == "Correct!") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//            }
//
//            // Wager Buttons
//            if (showWagerButtons) {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "Place Your Wager",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Button(onClick = {
//                        viewModel.setWager((points * 0.25).toInt())
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet 1/4")
//                    }
//                    Button(onClick = {
//                        viewModel.setWager((points * 0.5).toInt())
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet 1/2")
//                    }
//                    Button(onClick = {
//                        viewModel.setWager(points)
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet Max")
//                    }
//                }
//            }
//        }
//    }
//}


//@Composable
//fun TriviaScreen(viewModel: TriviaViewModel, category: String) {
//    LaunchedEffect(category) {
//        viewModel.setCategory(category)
//    }
//
//    val questions by viewModel.questions.collectAsState()
//    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
//    val currentQuestion = questions.getOrNull(currentQuestionIndex)
//
//    val points by viewModel.points.collectAsState()
//    val isGameOver = remember { mutableStateOf(false) }
//
//    // State to control wager buttons visibility
//    var showWagerButtons by remember { mutableStateOf(true) }
//    var answerFeedback by remember { mutableStateOf<String?>(null) }
//
//    if (isGameOver.value) {
//        // Game Over Screen
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Game Over!",
//                style = MaterialTheme.typography.titleLarge,
//                color = MaterialTheme.colorScheme.error,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "Final Score: $points",
//                style = MaterialTheme.typography.titleMedium
//            )
//        }
//    } else {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Points Display
//            Text(
//                text = "Points: $points",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // Trivia Question UI
//            currentQuestion?.let { question ->
//                Spacer(modifier = Modifier.height(16.dp))
//                QuestionCard(questionText = question.decodedQuestion)
//
//                Spacer(modifier = Modifier.height(16.dp))
//                OptionsCard(
//                    options = question.options,
//                    selectedAnswer = null,
//                    onOptionSelected = { answer ->
//                        val isCorrect = viewModel.answerQuestion(answer)
//                        answerFeedback = if (isCorrect) "Correct!" else "Wrong!"
//
//                        // Handle game logic
//                        if (viewModel.isGameOver()) {
//                            isGameOver.value = true
//                        } else {
//                            showWagerButtons = true // Show wager buttons for the next question
//                        }
//                    }
//                )
//
//                // Feedback Text
//                answerFeedback?.let { feedback ->
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = feedback,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = if (feedback == "Correct!") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//            }
//
//            // Wager Buttons
//            if (showWagerButtons) {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "Place Your Wager",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Button(onClick = {
//                        viewModel.setWager((points * 0.25).toInt())
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet 1/4")
//                    }
//                    Button(onClick = {
//                        viewModel.setWager((points * 0.5).toInt())
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet 1/2")
//                    }
//                    Button(onClick = {
//                        viewModel.setWager(points)
//                        viewModel.moveToNextQuestion()
//                        showWagerButtons = false
//                        answerFeedback = null
//                    }) {
//                        Text(text = "Bet Max")
//                    }
//                }
//            }
//        }
//    }
//}





@Composable
fun QuestionCard(questionText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = questionText,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun OptionsCard(
    options: List<String>,
    selectedAnswer: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onOptionSelected(option) },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = if (option == selectedAnswer) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


