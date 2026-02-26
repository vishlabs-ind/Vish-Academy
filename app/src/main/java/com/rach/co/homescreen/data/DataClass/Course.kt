package com.rach.co.homescreen.data.DataClass

data class Course(
    val courseId: String = "",
    val title: String = "",
    val description: String = "",
    val subtitle: String = "",
    val displayName: String = "",
    val price: Int = 0,
    val order: Int = 0,
    val thumbnail: String = ""
)

data class Chapter(
    val name: String = ""
)

data class ChapterDetail(
    val ChapterName: String = "",
    val order: Int = 0,
    val ytlink: String = ""
)