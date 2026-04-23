package com.rach.co.auth.presentation.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.rach.co.R
import com.rach.co.utils.K

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()
    val state = viewModel.state
    val context = LocalContext.current
    var showForgotDialog by remember { mutableStateOf(false) }

    var rememberMe by rememberSaveable { mutableStateOf(false) }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }


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
            .padding(24.dp)
            .verticalScroll(state = scrollState)
    ) {

        Spacer(modifier = Modifier.weight(0.4f))

        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "app logo",
            modifier = Modifier
                .height(210.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )


        Text("Let's Sign In.!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(
            "SignIn to Your Account to Continue your Courses",
            fontSize = 13.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 10.dp)
        )


        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,


            label = {
                Icon(painter = painterResource(R.drawable.mail_24px), contentDescription = "mail")
                Text(
                    " Email",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 22.dp)
                )
            },

            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,


            label = {
                Icon(
                    painter = painterResource(R.drawable.lock_24px),
                    contentDescription = "null"
                )
                Text(
                    " Password",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 22.dp)
                )
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },

            trailingIcon = {
                IconButton({ passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) {
                                R.drawable.outline_visibility_24
                            } else {
                                R.drawable.outline_visibility_off_24
                            }
                        ), contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })

            Text("Remember Me", fontWeight = FontWeight.Medium, fontSize = 13.sp)

            TextButton(
                modifier = Modifier.padding(start = 78.dp),
                onClick = { showForgotDialog = true }
            ) {
                Text("Forgot Password?", fontWeight = FontWeight.Medium, fontSize = 13.sp)
            }

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
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xED062AEF)),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Or Continue With",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 30.dp, start = 120.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {


            //Gmail Button
            IconButton({
                googleClient.signOut().addOnCompleteListener {
                    launcher.launch(googleClient.signInIntent)
                }
            }, modifier = Modifier.width(220.dp)
                .height(45.dp)) {

                Image(
                    painter = painterResource(R.drawable.google_netural_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )

            }

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




        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            TextButton({ navController.navigate("signup") }) {

                Text(
                    "Don't have an Account?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    "  SIGN UP",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF062AEF)
                )
            }
        }




        Spacer(modifier = Modifier.height(16.dp))
    }

    LaunchedEffect(key1 = state.success) {
        if (state.success) {
            viewModel.savedatastorepremium(){
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
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
