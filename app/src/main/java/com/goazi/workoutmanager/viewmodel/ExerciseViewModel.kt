package com.goazi.workoutmanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.repository.ExerciseRepository
import com.goazi.workoutmanager.repository.WorkoutRepository
import com.goazi.workoutmanager.repository.cache.DatabaseHandler
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val exerciseDao: ExerciseDao = DatabaseHandler.getInstance(application)!!.exerciseDao()
    private val repository: ExerciseRepository = ExerciseRepository(exerciseDao)

    private val workoutId: MutableLiveData<Int> = MutableLiveData()

    fun searchById(param: Int) {
        workoutId.value = param
    }

    val exercisesById: LiveData<MutableList<Exercise>> =
        Transformations.switchMap(workoutId) { param ->
            repository.getExercisesById(param)
        }

    fun insert(exercise: Exercise) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(exercise)
        }
    }

    fun getExerciseById(id: String): Exercise {
        return repository.getExerciseById(id)
    }
}