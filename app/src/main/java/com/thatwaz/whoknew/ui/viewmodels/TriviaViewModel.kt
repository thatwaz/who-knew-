package com.thatwaz.whoknew.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.whoknew.data.models.TriviaQuestion
import com.thatwaz.whoknew.data.repository.TriviaRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class TriviaViewModel @Inject constructor(
    private val repository: TriviaRepository
) : ViewModel() {

    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    val questions: StateFlow<List<TriviaQuestion>> = _questions

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _currentDifficulty = MutableStateFlow("easy")
    val currentDifficulty: StateFlow<String> = _currentDifficulty

    private val _points = MutableStateFlow(1000) // Starting points
    val points: StateFlow<Int> = _points

    private val _wager = MutableStateFlow(0) // Wager for the current question
    val wager: StateFlow<Int> = _wager

    private val _allQuestions = MutableStateFlow<List<TriviaQuestion>>(emptyList())

    fun setCategory(category: String) {
        _selectedCategory.value = category
        fetchAllQuestionsForCategory(category)
    }

    private fun fetchAllQuestionsForCategory(category: String) {
        viewModelScope.launch {
            try {
                val questions = repository.getFilteredQuestions(
                    amount = 50,
                    difficulty = null, // Fetch all difficulties
                    category = category
                ).filter { it.question.isNotBlank() && it.correctAnswer.isNotBlank() }

                _allQuestions.value = questions
                filterQuestionsByDifficulty("easy")
            } catch (e: Exception) {
                Log.e("TriviaViewModel", "Error fetching questions: ${e.message}")
            }
        }
    }

    private fun filterQuestionsByDifficulty(difficulty: String) {
        val filteredQuestions = _allQuestions.value.filter {
            it.difficulty.equals(difficulty, ignoreCase = true)
        }
        if (filteredQuestions.isNotEmpty()) {
            _questions.value = filteredQuestions
            _currentQuestionIndex.value = 0
            _currentDifficulty.value = difficulty
        } else {
            when (difficulty) {
                "easy" -> filterQuestionsByDifficulty("medium")
                "medium" -> filterQuestionsByDifficulty("hard")
                else -> Log.d("TriviaViewModel", "No more questions available.")
            }
        }
    }

    fun setWager(wagerAmount: Int) {
        _wager.value = wagerAmount
    }

    fun answerQuestion(selectedAnswer: String): Boolean {
        val correctAnswer = _questions.value[_currentQuestionIndex.value].correctAnswer
        val isCorrect = selectedAnswer == correctAnswer

        if (isCorrect) {
            _points.value += _wager.value
        } else {
            _points.value -= _wager.value
        }

        return isCorrect
    }

    fun moveToNextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value += 1
        } else {
            filterQuestionsByDifficulty(_currentDifficulty.value)
        }
    }

    fun isGameOver(): Boolean {
        return _points.value <= 0
    }

    fun getTopicForQuestion(questionText: String): String {
        val keywordToTopicMap = mapOf(
            // Broad Categories
            "president" to "History",
            "landmark" to "Geography",
            "planet" to "Science & Nature",
            "furlong" to "Measurements",
            "language" to "Language",
            "company" to "Companies",
            "companies" to "Companies",
            "comic" to "Comics",
            "galaxy" to "Astronomy",
            "zodiac" to "Astrology",
            "game" to "Games",
            "toy" to "Pop Culture",
            "building" to "Architecture",
            "helicopter" to "Inventions",
            "mystery" to "Mysteries",
            "transportation" to "Transportation",
            "food" to "Food & Drink",
            "drink" to "Food & Drink",
            "color" to "Art & Design",
            "shape" to "Shapes",
            "city" to "Geography",
            "restaurant" to "Restaurants",
            "technology" to "Technology",
            "video game" to "Video Games",
            "candy" to "Food & Drink",
            "fashion" to "Fashion",
            "time" to "History",
            "movie" to "Entertainment",
            "space" to "Astronomy",
            "body" to "Biology",
            "biology" to "Biology",
            "dance" to "Entertainment",
            "religion" to "Religion",
            "animal" to "Animals",
            "astrology" to "Astrology",
            "science" to "Science & Nature",
            "history" to "History",
            "entertainment" to "Pop Culture"
        )



        // Check for a matching keyword
        for ((keyword, topic) in keywordToTopicMap) {
            if (questionText.contains(keyword, ignoreCase = true)) {
                return topic
            }
        }
        return "General Knowledge" // Default if no keywords match
    }

}

