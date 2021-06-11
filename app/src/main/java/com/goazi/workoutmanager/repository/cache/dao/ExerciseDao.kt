package com.goazi.workoutmanager.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.goazi.workoutmanager.model.Exercise

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insert(exercise: Exercise)

    @Query("SELECT * FROM tbl_exercise WHERE workoutId = :id")
    fun getById(id: Int): LiveData<MutableList<Exercise>>

    @Query("SELECT * FROM tbl_exercise  ORDER BY id ASC")
    fun getAllExercises(): LiveData<MutableList<Exercise>>
}