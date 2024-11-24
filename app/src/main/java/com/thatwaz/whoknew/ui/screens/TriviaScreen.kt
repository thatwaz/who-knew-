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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.thatwaz.ui.viewmodels.TriviaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaScreen(viewModel: TriviaViewModel, category: String) {
    // Initialize category in ViewModel if not already set
    LaunchedEffect(category) {
        viewModel.setCategory(category)
    }

    val questions by viewModel.questions.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    val currentDifficulty by viewModel.currentDifficulty.collectAsState()
    val points by viewModel.points.collectAsState()

    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var wager by remember { mutableStateOf(10) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Who New? Trivia Challenge",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Category: $category",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = "Difficulty: $currentDifficulty",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = "Points: $points",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (currentQuestion != null) {
            // Display the decoded question
            QuestionCard(questionText = currentQuestion.decodedQuestion)

            // Wager Input
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Wager: ",
                    style = MaterialTheme.typography.bodyLarge
                )
                TextField(
                    value = wager.toString(),
                    onValueChange = { input ->
                        wager = input.toIntOrNull() ?: 0
                    },
                    modifier = Modifier.width(80.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )

                )
            }

            // Display the answer options as individual buttons
            OptionsCard(
                options = currentQuestion.options,
                selectedAnswer = selectedAnswer,
                onOptionSelected = { answer ->
                    selectedAnswer = answer
                    val isCorrect = answer == currentQuestion.decodedCorrectAnswer
                    isAnswerCorrect = isCorrect
                    viewModel.updatePoints(wager, isCorrect)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display feedback and Next button
            isAnswerCorrect?.let { correct ->
                Text(
                    text = if (correct) "Correct!" else "Wrong!",
                    color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(
                    onClick = {
                        viewModel.moveToNextQuestion()
                        selectedAnswer = null
                        isAnswerCorrect = null
                    }
                ) {
                    Text(text = "Next Question")
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}



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

//@Composable
//fun SinglePlayerTriviaScreen(viewModel: TriviaViewModel) {
//    val questions by viewModel.questions.collectAsState()
//    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
//    val currentQuestion = questions.getOrNull(currentQuestionIndex)
//
//    var selectedAnswer by remember { mutableStateOf<String?>(null) }
//    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Who New? Trivia Challenge",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(16.dp),
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (currentQuestion != null) {
//            // Display the question
//            QuestionCard(questionText = currentQuestion.question)
//
//            // Display answer options
//            OptionsCard(
//                options = currentQuestion.options,
//                selectedAnswer = selectedAnswer,
//                onOptionSelected = { answer ->
//                    selectedAnswer = answer
//                    isAnswerCorrect = answer == currentQuestion.correctAnswer
//                }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Display feedback and Next button
//            isAnswerCorrect?.let { correct ->
//                Text(
//                    text = if (correct) "Correct!" else "Wrong!",
//                    color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(
//                    onClick = {
//                        viewModel.moveToNextQuestion()
//                        selectedAnswer = null
//                        isAnswerCorrect = null
//                    }
//                ) {
//                    Text(text = "Next Question")
//                }
//            }
//        } else {
//            CircularProgressIndicator()
//        }
//    }
//}
//
//@Composable
//fun QuestionCard(questionText: String) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = MaterialTheme.shapes.medium,
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Box(modifier = Modifier.padding(16.dp)) {
//            Text(
//                text = questionText,
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}
//
//@Composable
//fun OptionsCard(
//    options: List<String>,
//    selectedAnswer: String?,
//    onOptionSelected: (String) -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        options.forEach { option ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .clickable { onOptionSelected(option) },
//                shape = MaterialTheme.shapes.medium,
//                colors = CardDefaults.cardColors(
//                    containerColor = if (option == selectedAnswer) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
//                )
//            ) {
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    Text(
//                        text = option,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
