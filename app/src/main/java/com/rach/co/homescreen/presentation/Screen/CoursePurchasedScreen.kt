package com.rach.co.homescreen.presentation.Screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Box(Modifier.fillMaxWidth().height(300.dp)){
            AsyncImage(
                model = data.thumbnail.takeIf {
                    it.isNotEmpty()
                } ?: R.drawable.learning,
                contentDescription = "image course",
                Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }



        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())

        ) {
            Text(
                text = data.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                data.subtitle, fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(12.dp))


            // ✅ Description
            Text(
                text = data.description,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(20.dp))

            // ✅ Display Name
            Text(
                text = "Course: ${data.displayName}",
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(20.dp))

            // ✅ Display Name
            Text(
                text = "Course: ${data.displayName}",
                fontWeight = FontWeight.Medium
            )

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
            .height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Buy Now")
    }
    Spacer(Modifier.height(20.dp))



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