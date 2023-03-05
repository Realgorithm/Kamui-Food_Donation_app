package com.kamui.fooddonation.volunteer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.Donation
import com.kamui.fooddonation.DonationAdapter
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

        // Retrieve the claimed donation list passed from the VolHomeFragment
        val claimedDonationsList = arguments?.getParcelableArrayList<Donation>("donationsList")

        // If the list is not null, update the donationsList and notify the adapter
        if (claimedDonationsList != null) {
            donationsList.addAll(claimedDonationsList)
            donationAdapter.notifyDataSetChanged()
        }

        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        donationAdapter = DonationAdapter(requireContext(), donationsList)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = donationAdapter
//        loadMyDonations()
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadMyDonations() {
        // TODO: Implement code to load donations claimed by the user from the server


        // Add some dummy data for testing purposes
        donationsList.add(Donation("Food", "John Doe", "restaurant","123 Main St", "claimed","Volunteer1" ))
        donationsList.add(Donation("Toys", "Bob Johnson","volunteer", "789 Oak St", "claimed","Volunteer2"))

        // Notify the adapter that the data has changed
        donationAdapter.notifyDataSetChanged()
    }

}
