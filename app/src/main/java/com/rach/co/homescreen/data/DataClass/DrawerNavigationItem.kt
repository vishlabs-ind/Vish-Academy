package com.rach.co.homescreen.data.DataClass

import com.rach.co.R

sealed class NavigationItem(val icon: Int,val item: String) {
    data object Profile: NavigationItem(R.drawable.profile,"Profile")

    data object MyClasses: NavigationItem(R.drawable.myclass,"My Classes")
    data object Wallet: NavigationItem(R.drawable.wallet,"Wallet")
    data object ContactUs: NavigationItem(R.drawable.call,"Contact Us")
    data object PrivacyPolicy: NavigationItem(R.drawable.privacy,"Privacy Policy")

    data object Share: NavigationItem(R.drawable.share,"Share")

    data object LogOut: NavigationItem(R.drawable.logout,"Log Out")
}
sealed class Route(val route: String){
    object Profile : Route("Profile")
    object Privacy_Policy:Route("Privacy_Policy")
    object Share:Route("Share")
    object deshboard:Route("deshboard")
    object My_class:Route("My_class")
    object Wallet:Route("Wallet")
    object Contact_Us:Route("Contact_Us")


}
