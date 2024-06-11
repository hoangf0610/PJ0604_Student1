package com.example.pj0604_student1.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList


class Student(
    var img: String,
    var name: String,
    var age: Int,
    var point: Double = 0.0,
    var gender: String = "gender",
    var subject: Int = 0,
    var subjectArray: ArrayList<Subject> = ArrayList(),
    var rank: Int = 1,
    var classStudent: String = "A"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.createTypedArrayList(Subject.CREATOR) as ArrayList<Subject>,
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(img)
        parcel.writeString(name)
        parcel.writeInt(age)
        parcel.writeDouble(point)
        parcel.writeString(gender)
        parcel.writeInt(subject)
        parcel.writeTypedList(subjectArray)
        parcel.writeInt(rank)
        parcel.writeString(classStudent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }

}