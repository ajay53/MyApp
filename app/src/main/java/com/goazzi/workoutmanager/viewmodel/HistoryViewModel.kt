package com.goazzi.workoutmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.goazzi.workoutmanager.model.History
import com.goazzi.workoutmanager.repository.HistoryRepository
import com.goazzi.workoutmanager.repository.cache.DatabaseHandler
import com.goazzi.workoutmanager.repository.cache.dao.HistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val historyDao: HistoryDao = DatabaseHandler.getInstance(application)!!.historyDao()
    private val repository: HistoryRepository = HistoryRepository(historyDao)

    fun insert(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(history)
        }
    }
}