package com.goazi.workoutmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_session")
data class Session(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val workTime: Long,
    val restTime: Long,
    val timeStamp: Long,
    val exerciseId: String
)