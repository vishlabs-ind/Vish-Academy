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
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import com.valentinilk.shimmer.shimmer


@SuppressLint("ContextCastToActivity")
@Composable
fun CoursePurchasedScreen(
    order: Int?,
//    vm: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val activity = LocalContext.current as ComponentActivity

    val vm: HomeViewModel =
        hiltViewModel(activity)

    val course by vm.courseP.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(order) {
        order?.let {
            vm.purchasecoursevm(it)
        }
    }

    if (course == null) {
        CoursePurchasedShimmer()
        return
    }
    val data = course!!
    Column(Modifier.fillMaxSize().padding(top = 16.dp, ).background(Color(0xFFE6E6EC))) {

        Spacer(Modifier.height(12.dp))

            Box(Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model = data.thumbnail.takeIf {
                        it.isNotEmpty()
                    } ?: R.drawable.learning,
                    contentDescription = "image course",
                    Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }


            Column(modifier = Modifier.padding(8.dp)) {

                Card(modifier = Modifier.padding(6.dp), elevation = CardDefaults.cardElevation(8.dp))

                {

                    Column(modifier = Modifier.padding(8.dp)) {

                        Text(
                            text = data.title,
                            fontSize = 24.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            data.subtitle, fontSize = 16.sp,
                            color = Color.Blue, fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(9.dp))


                        Text(
                            text = "Course: ${data.description}",
                            fontSize = 11.sp
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                }


                Card(modifier = Modifier.padding(6.dp), elevation = CardDefaults.cardElevation(8.dp)) {


                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ✅ Display Name


                            Row {

                                Text(
                                    text = "Course:",
                                    fontSize = 18.sp,

                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = " ${data.displayName}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                )

                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Row() {
                                Text(
                                    text = "Price:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = " ₹ ${data.price}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Blue,

                                    )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                vm.startPurchase(data.courseId)

                                vm.startPayment(
                                    activity = context as Activity,
                                    email = "",
                                    amountInRupees = data.price,
                                    keyId = "rzp_test_PCx5CdAmvXX78k",
                                    appName = "Vish Academy",
                                    description = data.title,
                                    userID = ""
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3653F3))
                        ) {
                            Text("Buy Now", color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }




}

@Composable
fun CoursePurchasedShimmer() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .shimmer()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.LightGray)
        )

        Spacer(Modifier.height(20.dp))

        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(vertical = 6.dp)
                    .background(Color.LightGray)
            )
        }
    }
}