package com.goazi.workoutmanager.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goazi.workoutmanager.R
import com.goazi.workoutmanager.adapter.ExerciseListAdapter
import com.goazi.workoutmanager.databinding.CardSessionBinding
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.viewmodel.ExerciseViewModel
import com.goazi.workoutmanager.viewmodel.SessionViewModel
import java.util.*


class ExerciseActivity : AppCompatActivity(), ExerciseListAdapter.OnExerciseCLickListener,
    View.OnClickListener {
    companion object {
        private const val TAG = "ExerciseActivity"
    }

    //widgets
    private lateinit var clAddExercise: ConstraintLayout
    private lateinit var llTimer: LinearLayoutCompat
    private lateinit var tvPlay: TextView
    private lateinit var tvExerciseName: TextView
    private lateinit var tvSeconds: TextView
    private lateinit var imgStop: ImageView
    private lateinit var imgLock: ImageView
    private lateinit var imgReplay: ImageView
    private lateinit var imgPause: ImageView
    private lateinit var imgForward: ImageView

    //variables
    private lateinit var exercises: List<Exercise>
    private var exerciseCount: Int = 0
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private var workoutId: Int = 0
    private lateinit var workout: Workout
    private var isAddExerciseClicked: Boolean = false
    private var isTimerRunning: Boolean = false
    private var seconds: Long = 10

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_exercise)

        exerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        workout = intent.extras!!.getParcelable("obj")!!
        workoutId = workout.id
        exerciseViewModel.searchById(workoutId)

        initViews()
    }

    private fun initViews() {
        seconds *= 1000

        clAddExercise = findViewById(R.id.cl_add_exercise)
        clAddExercise.setOnClickListener(this)

        tvPlay = findViewById(R.id.tv_play)
        tvPlay.setOnClickListener(this)

        imgStop = findViewById(R.id.img_stop)
        imgStop.setOnClickListener(this)

        imgLock = findViewById(R.id.img_lock)
        imgLock.setOnClickListener(this)

        imgReplay = findViewById(R.id.img_replay)
        imgReplay.setOnClickListener(this)

        imgPause = findViewById(R.id.img_pause)
        imgPause.setOnClickListener(this)

        imgForward = findViewById(R.id.img_forward)
        imgForward.setOnClickListener(this)

        tvSeconds = findViewById(R.id.tv_seconds)

        llTimer = findViewById(R.id.ll_timer)
        val tvWorkoutName = findViewById<TextView>(R.id.tv_workout_name)
        tvWorkoutName.text = workout.name

        /*val imgAdd = findViewById<ImageView>(R.id.img_add)
        imgAdd.setOnClickListener(View.OnClickListener {
            isAddExerciseClicked = true
            addExerciseDialog()
//            viewModel.insert(Exercise(0, "123", workoutId))
        })*/

        val rvExercise = findViewById<RecyclerView>(R.id.rv_exercise)
        var adapter =
            ExerciseListAdapter(applicationContext, exerciseViewModel.exercisesById.value, this)

        exerciseViewModel.exercisesById.observe(this, Observer { exercises ->
            this.exercises = exercises
            if (exerciseCount == 0) {
                exerciseCount = exercises.size
                adapter =
                    ExerciseListAdapter(
                        applicationContext,
                        exerciseViewModel.exercisesById.value,
                        this
                    )
                rvExercise?.adapter = adapter
                rvExercise?.layoutManager = LinearLayoutManager(applicationContext)
                rvExercise?.setHasFixedSize(false)
            } else {
                adapter.updateList(exercises)
            }
        })
    }

    private fun startTimer() {
        timer = object : CountDownTimer(seconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                seconds = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                isTimerRunning = false
                /*mButtonStartPause.setText("Start")
                mButtonStartPause.setVisibility(View.INVISIBLE)
                mButtonReset.setVisibility(View.VISIBLE)*/
            }
        }.start()
        isTimerRunning = true
        imgPause.setImageResource(R.drawable.ic_pause)
    }

    private fun pauseTimer() {
        /*isTimerRunning = if (isTimerRunning) {
            imgPause.setImageResource(R.drawable.ic_play)
            timer.cancel()
            false
        } else {
            imgPause.setImageResource(R.drawable.ic_pause)
            timer.start()
            true
        }*/
        imgPause.setImageResource(R.drawable.ic_play)
        timer.cancel()
        isTimerRunning = false
    }

    private fun stopTimer() {

    }

    private fun updateCountDownText() {
        val minutes = (seconds / 1000).toInt() / 60
        val seconds = (seconds / 1000).toInt() % 60
        val timeLeftFormatted: String =
            java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        tvSeconds.text = timeLeftFormatted
    }

    private fun addExerciseDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup: ViewGroup = findViewById(R.id.activity_exercise)
        val view: View = LayoutInflater.from(applicationContext)
            .inflate(R.layout.dialog_add_exercise, viewGroup, false)
        val edtExerciseName = view.findViewById<EditText>(R.id.edt_exercise_name)
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        builder.setView(view)
        val alertDialog: AlertDialog = builder.create()
        btnSave.setOnClickListener {
            exerciseViewModel.insert(Exercise(0, edtExerciseName.text.toString(), workoutId))
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onExerciseClick(position: Int) {
        Log.d(TAG, "onExerciseClick: ")
    }

    override fun onSessionAddClick(position: Int, llSessions: LinearLayoutCompat) {
        addSessionDialog(position, llSessions)
        //UI part
        /*val binding: CardSessionBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.card_session, null, false)
        val session = Session(0, "456", 60, exercises[position].id)
        binding.session = session

        llSessions.addView(binding.root);*/

        //DB part

    }

    private fun addSessionDialog(position: Int, llSessions: LinearLayoutCompat) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup: ViewGroup = findViewById(R.id.activity_exercise)
        val view: View = LayoutInflater.from(applicationContext)
            .inflate(R.layout.dialog_add_session, viewGroup, false)
        val edtWorkTime = view.findViewById<EditText>(R.id.edt_work_time)
        val edtRestTime = view.findViewById<EditText>(R.id.edt_rest_time)
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        builder.setView(view)
        val alertDialog: AlertDialog = builder.create()
        btnSave.setOnClickListener {
            val session = Session(
                0,
                Integer.parseInt(edtWorkTime.text.toString()),
                Integer.parseInt(edtRestTime.text.toString()),
                exercises[position].id
            )
            //update UI
            val binding: CardSessionBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.card_session, null, false)
            binding.session = session

            val layoutParams: LinearLayoutCompat.LayoutParams =
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            layoutParams.setMargins(30, 0, 30, 20)
            binding.root.layoutParams = layoutParams
            llSessions.addView(binding.root);
            //insert in db
            sessionViewModel.insert(session)

            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onExerciseAdded(position: Int, llSessions: LinearLayoutCompat) {
        val divider: View = llSessions.findViewById(R.id.divider)
        divider.visibility = View.GONE

        if (isAddExerciseClicked) return
        val sessions: MutableList<Session> = sessionViewModel.getSessions(exercises[position].id)

        Log.d(TAG, "onExerciseAdded: ")

        if (sessions.size == 0) {
            divider.visibility = View.GONE
        } else {
            divider.visibility = View.VISIBLE
        }

        for (session in sessions) {
            val binding: CardSessionBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.card_session, null, false)
            binding.session = session

            val layoutParams: LinearLayoutCompat.LayoutParams =
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            layoutParams.setMargins(30, 0, 30, 20)
            binding.root.layoutParams = layoutParams
            llSessions.addView(binding.root);
        }
    }

    private fun stopWorkoutDialog(exit: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Stop Workout?")
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                Log.d(TAG, "stopWorkoutDialog: Yes")
                llTimer.visibility = View.GONE
                if (exit == "exit") {
                    finish()
                } else {
                    clAddExercise.visibility = View.VISIBLE
                }
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                Log.d(TAG, "stopWorkoutDialog: Cancel")
                dialog.dismiss()
            }
        // Create the AlertDialog object and return it
        val dialog: AlertDialog = builder.create()
//        dialog.setTitle("title")
        dialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_add_exercise -> {
                isAddExerciseClicked = true
                addExerciseDialog()
            }
            R.id.img_stop -> {
                stopWorkoutDialog("")
            }
            R.id.tv_play -> {
                Log.d(TAG, "onClick: Play")
                llTimer.visibility = View.VISIBLE
                startTimer()
                clAddExercise.visibility = View.GONE
            }
            R.id.img_lock -> {
                Log.d(TAG, "onClick: Lock")
            }
            R.id.img_pause -> {
                Log.d(TAG, "onClick: Pause")
                if (isTimerRunning) {
                    pauseTimer()
                } else {
                    startTimer()
                }

                /*if (isTimerRunning) {
                    imgPause.setImageResource(R.drawable.ic_pause)
                } else {
                    imgPause.setImageResource(R.drawable.ic_play)
                }*/
            }
            R.id.img_replay -> {
                Log.d(TAG, "onClick: Replay")
            }
            R.id.img_forward -> {
                Log.d(TAG, "onClick: Forward")
            }
        }
    }

    override fun onBackPressed() {
        stopWorkoutDialog("exit")
    }
}