package com.kamui.fooddonation.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import java.security.Timestamp
import java.util.Date
import kotlin.String as String

data class Donation(
    var donationId: String? ="",
    val donor: String? ="",
    val donorId: String? ="",
    var donorAddress: String? ="",
    val receiver: String? ="",
    val receiverId: String? ="",
    var receiverAddress: String? ="",
    var pickupAddress: GeoPoint? =null,
    val destAddress: GeoPoint? =null,
    var status: String? ="",
    var claimedBy: String? ="",
    var volunteerId: String? ="",
    val foodName: String? ="",
    val foodQuantity: String? ="",
    val foodType: String? ="",
    val expiryDate: String? ="",
    var pickupDate: Date? = null
):Parcelable
 {
     constructor(parcel: Parcel) : this(
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readParcelable(GeoPoint::class.java.classLoader),
         parcel.readParcelable(GeoPoint::class.java.classLoader),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readString(),
         parcel.readParcelable(Timestamp::class.java.classLoader)
     ) {
  }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(donationId)
         parcel.writeString(donor)
         parcel.writeString(donorId)
         parcel.writeString(donorAddress)
         parcel.writeString(receiver)
         parcel.writeString(receiverId)
         parcel.writeString(receiverAddress)
         parcel.writeValue(pickupAddress)
         parcel.writeValue(destAddress)
         parcel.writeString(status)
         parcel.writeString(claimedBy)
         parcel.writeString(volunteerId)
         parcel.writeString(foodName)
         parcel.writeString(foodQuantity)
         parcel.writeString(foodType)
         parcel.writeString(expiryDate)
         parcel.writeValue(pickupDate) // Write as Long
  }

  override fun describeContents(): Int {
   return 0
  }

  companion object CREATOR : Parcelable.Creator<Donation> {
   override fun createFromParcel(parcel: Parcel): Donation {
    return Donation(parcel)
   }

   override fun newArray(size: Int): Array<Donation?> {
    return arrayOfNulls(size)
   }
  }
 }

