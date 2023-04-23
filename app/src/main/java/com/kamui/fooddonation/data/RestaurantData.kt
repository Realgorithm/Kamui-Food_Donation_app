package com.kamui.fooddonation.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class RestaurantData(
    val role: String? = "",
    val name: String? = "",
    val email: String? = "",
    val location: GeoPoint? = null,
    val phone: String? = "",
    val restaurantName: String? = "",
    val cuisine: String? = "",
    val foodType: String? = "",
    val pickTime: String? = "",
    val password: String? = ""
    // Add more fields as needed
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(GeoPoint::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(role)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeValue(location)
        parcel.writeString(phone)
        parcel.writeString(restaurantName)
        parcel.writeString(cuisine)
        parcel.writeString(foodType)
        parcel.writeString(pickTime)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantData> {
        override fun createFromParcel(parcel: Parcel): RestaurantData {
            return RestaurantData(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantData?> {
            return arrayOfNulls(size)
        }
    }
}




