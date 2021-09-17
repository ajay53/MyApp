package com.goazzi.workoutmanager.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.goazzi.workoutmanager.model.Exercise
import com.goazzi.workoutmanager.model.Session
import com.goazzi.workoutmanager.model.Workout
import com.goazzi.workoutmanager.repository.ExerciseRepository
import com.goazzi.workoutmanager.repository.SessionRepository
import com.goazzi.workoutmanager.repository.cache.DatabaseHandler
import com.goazzi.workoutmanager.repository.cache.dao.ExerciseDao
import com.goazzi.workoutmanager.repository.cache.dao.SessionDao
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

        fun getUpperCaseInitials(text: String): String {
            val result = StringBuilder()
            for (word in text.split(" ")) {
                result.append(word.substring(0, 1)
                        .uppercase() + word.substring(1))
                result.append(" ")
            }
            return result.toString()
                    .trim()
        }

        fun getLineSplitText(text: String): String {
            val max = 25
            val words = text.split("   ")

            if (words.size > 1) {
                val builder: StringBuilder = java.lang.StringBuilder()
                builder.append(words[0])
                var counter = 1
                for (i in 1 until words.size) {
                    counter = i
                    if ("$builder   ${words[i]}".length > max) {
                        builder.append("\n")
                        break
                    } else {
                        counter++
                        builder.append("   ${words[i]}")
                    }
                }
                for (i in counter until words.size) {
                    builder.append("${words[i]}   ")
                }
                return builder.toString()
                        .trim()
            } else {
                return text
            }
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
//            val totalTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // HH for 0-23
            val totalTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // HH for 0-23
            cycleTimeFormat.timeZone = TimeZone.getTimeZone("GMT")
            totalTimeFormat.timeZone = TimeZone.getTimeZone("GMT")


//            val workTimeString = "${cycleTimeFormat.format(Date(workTime))} min"
            val workTimeString = if (workTime < 3600000) {
                "${cycleTimeFormat.format(Date(workTime))} min"
            } else {
                "${totalTimeFormat.format(Date(workTime))} hrs"
            }
            val restTimeString = "${cycleTimeFormat.format(Date(restTime))} min"
            val totalTimeString = "${totalTimeFormat.format(Date(totalTime))} hrs"

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

    interface OnSessionChangedListener {
        fun onSessionTextChanged(text: String, session: Session, isWork: Boolean)
    }

    class SessionTextChangedListener(private val session: Session, private val isWork: Boolean, private val onSessionChangedListener: OnSessionChangedListener) :
        TextWatcher {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = if (s.isNullOrBlank()) "0" else s.toString() + "000"
            onSessionChangedListener.onSessionTextChanged(text, session, isWork)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    interface OnExerciseChangedListener {
        fun onExerciseTextChanged(text: String, session: Session, isWork: Boolean)
    }

    class ExerciseTextChangedListener(private val exercise: Exercise, private val onExerciseChangedListener: OnExerciseChangedListener) :
        TextWatcher {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            val text = if (s.isNullOrBlank()) "0" else s.toString() + "000"
//            onSessionChangedListener.onTextChanged(text, session, isWork)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    interface OnWorkoutChangedListener {
        fun beforeTextChanged(text: String, workout: Workout)
        fun onTextChanged(text: String, workout: Workout)
    }

    class WorkoutTextChangedListener(private val workout: Workout, private val edtWorkout: AppCompatEditText, private val onWorkoutChangedListener: OnWorkoutChangedListener) :
        TextWatcher {

        lateinit var preText: String
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()
            edtWorkout.removeTextChangedListener(this)
            val name: String = if (s.toString().length > preText.length) {
                if (text[text.length - 1] != ' ') {
                    if (preText.isBlank()) {
                        s.toString()
                    } else {
                        text.subSequence(0, text.length - 1)
                                .toString() + " " + text[text.length - 1]
                    }
                } else {
                    s.toString() + " "
                }
            } else {
                if (preText.length == 1) {
                    ""
                } else {
                    text.subSequence(0, text.length - 1)
                            .toString()
                }
            }
            edtWorkout.setText(name.uppercase())
            edtWorkout.setSelection(edtWorkout.text.toString().length)
            edtWorkout.addTextChangedListener(this)
            onWorkoutChangedListener.onTextChanged(name, workout)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            preText = s.toString()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}