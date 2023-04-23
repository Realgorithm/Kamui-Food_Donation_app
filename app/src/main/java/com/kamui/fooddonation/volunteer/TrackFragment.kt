package com.kamui.fooddonation.volunteer

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.admin.BassFragment
import com.kamui.fooddonation.data.Donation
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.IOException
import java.util.Locale

class TrackFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter
    private var receiverMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_track, container, false)
        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        donationAdapter = DonationAdapter(requireContext(),donationsList, this)

        // Load the donations from the server
//        loadMyDonations()
        
        val currentUserUid = FireStoreClass().getCurrentUserID()
        FirebaseFirestore.getInstance().collection("donations")
            .whereEqualTo("donorId", currentUserUid)
            .whereEqualTo("status", "claimedByVol")
            .get()
            .addOnSuccessListener {
                Log.d("PendingData", currentUserUid)
                getDonationList()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error querying donations", e)
            }

        // Set the listener for the "Claim" button in the adapter
        donationAdapter.setOnTrackClickListener(object : DonationAdapter.OnTrackClickListener {
            @SuppressLint("MissingInflatedId", "SetTextI18n")
            override fun onTrackClick(position: Int) {
                // Get the selected donation
                val selectedDonation = donationsList[position]
                val geocoder = Geocoder(requireContext())
                val donorAddress = selectedDonation.pickupAddress
                val receiverAddress = selectedDonation.destAddress

                if (donorAddress != null && receiverAddress != null) {

                    val donorLocation = GeoPoint(donorAddress.latitude, donorAddress.longitude)
                    val receiverLocation = GeoPoint(receiverAddress.latitude, receiverAddress.longitude)
                    val dialogView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_map, null)

                    var mapView = dialogView.findViewById(R.id.map) as MapView
                    updateReceiverMarker(receiverLocation,mapView)
                    val mapController = mapView.controller
                    val closeButton = dialogView.findViewById<Button>(R.id.closeButton)
                    val deliverButton = dialogView.findViewById<Button>(R.id.deliverButton)
                    deliverButton.visibility = View.GONE // Hide the button by default

                    val builderAlert = AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setCancelable(false)
                    val dialog = builderAlert.create()

                    mapView = dialogView.findViewById(R.id.map)
                    mapView.setTileSource(TileSourceFactory.MAPNIK)
                    mapView.setBuiltInZoomControls(true)

                    mapController.setZoom(16.0)

                    // Get the user's current location
                    val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val locationProvider = LocationManager.NETWORK_PROVIDER
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // Request location permission if not granted
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
                        return
                    }
                    locationManager.requestLocationUpdates(locationProvider, 1000L, 10f, object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            val userLocation = GeoPoint(location.latitude, location.longitude)
                            updateReceiverMarker(userLocation, mapView)
                        }
                        @Deprecated("Deprecated in Java")
                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    })


