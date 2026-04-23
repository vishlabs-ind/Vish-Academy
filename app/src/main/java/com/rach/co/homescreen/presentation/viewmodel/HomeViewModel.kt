package com.rach.co.homescreen.presentation.home.presentation.viewmodelHome

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rach.co.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import com.rach.co.Rozarpay.RzManager
import com.rach.co.auth.data.Model.UserPrefs
import com.rach.co.auth.data.remote.FirebaseAuthSource
import com.rach.co.homescreen.data.DataClass.Chapter
import com.rach.co.homescreen.data.DataClass.ChapterDetail
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.data.Remote.PurchaseRepository
import com.rach.co.homescreen.data.RepoImpl.CourseRepositoryDb
import com.rach.co.homescreen.domain.Repo.CourseRepository

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val repository: CourseRepository,
    private val rzManager: RzManager,
    private val purchaseRepository: PurchaseRepository,
    private val savedStateHandle: SavedStateHandle,
    private val repoDb: CourseRepositoryDb,
    private val userPrefs: UserPrefs


) : ViewModel() {


    val isPremium = userPrefs.isPremium

    fun onadspaymentsuccess(){
        viewModelScope.launch {
            userPrefs.savePremium(true)
            purchaseRepository.addpremiumtouser()
        }
    }

    fun goadsfreepaymentvm( activity: Activity,
                            email: String,
                            amountInRupees: Int,
                            keyId: String,
                            appName: String,
                            description: String,){

        rzManager.Adfreepayment(
            activity = activity,
            keyId = keyId,
            appName = appName,
            description = description,
            amountInRupees = amountInRupees,
            userEmail = email
        )

    }


    fun logout() {
        viewModelScope.launch {
            repoDb.clearCourses()
        }
        repo.logout()
    }

    val coursesDbOffline = repoDb.getCourses()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun saveCourses(list: List<Course>) {

        viewModelScope.launch {
            repoDb.saveCourses(list)
        }
    }


    private val _chaptersS =
        MutableStateFlow<List<ChapterDetail>>(emptyList())

    val chaptersS: StateFlow<List<ChapterDetail>>
            = _chaptersS

    fun loadChaptersS(
        courseId: String,
        subjectName: String
    ) {

        viewModelScope.launch {

            _chaptersS.value =
                repository.getChapterDetails(
                    courseId,
                    subjectName
                )
        }
    }


    private val _chapters =
        MutableStateFlow<List<Chapter>>(emptyList())

    val chapters: StateFlow<List<Chapter>>
            = _chapters

    fun loadChapters(courseId: String) {

        viewModelScope.launch {

            _chapters.value =
                repository.getChapters(courseId)
        }
    }



    private val _myCourses =
        MutableStateFlow<List<Course>>(emptyList())

    val myCourses: StateFlow<List<Course>>
            = _myCourses

    fun loadPurchasedCourses() {

        viewModelScope.launch {
            val id = repository.getPurchasedCourseIds()
            val coursesL = repository.getCoursesByIds(id)
            repoDb.saveCourses(coursesL)



            _myCourses.value =
                repository.getCoursesByIds(id)
        }
    }




    private val _courses =
        MutableStateFlow<List<Course>>(emptyList())

    val courses: StateFlow<List<Course>> = _courses

    fun fetchCourse() {

        viewModelScope.launch {

            _courses.value =
                repository.getCourses()
        }
    }


    private val _courseP =
        MutableStateFlow<Course?>(null)

    val courseP: StateFlow<Course?> = _courseP

    fun purchasecoursevm(order: Int){
        try {
            viewModelScope.launch {
                println("VM FETCH ORDER = $order")

                _courseP.value = repository.buyCourse(order)
            }
        }catch (e: Exception){
            println(e.toString())
        }
    }

    fun startPurchase(courseId: String) {
        savedStateHandle["courseId"] = courseId
    }

    fun onPaymentSuccess() {

        val courseId =
            savedStateHandle.get<String>("courseId")
                ?: return

        viewModelScope.launch {

            purchaseRepository
                .addCourseToUser(courseId)

            println("COURSE SAVED = $courseId")
        }
    }


    fun startPayment(
        activity: Activity,
        email: String,
        amountInRupees: Int,
        keyId: String,
        appName: String,
        description: String,
        userID: String
    ) {

        rzManager.startPayment(
            activity = activity,
            keyId = keyId,
            appName = appName,
            description = description,
            amountInRupees = amountInRupees,
            userEmail = email
        )

    }

}




