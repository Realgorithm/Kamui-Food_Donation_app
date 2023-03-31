package com.kamui.fooddonation.volunteer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.R


class VolHomeFragment : Fragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.vol_fragment_home, container, false)

            // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        donationAdapter = DonationAdapter(requireContext(), donationsList, this)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = donationAdapter

        // Load the donations from the server
        loadDonations()

        // Set the listener for the "Claim" button in the adapter
        donationAdapter.setOnClaimClickListener(object : DonationAdapter.OnClaimClickListener {
            override fun onClaimClick(position: Int) {
                // Get the selected donation
                val selectedDonation = donationsList[position]

                // Update the status of the donation to "claimed"
                selectedDonation.status = "claimed"

                // Set the claimedBy field to the current user's ID
                selectedDonation.ClaimedBy = getUserId()

                // Remove the claimed donation from the list
//                donationsList.removeAt(position)

                // Update the UI
//                donationAdapter.notifyItemRemoved(position)
                donationAdapter.notifyItemChanged(position)
//                donationAdapter.notifyItemRangeChanged(position, donationsList.size)

                // Assign the task to the volunteer
                assignTask(selectedDonation)

            }
        })

        donationAdapter.setOnDeliverClickListener(object : DonationAdapter.OnDeliverClickListener {
            override fun onDeliverClick(position: Int) {
                val selectedDonation = donationsList[position]
                selectedDonation.status = "delivered"
                donationsList.removeAt(position)
                donationAdapter.notifyItemRemoved(position)
                donationAdapter.notifyItemChanged(position)
            }
        })

        return view
    }

    private fun getUserId(): String {
// TODO: Implement code to get the ID of the current user
        return "user123"
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadDonations() {
        // TODO: Implement code to load donations from the server

        // Add some dummy data for testing purposes
        donationsList.add(Donation("Food", "John Doe", "restaurant","123 Main St","789 Oak st", "Available"))
        donationsList.add(Donation("Clothes", "Jane Smith", "ngo","456 Elm St","123 Main St", "Available"))
        donationsList.add(Donation("Toys", "Bob Johnson","volunteer", "789 Oak St","56 Elm St", "Available"))

        // Notify the adapter that the data has changed
        donationAdapter.notifyDataSetChanged()
    }
    private fun assignTask(donation: Donation) {
        // TODO: Implement code to assign the task to the volunteer
        Toast.makeText(requireContext(),"Claimed",Toast.LENGTH_SHORT).show()

    }
}



