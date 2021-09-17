package com.goazzi.workoutmanager.repository

import com.goazzi.workoutmanager.model.History
import com.goazzi.workoutmanager.model.Session
import com.goazzi.workoutmanager.repository.cache.dao.HistoryDao

class HistoryRepository(private val historyDao: HistoryDao) {

    suspend fun insert(history: History) {
        historyDao.insert(history)
    }


}