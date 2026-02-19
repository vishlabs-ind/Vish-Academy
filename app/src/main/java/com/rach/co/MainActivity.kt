package com.rach.co

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rach.co.AUTH.presentation.home.HomeScreen
import com.rach.co.AUTH.presentation.login.LoginScreen
import com.rach.co.AUTH.presentation.signup.SignupScreen
import com.rach.co.ui.theme.VishAcademyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VishAcademyTheme {
                val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                val user = auth.currentUser

                val isLoggedIn = user != null && user.isEmailVerified

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthApp(isLoggedIn=isLoggedIn)
                }
            }
        }
    }
}


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