package com.kamui.fooddonation.volunteer

import android.os.Parcel
import android.os.Parcelable
import kotlin.String as String

class Donation(val title: String?, val donor: String?, val receiver: String?, val pickupAddress: String?,val destAddress: String?, var status: String?, var ClaimedBy: String? =""):Parcelable
 {
  constructor(parcel: Parcel) : this(
   parcel.readString(),
   parcel.readString(),
   parcel.readString(),
   parcel.readString(),
   parcel.readString(),
   parcel.readString(),
   parcel.readString()
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
   parcel.writeString(title)
   parcel.writeString(donor)
   parcel.writeString(receiver)
   parcel.writeString(pickupAddress)
   parcel.writeString(destAddress)
   parcel.writeString(status)
   parcel.writeString(ClaimedBy)
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

