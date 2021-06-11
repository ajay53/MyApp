package com.goazi.workoutmanager.repository.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import com.goazi.workoutmanager.repository.cache.dao.SessionDao
import com.goazi.workoutmanager.repository.cache.dao.WorkoutDao

@Database(
    entities = [Workout::class, Exercise::class, Session::class],
    version = 4,
    exportSchema = false
)
abstract class DatabaseHandler : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(context: Context): DatabaseHandler? {
            if (INSTANCE != null) {
                return INSTANCE
            }
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHandler::class.java,
                    "db_utility"
                ).fallbackToDestructiveMigration()
                    .build()
                return INSTANCE
            }
        }
    }
}