package com.rach.co.quiz.presentation.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.ads.AdSize
import com.rach.co.navigation.Routes
import com.rach.co.ad.AdViewModel
import com.rach.co.quiz.presentation.viewmodel.QuizViewModel
import com.rach.co.utils.K

@Composable
fun QuizScreen(
    courseId: String,
    navController: NavController,
    viewModel: QuizViewModel = hiltViewModel(),
    viewModels: AdViewModel = hiltViewModel()
) {

    BackHandler {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) { inclusive = true }
        }
    }

    // 1. Collect States from ViewModel
    val course by viewModel.course
    val questionIndex by viewModel.currentQuestionIndex

    // ad's
    val context = LocalContext.current
    val activity = context as Activity

    val selectedOptionIndex = viewModel.selectedAnswers[questionIndex]

    // 2. Load course once
    LaunchedEffect(Unit) {
        viewModel.loadCourse(courseId)
        viewModels.loadAd2(context)
    }


    val question = course?.questions?.getOrNull(questionIndex)
    val totalQuestions = course?.questions?.size ?: 0

    // Loading State
    if (question == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF7C4DFF))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(24.dp)
    ) {


        // --- Header ---
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                    )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Quiz Test", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground)
        }


        Spacer(modifier = Modifier.height(20.dp))

        // --- Progress ---
        Text("Question ${questionIndex + 1} / $totalQuestions", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = {(questionIndex + 1).toFloat() / totalQuestions},
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(10.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color(0xFF7C4DFF).copy(alpha = 0.2f)
        )

        // --- Scrollable Content ---
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = question.question, // DYNAMIC DATA
                    modifier = Modifier.padding(20.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Options List
            question.options.forEachIndexed { index, optionText ->

                val hasAnswered = selectedOptionIndex != null
                QuizOption(
                    text = optionText,
                    isSelected = selectedOptionIndex == index,
                    isCorrect = if (hasAnswered) question.correctAnswerIndex == index else false,
                    onClick = {
                        viewModel.checkAnswer(index)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // --- Banner Ad Section ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for AdView
            BannerAdPlaceholder()
        }

        // --- Navigation Footer ---
        Row(modifier = Modifier.fillMaxWidth()
            .navigationBarsPadding(),   // adds space above navigation bar
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { viewModel.previousQuestion()},
                enabled = questionIndex > 0,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF)),
                shape = RoundedCornerShape(50)
                ) {
                Text("Back", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowBack, null, modifier = Modifier.size(18.dp))
            }

            Button(
                onClick = {
                    if (viewModel.isLastQuestion()) {
                        viewModels.showAd2(activity) {
                            viewModel.calculateScore()
                            val score = viewModel.score.value
                            navController.navigate("${Routes.SCORE}/$score/$totalQuestions")
                        }
                    } else {
                        viewModel.nextQuestion()
                    }
                },
              //  enabled = selectedOptionIndex != null, // Force user to answer before "Next"
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF)),
                shape = RoundedCornerShape(50)
            ) {
                Text("Next", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun QuizOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor = when {
        isSelected && isCorrect -> Color(0xFF4CAF50)   // Green if correct
        isSelected && !isCorrect -> Color(0xFFF44336)  // Red if wrong
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when {
        isSelected -> Color.White // Text on Red/Green should be white
        else -> MaterialTheme.colorScheme.onSurfaceVariant // Default theme text color
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(backgroundColor)
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun BannerAdPlaceholder() {
    // This uses AndroidView to host the legacy XML-based AdView
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->

            com.google.android.gms.ads.AdView(context).apply {
                    // 2. Calculate Adaptive Size(auto size)
                val adSize = getAdaptiveSize(context)
                setAdSize(adSize)

                // set Ad
                adUnitId = K.BANNER_ID

                loadAd(com.google.android.gms.ads.AdRequest.Builder().build())
            }
        }
    )
}

// Helper function to calculate the adaptive width
private fun getAdaptiveSize(context: android.content.Context): AdSize {
    val displayMetrics = context.resources.displayMetrics
    val adWidthPixels = displayMetrics.widthPixels.toFloat()
    val density = displayMetrics.density
    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}