//@HiltViewModel
//class TriviaViewModel @Inject constructor(
//    private val repository: TriviaRepository
//) : ViewModel() {
//
//    // Question data
//    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
//    val questions: StateFlow<List<TriviaQuestion>> = _questions
//
//    private val _currentQuestionIndex = MutableStateFlow(0)
//    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex
//
//    private val _allQuestions = MutableStateFlow<List<TriviaQuestion>>(emptyList()) // Store all fetched questions
//
//    // User progress
//    private val _points = MutableStateFlow(0)
//    val points: StateFlow<Int> = _points
//
//    private val _wageredPoints = MutableStateFlow(0)
//    val wageredPoints: StateFlow<Int> = _wageredPoints
//
//    private val _selectedAnswer = MutableStateFlow<String?>(null)
//    val selectedAnswer: StateFlow<String?> = _selectedAnswer
//
//    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
//    val isAnswerCorrect: StateFlow<Boolean?> = _isAnswerCorrect
//
//    private val _selectedCategory = MutableStateFlow<String?>(null)
//    val selectedCategory: StateFlow<String?> = _selectedCategory
//
//    private val _currentDifficulty = MutableStateFlow("Easy")
//    val currentDifficulty: StateFlow<String> = _currentDifficulty
//
//    private val _gameOver = MutableStateFlow(false)
//    val gameOver: StateFlow<Boolean> = _gameOver
//
////    val points: StateFlow<Int> = _points
//
//    private val _wager = MutableStateFlow<Int>(0)
//    val wager: StateFlow<Int> = _wager
//
//    fun setWager(amount: Int) {
//        _wager.value = amount
//    }
//
//
//    fun updatePoints(wager: Int, isCorrect: Boolean) {
//        _points.value = if (isCorrect) {
//            _points.value + wager
//        } else {
//            (_points.value - wager).coerceAtLeast(0) // Prevent negative points
//        }
//    }
//
//    fun setCategory(category: String) {
//        _selectedCategory.value = category
//        fetchAllQuestionsForCategory(category)
//    }
//
//    private fun fetchAllQuestionsForCategory(category: String) {
//        viewModelScope.launch {
//            try {
//                Log.d("TriviaViewModel", "Fetching questions for category: $category")
//                val questions = repository.getFilteredQuestions(
//                    amount = 50, // Fetch a large batch of questions
//                    difficulty = null, // Fetch all difficulties
//                    category = category
//                ).filter { it.question.isNotBlank() && it.correctAnswer.isNotBlank() }
//
//                Log.d("TriviaViewModel", "Fetched ${questions.size} questions for category: $category")
//                _allQuestions.value = questions
//                filterQuestionsByDifficulty("easy") // Start with easy questions
//            } catch (e: Exception) {
//                Log.e("TriviaViewModel", "Error fetching questions: ${e.message}")
//            }
//        }
//    }
//
//    private fun filterQuestionsByDifficulty(difficulty: String) {
//        val filteredQuestions = _allQuestions.value.filter {
//            it.difficulty.equals(difficulty, ignoreCase = true)
//        }
//        if (filteredQuestions.isNotEmpty()) {
//            _questions.value = filteredQuestions
//            _currentQuestionIndex.value = 0
//            _currentDifficulty.value = difficulty.capitalize()
//            Log.d("TriviaViewModel", "Filtered ${filteredQuestions.size} questions for difficulty: $difficulty")
//        } else {
//            Log.d("TriviaViewModel", "No more questions for difficulty: $difficulty")
//            // Move to the next difficulty if no questions are left
//            when (difficulty) {
//                "easy" -> filterQuestionsByDifficulty("medium")
//                "medium" -> filterQuestionsByDifficulty("hard")
//                else -> Log.d("TriviaViewModel", "No more questions available in any difficulty")
//            }
//        }
//    }
//
//    fun moveToNextQuestion() {
//        if (_currentQuestionIndex.value < _questions.value.size - 1) {
//            _currentQuestionIndex.value += 1
//            _selectedAnswer.value = null
//            _isAnswerCorrect.value = null
//        } else {
//            // When all questions in the current difficulty are answered, move to the next difficulty
//            Log.d("TriviaViewModel", "Completed difficulty: ${_currentDifficulty.value}")
//            when (_currentDifficulty.value.lowercase()) {
//                "easy" -> filterQuestionsByDifficulty("medium")
//                "medium" -> filterQuestionsByDifficulty("hard")
//                "hard" -> Log.d("TriviaViewModel", "All questions completed!")
//            }
//        }
//    }
//
//    fun selectAnswer(answer: String) {
//        _selectedAnswer.value = answer
//        val correctAnswer = _questions.value[_currentQuestionIndex.value].correctAnswer
//        _isAnswerCorrect.value = answer == correctAnswer
//
//        // Update points based on correctness
//        val pointsForQuestion = when (_currentDifficulty.value.lowercase()) {
//            "easy" -> 100
//            "medium" -> 250
//            "hard" -> 500
//            else -> 0
//        }
//        if (_isAnswerCorrect.value == true) {
//            _points.value += wageredPoints.value
//        } else {
//            _points.value -= wageredPoints.value
//        }
//
//        if (_points.value <= 0) {
//            _gameOver.value = true
//        }
//    }
//
//    fun setWageredPoints(wager: Int) {
//        _wageredPoints.value = wager
//    }
//
//    fun resetGame() {
//        _points.value = 0
//        _wageredPoints.value = 0
//        _currentQuestionIndex.value = 0
//        _gameOver.value = false
//        _currentDifficulty.value = "Easy"
//        _allQuestions.value = emptyList()
//        _questions.value = emptyList()
//        Log.d("TriviaViewModel", "Game reset.")
//    }
//}

