package com.goazzi.workoutmanager.repository.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.goazzi.workoutmanager.model.History

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * FROM tbl_history WHERE date =:date")
    fun get(date:String):List<History>
}