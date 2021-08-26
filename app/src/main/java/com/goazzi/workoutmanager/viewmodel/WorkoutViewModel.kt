package com.goazzi.workoutmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.goazzi.workoutmanager.adapter.WorkoutListAdapter
import com.goazzi.workoutmanager.model.Workout
import com.goazzi.workoutmanager.repository.WorkoutRepository
import com.goazzi.workoutmanager.repository.cache.DatabaseHandler
import com.goazzi.workoutmanager.repository.cache.dao.WorkoutDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var workouts: List<Workout>
//    var isFabClicked: Boolean = false
    var adapter: WorkoutListAdapter? = null
    var swipedPosition: Int = 0
    var clickedPosition: Int = -1
    var clickedMenuPosition: Int = 0
    lateinit var updatedName: String
    lateinit var currId: String
    var isEditing: Boolean = false

    //Database Part
    private val workoutDao: WorkoutDao = DatabaseHandler.getInstance(application)!!
            .workoutDao()
    private val repository: WorkoutRepository = WorkoutRepository(workoutDao)
    val getLiveWorkout: LiveData<MutableList<Workout>> = repository.getLiveWorkouts

    fun insert(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(workout)
        }
    }

    fun updateName(id: String, name:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateName(id, name)
        }
    }

    fun delete(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(workout)
        }
    }
}