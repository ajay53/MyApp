package com.goazi.workoutmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.goazi.workoutmanager.R
import com.goazi.workoutmanager.helper.Util
import com.goazi.workoutmanager.model.Workout

class WorkoutListAdapter(private val context: Context, private val workouts: MutableList<Workout>?, private val onWorkoutCLickListener: OnWorkoutCLickListener) :
    RecyclerView.Adapter<WorkoutListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.workout_list_item, parent, false)

        return ViewHolder(itemView, onWorkoutCLickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workouts!![position]
        val data = Util.getData(context, workout.id)
        holder.tvWorkoutName.text = Util.getSpacedText(workout.name)
        holder.tvExerciseCount.text = data[0]
        holder.tvSessionCount.text = data[1]
        holder.tvWorkTime.text = data[2]
        holder.tvRestTime.text = data[3]
        holder.tvTotalTime.text = data[4]

        if (position % 2 == 0) {
            holder.llWorkoutListItem.background = AppCompatResources.getDrawable(context, R.drawable.purple_stroke_background)
        } else {
            holder.llWorkoutListItem.background = AppCompatResources.getDrawable(context, R.drawable.blue_stroke_background)
        }

        val layoutParams: RecyclerView.LayoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        if (position == workouts.size - 1) {
            layoutParams.setMargins(0, 0, 0, 240)
        } else {
            layoutParams.setMargins(0, 0, 0, 60)
        }
        holder.llWorkoutListItem.layoutParams = layoutParams
    }

    fun updateList(workouts: List<Workout>) {
        this.workouts?.clear()
        this.workouts?.addAll(workouts)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (workouts == null) {
            return 0
        }
        return workouts.size
    }

    class ViewHolder(view: View, private val onWorkoutCLickListener: OnWorkoutCLickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var llWorkoutListItem: LinearLayoutCompat = view.findViewById(R.id.ll_workout_list_item)
        var tvWorkoutName: TextView = view.findViewById(R.id.tv_workout_name)
        var tvExerciseCount: TextView = view.findViewById(R.id.tv_exercise_count)
        var tvSessionCount: TextView = view.findViewById(R.id.tv_session_count)
        var tvWorkTime: TextView = view.findViewById(R.id.tv_work_time)
        var tvRestTime: TextView = view.findViewById(R.id.tv_rest_time)
        var tvTotalTime: TextView = view.findViewById(R.id.tv_total_time)
        private var imgMenu: AppCompatImageView = view.findViewById(R.id.img_menu)

        init {
            imgMenu.setOnClickListener { onWorkoutCLickListener.onMenuClick(bindingAdapterPosition) }
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onWorkoutCLickListener.onWorkoutClick(bindingAdapterPosition)
        }
    }

    interface OnWorkoutCLickListener {
        fun onWorkoutClick(position: Int)
        fun onMenuClick(position: Int)
    }
}