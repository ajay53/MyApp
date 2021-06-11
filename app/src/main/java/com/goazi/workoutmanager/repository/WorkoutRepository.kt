package com.goazi.workoutmanager.repository

import androidx.lifecycle.LiveData
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import com.goazi.workoutmanager.repository.cache.dao.WorkoutDao

class WorkoutRepository(private val workoutDao: WorkoutDao, private val exerciseDao: ExerciseDao) {

    val getAllWorkouts: LiveData<MutableList<Workout>> =workoutDao.getAllWorkouts()
    val getAllExercises: LiveData<MutableList<Exercise>> =exerciseDao.getAllExercises()

    suspend fun insert(workout: Workout) {
        workoutDao.insert(workout)
    }
}