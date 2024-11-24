package com.thatwaz.whoknew.data.network



import com.google.gson.annotations.SerializedName
import com.thatwaz.whoknew.data.models.TriviaQuestion


data class TriviaResponse(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("results") val results: List<TriviaQuestion>
)

