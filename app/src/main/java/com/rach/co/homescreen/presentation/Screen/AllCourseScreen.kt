package com.rach.co.homescreen.presentation.Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rach.co.R
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay

@Composable
fun AllCourseScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val courses by viewModel.courses.collectAsState()
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchCourse()
        delay(2500)
        loading = false
    }

    Column(
        Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Available Courses",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))

        LazyColumn {
            if (loading) {
                items(5) {
                    CourseShimmer()
                }
            } else {
                items(courses) { course ->

                    Coursecard(false, course, navController)
                }
            }
        }
    }


}


@Composable
fun CourseShimmer() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shimmer()
            .background(Color.LightGray.copy(alpha = 0.4f))
            .height(100.dp)
    ) {}
}

@Composable
fun Coursecard(isMyCourse: Boolean = false, course: Course, NavController: NavController) {


    val order: Int = course.order
    Card(
        Modifier
            .fillMaxWidth()
            .height(164.dp)
            .padding(vertical = 8.dp)
            .clickable(onClick = {
                if (isMyCourse) {
                    NavController.navigate(
                        "chapters/${course.courseId}"
                    )
                    Toast.makeText(
                        NavController.context,
                        "This course is purchased",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    NavController.navigate(
                        "course_purchased/$order"
                    )
                }

            }),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        CardDefaults.cardElevation(12.dp)
    ) {
        Row(
            Modifier.background(Color(0xFFE3F2FD)), verticalAlignment = Alignment.CenterVertically
        ) {
            if (course.thumbnail.isNullOrEmpty()) {

                // ✅ Default Image
                Image(
                    painter = painterResource(R.drawable.learning),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

            } else {

                // ✅ Firestore Thumbnail Image
                AsyncImage(
                    model = course.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = course.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )


                Text(
                    text = course.description,
                    maxLines = 2,
                    fontSize = 10.sp
                )

                if (!isMyCourse) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹ ${course.price}  ",
                            fontWeight = FontWeight.Bold,
                            color = Color.Blue
                        )

                        Text(
                            text = "₹ 399",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.LineThrough
                        )



                        Button(
                            {},
                            modifier = Modifier
                                .padding(start = 52.dp)
                                .height(34.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF2A53EE
                                )
                            )
                        ) {

                            Text("Enroll Now", fontSize = 10.sp, color = Color.White)
                        }
                    }
                }

            }
        }
    }
}