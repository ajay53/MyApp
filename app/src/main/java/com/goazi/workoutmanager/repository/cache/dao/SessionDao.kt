package com.goazi.workoutmanager.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.goazi.workoutmanager.model.Session

@Dao
interface SessionDao {

    @Insert
    suspend fun insert(session: Session)

    @Query("SELECT * FROM tbl_session  ORDER BY id ASC")
    fun getAllSessions(): LiveData<MutableList<Session>>

    @Query("SELECT * FROM tbl_session WHERE exerciseId = :id")
    fun getSessionsById(id: Int): LiveData<MutableList<Session>>

    @Query("SELECT * FROM tbl_session WHERE exerciseId = :id")
    fun getSessions(id: String): MutableList<Session>
}