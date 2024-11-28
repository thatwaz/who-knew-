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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thatwaz.whoknew.ui.viewmodels.TriviaViewModel
import kotlinx.coroutines.delay

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

    var hasPlacedFirstWager by remember { mutableStateOf(false) }
    var showWagerButtons by remember { mutableStateOf(true) }
    var answerFeedback by remember { mutableStateOf<String?>(null) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var revealCorrectAnswer by remember { mutableStateOf(false) }
    var topicTypingDone by remember { mutableStateOf(false) } // Track when the topic has finished typing
    val currentDifficulty = viewModel.currentDifficulty.collectAsState().value

    if (isGameOver.value) {
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

            // Show question and options if wager has been placed
            if (hasPlacedFirstWager && currentQuestion != null) {
                Spacer(modifier = Modifier.height(16.dp))
                QuestionCard(questionText = currentQuestion.decodedQuestion)

                Spacer(modifier = Modifier.height(16.dp))
                OptionsCard(
                    options = currentQuestion.options,
                    selectedAnswer = selectedAnswer,
                    correctAnswer = if (revealCorrectAnswer) currentQuestion.correctAnswer else null,
                    onOptionSelected = { answer ->
                        selectedAnswer = answer
                        revealCorrectAnswer = true
                        val isCorrect = viewModel.answerQuestion(answer)
                        answerFeedback = if (isCorrect) "Correct!" else "Wrong!"

                        if (viewModel.isGameOver()) {
                            isGameOver.value = true
                        } else {
                            showWagerButtons = true
                        }
                    }
                )

                answerFeedback?.let { feedback ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = feedback,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (feedback == "Correct!") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            // Display the topic with typewriter effect
            if (showWagerButtons && nextQuestion != null) {
                val nextTopic = viewModel.getTopicForQuestion(nextQuestion.decodedQuestion)
                TopicCard(topic = "Your next topic is: $nextTopic (Difficulty: ${currentDifficulty.capitalize()})") {
                    topicTypingDone = true // Signal when topic typing is done
                }
            }

            // Wager Buttons
            if (showWagerButtons && topicTypingDone) {
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
                        hasPlacedFirstWager = true
                        selectedAnswer = null
                        revealCorrectAnswer = false
                        topicTypingDone = false // Reset topic typing state
                    }) {
                        Text(text = "Bet 1/4")
                    }
                    Button(onClick = {
                        viewModel.setWager((points * 0.5).toInt())
                        viewModel.moveToNextQuestion()
                        showWagerButtons = false
                        answerFeedback = null
                        hasPlacedFirstWager = true
                        selectedAnswer = null
                        revealCorrectAnswer = false
                        topicTypingDone = false // Reset topic typing state
                    }) {
                        Text(text = "Bet 1/2")
                    }
                    Button(onClick = {
                        viewModel.setWager(points)
                        viewModel.moveToNextQuestion()
                        showWagerButtons = false
                        answerFeedback = null
                        hasPlacedFirstWager = true
                        selectedAnswer = null
                        revealCorrectAnswer = false
                        topicTypingDone = false // Reset topic typing state
                    }) {
                        Text(text = "Bet Max")
                    }
                }
            }
        }
    }
}


@Composable
fun TopicCard(topic: String, onTypingDone: () -> Unit) {
    val visibleText = remember { mutableStateOf("") }

    // Typewriter effect
    LaunchedEffect(topic) {
        visibleText.value = ""
        topic.forEachIndexed { index, _ ->
            visibleText.value = topic.take(index + 1)
            delay(45) // Adjust typing speed
        }
        onTypingDone() // Signal when typing is complete
    }

    Text(
        text = visibleText.value,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}




@Composable
fun QuestionCard(questionText: String) {
    // State to hold the currently visible portion of the text
    val visibleText = remember { mutableStateOf("") }

    // Typewriter effect
    LaunchedEffect(questionText) {
        visibleText.value = "" // Reset visible text for new question
        questionText.forEachIndexed { index, _ ->
            visibleText.value = questionText.take(index + 1)
            delay(45) // Adjust delay for typing speed
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = visibleText.value, // Display the progressively revealed text
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
    correctAnswer: String?,
    onOptionSelected: (String) -> Unit
) {
    val correctColor = Color(0xFF4CAF50) // Explicit green color for correct answers
    val incorrectColor = Color(0xFFF44336) // Explicit red color for incorrect answers
    val defaultColor = MaterialTheme.colorScheme.primary // Default button color

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val backgroundColor = when {
                selectedAnswer == option && option == correctAnswer -> correctColor // Correct answer chosen
                selectedAnswer == option && option != correctAnswer -> incorrectColor // Incorrect answer chosen
                correctAnswer != null && option == correctAnswer -> correctColor // Highlight correct answer
                else -> defaultColor // Default
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable(enabled = selectedAnswer == null) { onOptionSelected(option) },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
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





