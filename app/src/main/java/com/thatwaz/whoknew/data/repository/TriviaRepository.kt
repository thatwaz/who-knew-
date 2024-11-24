package com.thatwaz.whoknew.data.repository

import com.thatwaz.whoknew.data.models.TriviaQuestion




interface TriviaRepository {
    suspend fun getFilteredQuestions(amount: Int, difficulty: String? = null, category: String? = null): List<TriviaQuestion>
}
