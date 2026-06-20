package com.rach.co.mock.presentation.screen


import com.rach.co.mock.presentation.viewModel.MockScreenState
import com.rach.co.mock.presentation.viewModel.MockViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.rach.co.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun MockExamScreen(
    subjectId: String,
    navController: NavController,
    viewModel: MockViewModel = hiltViewModel()
) {
    val subject by viewModel.subject
    val currentIndex by viewModel.currentIndex
    val timeLeftSeconds by viewModel.timeLeftSeconds
    val isSubmitting by viewModel.isSubmitting
    val isSubmitted by viewModel.isSubmitted
    val submitError by viewModel.submitError
    val examLoadState by viewModel.examLoadState
    val selectedAnswers = viewModel.selectedAnswers
    val showWarningDialog by viewModel.showWarningDialog
    val savedResultId by viewModel.savedResultId

    val scope = rememberCoroutineScope()
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSubject(subjectId)
    }

    // navigate to result when submitted
    LaunchedEffect(savedResultId) {
        savedResultId?.let { id ->
            val score = viewModel.calculateScore()
            val timeTaken = viewModel.timeTakenSeconds.value
            navController.navigate(
                "${Routes.MOCK_RESULT}/$id/$score/${subject?.questions?.size ?: 0}/$timeTaken"
            ) {
                popUpTo(Routes.MOCK_SUBJECT_SELECTION) { inclusive = false }
            }
        }
    }

    // intercept back press
    BackHandler { showExitDialog = true }

    // --- 30 Second Warning Dialog ---
    if (showWarningDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissWarningDialog() },
            title = {
                Text(
                    text = "⚠️ Time Running Out!",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text("30 seconds remaining! Your mock will be auto saved.")
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissWarningDialog() }) {
                    Text("OK")
                }
            }
        )
    }

    // --- Exit Dialog ---
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "Exit Mock?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to exit? Your progress will be saved.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        scope.launch {
                            viewModel.submitMock()
                        }
                    },
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Confirm", color = Color.White)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- Submit Error Dialog ---
    submitError?.let { error ->
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "Save Failed",
                    fontWeight = FontWeight.Bold
                )
            },
            text = { Text(error) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch { viewModel.submitMock() }
                    }
                ) {
                    Text("Try Again")
                }
            }
        )
    }

    // --- Loading State ---
    when (examLoadState) {
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
                    text = (examLoadState as MockScreenState.Error).message,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            return
        }
        else -> {}
    }

    val question = subject?.questions?.getOrNull(currentIndex)
    val totalQuestions = subject?.questions?.size ?: 0

    if (question == null) return

    // timer formatting
    val minutes = timeLeftSeconds / 60
    val seconds = timeLeftSeconds % 60
    val timerText = "%02d:%02d".format(minutes, seconds)

    // timer color — red when 30 seconds or less
    val timerColor = if (timeLeftSeconds <= 30) Color.Red
    else MaterialTheme.colorScheme.onBackground

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
            Text(
                text = subject?.subjectTitle ?: "",
                modifier = Modifier.weight(1f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "⏱ $timerText",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = timerColor
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

            // Question Card
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

            // --- Options ---
            val selectedOption = selectedAnswers[currentIndex]

            question.options.forEachIndexed { index, optionText ->
                MockOptionCard(
                    text = optionText,
                    isSelected = selectedOption == index,
                    onClick = { viewModel.selectAnswer(index) }
                )
                Spacer(modifier = Modifier.height(12.dp))
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

            // Back Button
            TextButton(
                onClick = { viewModel.previousQuestion() },
                enabled = currentIndex > 0 && !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.ArrowBack, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back", fontSize = 15.sp)
            }

            // Next / Submit Button
            Button(
                onClick = {
                    if (viewModel.isLastQuestion()) {
                        scope.launch {
                            viewModel.submitMock()
                        }
                    } else {
                        viewModel.nextQuestion()
                    }
                },
                enabled = !isSubmitting,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    if (viewModel.isLastQuestion()) {
                        Text("Submit", fontSize = 15.sp)
                    } else {
                        Text("Next", fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ArrowForward,
                            null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- Option Card ---
// Gray highlight on selection — no green/red during mock
@Composable
fun MockOptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                Color.Gray.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(
                2.dp,
                Color.Gray
            )
        else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}