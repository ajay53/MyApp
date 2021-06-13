package com.goazi.workoutmanager.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
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


class ExerciseActivity : AppCompatActivity(), ExerciseListAdapter.OnExerciseCLickListener {
    companion object {
        private const val TAG = "ExerciseActivity"
    }

    private lateinit var exercises: List<Exercise>
    private var exerciseCount: Int = 0
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private var workoutId: Int = 0
    private lateinit var workout: Workout
    private var isAddExerciseClicked: Boolean = false

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
        val tvWorkoutName = findViewById<TextView>(R.id.tv_workout_name)
        tvWorkoutName.text = workout.name

        val btnAddExercise = findViewById<Button>(R.id.btn_add_exercise)
        btnAddExercise.setOnClickListener(View.OnClickListener {
            isAddExerciseClicked = true
            addExerciseDialog()
//            viewModel.insert(Exercise(0, "123", workoutId))
        })

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
}