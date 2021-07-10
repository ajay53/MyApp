package com.goazi.workoutmanager.viewmodel

import android.app.Application
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.repository.ExerciseRepository
import com.goazi.workoutmanager.repository.cache.DatabaseHandler
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var smoothScroller: RecyclerView.SmoothScroller
    lateinit var exercises: List<Exercise>
    var exerciseCount: Int = 0
    lateinit var exerciseViewModel: ExerciseViewModel
    lateinit var sessionViewModel: SessionViewModel
    lateinit var workoutId: String
    var isAddExerciseClicked: Boolean = false
    var isTimerRunning: Boolean = false
    var seconds: Long = 10
    var currExerciseName: String = ""
    var currExerciseId: String = ""
    var currExercisePosition: Int = 0
    var currSessionPosition: Int = -1
    lateinit var currentSession: Session
    var isWork: Boolean = false
    var isWorkoutRunning: Boolean = false
    var isLocked: Boolean = false
    lateinit var timer: CountDownTimer
    var dataMap: MutableMap<String?, MutableList<Session>> = HashMap()
    var viewMap: MutableMap<String?, MutableList<View>> = LinkedHashMap()
    lateinit var tts: TextToSpeech

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
        });
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