package com.rach.co.mock.data

object AnswerUtils {

    // convert Map<Int, Int> to String
    // example: {0:1, 1:0, 2:-1, 3:2} → "0:1,1:0,2:-1,3:2"
    fun encodeAnswers(answers: Map<Int, Int>, totalQuestions: Int): String {
        return (0 until totalQuestions).joinToString(",") { index ->
            val selected = answers[index] ?: -1  // -1 means skipped
            "$index:$selected"
        }
    }

    // convert String back to Map<Int, Int>
    // example: "0:1,1:0,2:-1,3:2" → {0:1, 1:0, 2:-1, 3:2}
    fun decodeAnswers(encoded: String): Map<Int, Int> {
        if (encoded.isEmpty()) return emptyMap()
        return encoded.split(",").associate { pair ->
            val parts = pair.split(":")
            parts[0].toInt() to parts[1].toInt()
        }
    }
}