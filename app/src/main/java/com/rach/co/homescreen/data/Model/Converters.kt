package com.rach.co.homescreen.data.Model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rach.co.quiz.data.dataClass.Question

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromQuestionList(value: List<Question>): String {
        val type = object : TypeToken<List<Question>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toQuestionList(value: String): List<Question> {
        val type = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}
