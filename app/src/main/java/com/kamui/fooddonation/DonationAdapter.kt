package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonationAdapter(context: Context, private val donationsList: ArrayList<Donation>) : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    // Define a listener for the "Claim" button
    private var onClaimClickListener: OnClaimClickListener? = null

    // Interface for the listener
    interface OnClaimClickListener {
        fun onClaimClick(position: Int)
    }

    // Method to set the listener
    fun setOnClaimClickListener(listener: OnClaimClickListener) {
        onClaimClickListener = listener
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

        if (donation.status == "claimed") {
            if (donation.ClaimedBy != "") {
                holder.claimedBy.visibility = View.VISIBLE
                holder.claimedBy.text = "Claimed by: ${donation.ClaimedBy}"
            } else {
                holder.claimedBy.visibility = View.GONE
            }
            holder.claimButton.isEnabled = false
        } else {
            holder.claimedBy.visibility = View.GONE
            holder.claimButton.isEnabled = true
        }

        holder.claimButton.setOnClickListener {
            onClaimClickListener?.onClaimClick(position)
        }


        holder.claimButton.setOnClickListener {
            onClaimClickListener?.onClaimClick(position)
        }

        holder.titleTextView.text = donation.title
        holder.donorTextView.text = "Donor: ${donation.donor}"
        holder.addressTextView.text = "Address: ${donation.address}"
        holder.statusTextView.text = "Status: ${donation.status}"

        // Set the listener for the "Claim" button
        holder.claimButton.setOnClickListener {
            onClaimClickListener?.onClaimClick(position)
        }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount() = donationsList.size
    fun disableClaimButton(position: Int) {

    }

    // Provide a reference to the views for each data item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.donation_title)
        val donorTextView: TextView = itemView.findViewById(R.id.donor_name)
        val addressTextView: TextView = itemView.findViewById(R.id.donation_address)
        val statusTextView: TextView = itemView.findViewById(R.id.donation_status)
        val claimButton: Button = itemView.findViewById(R.id.claim_button)
        val claimedBy: TextView = itemView.findViewById(R.id.claimed_by)

    }
}

