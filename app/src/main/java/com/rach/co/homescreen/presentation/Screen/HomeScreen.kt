package com.rach.co.homescreen.presentation.home.presentation.Screen

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val categories = listOf(
        CategoryItem("Courses", R.drawable.teach, Routes.COURSES),
        CategoryItem("PYQ", com.rach.co.R.drawable.exam, Routes.COURSES),
        CategoryItem("My Courses", R.drawable.mycourse, Routes.My_COURSES)

    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    )  {



        Row() {
            Text(
                text = "Vish Academy",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Button(onClick = {
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
        ){
            items(categories) { item ->
                CategoryCard(item, navController)
            }
        }



    }
}


@Composable
fun CategoryCard(item: CategoryItem, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .clickable(onClick = {
                navController.navigate(item.route)

            }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.name,
            modifier = Modifier
                .size(80.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {

    val images = listOf(
        "https://images.unsplash.com/photo-1523240795612-9a054b0db644",
        "https://images.unsplash.com/photo-1588072432836-e10032774350",
        "https://images.unsplash.com/photo-1513258496099-48168024aec0",
        "https://images.unsplash.com/photo-1503676260728-1c00da094a0b"
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


