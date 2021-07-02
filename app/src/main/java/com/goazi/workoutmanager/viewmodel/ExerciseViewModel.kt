package com.goazi.workoutmanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.repository.ExerciseRepository
import com.goazi.workoutmanager.repository.cache.DatabaseHandler
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val exerciseDao: ExerciseDao = DatabaseHandler.getInstance(application)!!.exerciseDao()
    private val repository: ExerciseRepository = ExerciseRepository(exerciseDao)

    private val workoutId: MutableLiveData<String> = MutableLiveData()

    fun searchById(param: String) {
        workoutId.value = param
    }

    val getLiveExercisesById: LiveData<MutableList<Exercise>> =
        Transformations.switchMap(workoutId) { param ->
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


}