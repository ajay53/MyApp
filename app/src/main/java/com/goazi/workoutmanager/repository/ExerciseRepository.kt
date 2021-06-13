package com.goazi.workoutmanager.repository

import androidx.lifecycle.LiveData
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val getAllExercises: LiveData<MutableList<Exercise>> =exerciseDao.getAllExercises()

    suspend fun insert(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    fun getExercisesById(id: Int): LiveData<MutableList<Exercise>>{
        return exerciseDao.getExercisesById(id)
    }
}