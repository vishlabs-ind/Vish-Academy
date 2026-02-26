package com.rach.co.homescreen.presentation.MyCouseScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel


@Composable
fun ChapterScreen(
    courseId: String,
    vm: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val chapters by vm.chapters.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadChapters(courseId)
    }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(6.dp)) {
        LazyColumn {

            items(chapters) { chapter ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(onClick = {
                            navController.navigate(
                                "chapterDetails/${courseId}/${chapter.name}"
                            )
                        })
                ){
                    Text(
                        text = chapter.name,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

    }


}