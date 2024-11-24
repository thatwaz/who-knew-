package com.thatwaz.whoknew.data.models


import android.os.Build
import android.text.Html
import com.google.gson.annotations.SerializedName

data class TriviaQuestion(
    @SerializedName("question")
    val question: String = "",

    @SerializedName("correct_answer")
    val correctAnswer: String = "",

    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String> = emptyList(),

    @SerializedName("difficulty")
    val difficulty: String? = null,

    @SerializedName("category")
    val category: String? = null
) {
    val decodedQuestion: String
        get() = question.decodeHtml()

    val decodedCorrectAnswer: String
        get() = correctAnswer.decodeHtml()

    val decodedIncorrectAnswers: List<String>
        get() = incorrectAnswers.map { it.decodeHtml() }

    val options: List<String> by lazy {
        (decodedIncorrectAnswers + decodedCorrectAnswer).shuffled()
    }
}

// Extension function to decode HTML entities in questions and answers
fun String.decodeHtml(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}


//import com.google.gson.annotations.SerializedName
//
//data class TriviaQuestion(
//    @SerializedName("question")
//    val question: String = "",
//
//    @SerializedName("correct_answer")
//    val correctAnswer: String = "",
//
//    @SerializedName("incorrect_answers")
//    val incorrectAnswers: List<String> = emptyList(),
//
//    @SerializedName("difficulty")
//    val difficulty: String? = null,  // For filtering by difficulty
//
//    @SerializedName("category")
//    val category: String? = null     // For filtering by category
//) {
//    // Use lazy to initialize options once and shuffle
//    val options: List<String> by lazy {
//        (incorrectAnswers + correctAnswer).shuffled()
//    }
//}





