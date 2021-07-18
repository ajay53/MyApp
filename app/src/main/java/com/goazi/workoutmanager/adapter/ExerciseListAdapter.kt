package com.goazi.workoutmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
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
        val isLast: Boolean = position == exercises!!.size - 1
        onExerciseCLickListener.onExerciseAdded(
            position,
            isLast,
            holder.llSessions
        )

        /*if (position % 2 == 0) {
            holder.exerciseListItem.background = AppCompatResources.getDrawable(context, R.drawable.green_fade_gradient)
        } else {
            holder.exerciseListItem.background = AppCompatResources.getDrawable(context, R.drawable.green_fade_gradient)
        }*/

        val layoutParams: RecyclerView.LayoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        if (position == exercises.size - 1) {
            layoutParams.setMargins(0, 0, 0, 280)
        } else {
            layoutParams.setMargins(0, 0, 0, 60)
        }
        holder.exerciseListItem.layoutParams = layoutParams
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
        var exerciseListItem: LinearLayoutCompat = view.findViewById(R.id.exercise_list_item)
        var tvName: TextView = view.findViewById(R.id.tv_name)
        private var imgMenu: AppCompatImageView = view.findViewById(R.id.img_menu)
        var llSessions: LinearLayoutCompat = view.findViewById(R.id.ll_sessions)

        init {
            imgMenu.setOnClickListener {
                onExerciseCLickListener.onMenuClick(
                    bindingAdapterPosition,
                    imgMenu,
                    llSessions
                )
            }
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onExerciseCLickListener.onExerciseClick(bindingAdapterPosition)
        }
    }

    interface OnExerciseCLickListener {
        fun onExerciseClick(position: Int)
        fun onMenuClick(position: Int, imgMenu: AppCompatImageView, llSessions: LinearLayoutCompat)
        fun onExerciseAdded(
            position: Int,
            isLast: Boolean,
            llSessions: LinearLayoutCompat
        )
    }
}