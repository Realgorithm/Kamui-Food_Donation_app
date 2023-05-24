package com.kamui.fooddonation.volunteer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.R
import com.kamui.fooddonation.admin.AdminDonationFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.ngo.DonationsFragment
import com.kamui.fooddonation.ngo.RecordFragment
import com.kamui.fooddonation.receiver.RcClaimedFragment
import com.kamui.fooddonation.receiver.RcHomeFragment
import com.kamui.fooddonation.restaurant.AcceptedFragment
import com.kamui.fooddonation.restaurant.ApprovedFragment
import com.kamui.fooddonation.restaurant.HomeFragment
import com.kamui.fooddonation.restaurant.PendingFragment

class DonationAdapter(
    context:Context,
    private var donationsList: ArrayList<Donation>,
    private val fragment: Fragment?,

    ) : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    // Define a listener for the "Claim" button
    private var onClaimClickListener: OnClaimClickListener? = null
    private var onTrackClickListener: OnTrackClickListener? = null

    // Interface for the listener
    interface OnClaimClickListener {
        fun onClaimClick(position: Int)
    }
    interface OnTrackClickListener {
        fun onTrackClick(position: Int)
    }
    // Method to set the listener
    fun setOnClaimClickListener(listener: OnClaimClickListener) {
        onClaimClickListener = listener
    }
    fun setOnTrackClickListener(listener: OnTrackClickListener) {
        onTrackClickListener = listener
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    private fun configureForAdminDonationList(holder: ViewHolder, donation: Donation){
        holder.itemView.visibility = View.GONE
        holder.trackButton.visibility = View.GONE
        holder.claimButton.visibility = View.GONE
        if(donation.status == "unapproved"){
            holder.itemView.visibility=View.VISIBLE
            holder.donorTextView.text =        "Donor:                 ${donation.donor}"
            holder.pickupAddressTextView.text ="Pickup:                ${donation.donorAddress}"
            holder.foodNameTextView.text =     "Food name:       ${donation.foodName}"
            holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
            holder.foodTypeTextView.text =     "Food Type:         ${donation.foodType}"
            holder.expiryDateTextView.text =   "Expiry Date:       ${donation.expiryDate}"
            holder.statusTextView.text =       "Status:                 Unapproved"
            holder.receiverTextView.visibility =View.GONE
            holder.distance.visibility = View.GONE
            holder.destAddressTextView.visibility = View.GONE
            holder.claimedBy.visibility = View.GONE
        }
    }
    @SuppressLint("SetTextI18n")
    private fun configureForRcHomeFragment(holder: ViewHolder, donation: Donation) {
        // Display only available donations within the maximum distance
        holder.itemView.visibility = View.GONE
        if (donation.status == "approved") {
            holder.itemView.visibility = View.VISIBLE
            holder.donorTextView.text =        "Donor:                 ${donation.donor}"
            holder.pickupAddressTextView.text ="Address:             ${donation.donorAddress}"
            holder.foodNameTextView.text =     "Food name:       ${donation.foodName}"
            holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
            holder.foodTypeTextView.text =     "Food Type:         ${donation.foodType}"
            holder.expiryDateTextView.text =   "Expiry Date:       ${donation.expiryDate}"
            holder.statusTextView.text =       "Status:                 Available"
            holder.receiverTextView.visibility =View.GONE
            holder.distance.visibility = View.GONE
            holder.destAddressTextView.visibility = View.GONE
            holder.claimedBy.visibility = View.GONE
            holder.trackButton.visibility=View.GONE
//        holder.distance.text = "Distance: ${
//            donation.location?.let {
//                donation.getDistanceFromReceiver(
//                    it
//                )
//            }
//        } meters"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureForVolHomeFragment(holder: ViewHolder, donation: Donation) {
        holder.itemView.visibility = View.GONE
        holder.distance.visibility=View.GONE
        holder.trackButton.visibility = View.GONE
        if (donation.status == "availableForVol") {
            holder.itemView.visibility = View.VISIBLE
            holder.claimButton.visibility = View.VISIBLE
            holder.claimButton.isEnabled = true
            holder.claimButton.text = "Claim"
            holder.foodNameTextView.text =     "Food name:      ${donation.foodName}"
            holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
            holder.donorTextView.text =        "Donor:                ${donation.donor}"
            holder.pickupAddressTextView.text ="Pickup:               ${donation.donorAddress}"
            holder.receiverTextView.text =     "Receiver:           ${donation.receiver}"
            holder.destAddressTextView.text =  "Drop:                  ${donation.receiverAddress}"
            holder.statusTextView.text =       "Status:                Available"
            holder.foodTypeTextView.visibility =View.GONE
            holder.distance.visibility = View.GONE
            holder.expiryDateTextView.visibility = View.GONE
            holder.claimedBy.visibility = View.GONE
        }
        else if (donation.status == "claimedByVol") {
            if (donation.claimedBy != "") {
                holder.itemView.visibility = View.VISIBLE
                holder.foodNameTextView.text =     "Food name:      ${donation.foodName}"
                holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
                holder.donorTextView.text =        "Donor:                ${donation.donor}"
                holder.pickupAddressTextView.text ="Pickup:               ${donation.donorAddress}"
                holder.receiverTextView.text =     "Receiver:           ${donation.receiver}"
                holder.destAddressTextView.text =  "Drop:                  ${donation.receiverAddress}"
                holder.statusTextView.text =       "Status:                Claimed"
                holder.claimedBy.text =            "Claimed by:      ${donation.claimedBy}"
                holder.claimButton.text = "Claimed"
                holder.claimButton.isEnabled=false
                holder.foodTypeTextView.visibility = View.GONE
                holder.expiryDateTextView.visibility = View.GONE
            } else {
                holder.claimedBy.visibility = View.GONE
            }
        } else {
            holder.claimedBy.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureForTrackFragment(holder: ViewHolder, donation: Donation) {
        holder.distance.visibility=View.GONE
        holder.claimButton.visibility = View.GONE
        if (donation.status == "claimedByVol") {
            if (donation.claimedBy != "") {
                holder.itemView.visibility = View.VISIBLE
                holder.foodNameTextView.text =     "Food name:      ${donation.foodName}"
                holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
                holder.donorTextView.text =        "Donor:                ${donation.donor}"
                holder.pickupAddressTextView.text ="Pickup:               ${donation.donorAddress}"
                holder.receiverTextView.text =     "Receiver:           ${donation.receiver}"
                holder.destAddressTextView.text =  "Drop:                  ${donation.receiverAddress}"
                holder.statusTextView.text =       "Status:                Claimed"
                holder.claimedBy.text =            "Claimed by:      ${donation.claimedBy}"
                holder.foodTypeTextView.visibility = View.GONE
                holder.expiryDateTextView.visibility = View.GONE
            } else {
                holder.claimedBy.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureForPendingDonationList(holder: DonationAdapter.ViewHolder, donation: Donation) {
        holder.itemView.visibility = View.GONE
        holder.trackButton.visibility = View.GONE
        holder.claimButton.visibility = View.GONE
        if(donation.status == "unapproved"){
            holder.itemView.visibility=View.VISIBLE
            holder.donorTextView.text =        "Donor:                 ${donation.donor}"
            holder.pickupAddressTextView.text ="Pickup:                ${donation.donorAddress}"
            holder.foodNameTextView.text =     "Food name:       ${donation.foodName}"
            holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
            holder.foodTypeTextView.text =     "Food Type:         ${donation.foodType}"
            holder.expiryDateTextView.text =   "Expiry Date:       ${donation.expiryDate}"
            holder.statusTextView.text =       "Status:                 Unapproved"
            holder.receiverTextView.visibility =View.GONE
            holder.distance.visibility = View.GONE
            holder.destAddressTextView.visibility = View.GONE
            holder.claimedBy.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureForApprovedDonationList(holder: DonationAdapter.ViewHolder, donation: Donation) {
        holder.itemView.visibility = View.GONE
        holder.trackButton.visibility = View.GONE
        holder.claimButton.visibility = View.GONE
        if(donation.status == "approved"){
            holder.itemView.visibility=View.VISIBLE
            holder.donorTextView.text =        "Donor:                 ${donation.donor}"
            holder.pickupAddressTextView.text ="Pickup:                ${donation.donorAddress}"
            holder.foodNameTextView.text =     "Food name:       ${donation.foodName}"
            holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
            holder.foodTypeTextView.text =     "Food Type:         ${donation.foodType}"
            holder.expiryDateTextView.text =   "Expiry Date:       ${donation.expiryDate}"
            holder.statusTextView.text =       "Status:                 approved"
            holder.receiverTextView.visibility =View.GONE
            holder.distance.visibility = View.GONE
            holder.destAddressTextView.visibility = View.GONE
            holder.claimedBy.visibility = View.GONE
        }
    }
    @SuppressLint("SetTextI18n")
    private fun configureForAcceptedFragment(holder: ViewHolder, donation: Donation) {
        holder.distance.visibility=View.GONE
        holder.trackButton.visibility = View.GONE
        holder.claimButton.visibility = View.GONE
        holder.itemView.visibility = View.GONE
        if (donation.status == "availableForVol") {
            holder.itemView.visibility = View.VISIBLE
            holder.foodNameTextView.text =     "Food name:      ${donation.foodName}"
            holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
            holder.donorTextView.text =        "Donor:                ${donation.donor}"
            holder.pickupAddressTextView.text ="Pickup:               ${donation.donorAddress}"
            holder.receiverTextView.text =     "Receiver:           ${donation.receiver}"
            holder.destAddressTextView.text =  "Drop:                  ${donation.receiverAddress}"
            holder.statusTextView.text =       "Status:                Available"
            holder.foodTypeTextView.visibility =View.GONE
            holder.distance.visibility = View.GONE
            holder.expiryDateTextView.visibility = View.GONE
            holder.claimedBy.visibility = View.GONE

        } else if (donation.status == "claimedByVol") {
            if (donation.claimedBy != "") {
                holder.itemView.visibility = View.VISIBLE
                holder.foodNameTextView.text =     "Food name:      ${donation.foodName}"
                holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
                holder.donorTextView.text =        "Donor:                ${donation.donor}"
                holder.pickupAddressTextView.text ="Pickup:               ${donation.donorAddress}"
                holder.receiverTextView.text =     "Receiver:           ${donation.receiver}"
                holder.destAddressTextView.text =  "Drop:                  ${donation.receiverAddress}"
                holder.statusTextView.text =       "Status:                Claimed"
                holder.claimedBy.text =            "Claimed by:      ${donation.claimedBy}"
                holder.foodTypeTextView.visibility = View.GONE
                holder.expiryDateTextView.visibility = View.GONE
            } else {
                holder.claimedBy.visibility = View.GONE
            }
        } else {
            holder.claimedBy.visibility = View.GONE
        }
    }
    @SuppressLint("SetTextI18n")
    private fun configureForRcClaimedFragment(holder:ViewHolder, donation: Donation) {
        holder.itemView.visibility = View.GONE
        holder.distance.visibility=View.GONE
        holder.trackButton.visibility = View.GONE
        if (donation.status != "") {
            holder.itemView.visibility = View.VISIBLE
            holder.foodNameTextView.text =     "Food name:      ${donation.foodName}"
            holder.foodQuantityTextView.text = "Food Quantity: ${donation.foodQuantity}"
            holder.donorTextView.text =        "Donor:                ${donation.donor}"
            holder.pickupAddressTextView.text ="Pickup:               ${donation.donorAddress}"
            holder.receiverTextView.text =     "Receiver:           ${donation.receiver}"
            holder.destAddressTextView.text =  "Drop:                  ${donation.receiverAddress}"
            holder.claimedBy.text =            "Claimed by:      ${donation.claimedBy}"
            holder.statusTextView.text =       "Status:                claimed"
            holder.foodTypeTextView.visibility =View.GONE
            holder.claimButton.visibility = View.GONE
            holder.distance.visibility = View.GONE
            holder.expiryDateTextView.visibility = View.GONE
        }
    }


    @SuppressLint("SetTextI18n")
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donation = donationsList[position]
        when(fragment) {
            is VolHomeFragment -> configureForVolHomeFragment(holder, donation)
            is TrackFragment -> configureForTrackFragment(holder, donation)
            is RcHomeFragment -> configureForRcHomeFragment(holder, donation)
            is AdminDonationFragment -> configureForAdminDonationList(holder, donation)
            is PendingFragment ->configureForPendingDonationList(holder,donation)
            is ApprovedFragment ->configureForApprovedDonationList(holder,donation)
            is AcceptedFragment ->configureForAcceptedFragment(holder, donation)
            is RcClaimedFragment -> configureForRcClaimedFragment(holder,donation)
            is DonationsFragment -> configureForRcHomeFragment(holder,donation)
            is RecordFragment ->configureForRcClaimedFragment(holder,donation)
            is HomeFragment ->configureForPendingDonationList(holder,donation)
        }

        holder.claimButton.setOnClickListener {
            onClaimClickListener?.onClaimClick(position)
        }

        holder.trackButton.setOnClickListener {
            onTrackClickListener?.onTrackClick(position)
        }
        // ...
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = donationsList.size
    @SuppressLint("NotifyDataSetChanged")
    fun updateDonations(newDonationsList: List<Donation>) {
        donationsList = newDonationsList as ArrayList<Donation>
        notifyDataSetChanged()
    }


    // Define the ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val donorTextView: TextView = view.findViewById(R.id.donor_name)
        val receiverTextView: TextView = view.findViewById(R.id.receiver_name)
        val distance : TextView = view.findViewById(R.id.distance)
        val pickupAddressTextView: TextView = view.findViewById(R.id.donation_pickup_address)
        val destAddressTextView: TextView = view.findViewById(R.id.donation_dest_address)
        val statusTextView: TextView = view.findViewById(R.id.donation_status)
        val claimedBy: TextView = view.findViewById(R.id.claimed_by)
        val foodNameTextView: TextView = view.findViewById(R.id.donation_food_name)
        val foodQuantityTextView: TextView = view.findViewById(R.id.food_quantity)
        val foodTypeTextView:TextView = view.findViewById(R.id.food_type)
        val expiryDateTextView:TextView = view.findViewById(R.id.expiryDate)
//        val emptyView:TextView =view.findViewById(R.id.empty_view)
        val claimButton: Button = view.findViewById(R.id.claim_button)
        val trackButton: Button = view.findViewById(R.id.track_button)
    }
}