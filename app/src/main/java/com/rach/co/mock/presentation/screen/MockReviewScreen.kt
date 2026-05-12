package com.rach.co.mock.presentation.screen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rach.co.mock.presentation.viewModel.MockReviewViewModel
import com.rach.co.mock.presentation.viewModel.MockScreenState
import com.rach.co.navigation.Routes
import kotlin.collections.getOrNull

@Composable
fun MockReviewScreen(
    resultId: Int,
    navController: NavController,
    viewModel: MockReviewViewModel = hiltViewModel()
) {
    val questions by viewModel.questions
    val userAnswers by viewModel.userAnswers
    val subjectTitle by viewModel.subjectTitle
    val screenState by viewModel.screenState
    val currentIndex by viewModel.currentIndex

    BackHandler {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) { inclusive = false }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadReview(resultId)
    }

    // --- Loading / Error ---
    when (screenState) {
        is MockScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }
        is MockScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (screenState as MockScreenState.Error).message,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            return
        }
        else -> {}
    }

    // ← fixed: correct Elvis operator
    val question = questions.getOrNull(currentIndex) ?: return
    val totalQuestions = questions.size
    val selectedOption = userAnswers[currentIndex]  // null or -1 = skipped

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp)
    ) {

        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$subjectTitle Review",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Progress ---
        Text(
            text = "Question ${currentIndex + 1} / $totalQuestions",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / totalQuestions },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Scrollable Content ---
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            // --- Question Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = question.question,
                    modifier = Modifier.padding(20.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 26.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Options with color logic ---
            question.options.forEachIndexed { index: Int, optionText: String ->

                val isUserSelected = selectedOption == index
                val isCorrect = question.correctAnswerIndex == index
                val isSkipped = selectedOption == null || selectedOption == -1

                val backgroundColor = when {
                    isCorrect -> Color(0xFF4CAF50)
                    isUserSelected && !isCorrect -> Color(0xFFF44336)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                val textColor = when {
                    isCorrect -> Color.White
                    isUserSelected && !isCorrect -> Color.White
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                val label = when {
                    isUserSelected && isCorrect -> "✓ Your Answer (Correct)"
                    isUserSelected && !isCorrect -> "✗ Your Answer (Wrong)"
                    isCorrect && isSkipped -> "✓ Correct Answer"
                    isCorrect -> "✓ Correct Answer"
                    else -> null
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = backgroundColor
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = optionText,
                            fontSize = 15.sp,
                            color = textColor,
                            fontWeight = if (isCorrect || isUserSelected)
                                FontWeight.SemiBold else FontWeight.Normal
                        )
                        label?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                color = textColor.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // --- Skipped label ---
            if (selectedOption == null || selectedOption == -1) {
                Text(
                    text = "⚠️ You skipped this question",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Navigation Footer ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { viewModel.previousQuestion() },
                enabled = currentIndex > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous", fontSize = 15.sp)
            }

            if (currentIndex < totalQuestions - 1) {
                Button(
                    onClick = { viewModel.nextQuestion() },
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Next", fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Button(
                    onClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = false }
                        }
                    },
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Finish", fontSize = 15.sp)
                }
            }
        }
    }
}