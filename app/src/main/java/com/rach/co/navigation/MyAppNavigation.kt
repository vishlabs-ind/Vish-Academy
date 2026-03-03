package com.rach.co.navigation

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
import com.google.firebase.auth.FirebaseAuth
import com.rach.co.auth.presentation.onboard.OnboardScreen
import com.rach.co.homescreen.presentation.home.presentation.Screen.HomeScreen
import com.rach.co.auth.presentation.login.LoginScreen
import com.rach.co.auth.presentation.signup.SignupScreen
import com.rach.co.homescreen.presentation.MyCouseScreen.ChapterDetailScreen
import com.rach.co.homescreen.presentation.MyCouseScreen.ChapterScreen
import com.rach.co.homescreen.presentation.Screen.AllCourseScreen
import com.rach.co.homescreen.presentation.Screen.CoursePurchasedScreen
import com.rach.co.homescreen.presentation.Screen.MyCourse
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


        composable(Routes.COURSES) {
            AllCourseScreen(navController= navController)
        }
        composable(Routes.My_COURSES) {
            MyCourse(navController)
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
            route = Routes.COURSE_PURCHASED,
            arguments = listOf(
                navArgument("order") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val order =
                backStackEntry.arguments
                    ?.getInt("order") ?: 0

            CoursePurchasedScreen(
                order, navController = navController,
            )
        }



        composable(
            route = Routes.VIDEO_PLAYER_SCREEN,
            arguments = listOf(
                navArgument("ytlink") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val ytLink =
                backStackEntry.arguments?.getString("ytlink") ?: ""

            VideoPlayerScreen(ytLink)
        }

    }
}


object Routes {

    const val HOME = "home"
    const val COURSES = "courses"

    const val My_COURSES = "my_courses"
    const val COURSE_PURCHASED =
        "course_purchased/{order}"


    const val CHAPTER = "chapters/{courseId}"
    const val SUBJECT = "chapterDetails/{courseId}/{subjectName}"

    const val VIDEO_PLAYER_SCREEN = "videoplayscreen/{ytlink}"

}