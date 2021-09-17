package com.goazzi.workoutmanager.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_session")
data class Session(@PrimaryKey(autoGenerate = false) val id: String, var workTime: Long, var restTime: Long, val timeStamp: Long, val exerciseId: String) :
    Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(workTime)
        parcel.writeLong(restTime)
        parcel.writeLong(timeStamp)
        parcel.writeString(exerciseId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Session> {
        override fun createFromParcel(parcel: Parcel): Session {
            return Session(parcel)
        }

        override fun newArray(size: Int): Array<Session?> {
            return arrayOfNulls(size)
        }
    }
}