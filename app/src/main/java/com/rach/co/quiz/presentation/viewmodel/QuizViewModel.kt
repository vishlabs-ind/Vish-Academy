package com.rach.co.quiz.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.quiz.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _course = mutableStateOf<Course?>(null)
    val course: State<Course?> = _course

    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    private val _selectedAnswers = mutableStateMapOf<Int, Int>()
    val selectedAnswers: Map<Int, Int> = _selectedAnswers

    var interstitialAd: InterstitialAd? = null
    fun loadCourse(courseId: String) {
        _course.value = repository.getCourseById(courseId)
    }

    fun nextQuestion() {
        val total = _course.value?.questions?.size ?: 0
        if (_currentQuestionIndex.value < total - 1) {
            _currentQuestionIndex.value++
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value = currentQuestionIndex.value - 1
        }
    }

    fun isLastQuestion(): Boolean {
        val total = _course.value?.questions?.size ?: 0
        return _currentQuestionIndex.value == total - 1
    }
    fun checkAnswer(optionIndex: Int) {


        val questionIndex = currentQuestionIndex.value
        // Simply overwrite — no toggling, no sets
        _selectedAnswers[questionIndex] = optionIndex
    }

    fun calculateScore() {
        val questions = _course.value?.questions ?: return
        var count = 0

        questions.forEachIndexed { index, question ->
            val selectedAnswer = _selectedAnswers[index]  // Int?
            if (selectedAnswer == question.correctAnswerIndexs) {
                count++
            }
        }

        _score.value = count
    }

    // load Ad
    fun loadAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712", // TEST AD UNIT
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("AdMob", "Ad Loaded Successfully")
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d("AdMob", "Ad Failed: ${error.message}")
                    interstitialAd = null
                }
            }
        )
    }

    // show Ad
    fun showAd(activity: Activity, onAdClosed: () -> Unit) {

        if (interstitialAd != null) {

            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        interstitialAd = null
                        onAdClosed()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        onAdClosed()
                    }
                }

            interstitialAd?.show(activity)

        } else {
            onAdClosed()
        }
    }
}