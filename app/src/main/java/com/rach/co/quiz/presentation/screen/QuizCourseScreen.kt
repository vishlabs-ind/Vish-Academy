import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rach.co.ad.AdViewModel
import com.rach.co.quiz.presentation.viewmodel.QuizCategoryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun QuizCourseScreen(
    navController: NavController,
    viewModel: QuizCategoryViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel()
) {
    val courses by viewModel.courseList
    val context = LocalContext.current
    val activity = context as Activity
    val isAdReady by adViewModel.isAdReady

    // 1. Create a state to manage the "Waiting" period
    var isWaitingForAd by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .statusBarsPadding()
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Select Course",
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                items(courses) { course ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable(enabled = !isWaitingForAd) { // 2. Disable clicks while waiting
//                                scope.launch {
//                                    isWaitingForAd = true
//
//                                    // 3. The 2.5 second wait logic
//                                    delay(2500)
//
//                                    // 4. After wait, show Ad and navigate
//                                    adViewModel.showAd(activity) {
//                                        isWaitingForAd = false
//                                        navController.navigate("quiz/${course.courseId}")
//                                    }
//                                }
//                            },
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = MaterialTheme.colorScheme.surfaceVariant
//                        )
//                    ) {
//                        Text(
//                            text = course.courseTitle,
//                            modifier = Modifier.padding(20.dp),
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                    }
//                }
//            }
//        }
//
//        // 5. Full-screen Loading Overlay to block user interaction
//        if (isWaitingForAd) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f)) // Dim the background
//                    .clickable(enabled = false) { }, // Block clicks to background
//                contentAlignment = Alignment.Center
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    CircularProgressIndicator(color = Color.White)
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text(
//                        "Preparing your quiz...",
//                        color = Color.White,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.loadCourses()
        // adViewModel.loadAd(context) // Ensure ad is preloading
    }

    // Handle back button
    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fix for Dark Mode
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // --- Header Section ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Select Course",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            // Ad Loading Indicator (Moved inside the header row)
            if (!isAdReady) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Ad Loading...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Text(
            text = "Choose a series to start your quiz",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- Course List ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Gap between cards
        ) {
            items(courses) { course ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isWaitingForAd) { // 2. Disable clicks while waiting
                            scope.launch {
                                isWaitingForAd = true


                                // Wait for 'isAdReady' to be true, max 2.5 seconds
                                withTimeoutOrNull(2500) {
                                    snapshotFlow { isAdReady }.first { it == true }
                                }

                                // 4. After wait, show Ad and navigate
                                adViewModel.showAd(activity) {
                                    isWaitingForAd = false
                                    navController.navigate("quiz/${course.courseId}")
                                }
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = course.courseTitle,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Tap to start test",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Small arrow icon for better UI
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}