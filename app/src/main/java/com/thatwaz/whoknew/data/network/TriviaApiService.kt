package com.thatwaz.whoknew.data.network



import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {
    @GET("api.php")
    suspend fun getTriviaQuestions(
        @Query("amount") amount: Int,
        @Query("type") type: String = "multiple", // Default is multiple-choice
        @Query("category") category: String? = null, // Nullable category
        @Query("difficulty") difficulty: String? = null // Nullable difficulty
    ): TriviaResponse
}

