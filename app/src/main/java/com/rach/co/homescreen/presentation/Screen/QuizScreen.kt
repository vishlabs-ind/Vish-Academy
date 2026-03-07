import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rach.co.homescreen.presentation.viewmodel.QuizViewModel
import com.rach.co.navigation.Routes

@Composable
fun QuizScreen(
    courseId: String,
    navController: NavController,
    viewModel: QuizViewModel = hiltViewModel()
) {

    val course by viewModel.course
    val questionIndex by viewModel.currentQuestionIndex

    LaunchedEffect(Unit) {
        viewModel.loadCourse(courseId)
    }

    val question = course?.questions?.getOrNull(questionIndex)

    if (question == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        Text(
            text = "Question ${questionIndex + 1}",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        question.options.forEachIndexed { index, option ->
            Button(
                onClick = {

                    viewModel.checkAnswer(index)

                    if (viewModel.isLastQuestion()) {

                        val score = viewModel.score.value
                        val total = viewModel.course.value?.questions?.size ?: 0

                        navController.navigate("${Routes.SCORE}/$score/$total")

                    } else {
                        viewModel.nextQuestion()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(option)
            } 
        }
    }
//    Button(
//        onClick = {
//
//            if (viewModel.isLastQuestion()) {
//
//                val score = viewModel.score.value
//                val total = viewModel.course.value?.questions?.size ?: 0
//
//                navController.navigate("${Routes.SCORE}/$score/$total")
//
//            } else {
//                viewModel.nextQuestion()
//            }
//
//        }
//    ) {
//        Text("Next")
//    }
}