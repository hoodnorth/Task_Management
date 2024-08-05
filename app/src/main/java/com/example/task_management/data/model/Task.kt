package com.example.task_management.data.model

import android.os.Parcel
import android.os.Parcelable

data class Task(
    val id: String = "",
    val taskName: String = "",
    val startDate: String = "",
    val deadlineDate: String = "",
    val description: String = "",
    val status: String = "",
    val priority: String = "",
    val category: String = "",
    val additionalNotes: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(taskName)
        parcel.writeString(startDate)
        parcel.writeString(deadlineDate)
        parcel.writeString(description)
        parcel.writeString(priority)
        parcel.writeString(status)
        parcel.writeString(category)
        parcel.writeString(additionalNotes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}
