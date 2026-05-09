# =============================================
#  KOTLINPOET + JAVAX (MUST BE AT TOP)
# =============================================
-dontwarn javax.lang.model.**
-dontwarn javax.lang.model.element.**
-dontwarn javax.lang.model.type.**
-dontwarn javax.lang.model.util.**
-dontwarn javax.tools.**
-dontwarn com.squareup.kotlinpoet.**
-dontwarn com.squareup.javapoet.**

# =============================================
#               BASIC CONFIG
# =============================================
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-dontwarn android.**
-dontwarn androidx.**
-dontwarn kotlin.**
-dontwarn kotlinx.**

# =============================================
#                 GSON
# =============================================
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keep class com.google.gson.** { *; }

-keep class com.rach.co.homescreen.data.Model.** { *; }
-keep class com.rach.co.quiz.data.dataClass.Question { *; }
-keep class com.rach.co.quiz.data.dataClass.** { *; }

# =============================================
#                 ROOM + CONVERTERS
# =============================================
-keep class com.rach.co.homescreen.data.Model.Converters { *; }
-keep class androidx.room.** { *; }

# =============================================
#                 KOTLIN + COROUTINES
# =============================================
-keep class kotlin.Metadata { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.** { *; }

# Keep everything in your package (safe for now)
-keep class com.rach.co.** { *; }