//                    // Get the user's current location
//                    val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                    val locationProvider = LocationManager.NETWORK_PROVIDER
//                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // Request location permission if not granted
//                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
//                        return
//                    }
//                    val location = locationManager.getLastKnownLocation(locationProvider)
//
//                    if (location != null) {
//                        // Add a marker for the user's location
//                        val userLocation = GeoPoint(location.latitude, location.longitude)
//                        Log.d("volunteerLocation",userLocation.toDoubleString())
//                        val userMarker = Marker(mapView)
//                        userMarker.position = userLocation
//                        userMarker.title = "You are here"
//                        mapView.overlays.add(userMarker)
//
//                        // Add a Polyline overlay to show the path between the user and donor locations
//                        val polyline = Polyline()
//                        polyline.addPoint(userLocation)
//                        polyline.addPoint(donorLocation)
//                        mapView.overlays.add(polyline)
//
//                        // Move map to show all markers and polyline
//                        val mapControllerBounds = BoundingBox.fromGeoPoints(listOf(userLocation, donorLocation))
//                        mapController.animateTo(mapControllerBounds.centerWithDateLine, 18.0, 1000L)
//
//                        // Check if the user's location is the same as the receiver location
//                        if (userLocation.distanceToAsDouble(receiverLocation) < 5000) { // Distance threshold is 5000 meters
//                            // Show the deliver button
//                            deliverButton.visibility = View.VISIBLE
//                        }
//                    } else {
//                        // Handle the case where the user's location could not be determined
//                    }
                    mapView?.post {
                        // Add markers for the donor and receiver locations
                        val donorMarker = Marker(mapView)
                        donorMarker.position = donorLocation
                        donorMarker.title = "Donor"
                        mapView.overlays.add(donorMarker)

                        val receiverMarker = Marker(mapView)
                        receiverMarker.position = receiverLocation
                        receiverMarker.title = "Receiver"
                        mapView.overlays.add(receiverMarker)

                        // Add a Polyline overlay to show the path between the donor and receiver locations
                        val polyline = Polyline()
                        polyline.addPoint(donorLocation)
                        polyline.addPoint(receiverLocation)
                        mapView.overlays.add(polyline)

                        // Move map to show both markers
                        val mapControllerBounds = BoundingBox.fromGeoPoints(listOf(donorLocation, receiverLocation))
                        mapController.animateTo(mapControllerBounds.centerWithDateLine, 18.0, 1000L)

                        // Center the map on the midpoint between the two markers
                        val mapCenter = GeoPoint(
                            (donorLocation.latitude + receiverLocation.latitude) / 2,
                            (donorLocation.longitude + receiverLocation.longitude) / 2
                        )
                        mapController.setCenter(mapCenter)

                    }
                    closeButton.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                } else {
                    // Handle the case where the addresses could not be resolved
                }

                donationAdapter.notifyItemChanged(position)

                // Assign the task to the volunteer
//                assignTask(selectedDonation)
            }
        })



        return view
    }
    private fun updateReceiverMarker(receiverLocation: GeoPoint,mapView:MapView) {
        if (receiverMarker != null) {
            mapView.overlays.remove(receiverMarker)
        }
        receiverMarker = Marker(mapView)
        receiverMarker?.position = receiverLocation
        receiverMarker?.title = "Receiver"
        mapView.overlays.add(receiverMarker)
        mapView.invalidate()
    }

//    private fun assignTask(selectedDonation: Donation) {
////            val intent =Intent(requireContext(),Example::class.java)
////        StartActivity(intent)
//    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDonationList() {
        FireStoreClass().listenForDonationUpdates("claimedByVol",
            onUpdate = { donations ->
                // Update the adapter with the new donations data
//                donationsList.clear()
                for (donation in donations) {
                    // Fetch the address from the latitude and longitude using geocoder
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    var addresses: List<Address?>? = null
                    try {
                        addresses = geocoder.getFromLocation(donation.pickupAddress?.latitude!!,
                            donation.pickupAddress!!.longitude, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    // Update the donation object with the address
                    val address = addresses?.get(0)
                    donation.donorAddress = "${address?.locality ?: ""}, ${address?.subLocality ?: ""}"

                    // Fetch the address from the latitude and longitude using geocoder
                    var recAddresses: List<Address?>? = null
                    try {
                        recAddresses = geocoder.getFromLocation(donation.destAddress?.latitude!!,
                            donation.destAddress.longitude, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    // Update the donation object with the address
                    val recAddress = recAddresses?.get(0)
                    donation.receiverAddress = "${recAddress?.locality ?: ""}, ${recAddress?.subLocality ?: ""}"

                    // Add the updated donation to the list
                    donationsList.addAll(donations)
                }
                donationsList.addAll(donations)
                donationAdapter.updateDonations(donations)
                Log.d("DonationListUpdate",donationsList.toString())
                hideProgressDialog()
            },
            onError = { error ->
                // Handle the error, for example by displaying an error message
                Toast.makeText(requireContext(), "Error listening for donation updates", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error listening for donation updates", error)
            }
        )

//        FireStoreClass().getAllDonations { donations ->
//            // Update the donationsList with the retrieved data
//            donationsList.clear()
//            donationsList.addAll(donations)
//            hideProgressDialog()
//
//            // Notify the adapter that the data set has changed
//            recyclerViewDonations.adapter?.notifyDataSetChanged()
//        }
    }
    fun onBackPressed() {
        doubleBackToExit()
    }
}
