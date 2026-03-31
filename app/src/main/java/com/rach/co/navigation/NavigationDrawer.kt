package com.rach.co.navigation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rach.co.homescreen.data.DataClass.NavigationItem


@Composable
fun NavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current.applicationContext
val uriHandler= LocalUriHandler.current

    val navigationItemList = listOf(
        NavigationItem.Profile,
        NavigationItem.MyClasses,
        NavigationItem.Wallet,
        NavigationItem.Share,
        NavigationItem.ContactUs,
        NavigationItem.PrivacyPolicy,
        NavigationItem.LogOut
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.Black){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
                    .background(Color.Black)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Vish Acedmy",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()

                navigationItemList.forEach {
                    NavigationDrawerItem(
                        label = { Text(text = it.item) },
                        icon = {
                            Image(
                                painter = painterResource(id = it.icon),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(if (it.item == "Log Out") Color.Red else Color.White)
                            )
                        },
                        selected = false,
                        onClick = {
                            //this only for check now3
                            when (it.item) {
                                "Wallet" ->  Toast.makeText(context, it.item, Toast.LENGTH_SHORT).show()
                                "My Classes" ->  Toast.makeText(context, it.item, Toast.LENGTH_SHORT).show()
                                "Privacy Policy" -> uriHandler.openUri("https://sites.google.com/view/vishacademyvishlabs/home")
                                "Profile" ->  Toast.makeText(context, it.item, Toast.LENGTH_SHORT).show()
                                "Share" ->  Toast.makeText(context, it.item, Toast.LENGTH_SHORT).show()
                                "Contact Us" ->  Toast.makeText(context, it.item, Toast.LENGTH_SHORT).show()
                                "Log Out" -> Toast.makeText(context, it.item, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
        }
    ){
        content()
    }
}