//@HiltViewModel
//class TriviaViewModel @Inject constructor(
//    private val repository: TriviaRepository
//) : ViewModel() {
//
//    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
//    val questions: StateFlow<List<TriviaQuestion>> = _questions
//
//    private val _currentQuestionIndex = MutableStateFlow(0)
//    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex
//
//    private val _selectedAnswer = MutableStateFlow<String?>(null)
//    val selectedAnswer: StateFlow<String?> = _selectedAnswer
//
//    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
//    val isAnswerCorrect: StateFlow<Boolean?> = _isAnswerCorrect
//
//    private val _selectedCategory = MutableStateFlow<String?>(null)
//    val selectedCategory: StateFlow<String?> = _selectedCategory
//
//    private val _currentDifficulty = MutableStateFlow("Easy")
//    val currentDifficulty: StateFlow<String> = _currentDifficulty
//
//    private val _allQuestions = MutableStateFlow<List<TriviaQuestion>>(emptyList()) // Store all fetched questions
//
//    fun setCategory(category: String) {
//        _selectedCategory.value = category
//        fetchAllQuestionsForCategory(category)
//    }
//
//    private fun fetchAllQuestionsForCategory(category: String) {
//        viewModelScope.launch {
//            try {
//                Log.d("TriviaViewModel", "Fetching questions for category: $category")
//                val questions = repository.getFilteredQuestions(
//                    amount = 50, // Fetch a large batch of questions
//                    difficulty = null, // Fetch all difficulties
//                    category = category
//                ).filter { it.question.isNotBlank() && it.correctAnswer.isNotBlank() }
//
//                Log.d("TriviaViewModel", "Fetched ${questions.size} questions for category: $category")
//                _allQuestions.value = questions
//                filterQuestionsByDifficulty("easy") // Start with easy questions
//            } catch (e: Exception) {
//                Log.e("TriviaViewModel", "Error fetching questions: ${e.message}")
//            }
//        }
//    }
//
//    private fun filterQuestionsByDifficulty(difficulty: String) {
//        val filteredQuestions = _allQuestions.value.filter {
//            it.difficulty.equals(difficulty, ignoreCase = true)
//        }
//        if (filteredQuestions.isNotEmpty()) {
//            _questions.value = filteredQuestions
//            _currentQuestionIndex.value = 0
//            _currentDifficulty.value = difficulty.capitalize()
//            Log.d("TriviaViewModel", "Filtered ${filteredQuestions.size} questions for difficulty: $difficulty")
//        } else {
//            Log.d("TriviaViewModel", "No more questions for difficulty: $difficulty")
//            // Move to the next difficulty if no questions are left
//            when (difficulty) {
//                "easy" -> filterQuestionsByDifficulty("medium")
//                "medium" -> filterQuestionsByDifficulty("hard")
//                else -> Log.d("TriviaViewModel", "No more questions available in any difficulty")
//            }
//        }
//    }
//
//    fun moveToNextQuestion() {
//        if (_currentQuestionIndex.value < _questions.value.size - 1) {
//            _currentQuestionIndex.value += 1
//            _selectedAnswer.value = null
//            _isAnswerCorrect.value = null
//        } else {
//            // When all questions in the current difficulty are answered, move to the next difficulty
//            Log.d("TriviaViewModel", "Completed difficulty: ${_currentDifficulty.value}")
//            when (_currentDifficulty.value.lowercase()) {
//                "easy" -> filterQuestionsByDifficulty("medium")
//                "medium" -> filterQuestionsByDifficulty("hard")
//                "hard" -> Log.d("TriviaViewModel", "All questions completed!")
//            }
//        }
//    }
//
//    fun selectAnswer(answer: String) {
//        _selectedAnswer.value = answer
//        val correctAnswer = _questions.value[_currentQuestionIndex.value].correctAnswer
//        _isAnswerCorrect.value = answer == correctAnswer
//    }
//}


