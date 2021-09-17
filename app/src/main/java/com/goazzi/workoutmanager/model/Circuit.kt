package com.goazzi.workoutmanager.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_circuit")
data class Circuit(@PrimaryKey val id: String, val name: String, val exerciseId: String, val workoutId: String, val timeStamp: String, val loopVal: Int, val position: Int)

