package com.goazzi.workoutmanager.repository.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import com.goazzi.workoutmanager.model.Circuit

@Dao interface CircuitDao {

    @Insert
    suspend fun insert(circuit: Circuit)
}