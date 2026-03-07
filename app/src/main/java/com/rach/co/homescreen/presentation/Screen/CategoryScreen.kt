package com.rach.co.homescreen.presentation.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rach.co.homescreen.presentation.viewmodel.QuizCategoryViewModel

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