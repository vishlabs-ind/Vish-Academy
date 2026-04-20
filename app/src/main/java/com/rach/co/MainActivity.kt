package com.rach.co

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel
import com.rach.co.navigation.AuthApp
import com.rach.co.quiz.presentation.screen.NoInternetScreen
import com.rach.co.ui.theme.VishAcademyTheme
import com.rach.co.utils.NetworkMonitor
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateRequestCode = 100
    val context = LocalContext

    val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val networkMonitor = NetworkMonitor(this)

        Checkout.preload(applicationContext)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        setContent {

            val isConnected by networkMonitor.isConnected()
                .collectAsState(initial = true)
            VishAcademyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isConnected){
                        AuthApp()

                    }

                    else {
                        NoInternetScreen()
                    }
                }
            }
        }
        checkForUpdate()
        MobileAds.initialize(this)
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(
                    listOf("C6C9AED3199668D27D180B653D29B3ED")
                )
                .build()
        )
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
//        homeViewModel.onPaymentSuccess()
        homeViewModel.onadspaymentsuccess()


        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()


    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()

    }

    private fun checkForUpdate() {

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        updateRequestCode
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    updateRequestCode
                )
            }
        }
    }
}



