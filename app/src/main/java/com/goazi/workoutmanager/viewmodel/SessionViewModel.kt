package com.goazi.workoutmanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.repository.SessionRepository
import com.goazi.workoutmanager.repository.cache.DatabaseHandler
import com.goazi.workoutmanager.repository.cache.dao.SessionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) :AndroidViewModel(application) {
    private val sessionDao: SessionDao = DatabaseHandler.getInstance(application)!!.sessionDao()
    private val repository: SessionRepository = SessionRepository(sessionDao)

    private val exerciseId: MutableLiveData<Int> = MutableLiveData()

    fun searchById(param: Int) {
        exerciseId.value = param
    }

    val sessionsById: LiveData<MutableList<Session>> = Transformations.switchMap(exerciseId) { param->
        repository.getSessionsById(param)
    }

    fun insert(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(session)
        }
    }
}