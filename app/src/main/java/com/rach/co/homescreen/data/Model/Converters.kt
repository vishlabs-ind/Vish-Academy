package com.rach.co.homescreen.data.Model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rach.co.quiz.data.dataClass.Question
import java.lang.reflect.Type

class Converters {

    private val gson = Gson()

    // Pre-define types (most efficient way)
    private val questionListType: Type = TypeToken.getParameterized(
        List::class.java,
        Question::class.java
    ).type

    private val stringListType: Type = TypeToken.getParameterized(
        List::class.java,
        String::class.java
    ).type

    @TypeConverter
    fun fromQuestionList(value: List<Question>?): String {
        return gson.toJson(value, questionListType)
    }

    @TypeConverter
    fun toQuestionList(value: String?): List<Question> {
        if (value.isNullOrEmpty()) return emptyList()
        return gson.fromJson(value, questionListType) ?: emptyList()
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value, stringListType)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return gson.fromJson(value, stringListType) ?: emptyList()
    }
}