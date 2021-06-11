package com.goazi.workoutmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.goazi.workoutmanager.R
import com.goazi.workoutmanager.model.Workout

class WorkoutListAdapter(
    private val context: Context,
    private val workouts: MutableList<Workout>?,
    private val onWorkoutCLickListener: WorkoutListAdapter.OnWorkoutCLickListener
) : RecyclerView.Adapter<WorkoutListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_list_item, parent, false)

        return WorkoutListAdapter.ViewHolder(itemView, onWorkoutCLickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workouts?.get(position)
        holder.tvWorkout.text = workout?.name
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
        var tvWorkout: TextView = view.findViewById(R.id.tv_workout)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onWorkoutCLickListener.onWorkoutClick(bindingAdapterPosition)
        }
    }

    interface OnWorkoutCLickListener {
        fun onWorkoutClick(position: Int)
    }


}