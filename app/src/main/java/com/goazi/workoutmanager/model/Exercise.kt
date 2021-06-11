package com.goazi.workoutmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val exerciseName: String?,
    val workoutId: Int?
)