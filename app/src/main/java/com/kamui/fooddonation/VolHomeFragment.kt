package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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
        donationAdapter = DonationAdapter(requireContext(), donationsList)

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
                donationsList.removeAt(position)

                // Update the UI
                donationAdapter.notifyItemRemoved(position)

                // Assign the task to the volunteer
                assignTask(selectedDonation)

                // Pass the claimed donation list to the TrackFragment
//                val intent = Intent(requireContext(), TrackFragment::class.java)
//                intent.putParcelableArrayListExtra("donationsList", donationsList)
//                Log.d("VolHome","Status$intent")
//                startActivity(intent)
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
        donationsList.add(Donation("Food", "John Doe", "restaurant","123 Main St", "Available"))
        donationsList.add(Donation("Clothes", "Jane Smith", "ngo","456 Elm St", "available"))
        donationsList.add(Donation("Toys", "Bob Johnson","volunteer", "789 Oak St", "Available"))

        // Notify the adapter that the data has changed
        donationAdapter.notifyDataSetChanged()
    }
    private fun assignTask(donation: Donation) {
        // TODO: Implement code to assign the task to the volunteer
        Toast.makeText(requireContext(),"Claimed",Toast.LENGTH_SHORT).show()

    }
}



