package com.rach.co.quiz.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rach.co.quiz.presentation.viewmodel.QuizCategoryViewModel

@Composable
fun CategoryScreen(
    viewModel: QuizCategoryViewModel = viewModel()
) {

    // Observe states from ViewModel
    val courses by viewModel.courseList
    val isDialogOpen by viewModel.isDialogOpen

    // Main Screen Layout
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = {
                viewModel.loadCourses()
                viewModel.openDialog()
            }
        ) {
            Text("Start Quiz")
        }
    }

    // Popup Dialog
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.closeDialog() },
            confirmButton = {},
            title = {
                Text("Select Course")
            },
            text = {
                LazyColumn {
                    items(courses) { course ->
                        TextButton(
                            onClick = {
                                viewModel.selectCourse(course)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(course.courseId)
                        }
                    }
                }
            }
        )
    }
}