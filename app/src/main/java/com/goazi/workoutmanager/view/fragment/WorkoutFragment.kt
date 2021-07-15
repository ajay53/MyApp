package com.goazi.workoutmanager.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goazi.workoutmanager.R
import com.goazi.workoutmanager.adapter.WorkoutListAdapter
import com.goazi.workoutmanager.helper.Util
import com.goazi.workoutmanager.model.Exercise
import com.goazi.workoutmanager.model.Session
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.view.activity.ExerciseActivity
import com.goazi.workoutmanager.viewmodel.ExerciseViewModel
import com.goazi.workoutmanager.viewmodel.SessionViewModel
import com.goazi.workoutmanager.viewmodel.WorkoutViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WorkoutFragment : Fragment(), WorkoutListAdapter.OnWorkoutCLickListener,
    View.OnClickListener {

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var applicationContext: Context
    private lateinit var viewModel: WorkoutViewModel
    private lateinit var root: View

    companion object {
        private const val TAG = "WorkoutFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        fragmentActivity = requireActivity()
        applicationContext = fragmentActivity.applicationContext
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_workout, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View?) {
        val tvNoWorkouts = root?.findViewById<TextView>(R.id.tv_no_workouts)
        val fabAddWorkout = root?.findViewById<FloatingActionButton>(R.id.fab_add_workout)
        val rvWorkout = root?.findViewById<RecyclerView>(R.id.rv_workout)
        fabAddWorkout?.setOnClickListener(this)

        //set recycler view
        var adapter = WorkoutListAdapter(applicationContext, viewModel.getLiveWorkout.value, this)

        viewModel.getLiveWorkout.observe(viewLifecycleOwner, { workouts ->
            if (workouts == null || workouts.size == 0) {
                tvNoWorkouts?.visibility = View.VISIBLE
            } else {
                tvNoWorkouts?.visibility = View.GONE
            }
            viewModel.workouts = workouts
            if (viewModel.workoutCount == 0) {
                viewModel.workoutCount = workouts.size
                adapter = WorkoutListAdapter(applicationContext, viewModel.getLiveWorkout.value, this)
                rvWorkout?.adapter = adapter
                rvWorkout?.layoutManager = LinearLayoutManager(applicationContext)
                rvWorkout?.setHasFixedSize(false)
            } else {
                adapter.updateList(workouts)
            }
        })
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rvWorkout)
    }

    override fun onWorkoutClick(position: Int) {
//        Util.showSnackBar(root, "Workout: " + workouts[position].name)
        val intent = Intent(applicationContext, ExerciseActivity::class.java).putExtra("id", viewModel.workouts[position].id)
                .putExtra("name", viewModel.workouts[position].name)
        fragmentActivity.startActivity(intent)
    }

    override fun onMenuClick(position: Int) {
        Log.d(TAG, "onMenuClick: ")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_workout -> {
                if (!viewModel.isFabClicked) {
                    viewModel.isFabClicked = true
                    addWorkoutDialog()
                }
            }
        }
    }

    private fun addWorkoutDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(fragmentActivity)
        val viewGroup: ViewGroup = root.findViewById(R.id.fragment_workout)
        val view: View = LayoutInflater.from(applicationContext)
                .inflate(R.layout.dialog_add_workout, viewGroup, false)
        val edtWorkoutName = view.findViewById<EditText>(R.id.edt_workout_name)
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        builder.setView(view)
        val alertDialog: AlertDialog = builder.create()
        btnSave.setOnClickListener {
            if (edtWorkoutName.text.toString()
                        .isEmpty()) {
                Util.showSnackBar(root.findViewById<RelativeLayout>(R.id.fragment_workout), "Name cannot be Empty!")
            } else {
                viewModel.insert(Workout(Util.getUUID(), edtWorkoutName.text.toString(), Util.getTimeStamp()))
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
        alertDialog.setOnDismissListener {
            viewModel.isFabClicked = false
        }
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            val executor: ExecutorService = Executors.newSingleThreadExecutor()
            executor.execute(kotlinx.coroutines.Runnable {
                deleteWorkout(viewHolder.bindingAdapterPosition)
            })
        }
    }

    private fun deleteWorkout(position: Int) {
        val workout: Workout = viewModel.workouts[position]
        val exerciseViewModel: ExerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        val sessionViewModel: SessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        val exercises: List<Exercise> = exerciseViewModel.getExercisesById(workout.id)
        val sessions: MutableList<Session> = mutableListOf()

        exercises.forEach { exercise ->
            val currSessions: List<Session> = sessionViewModel.getSessionsById(exercise.id)
            sessions.addAll(currSessions)
            currSessions.forEach { session ->
                sessionViewModel.delete(session)
            }
            exerciseViewModel.delete(exercise)
        }
        viewModel.delete(workout)
        val handler = Handler(Looper.getMainLooper())
        handler.post(kotlinx.coroutines.Runnable {
            showSnackBar(workout, exercises, sessions, exerciseViewModel, sessionViewModel)
        })
    }

    private fun showSnackBar(workout: Workout, exercises: List<Exercise>, sessions: List<Session>, exerciseViewModel: ExerciseViewModel, sessionViewModel: SessionViewModel) {
        Snackbar.make(root.findViewById<RelativeLayout>(R.id.fragment_workout), getString(R.string.workout_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo)) {
                    Log.d(TAG, "showSnackBar: UNDO clicked")

                    sessions.forEach { session ->
                        sessionViewModel.insert(session)
                    }
                    exercises.forEach { exercise ->
                        exerciseViewModel.insert(exercise)
                    }
                    viewModel.insert(workout)
                }
                .show()
    }
}