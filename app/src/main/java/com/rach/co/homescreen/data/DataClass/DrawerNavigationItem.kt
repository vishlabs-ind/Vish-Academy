package com.rach.co.homescreen.data.DataClass

import com.rach.co.R

sealed class NavigationItem(val icon: Int,val item: String) {
    data object Profile: NavigationItem(R.drawable.profile,"Profile")
    data object ContactUs: NavigationItem(R.drawable.call,"Contact Us")
    data object PrivacyPolicy: NavigationItem(R.drawable.privacy,"Privacy Policy")

    data object Share: NavigationItem(R.drawable.share,"Share")

    data object LogOut: NavigationItem(R.drawable.logout,"Log Out")
}