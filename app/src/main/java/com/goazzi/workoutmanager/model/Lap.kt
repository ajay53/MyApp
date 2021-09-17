package com.goazzi.workoutmanager.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_lap")
data class Lap(@PrimaryKey val id: String, val name: String, val circuitId: String, val timeStamp: String, val time: Long)
