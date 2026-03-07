package com.rach.co.homescreen.data.local

import android.content.Context
import com.google.gson.Gson
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.data.DataClass.RootResponse

class QuizLocalDataSource (private val context: Context) {

    fun getAllCourses(): List<Course> {

        // Step 1: Read JSON file from assets
        val jsonString = context.assets
            .open("ddu free test serie.json")
            .bufferedReader()
            .use { it.readText() }

        // Step 2: Convert JSON string to Kotlin object
        val gson = Gson()
        val rootResponse = gson.fromJson(jsonString, RootResponse::class.java)

        // Step 3: Return list of courses
        return rootResponse.freeTestSeries
    }
}