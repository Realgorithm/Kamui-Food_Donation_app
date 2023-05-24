package com.kamui.fooddonation.volunteer

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
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
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.Donation
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.IOException
import java.util.Locale

class TrackFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_track, container, false)

        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        showProgressDialog("Fetching Data")

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
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
                Log.w(TAG, "Error querying donations", e)
            }

        donationAdapter = DonationAdapter(requireContext(), donationsList,this)
        recyclerView.adapter = donationAdapter

        // Set the listener for the "Claim" button in the adapter
        donationAdapter.setOnTrackClickListener(object : DonationAdapter.OnTrackClickListener {
            @SuppressLint("MissingInflatedId", "SetTextI18n")
            override fun onTrackClick(position: Int) {
                // Get the selected donation
                val selectedDonation = donationsList[position]
                Geocoder(requireContext())
                val donorAddress = selectedDonation.pickupAddress
                val receiverAddress = selectedDonation.destAddress

                if (donorAddress != null && receiverAddress != null) {
                    Log.d("Inside ","come inside")
                    val donorLocation = GeoPoint(donorAddress.latitude, donorAddress.longitude)
                    val receiverLocation = GeoPoint(receiverAddress.latitude, receiverAddress.longitude)
                    val dialogView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_map, null)

                    val mapView = dialogView.findViewById<MapView>(R.id.map)
                    mapView.setTileSource(TileSourceFactory.MAPNIK)
                    mapView.setBuiltInZoomControls(true)

                    // Initialize userMarker and userLocation variables
                    var userMarker: Marker? = null
                    var userLocation: GeoPoint?

                    val closeButton = dialogView.findViewById<Button>(R.id.closeButton)
                    val deliverButton = dialogView.findViewById<Button>(R.id.deliverButton)
                    deliverButton.visibility = View.GONE // Hide the button by default

                    val builderAlert = AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setCancelable(false)
                    val dialog = builderAlert.create()

                    // When the deliverButton is clicked, show a dialog to confirm delivery
                    deliverButton.setOnClickListener {
                        val builder = AlertDialog.Builder(requireContext())
                            .setTitle("Confirm Delivery")
                            .setMessage("Have you delivered this donation to the receiver?")
                            .setPositiveButton("Yes") { _, _ ->
                                // Update the status of the selected donation
                                selectedDonation.status = "Delivered"
                                // Update the database
                                selectedDonation.donationId?.let { it1 ->
                                    FireStoreClass().updateDonationStatus(
                                        it1,"delivered"){
                                        Toast.makeText(requireContext(),"Donation delivered successfully",Toast.LENGTH_SHORT).show()

                                        val db = FirebaseFirestore.getInstance()
                                        db.collection("donation")
                                            .addSnapshotListener { snapshot, e ->
                                                if (e != null) {
                                                    Log.w(TAG, "Listen failed.", e)
                                                    return@addSnapshotListener
                                                }

                                                for (doc in snapshot!!.documents) {
                                                    val status = doc.getString("status")
                                                    if (status == "delivered") {
                                                        db.collection("donations").document(doc.id).update("reward","1")
                                                        // Delete the document with the matching ID
                                                        db.collection("donations").document(doc.id)
                                                            .delete()
                                                            .addOnSuccessListener {
                                                                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                                                            }
                                                            .addOnFailureListener { error ->
                                                                Log.w(TAG, "Error deleting document", error)
                                                            }
                                                    }
                                                }
                                            }

                                    }
                                }
                                deliverButton.text="Delivered"
                                deliverButton.isEnabled = false
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { _, _ ->
                                dialog.dismiss()
                            }
                        builder.create().show()
                    }

                    closeButton.setOnClickListener {
                        dialog.dismiss()
                    }
                    mapView?.post {
                        Log.d("Inside2 ","come inside  successfully")
                        val mapController = mapView.controller

                        mapController.setZoom(16.0)

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
                        val polyline =Polyline()
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

                        // Get the user's current location
                        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val locationProvider = LocationManager.NETWORK_PROVIDER
                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // Request location permission if not granted
                            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
                            return@post
                        }
                        locationManager.requestLocationUpdates(locationProvider, 1000L, 10f, object : LocationListener {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            override fun onLocationChanged(location: Location) {
                                userLocation = GeoPoint(location.latitude, location.longitude)
                                Log.d("LocationUser", userLocation!!.toDoubleString())
                                Log.d("LocationDonor",donorLocation.toDoubleString())
                                Log.d("LocationReceiver",receiverLocation.toDoubleString())
                                // Remove the old user marker
                                mapView.overlays.remove(userMarker)
                                // Add a new user marker at the new location
                                userMarker = Marker(mapView)
                                userMarker!!.position = userLocation
                                userMarker!!.title = "you are here"
                                mapView.overlays.add(userMarker)


                                // Add a Polyline overlay to show the path between the user and donor locations
                                polyline.addPoint(userLocation)
                                polyline.addPoint(donorLocation)
                                mapView.overlays.add(polyline)

                                // Calculate the distance between the user and receiver locations
                                val distance = userLocation!!.distanceToAsDouble(receiverLocation)

                                // Show or hide the deliverButton depending on the distance
                                if (distance <= 5000) { // Distance is less than or equal to 500 meters
                                    deliverButton.visibility = View.VISIBLE
                                } else { // Distance is greater than 500 meters
                                    deliverButton.visibility = View.GONE
                                }
                            }

                            @Deprecated("Deprecated in Java")
                            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                            override fun onProviderEnabled(provider: String) {}
                            override fun onProviderDisabled(provider: String) {}
                        })
                    }
                    dialog.show()

                } else {
                    Toast.makeText(requireContext(), "Unable to get donation location.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDonationList() {
        FireStoreClass().listenForDonationUpdates("claimedByVol",
            onUpdate = { donations ->
                // Update the adapter with the new donations data
//                donationsList.clear()
                for (donation in donations) {
                    // Fetch the donor address and receiver address from the latitude and longitude using geocoder
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    var donAddresses: List<Address?>? = null
                    var recAddresses: List<Address?>? = null
                    try {
                        donAddresses = geocoder.getFromLocation(donation.pickupAddress?.latitude!!,
                            donation.pickupAddress!!.longitude, 1)

                        recAddresses = geocoder.getFromLocation(donation.destAddress?.latitude!!,
                            donation.destAddress.longitude, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    // Update the donation object with the address
                    val address = donAddresses?.get(0)
                    donation.donorAddress = "${address?.locality ?: ""}, ${address?.subLocality ?: ""}"

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
    }
}
