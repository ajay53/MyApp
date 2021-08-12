package com.goazzi.workoutmanager.repository.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.goazzi.workoutmanager.helper.Util
import com.goazzi.workoutmanager.model.Exercise
import com.goazzi.workoutmanager.model.Session
import com.goazzi.workoutmanager.model.Workout
import com.goazzi.workoutmanager.repository.cache.dao.ExerciseDao
import com.goazzi.workoutmanager.repository.cache.dao.SessionDao
import com.goazzi.workoutmanager.repository.cache.dao.WorkoutDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Workout::class, Exercise::class, Session::class], version = 1, exportSchema = false)
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
                INSTANCE = Room.databaseBuilder(context.applicationContext, DatabaseHandler::class.java, "db_workout_manager")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                return INSTANCE
            }
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                GlobalScope.launch {
                    insertDefault()
                }
            }
        }

        suspend fun insertDefault() {
            val workoutDao = INSTANCE?.workoutDao()
            val exerciseDao = INSTANCE?.exerciseDao()
            val sessionDao = INSTANCE?.sessionDao()

            val uuidWorkout = Util.getUUID()
            var uuidExercise = Util.getUUID()
            workoutDao?.insert(Workout(uuidWorkout, "SHOULDER", Util.getTimeStamp()))

            sessionDao?.insert(Session(Util.getUUID(), 60000, 10000, 1L, uuidExercise))
            sessionDao?.insert(Session(Util.getUUID(), 50000, 90000, 2L, uuidExercise))
            sessionDao?.insert(Session(Util.getUUID(), 60000, 10000, 3L, uuidExercise))
            exerciseDao?.insert(Exercise(uuidExercise, 1L, "Front Delt", uuidWorkout))

            uuidExercise = Util.getUUID()
            sessionDao?.insert(Session(Util.getUUID(), 60000, 10000, 4L, uuidExercise))
            sessionDao?.insert(Session(Util.getUUID(), 50000, 90000, 5L, uuidExercise))
            sessionDao?.insert(Session(Util.getUUID(), 60000, 10000, 6L, uuidExercise))
            exerciseDao?.insert(Exercise(uuidExercise, 2L, "Side Delt", uuidWorkout))

            uuidExercise = Util.getUUID()
            sessionDao?.insert(Session(Util.getUUID(), 60000, 10000, 7L, uuidExercise))
            sessionDao?.insert(Session(Util.getUUID(), 50000, 90000, 8L, uuidExercise))
            sessionDao?.insert(Session(Util.getUUID(), 60000, 10000, 9L, uuidExercise))
            exerciseDao?.insert(Exercise(uuidExercise, 3L, "Rear Delt", uuidWorkout))
        }
    }
}