package com.goazzi.workoutmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_history")
data class History(@PrimaryKey(autoGenerate = false) val id: String, val name: String, val date: String, val startUnixTime: Long, var totalUnixTime: Long, val workoutId: String)


