package com.thatwaz.whoknew.filters



class GeneralKnowledgeFilter {

    private val keywordToTopicMap = mapOf(
        "president" to "History",
        "landmark" to "Geography",
        "planet" to "Science & Nature",
        "furlong" to "Measurements",
        "language" to "Language",
        "company" to "Companies",
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

    /**
     * Filters the question text to determine a related topic.
     * @param questionText The text of the trivia question.
     * @return The associated topic or "General Knowledge" if no match is found.
     */
    fun getTopicForQuestion(questionText: String): String {
        for ((keyword, topic) in keywordToTopicMap) {
            if (questionText.contains(keyword, ignoreCase = true)) {
                return topic
            }
        }
        return "General Knowledge" // Default if no keywords match
    }
}
