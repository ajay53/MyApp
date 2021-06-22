package com.goazi.workoutmanager.repository

import androidx.lifecycle.LiveData
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.repository.cache.dao.SessionDao
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun insert(session: Session) {
        sessionDao.insert(session)
    }

    fun getSessionsById(id: Int): LiveData<MutableList<Session>> {
        return sessionDao.getSessionsById(id)
    }

    fun getSessions(id: String): MutableList<Session> {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val future: Future<MutableList<Session>> = executor.submit(SelectCallable(id, sessionDao))
        return future.get()
    }

    companion object {
        private class SelectCallable(val id: String, val sessionDao: SessionDao) :
            Callable<MutableList<Session>> {

            override fun call(): MutableList<Session> {
                return sessionDao.getSessions(id)
            }
        }
    }
}