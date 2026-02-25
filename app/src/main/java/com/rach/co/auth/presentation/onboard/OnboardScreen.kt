package com.rach.co.auth.presentation.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rach.co.R

// Matches the indicator dots from bg_indicator_active.xml and bg_indicator_inactive.xml
private val ActiveIndicatorColor = Color(0xFFFFFFFF)
private val InactiveIndicatorColor = Color(0x80FFFFFF)
private val BackgroundColor = Color(0xFF0961F5)

@Composable
fun OnboardScreen(
    onSkip: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top space (60dp margin)
            Spacer(modifier = Modifier.height(60.dp))

            // Illustration image — matches ivIllustration (height 280dp, centerInside)
            Image(
                painter = painterResource(id = R.drawable.teaching_edit),
                contentDescription = "Onboard Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                alignment = Alignment.Center
            )

            // Title — matches tvTitle (marginTop 40dp, 28sp bold, white)
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Explore your\nnew skills today",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            )

            // Description — matches tvDescription (marginTop 16dp, 16sp, 80% alpha)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "You can learn various kinds of\ncourses in your hand",
                color = Color(0xFFE0E0E0),
                fontSize = 16.sp,
                modifier = Modifier.alpha(0.8f)
            )

            // Indicator dots — matches indicatorLayout (marginTop 24dp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Active indicator — 24dp wide, 6dp tall, white, rounded
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ActiveIndicatorColor)
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Inactive indicator — 6dp x 6dp, semi-white, rounded
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(InactiveIndicatorColor)
                )
            }
        }

        // Bottom buttons row — Skip (end) and Next (start), matching btnSkip and btnNext
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Next — bottom start (matches btnNext constraint)
            Text(
                text = "Next",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onNext() }
            )

            // Skip — bottom end (matches btnSkip constraint)
            Text(
                text = "Skip",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onSkip() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardScreenPreview() {
    OnboardScreen()
}
