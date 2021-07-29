package com.goazi.workoutmanager.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.goazi.workoutmanager.model.Session

@Dao
interface SessionDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
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