package com.goazi.workoutmanager.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.goazi.workoutmanager.model.Exercise

@Dao interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM tbl_exercise WHERE workoutId = :id ORDER BY timeStamp ASC")
    fun getLiveExercisesById(id: String): LiveData<MutableList<Exercise>>

    @Query("SELECT * FROM tbl_exercise  ORDER BY id ASC")
    fun getAllExercises(): LiveData<MutableList<Exercise>>

    @Query("SELECT * FROM tbl_exercise WHERE id = :id")
    fun getExerciseById(id: String): Exercise

    @Query("SELECT * FROM tbl_exercise WHERE workoutId = :id ORDER BY timeStamp ASC")
    fun getExercisesById(id: String): MutableList<Exercise>
}