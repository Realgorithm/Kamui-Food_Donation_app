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

class DonationAdapter(
    context: Context,
    private val donationsList: ArrayList<Donation>,
    private val fragment: Fragment?
) : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    // Define a listener for the "Claim" button
    private var onClaimClickListener: OnClaimClickListener? = null
    private var onDeliverClickListener: OnDeliverClickListener? = null

    // Interface for the listener
    interface OnClaimClickListener {
        fun onClaimClick(position: Int)
    }
    interface OnDeliverClickListener {
        fun onDeliverClick(position: Int)
    }
    // Method to set the listener
    fun setOnClaimClickListener(listener: OnClaimClickListener) {
        onClaimClickListener = listener
    }
    fun setOnDeliverClickListener(listener: OnDeliverClickListener) {
        onDeliverClickListener = listener
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donation = donationsList[position]
        if (fragment is VolHomeFragment) {
            holder.trackButton.visibility = View.GONE
            if (donation.status == "Available") {
                holder.claimButton.visibility = View.VISIBLE
                holder.deliverButton.visibility = View.GONE
                holder.claimedBy.visibility = View.GONE
            } else if (donation.status == "claimed") {
                if (donation.ClaimedBy != "") {
                    holder.claimedBy.visibility = View.VISIBLE
                    holder.claimButton.visibility = View.VISIBLE
                    holder.deliverButton.visibility = View.GONE
                    holder.claimButton.text = "Claimed"
                    holder.claimButton.isEnabled=false
                    holder.claimedBy.text = "Claimed by: ${donation.ClaimedBy}"
                } else {
                    holder.claimedBy.visibility = View.GONE
                }
            } else {
                holder.claimedBy.visibility = View.GONE
            }
        } else if (fragment is TrackFragment) {
            if (donation.status == "claimed") {
                holder.deliverButton.visibility = View.VISIBLE
                holder.claimButton.visibility = View.GONE
                holder.deliverButton.isEnabled = true
                holder.deliverButton.text = "Deliver"
            } else if (donation.status == "delivered") {
                holder.deliverButton.isEnabled = false
                holder.deliverButton.visibility = View.VISIBLE
                holder.claimButton.visibility = View.GONE
                holder.deliverButton.text = "Delivered"
            } else {
                holder.claimButton.visibility = View.GONE
                holder.deliverButton.visibility = View.GONE
                holder.claimedBy.visibility = View.GONE
            }
        }

        holder.titleTextView.text = donation.title
        holder.donorTextView.text = "Donor: ${donation.donor}"
        holder.pickupAddressTextView.text = "Pickup Address: ${donation.pickupAddress}"
        holder.destAddressTextView.text = "Drop Address: ${donation.destAddress}"
        holder.statusTextView.text = "Status: ${donation.status}"

        holder.claimButton.setOnClickListener {
            onClaimClickListener?.onClaimClick(position)
        }

        holder.deliverButton.setOnClickListener {
            onDeliverClickListener?.onDeliverClick(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = donationsList.size

    // Define the ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.donation_title)
        val donorTextView: TextView = view.findViewById(R.id.donor_name)
        val pickupAddressTextView: TextView = view.findViewById(R.id.donation_pickup_address)
        val destAddressTextView: TextView = view.findViewById(R.id.donation_dest_address)
        val statusTextView: TextView = view.findViewById(R.id.donation_status)
        val claimButton: Button = view.findViewById(R.id.claim_button)
        val deliverButton: Button = view.findViewById(R.id.deliver_button)
        val trackButton: Button = view.findViewById(R.id.track_button)
        val claimedBy: TextView = view.findViewById(R.id.claimed_by)
    }
}