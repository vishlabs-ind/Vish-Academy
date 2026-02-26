package com.rach.co.homescreen.presentation.MyCouseScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel

@Composable
fun ChapterDetailScreen(courseId: String, subjectName: String, navController: NavHostController,
                        vm: HomeViewModel = hiltViewModel()) {


    val chaptersS by vm.chaptersS.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadChaptersS(courseId, subjectName)
    }
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(6.dp)) {
        Text(
            text = subjectName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn {

            items(chaptersS) { chapterS ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(onClick = {

                        })
                ){
                    Text(
                        text = chapterS.ChapterName,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

    }
}