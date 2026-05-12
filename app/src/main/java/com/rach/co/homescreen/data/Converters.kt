package com.rach.co.homescreen.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rach.co.quiz.data.dataClass.Question

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromQuestionList(value: List<Question>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toQuestionList(value: String): List<Question> {
        val listType = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}