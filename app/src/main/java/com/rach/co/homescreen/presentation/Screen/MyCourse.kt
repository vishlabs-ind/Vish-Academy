package com.rach.co.homescreen.presentation.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rach.co.R
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel

@Composable
fun MyCourse(navController: NavHostController,
             vm: HomeViewModel = hiltViewModel()
) {
    val coursesDbOffline by vm.coursesDbOffline.collectAsState()

    LaunchedEffect(Unit) {
        if (coursesDbOffline.isEmpty()){
            vm.loadPurchasedCourses()
        }
    }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(6.dp)) {
        if (coursesDbOffline.isEmpty()){
            Box(Modifier.fillMaxSize().align(Alignment.CenterHorizontally)){
                Image(
                    painter = painterResource(R.drawable.forbidden),
                    contentDescription = "not available",
                    modifier = Modifier
                        .size(100.dp)
                )
                Text("No Courses Found")
            }

        }
        LazyColumn {

            items(coursesDbOffline) { course ->
                Coursecard(true, course, navController)
            }
        }

    }
}