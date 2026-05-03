##############################################
# 🔹 GENERAL SAFE RULES
##############################################

# Keep annotations & signatures (important for Retrofit/Firebase)
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses

##############################################
# 🔹 KOTLIN
##############################################

-keep class kotlin.Metadata { *; }

##############################################
# 🔹 VIEWMODEL
##############################################

-keep class * extends androidx.lifecycle.ViewModel { *; }

##############################################
# 🔹 REPOSITORY / DATA LAYER (IMPORTANT)
##############################################

-keep class com.rach.co.**.data.** { *; }
-keep class com.rach.co.**.repository.** { *; }

##############################################
# 🔹 RETROFIT / API CALLS
##############################################

# Keep API interfaces
-keep interface * {
    @retrofit2.http.* <methods>;
}

# Keep models (VERY IMPORTANT)
-keep class com.rach.co.**.model.** { *; }

# Gson / JSON parsing
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

##############################################
# 🔹 FIREBASE
##############################################

-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

##############################################
# 🔹 RAZORPAY (PAYMENT)
##############################################

-keep class com.razorpay.** { *; }
-dontwarn com.razorpay.**

##############################################
# 🔹 IMAGE LOADING (Glide / Coil / Picasso safe)
##############################################



# Coil (safe)
-keep class coil.** { *; }

##############################################
# 🔹 WEBVIEW (if used for YouTube / payment)
##############################################

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

##############################################
# 🔹 JETPACK COMPOSE
##############################################

-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

##############################################
# 🔹 COROUTINES
##############################################

-keep class kotlinx.coroutines.** { *; }

##############################################
# 🔹 NAVIGATION (if using Navigation Compose)
##############################################

-keep class androidx.navigation.** { *; }

##############################################
# 🔹 ENUMS (important sometimes)
##############################################

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

##############################################
# 🔹 KEEP YOUR MAIN APP PACKAGE SAFE
##############################################

-keep class com.rach.co.** { *; }

##############################################
# 🔹 OPTIONAL DEBUGGING
##############################################

# Uncomment if debugging crash
# -dontobfuscate