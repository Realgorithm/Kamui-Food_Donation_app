package com.kamui.fooddonation.volunteer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.Example
import com.kamui.fooddonation.R

class TrackFragment : Fragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_track, container, false)

        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        donationAdapter = DonationAdapter(requireContext(),donationsList, this)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = donationAdapter

        // Load the donations from the server
        loadMyDonations()

        // Set the listener for the "Claim" button in the adapter
        donationAdapter.setOnTrackClickListener(object : DonationAdapter.OnTrackClickListener {
            override fun onTrackClick(position: Int) {
                // Get the selected donation
                val selectedDonation = donationsList[position]

                donationAdapter.notifyItemChanged(position)

                // Assign the task to the volunteer
                assignTask(selectedDonation)

            }
        })

        return view
    }

    private fun assignTask(selectedDonation: Donation) {
//            val intent =Intent(requireContext(),Example::class.java)
//        StartActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadMyDonations() {
        // TODO: Implement code to load donations claimed by the user from the server


        // Add some dummy data for testing purposes
        donationsList.add(Donation("Food", "John Doe", "restaurant","123 Main St","789 Oak St", "claimed","Volunteer1" ))
        donationsList.add(Donation("Toys", "Bob Johnson","volunteer", "789 Oak St","123 Main St", "claimed","Volunteer2"))

        // Notify the adapter that the data has changed
        donationAdapter.notifyDataSetChanged()
    }

}
