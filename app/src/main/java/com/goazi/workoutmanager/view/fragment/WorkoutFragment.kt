package com.goazi.workoutmanager.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goazi.workoutmanager.R
import com.goazi.workoutmanager.adapter.WorkoutListAdapter
import com.goazi.workoutmanager.helper.Util
import com.goazi.workoutmanager.helper.Util.Companion.getTimeStamp
import com.goazi.workoutmanager.helper.Util.Companion.getUUID
import com.goazi.workoutmanager.model.Workout
import com.goazi.workoutmanager.view.activity.ExerciseActivity
import com.goazi.workoutmanager.viewmodel.WorkoutViewModel

class WorkoutFragment : Fragment(), WorkoutListAdapter.OnWorkoutCLickListener,
    View.OnClickListener {

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var applicationContext: Context
    private lateinit var viewModel: WorkoutViewModel
    private lateinit var root: View
    private var workoutCount: Int = 0
    private lateinit var workouts: List<Workout>

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_workout, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View?) {
        val btnAddWorkout = root?.findViewById<Button>(R.id.btn_add_workout)
        val rvWorkout = root?.findViewById<RecyclerView>(R.id.rv_workout)
        btnAddWorkout?.setOnClickListener(this)

        //set recycler view
        var adapter = WorkoutListAdapter(applicationContext, viewModel.getLiveWorkout.value, this)

        viewModel.getLiveWorkout.observe(viewLifecycleOwner, Observer { workouts ->
            this.workouts = workouts
            if (workoutCount == 0) {
                workoutCount = workouts.size
                adapter =
                    WorkoutListAdapter(applicationContext, viewModel.getLiveWorkout.value, this)
                rvWorkout?.adapter = adapter
                rvWorkout?.layoutManager = LinearLayoutManager(applicationContext)
                rvWorkout?.setHasFixedSize(false)
            } else {
                adapter.updateList(workouts)
            }
        })
    }

    override fun onWorkoutClick(position: Int) {
        Util.showSnackBar(root, "Workout: " + workouts[position].name)
        val intent = Intent(applicationContext, ExerciseActivity::class.java)
//            .putExtra("obj", workouts[position])
            .putExtra("id", workouts[position].id)
            .putExtra("name", workouts[position].name)
        fragmentActivity.startActivity(intent)
    }

    override fun onMenuClick(position: Int) {
        Log.d(TAG, "onMenuClick: ")
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.btn_add_workout) {
            addWorkoutDialog()
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
            viewModel.insert(Workout(getUUID(), edtWorkoutName.text.toString(), getTimeStamp()))
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}