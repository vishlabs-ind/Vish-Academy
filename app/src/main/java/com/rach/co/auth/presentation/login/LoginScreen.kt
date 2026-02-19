package com.rach.co.auth.presentation.login

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.GoogleAuthProvider
import com.rach.co.utils.K

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val context = LocalContext.current
    var showForgotDialog by remember { mutableStateOf(false) }

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(K.WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    val googleClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.result

                val credential = GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                )

                viewModel.googleLogin(credential)

            } catch (e: Exception) {
                viewModel.onError(e.message ?: "Google Sign-In Failed")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = { showForgotDialog = true }
        ) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(4.dp))

        state.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        Button(
            onClick = { viewModel.login() },
            enabled = !state.loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                googleClient.signOut().addOnCompleteListener {
                    launcher.launch(googleClient.signInIntent)
                }
            },
            enabled = !state.loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continue with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.weight(0.6f))

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                navController.navigate("signup")
            }
        ) {
            Text("Don’t have an account? Register")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    LaunchedEffect(key1 = state.success) {
        if (state.success) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }



    if (showForgotDialog) {

        AlertDialog(
            onDismissRequest = { showForgotDialog = false },

            title = { Text("Reset Password") },

            text = {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Enter your email") },
                    singleLine = true
                )
            },

            confirmButton = {
                TextButton(onClick = {
                    viewModel.forgotPassword(state.email)
                    showForgotDialog = false
                }) {
                    Text("Send")
                }
            },

            dismissButton = {
                TextButton(onClick = {
                    showForgotDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
