package com.goazzi.workoutmanager.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.adapter.ExerciseListAdapter
import com.goazzi.workoutmanager.model.Exercise
import com.goazzi.workoutmanager.model.Session
import com.goazzi.workoutmanager.repository.ExerciseRepository
import com.goazzi.workoutmanager.repository.cache.DatabaseHandler
import com.goazzi.workoutmanager.repository.cache.dao.ExerciseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    var adapter: ExerciseListAdapter? = null
    lateinit var smoothScroller: RecyclerView.SmoothScroller
    lateinit var exercises: List<Exercise>
    lateinit var workoutId: String
    var isAddExerciseClicked: Boolean = false
    var isAddSessionClicked: Boolean = false
    var isTimerRunning: Boolean = false
    var seconds: Long = 10
    var currExerciseName: String = ""
    var currExerciseId: String = ""
    var currExercisePosition: Int = 0
    var currSessionPosition: Int = -1
    lateinit var currentSession: Session
    var isWork: Boolean = false
    var isWorkoutRunning: Boolean = false
    var isLocked: Boolean = true
    var isItemMoved: Boolean = false
    lateinit var timer: CountDownTimer
    var dataMap: MutableMap<String?, MutableList<Session>> = HashMap()
    var viewMap: MutableMap<String?, MutableList<View>> = LinkedHashMap()
    lateinit var tts: TextToSpeech
    var clickedMenuPosition: Int = 0
    var isEditing: Boolean = false
    lateinit var currId: String

    val mediaBell: MediaPlayer
    val mediaWhistle: MediaPlayer

    init {
        smoothScroller = object : LinearSmoothScroller(application) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        tts = TextToSpeech(application, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.ENGLISH);
            }
        })
        mediaBell = MediaPlayer.create(application, R.raw.boxing_bell)
        mediaWhistle = MediaPlayer.create(application, R.raw.whistle)
    }

    fun speech(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun setMap(exeId: String, llSession: LinearLayoutCompat, sessionViewModel: SessionViewModel) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(kotlinx.coroutines.Runnable {

            val sessions: MutableList<Session> = sessionViewModel.getSessionsById(exeId)
            dataMap[exeId] = sessions

            val childCount: Int = llSession.childCount
            val sessionList: MutableList<View> = mutableListOf()
            for (i in 0 until childCount) {
                val ll: View = llSession.getChildAt(i)
                sessionList.add(ll)
            }
            viewMap[exeId] = sessionList
        })
    }

    //Database Part
    private val exerciseDao: ExerciseDao = DatabaseHandler.getInstance(application)!!
            .exerciseDao()
    private val repository: ExerciseRepository = ExerciseRepository(exerciseDao)

    private val workoutIdParam: MutableLiveData<String> = MutableLiveData()

    fun searchById(param: String) {
        workoutIdParam.value = param
    }

    val getLiveExercisesById: LiveData<MutableList<Exercise>> = Transformations.switchMap(workoutIdParam) { param ->
        repository.getLiveExercisesById(param)
    }

    fun insert(exercise: Exercise) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(exercise)
        }
    }

    fun updateName(id: String, name:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateName(id, name)
        }
    }

    fun delete(exercise: Exercise) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(exercise)
        }
    }

    fun getExerciseById(id: String): Exercise {
        return repository.getExerciseById(id)
    }

    fun getExercisesById(id: String): MutableList<Exercise> {
        return repository.getExercisesById(id)
    }
}