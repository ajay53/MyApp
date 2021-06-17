package com.goazi.workoutmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_session")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val workTime: Long?,
    val restTime: Long?,
    val exerciseId: String?
)