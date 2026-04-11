package com.rach.co.navigation

import CompleteProfileScreen
import QuizCourseScreen
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.webkit.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.rach.co.auth.presentation.onboard.OnboardScreen
import com.rach.co.homescreen.presentation.home.presentation.Screen.HomeScreen
import com.rach.co.auth.presentation.login.LoginScreen
import com.rach.co.auth.presentation.signup.SignupScreen
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.presentation.MyCouseScreen.ChapterDetailScreen
import com.rach.co.homescreen.presentation.MyCouseScreen.ChapterScreen
import com.rach.co.homescreen.presentation.Screen.AllCourseScreen
import com.rach.co.homescreen.presentation.Screen.CoursePurchasedScreen
import com.rach.co.homescreen.presentation.Screen.MyCourse
import com.rach.co.homescreen.presentation.Screen.PdfScreen
import com.rach.co.homescreen.presentation.Screen.Profile
import com.rach.co.quiz.presentation.screen.QuizScreen
import com.rach.co.quiz.presentation.screen.ScoreScreen
import com.rach.co.ui.VideoPlayerScreen
import com.rach.co.utils.OnboardingManager

@Composable
fun AuthApp() {

    val context = LocalContext.current
    val onboardingManager = remember { OnboardingManager(context) }

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {

        startDestination =
            if (onboardingManager.isFirstLaunch()) {
                "onboard"
            } else {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null && user.isEmailVerified)
                    "home"
                else
                    "login"
            }
    }


    if (startDestination == null) {
        // Splash / Loader
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {

        composable("onboard") {
            OnboardScreen(
                onSkip = {
                    onboardingManager.setFirstLaunchCompleted()
                    navController.navigate("login") {
                        popUpTo("onboard") { inclusive = true }
                    }
                },
                onNext = {
                    onboardingManager.setFirstLaunchCompleted()
                    navController.navigate("login") {
                        popUpTo("onboard") { inclusive = true }
                    }
                }
            )
        }

        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.PROFILE) { Profile(navController = navController) }
        composable(Routes.EXAM_RESULT) { Profile(navController = navController) }
        composable(Routes.COMPLETE_PROFILE) { CompleteProfileScreen(navController = navController) }

        composable(Routes.COURSES) {
            AllCourseScreen(navController= navController)
        }
        composable(Routes.My_COURSES) {
            MyCourse(navController)
        }

        composable("quiz_course") {
            QuizCourseScreen(navController)
        }

        // here
        composable(
            route = "${Routes.QUIZ}/{courseId}",
            arguments = listOf(
                navArgument("courseId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val courseId = backStackEntry.arguments?.getString("courseId")!!

            QuizScreen(
                courseId = courseId,
                navController = navController
            )
        }

        // score
        composable(
            route = "${Routes.SCORE}/{score}/{total}",
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0

            ScoreScreen(
                score = score,
                totalQuestions = total,
                onRestartClick = {
                    navController.popBackStack(Routes.HOME, false)
                },
                onBackClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(
            Routes.SUBJECT
        ) { backStackEntry ->

            val courseId =
                backStackEntry.arguments?.getString("courseId")!!

            val subjectName =
                backStackEntry.arguments?.getString("subjectName")!!

            ChapterDetailScreen(
                courseId,
                subjectName,
                navController = navController
            )
        }

        composable(
           Routes.CHAPTER
        ) { backStackEntry ->

            val courseId =
                backStackEntry.arguments
                    ?.getString("courseId")!!

            ChapterScreen(courseId, navController = navController)
        }

        composable(
            route = "course_purchased/{course}",
            arguments = listOf(
                navArgument("course") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val courseJson =
                backStackEntry.arguments?.getString("course")

            val course =
                Gson().fromJson(courseJson, Course::class.java)

            CoursePurchasedScreen(
                navController = navController,
                course = course
            )
        }



        composable(
            route = Routes.VIDEO_PLAYER_SCREEN,
            arguments = listOf(
                navArgument("ytlink") { type = NavType.StringType },
                        navArgument("pdflink") { type = NavType.StringType }   // 👈 new argument

            )
        ) { backStackEntry ->

            val ytLink =
                backStackEntry.arguments?.getString("ytlink") ?: ""
            val pdfLink =
                backStackEntry.arguments?.getString("pdflink") ?: ""


            VideoPlayerScreen(navController,ytLink, pdfLink)
        }



        composable(
            route = "pdfscreen/{pdflink}",
            arguments = listOf(
                navArgument("pdflink") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val pdfLink = Uri.decode(
                backStackEntry.arguments?.getString("pdflink") ?: ""
            )

            PdfScreen(pdfLink)
        }

    }
}


object Routes {

    const val HOME = "home"
    const val COURSES = "courses"
    const val COMPLETE_PROFILE = "complete_profile"
    const val PROFILE = "profile"
    const val My_COURSES = "my_courses"
    const val QUIZ = "quiz"
    const val SCORE = "score"
    const val EXAM_RESULT = "exam_result"
    const val COURSE_PURCHASED = "course_purchased/{course}"
    fun coursePurchased(course: String) = "course_purchased/$course"
    const val CHAPTER = "chapters/{courseId}"
    const val SUBJECT = "chapterDetails/{courseId}/{subjectName}"
    const val VIDEO_PLAYER_SCREEN = "videoplayscreen/{ytlink}/{pdflink}"

}