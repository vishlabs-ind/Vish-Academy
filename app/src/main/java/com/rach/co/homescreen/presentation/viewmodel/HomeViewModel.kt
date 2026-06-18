package com.rach.co.homescreen.presentation.home.presentation.viewmodelHome

import android.app.Activity
import android.util.Log
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
import com.rach.co.ytLive.LiveClass
import com.rach.co.ytLive.LiveClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val repository: CourseRepository,
    val rzManager: RzManager,
    private val purchaseRepository: PurchaseRepository,
    private val liveRepository: LiveClassRepository,
    private val savedStateHandle: SavedStateHandle,
    private val repoDb: CourseRepositoryDb,
    private val userPrefs: UserPrefs
) : ViewModel() {

    private val _paymentSuccessEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val paymentSuccessEvent = _paymentSuccessEvent.asSharedFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    // ytLive
    private val _liveClass = MutableStateFlow<LiveClass?>(null)
    val liveClass = _liveClass.asStateFlow()


    init {

        liveRepository.observeLiveClass {
            Log.d("LIVE_CLASS_VM", "Received from Repository = $it")
            _liveClass.value = it
        }
    }



    val isPremium = userPrefs.isPremium
    fun onadspaymentsuccess(){
        viewModelScope.launch {
            try {
                userPrefs.savePremium(true)
                withContext(Dispatchers.IO + NonCancellable) {
                    purchaseRepository.addpremiumtouser()
                }
                _paymentSuccessEvent.emit("PREMIUM")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error updating premium in Firestore: ${e.message}", e)
            }
        }
    }

    fun onMockPaymentSuccess() {
        viewModelScope.launch {
            try {
                userPrefs.saveMockAccess(true)
                withContext(Dispatchers.IO + NonCancellable) {
                    purchaseRepository.addMockAccessToUser()
                }
                _paymentSuccessEvent.emit("MOCK_ONLY")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error updating mock access in Firestore: ${e.message}", e)
            }
        }
    }

    fun goadsfreepaymentvm( activity: Activity,
                            email: String,
                            amountInRupees: Int,
                            keyId: String,
                            appName: String,
                            description: String,
                            planType: String = "PREMIUM"
                            ){
        rzManager.startPayment(
            activity = activity,
            keyId = keyId,
            appName = appName,
            description = description,
            amountInRupees = amountInRupees,
            userEmail = email,
            planType = planType
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
    val pendingPlanType: String?
        get() = rzManager.pendingPlanType

    private val _chaptersS = MutableStateFlow<List<ChapterDetail>>(emptyList())

    val chaptersS: StateFlow<List<ChapterDetail>> = _chaptersS

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


    private val _chapters = MutableStateFlow<List<Chapter>>(emptyList())

    val chapters: StateFlow<List<Chapter>> = _chapters

    fun loadChapters(courseId: String) {

        viewModelScope.launch {

            _chapters.value =
                repository.getChapters(courseId)
        }
    }



    private val _myCourses = MutableStateFlow<List<Course>>(emptyList())

    val myCourses: StateFlow<List<Course>> = _myCourses

    fun loadPurchasedCourses() {

        viewModelScope.launch {
            val id = repository.getPurchasedCourseIds()
            val coursesL = repository.getCoursesByIds(id)
            repoDb.saveCourses(coursesL)



            _myCourses.value = repository.getCoursesByIds(id)
        }
    }


    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private var lastCourse: Course? = null

    fun fetchCourse() {

        viewModelScope.launch {

            _courses.value =
                repository.getCourses()
        }
    }

    fun fetchInitialCourses() {
        viewModelScope.launch {
            val (newCourses, last) = repository.getCoursesPaginated(null)
            _courses.value = newCourses
            lastCourse = last
        }
    }


    fun loadMoreCourses() {

        if (_isLoadingMore.value) return

        viewModelScope.launch {

            _isLoadingMore.value = true

            val (newCourses, last) = repository.getCoursesPaginated(lastCourse)

            _courses.value = _courses.value + newCourses
            lastCourse = last

            _isLoadingMore.value = false
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
            purchaseRepository.addCourseToUser(courseId)
            println("COURSE SAVED = $courseId")
        }
    }

}




