package com.kamui.fooddonation.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class NgoData(
    val role: String? ="",
    val name: String? ="",
    val email: String? ="",
    val location: GeoPoint? = null,
    val phone: String? ="",
    val ngoName: String?="",
    val foodType: String? ="",
    val organizationType: String? ="",
    val foodPickup: String? ="",
    val registration: String? ="",
    val password: String? ="",

    ): Parcelable
{
    constructor(parcel: Parcel):this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(GeoPoint::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(role)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeValue(location)
        parcel.writeString(phone)
        parcel.writeString(ngoName)
        parcel.writeString(foodType)
        parcel.writeString(organizationType)
        parcel.writeString(foodPickup)
        parcel.writeString(registration)
        parcel.writeString(password)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NgoData> {
        override fun createFromParcel(parcel: Parcel): NgoData {
            return NgoData(parcel)
        }

        override fun newArray(size: Int): Array<NgoData?> {
            return arrayOfNulls(size)
        }
    }

}

