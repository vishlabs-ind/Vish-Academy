package com.rach.co.navigation.naviagtionDrawerItemPage

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HelpSupportScreen() {
    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFFF6A00))) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = "Need Help? Contact Us!",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(Color(0xFFFF6A00)),
                    contentAlignment = Alignment.Center
                ) {

                    ContactUsCoponentsUp(Icons.Default.Email, "Email us. ", "support@vishlabs.in")
                }
                Spacer(modifier = Modifier.height(30.dp))
                ContactUsCoponentsUp(
                    Icons.Default.Email,
                    "You'll recieve a reply within ",
                    "24 hours!"
                )
                Spacer(modifier = Modifier.height(30.dp))
                Divider(modifier = Modifier.padding(horizontal = 10.dp))

                Spacer(modifier = Modifier.height(30.dp))

                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = "Describe Your issue",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,

                        )
                    IssuePoint("Explain your issue in detail")
                    IssuePoint("Provide your name & contact info")
                    IssuePoint("Attach a screenshot (if needed)")
                }

                Spacer(modifier = Modifier.height(30.dp))
                Divider(modifier = Modifier.padding(horizontal = 10.dp))
                Button(
                    onClick = {

                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6A00)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp).padding(horizontal = 10.dp)
                ) {

                    Text(
                        text = "Contact Support",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


}

@Composable
fun ContactUsCoponentsUp(icon: ImageVector, text: String, text1: String) {
    Row (modifier= Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center){
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.W300,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = text1,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,

            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun IssuePoint(text: String) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {

        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFFFF6A00), CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }

}
