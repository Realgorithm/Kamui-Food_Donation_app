package com.kamui.fooddonation.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class VolunteerData(
    val role: String? ="",
    val id: String? ="",
    val fcmToken:String? ="",
    val rewardPoint:String?="",
    val imageUri: String? ="",
    val name: String? ="",
    val email: String? ="",
    val address: String? = "",
    val location: GeoPoint? = null,
    val phone: String? ="",
    val availability: String? ="",
    val preferences: String? ="",
    val password: String? ="",
    // Add more fields as needed
): Parcelable
{
    constructor(parcel: Parcel):this(
        parcel.readString(),
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
        parcel.writeString(rewardPoint)
        parcel.writeString(imageUri)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeValue(location)
        parcel.writeString(phone)
        parcel.writeString(availability)
        parcel.writeString(preferences)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VolunteerData> {
        override fun createFromParcel(parcel: Parcel): VolunteerData {
            return VolunteerData(parcel)
        }

        override fun newArray(size: Int): Array<VolunteerData?> {
            return arrayOfNulls(size)
        }
    }

}


