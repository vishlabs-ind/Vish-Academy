package com.rach.co.homescreen.presentation.home.presentation.Screen

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.rach.co.ad.AdViewModel
import com.rach.co.homescreen.data.DataClass.CategoryItem
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import com.rach.co.navigation.NavigationDrawer
import com.rach.co.navigation.Routes
import com.rach.co.quiz.presentation.viewmodel.QuizCategoryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    quizViewModel: QuizCategoryViewModel = hiltViewModel()
) {

    // 1. Get the activity safely
    val activity = LocalActivity.current as? ComponentActivity

    // 2. IMPORTANT: Scope this to the Activity so it survives navigation
    val adViewModel: AdViewModel = if (activity != null) {
        hiltViewModel(activity)
    } else {
        hiltViewModel()
    }

    LaunchedEffect(Unit) {
        activity?.let {
            adViewModel.loadAd(it)
            adViewModel.loadAd2(it)
            adViewModel.loadRewardedAd(it)
        }
    }

    val categories = listOf(
        CategoryItem("Courses", R.drawable.teach, Routes.COURSES),
        CategoryItem("My Courses", R.drawable.mycourse, Routes.My_COURSES),
        CategoryItem("Quiz", R.drawable.quiz_logo, Routes.QUIZ),
        CategoryItem("Notes", R.drawable.notes, Routes.Notes),
        CategoryItem("Exam", R.drawable.exam, Routes.SUBJECT_SELECTION)  // ← fix here
    )

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavigationDrawer(navController, viewModel, drawerState) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                BackHandler(enabled = drawerState.isOpen) {
                    coroutineScope.launch { drawerState.close() }
                }

                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }
                )

                Spacer(modifier = Modifier.width(17.dp))

                Text(
                    text = "Vish Academy",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(contentAlignment = Alignment.Center) {
                ImageSlider()
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { item ->
                    CategoryCard(
                        item = item,
                        navController = navController,
                        quizViewModel = quizViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {

    var isLoading by remember { mutableStateOf(true) }

    val images = listOf(
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7s3i00uafpqWAOGLktVUbnzXyI5esdBYPr6M1C",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7sFh76RMxUoHBEqXjkVvJsZDLCgAcin8eTPSwG",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7s1hS7p40BDUgIT4rYjJ8PcKk3sFMoZqtHNbyh"
    )

    val pagerState = rememberPagerState(pageCount = { images.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
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
            contentScale = ContentScale.Crop,
            onSuccess = { isLoading = false }
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
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .clickable {
                when (item.route) {
                    Routes.QUIZ -> {
                        // quiz goes to course selection
                        navController.navigate("quiz_course")
                    }
                    Routes.SUBJECT_SELECTION -> {
                        // exam goes to subject selection
                        navController.navigate(Routes.SUBJECT_SELECTION)
                    }
                    else -> {
                        quizViewModel.closeDialog()
                        navController.navigate(item.route)
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.name,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}