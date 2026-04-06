package com.rach.co.navigation
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rach.co.homescreen.data.DataClass.NavigationItem
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import androidx.core.net.toUri


@Composable
fun NavigationDrawer(
    drawerNavController: NavHostController,
    viewModel: HomeViewModel,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current.applicationContext
    val uriHandler = LocalUriHandler.current
    var isOpenDailog by remember { mutableStateOf(false) }

    val navigationItemList = listOf(
        NavigationItem.Profile,
        NavigationItem.Share,
        NavigationItem.ContactUs,
        NavigationItem.PrivacyPolicy,
        NavigationItem.LogOut
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp)
                        .background(Color.White)

                ) {
                    Text(
                        text = "Vish Acedmy",
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()

                    navigationItemList.forEach {
                        NavigationDrawerItem(
                            label = { Text(text =it.item, fontSize = 16.sp,  color=if (it.item == "Log Out") Color.Red else Color.Black,) },
                            icon = {
                                Image(
                                    painter = painterResource(id = it.icon),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    colorFilter = ColorFilter.tint(if (it.item == "Log Out") Color.Red else Color.Black)
                                )
                            },

                            selected = false,
                            onClick = {

                                when (it.item) {

                                    "Privacy Policy" -> uriHandler.openUri("https://sites.google.com/view/vishacademyvishlabs/home")
                                    "Profile" -> drawerNavController.navigate(Routes.PROFILE)

                                    "Share" -> {
                                        val intent= Intent(Intent.ACTION_SEND).apply {
                                            type="text/plain"
                                            putExtra(Intent.EXTRA_TEXT,"Check out this app:\nhttps://play.google.com/store/apps/details?id=com.rach.co")
                                        }
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(intent)
                                    }

                                    "Contact Us" ->{
                                        drawerNavController.navigate(Routes.CONTACT_US)
                                    }

                                    "Log Out" -> isOpenDailog = true

                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        content()
        if (isOpenDailog) {

            LogOutButtonDailog(onConfirm = {
                viewModel.logout()
                isOpenDailog = false
                drawerNavController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }, onDismiss = { isOpenDailog = false })

        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogOutButtonDailog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFF323636), // Amber icon color
                modifier = Modifier.size(40.dp)
            )
        },
        title = {
            Text(
                text = "Are you sure you want to log out?",
                fontSize = 15.sp,
                color = Color(0xFF000000)
            )
        },
        confirmButton = {
            OutlinedButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000) // red confirm button
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Text(
                    text = "Yes, Logout", fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE53935)),
                modifier = Modifier.height(40.dp)
            ) {
                Text(text = "Cancel")

            }
        },
        shape = RoundedCornerShape(10.dp)

    )
}