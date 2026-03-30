package com.rach.co.homescreen.presentation.home.presentation.Screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rach.co.R
import com.rach.co.homescreen.data.DataClass.CategoryItem
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import com.rach.co.navigation.Routes
import com.rach.co.quiz.presentation.viewmodel.QuizCategoryViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    quizViewModel: QuizCategoryViewModel = hiltViewModel()
) {


    val categories = listOf(
        CategoryItem("Courses", R.drawable.teach, Routes.COURSES),
        CategoryItem("My Courses", R.drawable.mycourse, Routes.My_COURSES),
        CategoryItem("Quiz", R.drawable.quiz_logo, Routes.QUIZ)

    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {


        Row() {
            Text(
                text = "Vish Academy",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                Modifier.wrapContentSize()
            ) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ImageSlider()
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { item ->
                CategoryCard(
                    item, navController,
                    quizViewModel = quizViewModel

                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {

    val images = listOf(
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7slm5D8eAWsQ5xIr2LjyTP430zUEKwhDMnqiX7",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7sFuZjnixUoHBEqXjkVvJsZDLCgAcin8eTPSwG",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7sVhxt2TFtoG4f3vP8xJWjHRuMmK5TbANDkYie",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7s5L6qxWqTwIlhWk0Ldz6QgPtBADpynqj19ar5",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7sNLtiEthtApfRXC7hJHMIGW31ZP8YxleTwrBm"
    )

    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )

    // Auto Slide
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            val nextPage =
                (pagerState.currentPage + 1) % images.size

            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) { page ->

        AsyncImage(
            model = images[page],
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun CategoryCard(
    item: CategoryItem,
    navController: NavController,
    quizViewModel: QuizCategoryViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .clickable {

                if (item.route == Routes.QUIZ) {
                    navController.navigate("quiz_course")

                } else {
                    quizViewModel.closeDialog()   // ensure dialog is closed
                    navController.navigate(item.route) //

                }

            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp)), // Clips the white background of the Quiz icon
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = item.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
