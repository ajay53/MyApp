package com.goazzi.workoutmanager.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_workout")
data class Workout(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    val id: String,
    val name: String,
    val timeStamp: Long
)


