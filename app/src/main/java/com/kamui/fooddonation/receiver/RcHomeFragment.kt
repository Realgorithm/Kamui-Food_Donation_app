
package com.kamui.fooddonation.receiver

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.kamui.fooddonation.R
import com.kamui.fooddonation.volunteer.Donation
import com.kamui.fooddonation.volunteer.DonationAdapter

class RcHomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DonationAdapter
    private val donationsList = ArrayList<Donation>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rc_home, container, false)

        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = DonationAdapter(requireContext(), donationsList,this)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        // Get the user's current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                currentLocation = location
                // Load the donations from the server based on the user's current location
                loadDonations(currentLocation, 500.0)
            }
        }
        return view
    }

    private fun loadDonations(location: Location, radius: Double) {
        // TODO: Implement code to load donations from the server based on the user's current location

        // Add some dummy data for testing purposes
        donationsList.add(Donation("Food", "John Doe", "restaurant", "123 Main St", "789 Oak st", "Available"))
        donationsList.add(Donation("Clothes", "Jane Smith", "ngo", "456 Elm St", "123 Main St", "Available"))
        donationsList.add(Donation("Toys", "Bob Johnson", "volunteer", "789 Oak St", "56 Elm St", "Available"))

        // Filter the donations based on the distance from the receiver's location
        val filteredDonations = ArrayList<Donation>()
        for (donation in donationsList) {
            val donationLocation = Location("")
            donationLocation.latitude = donation.latitude
            donationLocation.longitude = donation.longitude
            Log.d("RcHome","DonationLocation: $donationLocation")
            Log.d("RcHome","Location: $location")
            val distance = location.distanceTo(donationLocation)
            Log.d("RcHome","Distance $distance")
            // Filter out the donations that are outside the specified radius
            if (distance <= radius.toFloat()) {
                filteredDonations.add(donation)
            }
        }
        // Update the adapter with the filtered donations
        adapter.updateDonations(filteredDonations)

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged()
    }
}
