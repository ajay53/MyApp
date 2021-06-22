package com.goazi.workoutmanager.repository

import androidx.lifecycle.LiveData
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val getAllExercises: LiveData<MutableList<Exercise>> = exerciseDao.getAllExercises()

    suspend fun insert(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    fun getExercisesById(id: Int): LiveData<MutableList<Exercise>> {
        return exerciseDao.getExercisesById(id)
    }

    fun getExerciseById(id: String): Exercise {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val future: Future<Exercise> = executor.submit(SelectCallable(id, exerciseDao))
        return future.get()
    }

    companion object {
        private class SelectCallable(val id: String, val exerciseDao: ExerciseDao) :
            Callable<Exercise> {

            override fun call(): Exercise {
                return exerciseDao.getExerciseById(id)
            }
        }
    }
}