package com.goazi.workoutmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.repository.WorkoutRepository
import com.goazi.workoutmanager.repository.cache.DatabaseHandler
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import com.goazi.workoutmanager.repository.cache.dao.WorkoutDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutDao: WorkoutDao = DatabaseHandler.getInstance(application)!!.workoutDao()
    private val exerciseDao: ExerciseDao = DatabaseHandler.getInstance(application)!!.exerciseDao()
    private val repository: WorkoutRepository = WorkoutRepository(workoutDao, exerciseDao)
    val getAllWorkout: LiveData<MutableList<Workout>> = repository.getAllWorkouts
    val getAllExercises: LiveData<MutableList<Exercise>> =repository.getAllExercises

    fun insert(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(workout)
        }
    }
}