package com.goazi.workoutmanager.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.goazi.workoutmanager.model.Workout

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM tbl_workout ORDER BY id DESC")
    fun getAllWorkouts(): LiveData<MutableList<Workout>>
}