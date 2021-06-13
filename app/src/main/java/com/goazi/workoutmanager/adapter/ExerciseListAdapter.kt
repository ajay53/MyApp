package com.goazi.workoutmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.goazi.workoutmanager.R
import com.goazi.workoutmanager.model.Exercise

class ExerciseListAdapter(
    private val context: Context,
    private val exercises: MutableList<Exercise>?,
    private val onExerciseCLickListener: OnExerciseCLickListener
) : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_list_item, parent, false)

        return ViewHolder(itemView, onExerciseCLickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currItem = exercises?.get(position)
        holder.tvName.text = currItem?.exerciseName
        onExerciseCLickListener.onExerciseAdded(position, holder.llSessions)
    }

    fun updateList(exercises: List<Exercise>) {
        this.exercises?.clear()
        this.exercises?.addAll(exercises)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (exercises == null) {
            return 0
        }
        return exercises.size
    }

    class ViewHolder(view: View, private val onExerciseCLickListener: OnExerciseCLickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var tvName: TextView = view.findViewById(R.id.tv_name)
        private var btnAddSession: Button = view.findViewById(R.id.btn_add_session)
        var llSessions: LinearLayoutCompat = view.findViewById(R.id.ll_sessions)

        init {
            btnAddSession.setOnClickListener(View.OnClickListener {
                onExerciseCLickListener.onSessionAddClick(bindingAdapterPosition, llSessions)
            })
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onExerciseCLickListener.onExerciseClick(bindingAdapterPosition)
        }
    }

    interface OnExerciseCLickListener {
        fun onExerciseClick(position: Int)
        fun onSessionAddClick(position: Int, llSessions: LinearLayoutCompat)
        fun onExerciseAdded(position: Int, llSessions: LinearLayoutCompat)
    }
}