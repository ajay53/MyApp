package com.goazzi.workoutmanager.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_exercise")
data class Exercise(@PrimaryKey(autoGenerate = false) val id: String, var timeStamp: Long, val exerciseName: String, val isCircuit: Int, val workoutId: String) :
    Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readLong(), parcel.readString()!!, parcel.readInt(), parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(timeStamp)
        parcel.writeString(exerciseName)
        parcel.writeInt(isCircuit)
        parcel.writeString(workoutId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exercise> {
        override fun createFromParcel(parcel: Parcel): Exercise {
            return Exercise(parcel)
        }

        override fun newArray(size: Int): Array<Exercise?> {
            return arrayOfNulls(size)
        }
    }
}