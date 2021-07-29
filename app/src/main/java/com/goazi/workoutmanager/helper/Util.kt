package com.goazi.workoutmanager.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.repository.ExerciseRepository
import com.goazi.workoutmanager.repository.SessionRepository
import com.goazi.workoutmanager.repository.cache.DatabaseHandler
import com.goazi.workoutmanager.repository.cache.dao.ExerciseDao
import com.goazi.workoutmanager.repository.cache.dao.SessionDao
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        private const val TAG = "Util"

        fun showSnackBar(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                    .show()
        }

        fun getSecondsInString(long: Long): String {
            return (long / 1000).toString()
        }

        fun getUUID(): String {
            return UUID.randomUUID()
                    .toString()
        }

        fun getTimeStamp(): Long {
            return System.currentTimeMillis() / 1000L
        }

        fun getSpacedText(text: String): String {
            val result = StringBuilder()
            for (i in text.indices) {
                result.append(text[i])
                result.append(" ")
            }
            return result.toString()
                    .trim()
        }

        fun getData(context: Context, id: String): Array<String> {
//            val workoutDao: WorkoutDao = DatabaseHandler.getInstance(context)!!.workoutDao()
            val exerciseDao: ExerciseDao = DatabaseHandler.getInstance(context)!!
                    .exerciseDao()
            val sessionDao: SessionDao = DatabaseHandler.getInstance(context)!!
                    .sessionDao()

//            val workoutRepository: WorkoutRepository = WorkoutRepository(workoutDao)
            val exerciseRepository: ExerciseRepository = ExerciseRepository(exerciseDao)
            val sessionRepository: SessionRepository = SessionRepository(sessionDao)
//            val workouts: MutableList<Workout> = workoutRepository.getAllWorkouts()

            val exercises: MutableList<Exercise> = exerciseRepository.getExercisesById(id)

            var exerciseCount = 0
            var sessionCount = 0
            var workTime: Long = 0
            var restTime: Long = 0

            exercises.forEach { exercise ->
                exerciseCount++
                val sessions: MutableList<Session> = sessionRepository.getSessionsById(exercise.id)
                sessions.forEach { session ->
                    sessionCount++
                    workTime += session.workTime
                    restTime += session.restTime
                }
            }
            val totalTime: Long = workTime + restTime

            val cycleTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault()) // HH for 0-23
            val totalTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // HH for 0-23
            cycleTimeFormat.timeZone = TimeZone.getTimeZone("GMT")
            totalTimeFormat.timeZone = TimeZone.getTimeZone("GMT")

            val workTimeString = "${cycleTimeFormat.format(Date(workTime))} minutes"
            val restTimeString = "${cycleTimeFormat.format(Date(restTime))} minutes"
            val totalTimeString = "${totalTimeFormat.format(Date(totalTime))} hours"

            return arrayOf(exerciseCount.toString(), sessionCount.toString(), workTimeString, restTimeString, totalTimeString)
        }

        fun createSilentNotificationChannel(context: Context) {
            val serviceChannel = NotificationChannel(Constant.LOW_IMPORTANCE_CHANNEL_ID, Constant.LOW_IMPORTANCE_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW)

            val manager: NotificationManager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    interface WorkOnClick {
        fun onWorkClicked(view: View, session: Session)
    }

    interface RestOnClick {
        fun onRestClicked(view: View, session: Session)
    }

    interface DeleteOnClick {
        fun onDeleteClicked(view: View, session: Session)
    }

    interface OnTextChangedListener {
        fun onTextChanged(text: String, session: Session, isWork: Boolean)
    }

    class CustomTextChangedListener(private val session: Session, private val isWork: Boolean, private val onTextChangedListener: OnTextChangedListener) :
        TextWatcher {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = if (s.isNullOrBlank()) "0" else s.toString() + "000"
            onTextChangedListener.onTextChanged(text, session, isWork)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}