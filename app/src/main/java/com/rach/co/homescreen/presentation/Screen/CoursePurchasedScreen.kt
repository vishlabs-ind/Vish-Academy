package com.rach.co.homescreen.presentation.Screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.rach.co.R
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import com.rach.co.utils.K
import com.valentinilk.shimmer.shimmer

@SuppressLint("ContextCastToActivity")
@Composable
fun CoursePurchasedScreen(
    navController: NavController,
    course: Course
) {

    val activity = LocalContext.current as ComponentActivity
    val vm: HomeViewModel = hiltViewModel(activity)

    val context = LocalContext.current
    val courses by vm.coursesDbOffline.collectAsState()

    LaunchedEffect(Unit) {
        if (courses.isEmpty()){
            vm.loadPurchasedCourses()

        }
    }

    val alreadyPurchased =
        courses.any { it.courseId == course.courseId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6E6EC))
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {

                AsyncImage(
                    model = course.thumbnail.ifEmpty { R.drawable.learning },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(8.dp)) {

                Card(
                    modifier = Modifier.padding(6.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(modifier = Modifier.padding(8.dp)) {

                        Text(
                            text = course.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            course.subtitle,
                            fontSize = 16.sp,
                            color = Color.Blue
                        )

                        Spacer(Modifier.height(9.dp))

                        Text(
                            text = "Course: ${course.description}",
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(bottom = 20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Course: ${course.displayName}",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        "₹ ${course.price}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        vm.startPurchase(course.courseId)

                        vm.startPayment(
                            activity = context as Activity,
                            email = "",
                            amountInRupees = course.price,
                            keyId = K.RAZORPAY_KEY,
                            appName = "Vish Academy",
                            description = course.title,
                            userID = ""
                        )
                    },
                    enabled = !alreadyPurchased,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3653F3)
                    )
                ) {

                    Text(
                        if (alreadyPurchased)
                            "Already Purchased"
                        else
                            "Buy Now"
                    )
                }
            }
        }
    }
}
