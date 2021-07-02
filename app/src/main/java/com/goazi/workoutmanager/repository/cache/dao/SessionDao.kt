package com.goazi.workoutmanager.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.goazi.workoutmanager.model.Session

@Dao
interface SessionDao {

    @Insert
    suspend fun insert(session: Session)

    @Query("SELECT * FROM tbl_session  ORDER BY timeStamp ASC")
    fun getAllSessions(): LiveData<MutableList<Session>>

    @Query("SELECT * FROM tbl_session WHERE exerciseId = :id")
    fun getLiveSessionsById(id: String): LiveData<MutableList<Session>>

    @Query("SELECT * FROM tbl_session WHERE exerciseId = :id ORDER BY timeStamp ASC")
    fun getSessionsById(id: String): MutableList<Session>

    @Delete
    suspend fun delete(session: Session)
}