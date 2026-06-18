package com.rach.co.homescreen.presentation.home.presentation.Screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.rach.co.utils.K
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    quizViewModel: QuizCategoryViewModel = hiltViewModel(),
    adViewModel: AdViewModel
) {
    LaunchedEffect(Unit) {
        adViewModel.loadRewardedAd()  // ← preload here
    }

    val categories = listOf(
        CategoryItem("Courses", R.drawable.open_book_study_svgrepo_com, Routes.COURSES),
        CategoryItem("My Courses", R.drawable.course_form, Routes.My_COURSES),
        CategoryItem("Quiz", R.drawable.test_svgrepo_com, Routes.QUIZ),
        CategoryItem("Notes", R.drawable.notes_notepad_svgrepo_com, Routes.Notes),
        CategoryItem("Mock Test", R.drawable.exam_svgrepo_com, Routes.MOCK_SUBJECT_SELECTION)  // ← fix here
    )

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val isPremium by viewModel.isPremium.collectAsState(initial = false)
    val liveClass by viewModel.liveClass.collectAsState()

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

                Spacer(modifier = Modifier.weight(1f))

                if (isPremium){
                    PremiumGrayButton(){
                        Toast.makeText(context, "already a premium user" , Toast.LENGTH_SHORT).show()
                    }

                }
                else{
                    PremiumGradientButton {

                        viewModel.goadsfreepaymentvm(
                            activity = context as Activity,
                            email = "",
                            amountInRupees = 199,
                            keyId = K.RAZORPAY_KEY ,
                            appName = "Vish Academy",
                            description = "go ads free with 199 only",
                        )
                    }
                }
            }

          //  Spacer(modifier = Modifier.height(6.dp))

            // LIVE CLASS BANNER
            if (liveClass?.isLive == true) {

                LiveClassBanner(
                    title = liveClass!!.title,
                    thumbnail = liveClass!!.thumbnail,
                    youtubeLink = liveClass!!.link
                )

                Spacer(modifier = Modifier.height(6.dp))
            }

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

// ✅ FIXED: EXTRACTED STANDALONE LIVE BANNER UI COMPOSABLE
//@Composable
//fun LiveClassBanner(
//    title: String,
//    thumbnail: String,
//    youtubeLink: String
//) {
//    val context = LocalContext.current
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable {
//                if (youtubeLink.isNotBlank()) {
//                    val intent = Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse(youtubeLink)
//                    )
//                    context.startActivity(intent)
//                }
//            },
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFF1E1E1E)
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            AsyncImage(
//                model = thumbnail,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(90.dp)
//                    .clip(RoundedCornerShape(12.dp)),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = "🔴 LIVE NOW",
//                    color = Color.Red,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 14.sp
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = title,
//                    color = Color.White,
//                    fontWeight = FontWeight.SemiBold,
//                    fontSize = 16.sp
//                )
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Text(
//                    text = "Tap to join live class",
//                    color = Color.LightGray,
//                    fontSize = 12.sp
//                )
//            }
//        }
//    }
//}

@Composable
fun LiveClassBanner(
    title: String,
    youtubeLink: String,
    thumbnail: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Infinite transition for the blinking effect
    val infiniteTransition = rememberInfiniteTransition(label = "LiveBlink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaAnimation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
            .clickable {
                if (youtubeLink.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
                    context.startActivity(intent)
                }
            },
        shape = RoundedCornerShape(10.dp), // Set to 0.dp to seamlessly touch left/right edges
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD32F2F) // Classic Accent Red Background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp, horizontal = 10.dp), // Thin vertical padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Blinking Dot + LIVE NOW
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                // Real Blinking Red Dot (Using white or light grey dot here since background is red)
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .alpha(alpha)
                        .clip(CircleShape)
                        .background(Color.White) // White dot pops brilliantly on red background
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "LIVE NOW:",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp
                )
            }

            // Center: Title (Flexible to avoid pushing items out)
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            // Right Side: Action Hint
            Text(
                text = "Tap to Join Live Now",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {

    var isLoading by remember { mutableStateOf(true) }

    val images = listOf(
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7s3i00uafpqWAOGLktVUbnzXyI5esdBYPr6M1C",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7sUenxci40TGV6N8Ie3jbwtYHkAgqKaMmyhsXl",
        "https://ikm7674fcj.ufs.sh/f/ak9Yf1k7Pl7sFh76RMxUoHBEqXjkVvJsZDLCgAcin8eTPSwG",
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



@Composable
fun PremiumGrayButton(
    text: String = "Premium",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF9E9E9E), // gray
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 4.dp)
    ) {

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(3.dp))

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun PremiumGradientButton(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {

        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF43A047), Color(0xFF2E7D32))
                    ),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 5.dp, vertical = 4.dp)
            ,
            contentAlignment = Alignment.Center,
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = "No Ads ₹199",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}