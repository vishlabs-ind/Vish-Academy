package com.rach.co.homescreen.data.RepoImpl


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rach.co.homescreen.data.DataClass.Chapter
import com.rach.co.homescreen.data.DataClass.ChapterDetail
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.domain.Repo.CourseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CourseRepository {



    override suspend fun getChapterDetails(
        courseId: String,
        subjectName: String
    ): List<ChapterDetail> {

        val snapshot =
            firestore
                .collection("courses")
                .document(courseId)
                .collection("chapters")
                .document(subjectName)
                .collection("Chapters").orderBy("order")
                .get()
                .await()

        return snapshot.documents.mapNotNull {
            it.toObject(ChapterDetail::class.java)
        }
    }

    override suspend fun getChapters(
        courseId: String
    ): List<Chapter> {

        val snapshot =
            firestore
                .collection("courses")
                .document(courseId)
                .collection("chapters").orderBy("order")
                .get()
                .await()

        return snapshot.documents.map {
            Chapter(name = it.id)
        }
    }

    override suspend fun getCoursesByIds(
        ids: List<String>
    ): List<Course> {

        if (ids.isEmpty()) return emptyList()

        val courses = mutableListOf<Course>()

        ids.forEach { id ->

            val doc = firestore
                .collection("courses")
                .document(id)
                .get()
                .await()

            doc.toObject(Course::class.java)
                ?.let { courses.add(it) }
        }

        return courses
    }

    override suspend fun getPurchasedCourseIds(): List<String> {

        val uid = auth.currentUser?.uid
            ?: return emptyList()

        val document = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()

        return document
            .get("coursePurchased")
                as? List<String>
            ?: emptyList()
    }

    override suspend fun searchCourses(query: String): List<Course> {

        return try {

            val snapshot = firestore
                .collection("courses")
                .limit(50) // 🔥 fetch limited data
                .get()
                .await()

            val list = snapshot.documents.mapNotNull {
                it.toObject(Course::class.java)?.copy(courseId = it.id)
            }

            val q = query.lowercase().trim()
            val words = q.split(" ")

            // 🔥 MULTI-FIELD + MULTI-WORD SEARCH
            list.filter { course ->

                val combinedText = (
                        course.title +
                                course.description +
                                (course.subtitle ?: "") +
                                course.price
                        ).lowercase()

                words.any { word ->
                    combinedText.contains(word)
                }
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCoursesPaginated(
        lastCourse: Course?,
        limit: Long
    ): Pair<List<Course>, Course?> {

        var query = firestore
            .collection("courses")
            .orderBy("title") // ⚠️ must be indexed field

        if (lastCourse != null) {
            query = query.startAfter(lastCourse.title)
        }

        val snapshot = query
            .limit(limit)
            .get()
            .await()

        val courses = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Course::class.java)?.copy(courseId = doc.id)
        }

        val lastVisible = courses.lastOrNull()

        return Pair(courses, lastVisible)
    }

    override suspend fun getCourses(): List<Course> {

        return try {

            val snapshot = firestore
                .collection("courses")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {

                    val course = doc.toObject(Course::class.java)
                        ?.copy(courseId = doc.id)

                    Log.d("FirestoreCourse", "Loaded Course: $course")

                    course

                } catch (e: Exception) {

                    Log.e(
                        "FirestoreCourseError",
                        "Error parsing doc ${doc.id} -> ${doc.data}",
                        e
                    )

                    null
                }
            }

        } catch (e: Exception) {

            Log.e("FirestoreCourseError", "Firestore fetch failed", e)

            emptyList()
        }
    }

    override suspend fun buyCourse(order: Int): Course? {

        return try {

            val snapshot = firestore
                .collection("courses").orderBy("order")
                .whereEqualTo("order", order)
                .get()
                .await()

            snapshot.documents.firstOrNull()
                ?.toObject(Course::class.java)

        } catch (e: Exception) {
            null
        }
    }
}