//@HiltViewModel
//class TriviaViewModel @Inject constructor(
//    private val repository: TriviaRepository
//) : ViewModel() {
//
//    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
//    val questions: StateFlow<List<TriviaQuestion>> = _questions
//
//    private val _currentQuestionIndex = MutableStateFlow(0)
//    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex
//
//    private val _selectedAnswer = MutableStateFlow<String?>(null)
//    val selectedAnswer: StateFlow<String?> = _selectedAnswer
//
//    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
//    val isAnswerCorrect: StateFlow<Boolean?> = _isAnswerCorrect
//
//    private val _currentDifficulty = MutableStateFlow("Easy")
//    val currentDifficulty: StateFlow<String> = _currentDifficulty
//
//    // Holds the selected category
//    private val _selectedCategory = MutableStateFlow<String?>(null)
//    val selectedCategory: StateFlow<String?> = _selectedCategory
//
//    init {
////        fetchAllQuestionsAndLogCategories() // Fetch and log all categories for debugging
//    }
//    val categoryMapping = mapOf(
//        "General Knowledge" to "General Knowledge",
//        "History" to "History",
//        "Entertainment: Music" to "Music",
//        "Entertainment: Film" to "Film",
//        "Geography" to "Geography",
//        "Science & Nature" to "Science & Nature",
//        "Celebrities" to "Celebrities"
//    )
//
//
////    fun setCategory(category: String) {
////        val apiCategory = categoryMapping[category]
////        if (apiCategory != null) {
////            _selectedCategory.value = apiCategory
////            Log.d("TriviaViewModel", "Category selected: $apiCategory")
////            fetchQuestionsForCategoryAndDifficulty(apiCategory, "easy")
////        } else {
////            Log.e("TriviaViewModel", "Invalid category selected: $category")
////        }
////    }
////
////
////    private fun fetchQuestionsForCategoryAndDifficulty(category: String, difficulty: String) {
////        viewModelScope.launch {
////            Log.d("TriviaViewModel", "Triggering API call. Category: $category, Difficulty: $difficulty")
////            try {
////                val newQuestions = repository.getFilteredQuestions(
////                    amount = 100,
////                    category = category,
////                    difficulty = difficulty
////                )
////                Log.d("TriviaViewModel", "API call returned ${newQuestions.size} questions for Category: $category, Difficulty: $difficulty")
////
////                if (newQuestions.isEmpty() && difficulty != "hard") {
////                    val nextDifficulty = when (difficulty) {
////                        "easy" -> "medium"
////                        "medium" -> "hard"
////                        else -> "hard"
////                    }
////                    fetchQuestionsForCategoryAndDifficulty(category, nextDifficulty)
////                } else {
////                    _questions.value = _questions.value + newQuestions
////                    _currentQuestionIndex.value = 0
////                }
////            } catch (e: Exception) {
////                Log.e("TriviaViewModel", "Error fetching questions: ${e.message}")
////            }
////        }
////    }
//    fun setCategory(category: String) {
//        _selectedCategory.value = category
//        fetchQuestionsForCategory(category, "easy")
//    }
//
//    private fun fetchQuestionsForCategory(category: String, difficulty: String) {
//        viewModelScope.launch {
//            try {
//                Log.d("TriviaViewModel", "Triggering API call. Category: $category, Difficulty: $difficulty")
//                val fetchedQuestions = repository.getFilteredQuestions(
//                    amount = 10,
//                    category = category,
//                    difficulty = difficulty
//                )
//
//                if (fetchedQuestions.isEmpty() && difficulty != "hard") {
//                    // Fetch the next difficulty if no questions found
//                    val nextDifficulty = when (difficulty) {
//                        "easy" -> "medium"
//                        "medium" -> "hard"
//                        else -> "hard"
//                    }
//                    fetchQuestionsForCategory(category, nextDifficulty)
//                } else if (fetchedQuestions.isNotEmpty()) {
//                    _questions.value = fetchedQuestions
//                    _currentQuestionIndex.value = 0
//                    Log.d("TriviaViewModel", "Loaded ${fetchedQuestions.size} questions for Category: $category, Difficulty: $difficulty")
//                } else {
//                    Log.d("TriviaViewModel", "No more questions available for Category: $category")
//                }
//            } catch (e: Exception) {
//                Log.e("TriviaViewModel", "Error fetching questions for $category: ${e.message}")
//            }
//        }
//    }
//
//
//
//    fun selectAnswer(answer: String) {
//        _selectedAnswer.value = answer
//        val correctAnswer = _questions.value.getOrNull(_currentQuestionIndex.value)?.correctAnswer
//        _isAnswerCorrect.value = correctAnswer == answer
//        Log.d("TriviaViewModel", "Answer selected: $answer, Correct Answer: $correctAnswer")
//    }
//
//    fun moveToNextQuestion() {
////        if (_currentQuestionIndex.value < _questions.value.size - 1) {
////            _currentQuestionIndex.value += 1
////            _selectedAnswer.value = null
////            _isAnswerCorrect.value = null
////            Log.d("TriviaViewModel", "Moved to question index: ${_currentQuestionIndex.value}")
////        } else {
////            Log.d("TriviaViewModel", "No more questions left in the current set.")
////        }
//    }
//
//    // Fetch all questions and log categories for debugging
////    fun fetchAllQuestionsAndLogCategories() {
////        viewModelScope.launch {
////            try {
////                val fetchedQuestions = repository.getFilteredQuestions(
////                    amount = 10,
////                    difficulty = null // Fetch questions without filtering by difficulty
////                ).filter { it.question.isNotBlank() && it.correctAnswer.isNotBlank() }
////
////                Log.d("TriviaViewModel", "Fetched ${fetchedQuestions.size} questions for logging categories")
////
////                // Log the fetched questions
////                _questions.value = fetchedQuestions
////
////                // Extract unique categories and log them
////                val uniqueCategories = fetchedQuestions.mapNotNull { it.category }.distinct()
////                uniqueCategories.forEach { category ->
////                    Log.d("TriviaCategory", "Available Category: $category")
////                }
////            } catch (e: Exception) {
////                Log.e("TriviaViewModel", "Error fetching questions: ${e.message}")
////            }
////        }
////    }
//}



