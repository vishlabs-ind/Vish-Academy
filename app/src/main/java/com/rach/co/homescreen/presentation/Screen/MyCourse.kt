package com.rach.co.homescreen.presentation.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel

@Composable
fun MyCourse(navController: NavHostController,
             vm: HomeViewModel = hiltViewModel()
) {
    val courses by vm.myCourses.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadPurchasedCourses()
    }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(6.dp)) {
        LazyColumn {

            items(courses) { course ->
                Coursecard(true, course, navController)
            }
    }

    }
}