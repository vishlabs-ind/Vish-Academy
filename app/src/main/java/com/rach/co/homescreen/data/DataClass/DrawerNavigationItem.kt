package com.rach.co.homescreen.data.DataClass

import com.rach.co.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector






sealed class NavigationItem(val icon: ImageVector, val item: String) {

    data object Profile : NavigationItem(Icons.Default.Person, "Profile")

    data object ContactUs : NavigationItem(Icons.Default.Call, "Contact Us")

    data object PrivacyPolicy : NavigationItem(Icons.Default.Lock, "Privacy Policy")

    data object Share : NavigationItem(Icons.Default.Share, "Share")

    data object LogOut : NavigationItem(Icons.Default.ExitToApp, "Log Out")
}