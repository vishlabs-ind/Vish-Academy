package com.rach.co.quiz.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rach.co.quiz.presentation.screen.ui.buttonBlue
import com.rach.co.quiz.presentation.screen.ui.innerCircleBlue
import com.rach.co.quiz.presentation.screen.ui.outerCircleBlue

@Composable
fun ScoreScreen(
    score: Int,
    totalQuestions: Int,
    onShareClick: () -> Unit,
    onRestartClick: () -> Unit
) {

    // val percentage = (score * 100) / totalQuestions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        // Score Circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(240.dp)
                .background(outerCircleBlue, CircleShape) // Outer Border
                .padding(8.dp) // Thickness of the border
                .background(innerCircleBlue, CircleShape) // Inner Background
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Your Score",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$score/$totalQuestions",
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Congratulation",
            color = outerCircleBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

//        Text(
//            text = "Quiz Completed!",
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Score: $score / $totalQuestions",
//            style = MaterialTheme.typography.headlineSmall
//        )

        Spacer(modifier = Modifier.height(12.dp))

//        Text(
//            text = "Percentage: $percentage%",
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(onClick = onRestartClick) {
//            Text("Restart Quiz")
//        }

        Text(
            text = "Great job, user name! You did it.",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium        )

        Spacer(modifier = Modifier.weight(1.2f))


            Spacer(modifier = Modifier.height(16.dp))

            // Back to Home Button
            Button(
                onClick = onRestartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .navigationBarsPadding()   // pushes above nav bar
                    .padding(bottom = 16.dp),  // extra space (important)
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
            ) {
                Text("Restart Quiz", color = Color.White, fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
}