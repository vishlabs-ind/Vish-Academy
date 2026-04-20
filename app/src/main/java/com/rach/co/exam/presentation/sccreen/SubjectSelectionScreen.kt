package com.rach.co.exam.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rach.co.exam.data.dataClass.ExamCourse
import com.rach.co.exam.presentation.viewmodel.ExamAccessResult
import com.rach.co.exam.presentation.viewmodel.ExamScreenState
import com.rach.co.exam.presentation.viewmodel.ExamViewModel
import com.rach.co.navigation.Routes
import com.valentinilk.shimmer.shimmer
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SubjectSelectionScreen(
    navController: NavController,
    viewModel: ExamViewModel = hiltViewModel()
) {
    val courses = viewModel.examCourses
    val screenState by viewModel.selectionScreenState
    val attemptedCourseIds by viewModel.attemptedCourseIds

    // dialog state
    var dialogMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadEnabledCourses()
    }

    // --- Error/Info Dialog ---
    dialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { dialogMessage = null },
            title = {
                Text(
                    text = "Not Allowed",
                    fontWeight = FontWeight.Bold
                )
            },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { dialogMessage = null }) {
                    Text("OK")
                }
            }
        )
    }

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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Available Exams",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Only active exams are shown",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (screenState) {

            // --- Shimmer Loading ---
            is ExamScreenState.Loading -> {
                repeat(3) {
                    ShimmerCourseCard()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // --- Error / Empty ---
            is ExamScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (screenState as ExamScreenState.Error).message,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            // --- Success ---
            is ExamScreenState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(courses) { course ->

                        val isAlreadyAttempted = attemptedCourseIds.contains(course.courseId)

                        ExamCourseCard(
                            course = course,
                            isAlreadyAttempted = isAlreadyAttempted,
                            onStartClick = {

                                // time check
                                when (val access = viewModel.checkExamAccess(course)) {
                                    is ExamAccessResult.Allowed -> {
                                        navController.navigate("${Routes.EXAM}/${course.courseId}")
                                    }
                                    is ExamAccessResult.NotStartedYet -> {
                                        dialogMessage = "Exam has not started yet.\nStarts at: ${access.startTime}"
                                    }
                                    is ExamAccessResult.Ended -> {
                                        dialogMessage = "Exam has already ended.\nEnded at: ${access.endTime}"
                                    }
                                    is ExamAccessResult.AlreadyAttempted -> {
                                        dialogMessage = "You have already attempted this exam."
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- Exam Course Card ---
@Composable
fun ExamCourseCard(
    course: ExamCourse,
    isAlreadyAttempted: Boolean,
    onStartClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val startFormatted = course.startTime?.toDate()?.let { dateFormat.format(it) } ?: "N/A"
    val endFormatted = course.endTime?.toDate()?.let { dateFormat.format(it) } ?: "N/A"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- Course Title ---
            Text(
                text = course.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // --- Start Time ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Start: ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = startFormatted,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // --- End Time ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "End:   ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFF44336)
                )
                Text(
                    text = endFormatted,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Start Button ---
            Button(
                onClick = onStartClick,
                enabled = !isAlreadyAttempted,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text(
                    text = if (isAlreadyAttempted) "Already Attempted" else "Start Exam",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

// --- Shimmer Card ---
@Composable
fun ShimmerCourseCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // title shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(20.dp)
                    .shimmer()
            ) {
                Surface(color = Color.LightGray) {}
            }

            Spacer(modifier = Modifier.height(12.dp))

            // start time shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(14.dp)
                    .shimmer()
            ) {
                Surface(color = Color.LightGray) {}
            }

            Spacer(modifier = Modifier.height(8.dp))

            // end time shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .shimmer()
            ) {
                Surface(color = Color.LightGray) {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // button shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .shimmer()
            ) {
                Surface(color = Color.LightGray) {}
            }
        }
    }
}