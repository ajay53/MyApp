package com.goazzi.workoutmanager.repository.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import com.goazzi.workoutmanager.model.Lap

@Dao interface LapDao {

    @Insert
    suspend fun insert(lap: Lap)
}