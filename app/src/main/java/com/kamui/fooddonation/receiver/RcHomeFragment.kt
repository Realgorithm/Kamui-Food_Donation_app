package com.kamui.fooddonation.receiver

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.ReceiverData
import com.kamui.fooddonation.volunteer.DonationAdapter
import java.io.IOException
import java.util.Locale

class RcHomeFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerViewDonations: RecyclerView
    private lateinit var donationListAdapter: DonationAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private lateinit var currentLocation: Location
    private lateinit var emptyView: TextView

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rc_home, container, false)

        showProgressDialog("Fetching Data")
        // Initialize the RecyclerView and the adapter
        recyclerViewDonations = view.findViewById(R.id.recycler_view_donations)
        emptyView = view.findViewById(R.id.empty_view)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerViewDonations.layoutManager = layoutManager

        donationListAdapter = DonationAdapter(requireContext(), donationsList, this)
        recyclerViewDonations.adapter = donationListAdapter

        // Initialize currentLocation
        currentLocation = Location("")

        // Request for location updates
        requestLocationUpdates()

        getDonationList()

        // Set the listener for the "Claim" button in the adapter
        donationListAdapter.setOnClaimClickListener(object : DonationAdapter.OnClaimClickListener {
            override fun onClaimClick(position: Int) {

                // Check if the position is within bounds
                if (position >= donationsList.size) {
                    Log.d("position", position.toString())
                    Log.d("donation Size", donationsList.size.toString())
                    return
                }
                Log.d("claim button", "Claimed Successfully")
                // Get the selected donation
                val selectedDonation = donationsList[position]

                // Check if the donation is already claimed
                if (selectedDonation.status == "availableForVol") {
                    // Show an error message and return
                    Toast.makeText(
                        requireContext(),
                        "This donation has already been claimed",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                // Set the claimedBy field to the current user's ID
                val receiverId = FireStoreClass().getCurrentUserID()


                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirm Approval")
                builder.setMessage("Are you sure you want to claim this donation?")
                builder.setPositiveButton("Yes") { _, _ ->
                    // Get the current user's display name
                    FireStoreClass().getUserData("receiver", ReceiverData::class.java,receiverId) { userData ->
                        val receiverName = userData?.name.toString()
                        val receiverAddress = userData?.location!!
                        // Fetch the address from the latitude and longitude using geocoder
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        var addresses: List<Address?>? = null
                        try {
                            addresses = geocoder.getFromLocation(receiverAddress.latitude,
                                receiverAddress.longitude, 1)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

//                     Update the donation object with the address
                        val address = addresses?.get(0)?.getAddressLine(0)!!

                        selectedDonation.donationId?.let {
                            FireStoreClass().updateDonationStatus(it, "availableForVol") {
                                FireStoreClass().updateReceiverInfo(
                                    selectedDonation.donationId!!, receiverName, receiverAddress,
                                    receiverId,address
                                ) {
                                    // Update the status of the donation to "availableForVol"
                                    selectedDonation.status = "availableForVol"
                                    Toast.makeText(
                                        requireContext(),
                                        "You have successfully claimed donation",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Remove the claimed donation from the list
//                                    donationsList.removeAt(position)

                                    // Update the UI
                                    donationListAdapter.notifyItemChanged(position)

                                }
                            }
                        }
                    }
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }

        })

        return view
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun getDonationList() {
        FireStoreClass().listenForDonationUpdates("approved",
            onUpdate = { donations ->
                // Clear the current donations list
                donationsList.clear()
                //Filter the donations based on the distance from the receiver's location
                val filteredDonations = ArrayList<Donation>()
                for (donation in donations) {
                    //get donation inside the 15 km
                    val donationLocation = Location("")
                    donationLocation.latitude = donation.pickupAddress?.latitude!!
                    donationLocation.longitude = donation.pickupAddress!!.longitude
                    val distance = currentLocation.distanceTo(donationLocation)
                    Log.d("Current",distance.toString())
                    // Filter out the donations that are outside the specified radius
                    if (distance <= 15000000) {
                        filteredDonations.add(donation)
                        Log.d("filtered",filteredDonations.toString())
                    }
                }
                // Update the adapter with the new donations data
                donationsList.addAll(filteredDonations)
                donationListAdapter.updateDonations(donationsList)
                // Check if the list is now empty
                if (donationsList.isEmpty()) {
                    // Show an empty view or a message indicating there are no donations
                    recyclerViewDonations.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerViewDonations.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
                hideProgressDialog()
            },
            onError = { error ->
                // Handle the error, for example by displaying an error message
                Toast.makeText(
                    requireContext(),
                    "Error listening for donation updates",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Firestore", "Error listening for donation updates", error)
            }
        )
    }

    private fun requestLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // Update currentLocation with the user's current location
                    currentLocation.latitude = location.latitude
                    currentLocation.longitude = location.longitude
                }
            }
        }

        // Create a location request and set the interval and priority
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Check if the user has granted location permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        // Request location updates using the location request and callback
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback!!, null)
    }
}