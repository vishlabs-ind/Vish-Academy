package com.rach.co.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rach.co.auth.presentation.home.HomeScreen
import com.rach.co.auth.presentation.login.LoginScreen
import com.rach.co.auth.presentation.signup.SignupScreen


@Composable
fun AuthApp(
    isLoggedIn: Boolean
){
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = if(isLoggedIn) "home" else "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("home") { HomeScreen(navController) }
    }
}