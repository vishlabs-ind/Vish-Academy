package com.rach.co.auth.presentation.signup


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rach.co.R

@Composable
fun SignupScreen(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    var selectedTerms by rememberSaveable { mutableStateOf(false) }
    val state = viewModel.state
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp)
    ) {




            Image(painter = painterResource( id = R.drawable.app_logo), contentDescription = "app logo", modifier = Modifier.height(210.dp).align(alignment =Alignment.CenterHorizontally))



            Text("Getting Started.!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text("Create an Account to Continue your Courses", fontSize = 13.sp, color = Color.Gray,fontWeight =  FontWeight.SemiBold,modifier = Modifier.padding(top=10.dp))

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = {
                Icon(painter = painterResource(R.drawable.mail_24px), contentDescription = "mail")
                Text(" Email", fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 22.dp))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Icon(painter = painterResource(R.drawable.lock_24px),
                contentDescription = "null")
                Text(" Password",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 22.dp))
            }
            ,

            visualTransformation = if (passwordVisible){
                VisualTransformation.None} else{ PasswordVisualTransformation() },

            trailingIcon = {
                IconButton({passwordVisible  =  !passwordVisible}) {
                    Icon(painter = painterResource(if(passwordVisible){R.drawable.outline_visibility_24 }
                    else{ R.drawable.outline_visibility_off_24}),contentDescription = null)
                }
            },


            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Icon(painter = painterResource(R.drawable.lock_24px),
                contentDescription = "null")
                Text(" Password",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 22.dp))
            },

            visualTransformation = if (passwordVisible){
                VisualTransformation.None} else{ PasswordVisualTransformation() },

            trailingIcon = {
                IconButton({passwordVisible  =  !passwordVisible}) {
                    Icon(painter = painterResource(if(passwordVisible){R.drawable.outline_visibility_24 }
                    else{ R.drawable.outline_visibility_off_24}),contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        state.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }


        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedTerms,
                onClick = { selectedTerms = !selectedTerms }
            )

            Text("Agree to Terms & conditions")

        }

        Spacer(modifier = Modifier.height(12.dp))


        Button(
            onClick = { viewModel.signup() },
            enabled = !state.loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xED062AEF)),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.weight(0.6f))


        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {

            TextButton({      navController.navigate("login") {
                popUpTo("signup") { inclusive = true } }}) {

                Text(
                    "Already have an account?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Text(
                    "  Login",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF062AEF)
                )
            }
        }


       // Spacer(modifier = Modifier.height(16.dp))
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigate("login") {
                popUpTo("signup") { inclusive = true }
            }
        }
    }

}
