package com.goazzi.workoutmanager.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.adapter.WorkoutListAdapter
import com.goazzi.workoutmanager.helper.Util
import com.goazzi.workoutmanager.model.Exercise
import com.goazzi.workoutmanager.model.Session
import com.goazzi.workoutmanager.model.Workout
import com.goazzi.workoutmanager.view.activity.AddWorkoutActivity
import com.goazzi.workoutmanager.view.activity.ExerciseActivity
import com.goazzi.workoutmanager.viewmodel.ExerciseViewModel
import com.goazzi.workoutmanager.viewmodel.SessionViewModel
import com.goazzi.workoutmanager.viewmodel.WorkoutViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class WorkoutFragment : Fragment(), WorkoutListAdapter.OnWorkoutCLickListener, View.OnClickListener,
    PopupMenu.OnMenuItemClickListener, Util.OnWorkoutChangedListener,
    TextView.OnEditorActionListener {

    companion object {
        private const val TAG = "WorkoutFragment"
    }

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var applicationContext: Context
    private lateinit var viewModel: WorkoutViewModel
    private lateinit var root: View
    private lateinit var imgMenu: ImageView
    private lateinit var imgCheck: ImageView
    private lateinit var edtWorkout: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        fragmentActivity = requireActivity()
        applicationContext = fragmentActivity.applicationContext
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_workout, container, false)
        initViews()
        return root
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.clickedPosition != -1) {
            viewModel.adapter?.update(viewModel.clickedPosition, viewModel.workouts[viewModel.clickedPosition])
        }
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        viewModel.clickedPosition = -1
        val fabAddWorkout = root.findViewById<FloatingActionButton>(R.id.fab_add_workout)
        val rvWorkout = root.findViewById<RecyclerView>(R.id.rv_workout)
        fabAddWorkout?.setOnClickListener(this)

        val tvWorkouts = root.findViewById<TextView>(R.id.tv_workouts)
        tvWorkouts.setOnClickListener(this)

        //set recycler view
        viewModel.getLiveWorkout.observe(viewLifecycleOwner, { workouts ->
            if (viewModel.adapter == null) {
                viewModel.workouts = workouts
                viewModel.adapter = WorkoutListAdapter(applicationContext, workouts, this)
                rvWorkout?.adapter = viewModel.adapter
                rvWorkout?.layoutManager = LinearLayoutManager(applicationContext)
                //dont add this when height is set to WRAP_CONTENT
//                rvWorkout?.setHasFixedSize(true) ___ Error: When using `setHasFixedSize() in an RecyclerView(While building apk)
            } else {
                when {
                    viewModel.workouts.size < workouts.size -> {
                        viewModel.adapter?.add(workouts[viewModel.swipedPosition], viewModel.swipedPosition)
                    }
                    viewModel.workouts.size > workouts.size -> {
                        viewModel.adapter?.delete(viewModel.swipedPosition)
                    }
                    else -> if (this::imgMenu.isInitialized) {
                        viewModel.adapter?.update(viewModel.clickedMenuPosition, workouts[viewModel.clickedMenuPosition])
                    }
                }
                viewModel.workouts = workouts
            }
        })
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rvWorkout)
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
//            viewModel.swipedPosition = viewHolder.bindingAdapterPosition
            viewModel.swipedPosition = viewHolder.bindingAdapterPosition
            stopWorkoutDialog(viewHolder.bindingAdapterPosition)
        }
    }

    private fun stopWorkoutDialog(pos: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(fragmentActivity)
        val viewGroup: ViewGroup = root.findViewById(R.id.fragment_workout)
        val view: View = LayoutInflater.from(applicationContext)
                .inflate(R.layout.dialog_custom_alert, viewGroup, false)
        val tvMsg = view.findViewById<TextView>(R.id.tv_msg)
        tvMsg.text = "Delete Workout?"
        val btnNo = view.findViewById<AppCompatButton>(R.id.btn_no)
        val btnYes = view.findViewById<AppCompatButton>(R.id.btn_yes)
        builder.setView(view)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnNo.setOnClickListener {
            alertDialog.dismiss()
            viewModel.adapter?.notifyItemChanged(pos)
        }
        btnYes.setOnClickListener {
            val executor: ExecutorService = Executors.newSingleThreadExecutor()
            executor.execute(kotlinx.coroutines.Runnable {
                deleteWorkout(pos)
            })
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun deleteWorkout(position: Int) {
        viewModel.swipedPosition = position
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add_workout -> {
                if (viewModel.isEditing) {
                    editDone()
                    return
                }
                viewModel.swipedPosition = viewModel.workouts.size
                startActivity(Intent(applicationContext, AddWorkoutActivity::class.java))
//                dump()
            }
            R.id.tv_workouts -> {
                if (viewModel.isEditing) {
                    editDone()
                    return
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.edit_workout -> {
                Log.d(TAG, "onMenuItemClick: edit workout")
                editWorkout()
                true
            }
            R.id.delete_workout -> {
                Log.d(TAG, "onMenuItemClick: delete workout")
                val executor: ExecutorService = Executors.newSingleThreadExecutor()
                executor.execute(kotlinx.coroutines.Runnable {
                    deleteWorkout(viewModel.clickedMenuPosition)
                })
                true
            }
            else -> true
        }
    }

    override fun onWorkoutLongClick(position: Int, imgMenu: AppCompatImageView, imgCheck: AppCompatImageView) {
        editDone()
        this.imgMenu = imgMenu
        this.imgCheck = imgCheck

        viewModel.clickedMenuPosition = position

        val wrapper = ContextThemeWrapper(applicationContext, R.style.MyPopupMenu)
        val menu = PopupMenu(wrapper, imgMenu)
        menu.menuInflater.inflate(R.menu.menu_edit_workout, menu.menu)
        menu.gravity = Gravity.END
        menu.setOnMenuItemClickListener(this)
        menu.show()
    }

    override fun onWorkoutClick(position: Int) {
        if (viewModel.isEditing) {
            return
        }
        editDone()
        viewModel.clickedPosition = position
        val intent = Intent(applicationContext, ExerciseActivity::class.java).putExtra("id", viewModel.workouts[position].id)
                .putExtra("name", viewModel.workouts[position].name)
                .putExtra("workout", viewModel.workouts[position])
        fragmentActivity.startActivity(intent)
    }

    override fun onMenuClick(position: Int, imgMenu: AppCompatImageView, imgCheck: AppCompatImageView) {
        editDone()
        this.imgMenu = imgMenu
        this.imgCheck = imgCheck

        viewModel.clickedMenuPosition = position

        val wrapper = ContextThemeWrapper(applicationContext, R.style.MyPopupMenu)
        val menu = PopupMenu(wrapper, imgMenu)
        menu.menuInflater.inflate(R.menu.menu_edit_workout, menu.menu)
        menu.gravity = Gravity.END
        menu.setOnMenuItemClickListener(this)
        menu.show()
    }

    override fun onCheckClick(position: Int, imgMenu: AppCompatImageView, imgCheck: AppCompatImageView) {
        imgMenu.visibility = View.VISIBLE
        imgCheck.visibility = View.GONE

        viewModel.clickedMenuPosition = position

        editDone()
    }

    private fun editWorkout() {
        viewModel.isEditing = true
        imgMenu.visibility = View.GONE
        imgCheck.visibility = View.VISIBLE

        val view: LinearLayoutCompat = imgMenu.parent as LinearLayoutCompat
        edtWorkout = view.findViewById(R.id.edt_workout_name)
        edtWorkout.focusable = View.FOCUSABLE
        edtWorkout.isFocusableInTouchMode = true
        edtWorkout.isCursorVisible = true
        edtWorkout.requestFocus()
        edtWorkout.setOnEditorActionListener(this)
        edtWorkout.filters = arrayOf<InputFilter>(LengthFilter(25))
        edtWorkout.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS or InputType.TYPE_CLASS_TEXT
        edtWorkout.setText(viewModel.workouts[viewModel.clickedMenuPosition].name)
        edtWorkout.setSelection(edtWorkout.text.toString().length)
        //                edtWorkout.addTextChangedListener(Util.WorkoutTextChangedListener(viewModel.workouts[viewModel.clickedMenuPosition], edtWorkout, this))

        val imm = applicationContext.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        viewModel.currId = viewModel.workouts[viewModel.clickedMenuPosition].id
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "onEditorAction: ")
        editDone()
        return true
    }

    override fun beforeTextChanged(text: String, workout: Workout) {
        if (text[text.length - 1] != ' ') {
            val name = text.subSequence(0, text.length - 1)
                    .toString() + " " + text[text.length - 1]
            edtWorkout.setText(name)
        }
    }

    override fun onTextChanged(text: String, workout: Workout) {
        Log.d(TAG, "onWorkoutTextChanged: ")

        viewModel.currId = workout.id
        viewModel.updatedName = text
    }

    private fun editDone() {
//        if (!this::imgMenu.isInitialized || !this::edtWorkout.isInitialized) {
        if (!viewModel.isEditing) {
            return
        }
        viewModel.isEditing = false
        imgMenu.visibility = View.VISIBLE
        imgCheck.visibility = View.GONE

        edtWorkout.focusable = View.NOT_FOCUSABLE
        edtWorkout.isFocusableInTouchMode = false
        edtWorkout.isCursorVisible = false
        edtWorkout.filters = arrayOf<InputFilter>(LengthFilter(50))
        edtWorkout.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT

        //hide keyboard
        /*fragmentActivity.currentFocus?.let { view ->
            val imm = fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }*/

        val imm = fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
//        viewModel.updateName(viewModel.currId, viewModel.updatedName)
        viewModel.updateName(viewModel.currId, edtWorkout.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        editDone()
        viewModel.adapter = null
    }

    private fun dump() {

    }
}