package com.kamui.fooddonation.ngo

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import kotlin.String as String

data class Employee(
    var id: String? = "",
    var name: String? ="",
    var phone: String? ="",
    var imageUrl: String? =""
):Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Employee> {
        override fun createFromParcel(parcel: Parcel): Employee {
            return Employee(parcel)
        }

        override fun newArray(size: Int): Array<Employee?> {
            return arrayOfNulls(size)
        }
    }
}
