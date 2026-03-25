package com.rach.co.homescreen.presentation.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rach.co.R
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel

@Composable
fun MyCourse(navController: NavHostController,
             vm: HomeViewModel = hiltViewModel()
) {
    val coursesDbOffline by vm.coursesDbOffline.collectAsState()
//    val coursesDbOffline by remember { mutableStateOf(emptyList<Course>())}


    LaunchedEffect(Unit) {
        if (coursesDbOffline.isEmpty()){
            vm.loadPurchasedCourses()
        }
    }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(6.dp)) {
        if (coursesDbOffline.isEmpty()){
            Box(Modifier.fillMaxSize()  ,
                    contentAlignment = Alignment.Center   // 🔥 THIS is key
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.forbidden),
                        contentDescription = "not available",
                        modifier = Modifier
                            .size(100.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("No Courses Found")
                }

            }

        }
        LazyColumn {

            items(coursesDbOffline) { course ->
                Coursecard(true, course, navController)
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun showprev(){
    MyCourse(navController = NavHostController(LocalContext.current))
}