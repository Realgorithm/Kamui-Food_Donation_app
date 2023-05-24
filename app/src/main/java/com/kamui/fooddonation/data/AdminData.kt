package com.kamui.fooddonation.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class AdminData(
    val role: String? ="",
    val id: String?="",
    val fcmToken: String?="",
    val imageUri: String? ="",
    val name: String? ="",
    val email: String? ="",
    val address: String? ="",
    val location: GeoPoint? = null,
    val phone: String? ="",
    val designation: String? ="",
    val adminAccess: String? ="",
    val password: String? ="",
    // Add more fields as needed
):Parcelable
{
    constructor(parcel: Parcel):this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(GeoPoint::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(role)
        parcel.writeString(id)
        parcel.writeString(fcmToken)
        parcel.writeString(imageUri)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeValue(location)
        parcel.writeString(phone)
        parcel.writeString(designation)
        parcel.writeString(adminAccess)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdminData> {
        override fun createFromParcel(parcel: Parcel): AdminData {
            return AdminData(parcel)
        }

        override fun newArray(size: Int): Array<AdminData?> {
            return arrayOfNulls(size)
        }
    }

}
