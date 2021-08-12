package com.goazzi.workoutmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.model.Exercise
import java.util.*

class ExerciseListAdapter(private val context: Context, private val exercises: MutableList<Exercise>, private val onExerciseCLickListener: OnExerciseCLickListener) :
    RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.exercise_list_item, parent, false)

        return ViewHolder(itemView, onExerciseCLickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currItem = exercises[position]
        holder.tvName.text = currItem.exerciseName
        val isLast: Boolean = position == exercises.size - 1
        onExerciseCLickListener.onExerciseAdded(position, isLast, holder.llSessions)
    }

    fun add(exercise:Exercise, position: Int) {
        this.exercises.add(position, exercise)
        this.notifyItemInserted(position)
    }

    fun delete(position: Int) {
        this.exercises.removeAt(position)
        this.notifyItemRemoved(position)
    }

    fun swap(fromPosition: Int, toPosition: Int) {
        Collections.swap(this.exercises, fromPosition, toPosition)
        this.notifyItemMoved(fromPosition, toPosition)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ViewHolder(view: View, private val onExerciseCLickListener: OnExerciseCLickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
//        var exerciseListItem: LinearLayoutCompat = view.findViewById(R.id.exercise_list_item)
        var tvName: TextView = view.findViewById(R.id.tv_name)
        private var imgMenu: AppCompatImageView = view.findViewById(R.id.img_menu)
        private var imgCheck: AppCompatImageView = view.findViewById(R.id.img_check)
        var llSessions: LinearLayoutCompat = view.findViewById(R.id.ll_sessions)

        init {
            imgMenu.setOnClickListener {
                onExerciseCLickListener.onMenuClick(bindingAdapterPosition, imgMenu, imgCheck, llSessions)
            }
            imgCheck.setOnClickListener {
                onExerciseCLickListener.onCheckClick(bindingAdapterPosition, imgMenu, imgCheck, llSessions)
            }
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onExerciseCLickListener.onExerciseClick(bindingAdapterPosition)
        }
    }

    interface OnExerciseCLickListener {
        fun onExerciseClick(position: Int)
        fun onMenuClick(position: Int, imgMenu: AppCompatImageView, imgCheck: AppCompatImageView, llSessions: LinearLayoutCompat)
        fun onCheckClick(position: Int, imgMenu: AppCompatImageView, imgCheck: AppCompatImageView, llSessions: LinearLayoutCompat)
        fun onExerciseAdded(position: Int, isLast: Boolean, llSessions: LinearLayoutCompat)
    }
}