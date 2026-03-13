package com.rach.co.Rozarpay


import android.app.Activity
import com.razorpay.Checkout
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RzManager @Inject constructor() {

    fun startPayment(
        activity: Activity,
        keyId: String,
        appName: String,
        description: String,
        amountInRupees: Int,
        userEmail: String
    ) {

        val checkout = Checkout()
        checkout.setKeyID(keyId)

        try {

            val options = JSONObject().apply {

                put("name", appName)
                put("description", description)
                put("amount", amountInRupees * 100)
                put("currency", "INR")

                put(
                    "prefill",
                    JSONObject().apply {
                        put("email", userEmail)
                    }
                )
            }

            checkout.open(activity, options)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}