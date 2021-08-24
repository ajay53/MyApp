package com.goazzi.workoutmanager.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.goazzi.workoutmanager.model.Workout

@Dao interface WorkoutDao {

    @Insert
    suspend fun insert(workout: Workout)

    @Query("UPDATE tbl_workout SET name =:name WHERE id =:id")
    suspend fun updateName(id: String, name: String)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM tbl_workout ORDER BY timeStamp ASC")
    fun getLiveWorkouts(): LiveData<MutableList<Workout>>

    @Query("SELECT * FROM tbl_workout ORDER BY timeStamp DESC")
    fun getAllWorkouts(): MutableList<Workout>
}