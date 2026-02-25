package com.rach.co.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.google.firebase.auth.FirebaseAuth
import com.rach.co.auth.presentation.onboard.OnboardScreen
import com.rach.co.homescreen.presentation.home.HomeScreen
import com.rach.co.auth.presentation.login.LoginScreen
import com.rach.co.auth.presentation.signup.SignupScreen
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
        composable("home") { HomeScreen(navController) }
    }
}