//@HiltViewModel
//class TriviaViewModel @Inject constructor(
//    private val repository: TriviaRepository
//) : ViewModel() {
//
//    // State for the list of trivia questions
//    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
//    val questions: StateFlow<List<TriviaQuestion>> = _questions
//
//    // State for tracking the current question index
//    private val _currentQuestionIndex = MutableStateFlow(0)
//    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex
//
//    // State for tracking the selected answer
//    private val _selectedAnswer = MutableStateFlow<String?>(null)
//    val selectedAnswer: StateFlow<String?> = _selectedAnswer
//
//    // State to track if the answer is correct
//    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
//    val isAnswerCorrect: StateFlow<Boolean?> = _isAnswerCorrect
//
//    // States to hold selected difficulty and category
//    private var selectedDifficulty: String? = "Any"
//    private var selectedCategory: String? = "Any"
//
//    init {
//        fetchEasyQuestions()
//    }
//
//    private fun fetchEasyQuestions() {
//        viewModelScope.launch {
//            try {
//                // Fetch only easy questions
//                val fetchedQuestions = repository.getFilteredQuestions(
//                    amount = 20,
//                    difficulty = "easy"  // Set difficulty to "easy"
//                ).filter { it.question.isNotBlank() && it.correctAnswer.isNotBlank() }
//                _questions.value = fetchedQuestions
//            } catch (e: Exception) {
//                Log.e("TriviaViewModel", "Error fetching questions: ${e.message}")
//            }
//        }
//    }
//
//    fun moveToNextQuestion() {
//        if (_currentQuestionIndex.value < _questions.value.size - 1) {
//            _currentQuestionIndex.value += 1
//        } else {
//            Log.d("TriviaViewModel", "No more questions available.")
//        }
//    }
//
//
//
//}

