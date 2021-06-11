package com.goazi.workoutmanager.repository

import androidx.lifecycle.LiveData
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.repository.cache.dao.SessionDao

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun insert(session: Session) {
        sessionDao.insert(session)
    }

    fun getSessionsById(id: Int): LiveData<MutableList<Session>> {
        return sessionDao.getSessionsById(id)
    }
}