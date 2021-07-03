package com.goazi.workoutmanager.repository

import androidx.lifecycle.LiveData
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.repository.cache.dao.WorkoutDao
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    val getLiveWorkouts: LiveData<MutableList<Workout>> = workoutDao.getLiveWorkouts()

    suspend fun insert(workout: Workout) {
        workoutDao.insert(workout)
    }

    suspend fun delete(workout: Workout) {
        workoutDao.delete(workout)
    }

    fun getAllWorkouts(): MutableList<Workout> {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val future: Future<MutableList<Workout>> = executor.submit(SelectListCallable(workoutDao))
        return future.get()
    }

    companion object {
        private class SelectListCallable(val workoutDao: WorkoutDao) :
            Callable<MutableList<Workout>> {
            override fun call(): MutableList<Workout> {
                return workoutDao.getAllWorkouts()
            }
        }
    }
}