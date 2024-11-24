package com.thatwaz.whoknew.data.repository

import android.util.Log
import com.thatwaz.whoknew.data.models.TriviaQuestion
import com.thatwaz.whoknew.data.network.TriviaApiService
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val apiService: TriviaApiService
) : TriviaRepository {

    val categoryMap = mapOf(
        "General Knowledge" to 9,
        "Entertainment: Books" to 10,
        "Entertainment: Film" to 11,
        "Entertainment: Music" to 12,
        "Entertainment: Musicals & Theatres" to 13,
        "Entertainment: Television" to 14,
        "Entertainment: Video Games" to 15,
        "Entertainment: Board Games" to 16,
        "Science & Nature" to 17,
        "Science: Computers" to 18,
        "Science: Mathematics" to 19,
        "Mythology" to 20,
        "Sports" to 21,
        "Geography" to 22,
        "History" to 23,
        "Politics" to 24,
        "Art" to 25,
        "Celebrities" to 26,
        "Animals" to 27,
        "Vehicles" to 28,
        "Entertainment: Comics" to 29,
        "Science: Gadgets" to 30,
        "Entertainment: Japanese Anime & Manga" to 31,
        "Entertainment: Cartoon & Animations" to 32
    )



    private var requestCount = 0 // Track the number of requests made

    override suspend fun getFilteredQuestions(
        amount: Int,
        difficulty: String?,
        category: String?
    ): List<TriviaQuestion> {
        val categoryId = categoryMap[category]

        Log.d("TriviaRepository", "Category: $category, Category ID: $categoryId, Difficulty: $difficulty")

        return try {
            val response = apiService.getTriviaQuestions(
                amount = amount,
                category = categoryId?.toString(),
                difficulty = difficulty
            )

            Log.d("TriviaRepository", "API returned ${response.results.size} questions for Category ID: $categoryId")

            response.results
        } catch (e: Exception) {
            Log.e("TriviaRepository", "Error fetching questions: ${e.message}")
            emptyList()
        }
    }







//    override suspend fun getFilteredQuestions(
//        amount: Int,
//        difficulty: String?,
//        category: String?
//    ): List<TriviaQuestion> {
//        return try {
//            // Make the API request and log the response
//            val response = apiService.getTriviaQuestions(amount)
//            val questions = response.results
//
//            // Log each question to inspect the data
//            questions.forEach { question ->
//                Log.d("TriviaRepositoryImpl", "Question: ${question.question}")
//                Log.d("TriviaRepositoryImpl", "Correct Answer: ${question.correctAnswer}")
//                Log.d("TriviaRepositoryImpl", "Incorrect Answers: ${question.incorrectAnswers}")
//                Log.d("TriviaRepositoryImpl", "Category: ${question.category}")
//                Log.d("TriviaRepositoryImpl", "Difficulty: ${question.difficulty}")
//            }
//
//            // Filter questions based on difficulty and category if provided
//            questions.filter { question ->
//                val matchesDifficulty = difficulty == null || difficulty == "Any" || question.difficulty.equals(difficulty, ignoreCase = true)
//                val matchesCategory = category == null || category == "Any" || question.category.equals(category, ignoreCase = true)
//                matchesDifficulty && matchesCategory
//            }
//        } catch (e: Exception) {
//            Log.e("TriviaRepositoryImpl", "Error fetching questions: ${e.message}")
//            emptyList() // Return an empty list if there's an error
//        }
//    }
}


