package com.rach.co.mock.presentation.screen

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import com.rach.co.navigation.Routes
import com.rach.co.utils.K

enum class SelectedPlan { MOCK_ONLY, PREMIUM }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    userEmail: String = "user@example.com"
) {
    val activity = LocalActivity.current as? Activity
    var selectedPlan by remember { mutableStateOf(SelectedPlan.PREMIUM) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.paymentSuccessEvent.collect { planType ->
            navController.navigate(Routes.MOCK_SUBJECT_SELECTION) {
                // Remove SubscriptionScreen from backstack
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Your Plan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = Color(0xFFD4AF37), // Gold
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "VA Prime", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text(
                text = "Unlock features and accelerate your learning journey",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Comparison Table
            FeaturesComparisonTable()

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "How it works", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(12.dp))

            // 2. Selection Price Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mock Only Plan Card
                PlanPriceCard(
                    modifier = Modifier.weight(1f),
                    title = "MOCK ONLY",
                    price = "₹50",
                    originalPrice = "₹100",
                    discount = "50% Off",
                    isSelected = selectedPlan == SelectedPlan.MOCK_ONLY,
                    onClick = { selectedPlan = SelectedPlan.MOCK_ONLY },
                    borderColor = Color(0xFF00BCD4) // Cyan tint
                )

                // Premium Plan Card
                PlanPriceCard(
                    modifier = Modifier.weight(1f),
                    title = "PREMIUM",
                    price = "₹199",
                    originalPrice = "₹399",
                    discount = "50% Off",
                    isSelected = selectedPlan == SelectedPlan.PREMIUM,
                    onClick = { selectedPlan = SelectedPlan.PREMIUM },
                    borderColor = Color(0xFFFFB300), // Gold tint
                    isBestSeller = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Bottom Action Buttons
            Button(
                onClick = {
                    activity?.let { act ->
                        if (selectedPlan == SelectedPlan.PREMIUM) {
                            viewModel.goadsfreepaymentvm(
                                activity = act,
                                email = userEmail,
                                amountInRupees = 199,
                                keyId = K.RAZORPAY_KEY,
                                appName = "Vish Academy",
                                description = "Premium Membership",
                                planType = "PREMIUM"   // ← correct
                            )
                        } else {
                            viewModel.goadsfreepaymentvm(
                                activity = act,
                                email = userEmail,
                                amountInRupees = 50,
                                keyId = K.RAZORPAY_KEY,
                                appName = "Vish Academy",
                                description = "Mock Only Access",
                                planType = "MOCK_ONLY"  // ← correct
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), // Red CTA
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (selectedPlan == SelectedPlan.PREMIUM) "Continue with Premium →" else "Continue with Mock Only →",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Continue with Free Plan", color = Color.Gray, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FeaturesComparisonTable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Features", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "FREE", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
            Text(text = "MOCK", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF00BCD4))
            Text(text = "PREMIUM", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFFFB300))
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        // Row 1: Ads Status
        TableRow(feature = "Ads Support", free = "Show Ads", mock = "Show Ads", premium = "No Ads")

        Divider(color = Color.LightGray, thickness = 0.5.dp)

        // Row 2: Mock Availability
        TableRow(feature = "Mock Exams", free = false, mock = true, premium = true)

        Divider(color = Color.LightGray, thickness = 0.5.dp)

        // Row 3: Video/Notes access
        TableRow(feature = "Courses & Notes", free = true, mock = false, premium = true)
    }
}

@Composable
fun TableRow(feature: String, free: Any, mock: Any, premium: Any) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = feature, modifier = Modifier.weight(1.5f), fontSize = 13.sp, fontWeight = FontWeight.Medium)

        Box(modifier = Modifier.weight(1f)) { RenderTableCell(free) }
        Box(modifier = Modifier.weight(1f)) { RenderTableCell(mock) }
        Box(modifier = Modifier.weight(1f)) { RenderTableCell(premium) }
    }
}

@Composable
fun RenderTableCell(value: Any) {
    when (value) {
        is Boolean -> {
            if (value) {
                Icon(Icons.Default.Check, contentDescription = "Yes", tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            } else {
                Icon(Icons.Default.Clear, contentDescription = "No", tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
            }
        }
        is String -> {
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if(value == "No Ads") Color(0xFF4CAF50) else Color.Unspecified)
        }
    }
}

@Composable
fun PlanPriceCard(
    modifier: Modifier = Modifier,
    title: String,
    price: String,
    originalPrice: String,
    discount: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    borderColor: Color,
    isBestSeller: Boolean = false
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isBestSeller) 10.dp else 0.dp)
                .border(
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) borderColor else Color.LightGray),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .background(if (isSelected) borderColor.copy(alpha = 0.05f) else Color.White)
                .padding(12.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = borderColor)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = price, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = originalPrice,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
            }
            Text(text = discount, fontSize = 11.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
        }

        if (isBestSeller) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFD32F2F), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .align(Alignment.TopStart)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = "BEST SELLER", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}