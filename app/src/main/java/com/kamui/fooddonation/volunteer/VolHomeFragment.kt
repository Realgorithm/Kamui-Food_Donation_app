package com.kamui.fooddonation.volunteer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.admin.BassFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.VolunteerData
import java.io.IOException
import java.util.Calendar
import java.util.Locale


class VolHomeFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter
    private lateinit var emptyView: TextView
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.vol_fragment_home, container, false)
        showProgressDialog("Fetching Data")
        emptyView = view.findViewById(R.id.empty_view)
        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        donationAdapter = DonationAdapter(requireContext(), donationsList, this)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = donationAdapter

        // Call the listenForDonationUpdates function

        // Load the donations from the server
//        loadDonations()
        getDonationList()
        // Set the listener for the "Claim" button in the adapter
        donationAdapter.setOnClaimClickListener(object : DonationAdapter.OnClaimClickListener {
            override fun onClaimClick(position: Int) {
                Log.d("position",position.toString())
                Log.d("donation Size",donationsList.size.toString())
                // Check if the position is within bounds
                if (position >= donationsList.size) {

                    return
                }
                Log.d("claim button","Claimed Successfully")
                // Get the selected donation
                val selectedDonation = donationsList[position]

                // Check if the donation is already claimed
                if (selectedDonation.status == "claimedByVol") {
                    // Show an error message and return
                    Toast.makeText(
                        requireContext(),
                        "This donation has already been claimed",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                // Update the status of the donation to "claimed"
                selectedDonation.status = "claimedByVol"

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirm Approval")
                builder.setMessage("Are you sure you want to approve all donations?")
                builder.setPositiveButton("Yes") { _, _ ->

                    // Set up the time picker dialog
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)
                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minutes ->
                            val time = String.format("%02d:%02d", hourOfDay, minutes)
                            // Do something with the user input, such as pass it to a function or update the UI
                            showDeliveryTimeBuilderDialog()
                        },
                        hour,
                        minute,
                        DateFormat.is24HourFormat(requireContext())
                    )
                    timePickerDialog.setTitle("Select Pickup Time")
                    timePickerDialog.setMessage("Please select the pickup time for delivery.")
                    timePickerDialog.show()
                    val fireStoreClass = FireStoreClass()
                    val currentUser = FireStoreClass().getCurrentUserID()
                    fireStoreClass.getUserData("volunteer", VolunteerData::class.java) { userData ->
                        val volunteerName = userData?.name.toString()

                        selectedDonation.donationId?.let {
                            FireStoreClass().updateDonationStatus(it, "claimedByVol") {
                                fireStoreClass.updateDonationClaimedBy(
                                    selectedDonation.donationId!!,
                                    volunteerName
                                ) {
                                    fireStoreClass.updateVolunteerId(
                                        selectedDonation.donationId!!,
                                        currentUser
                                    ) {
                                        selectedDonation.claimedBy = volunteerName
                                        Toast.makeText(
                                            requireContext(),
                                            "You have successfully claimed donation",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }
                                }
                            }
                        }
                    }
                    // Remove the claimed donation from the list
//                donationsList.removeAt(position)

                    // Update the UI
//                donationAdapter.notifyItemRemoved(position)
                    donationAdapter.notifyItemChanged(position)
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }
        })
        return view
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getDonationList() {
        FireStoreClass().listenForDonationUpdates("availableForVol",
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

//                     Update the donation object with the address
                    val address = addresses?.get(0)
                    donation.donorAddress = "${address?.locality ?: ""}, ${address?.subLocality ?: ""}"

                    // Add the updated donation to the list
                    donationsList.addAll(donations)
                }
//                donationsList.addAll(donations)
                donationAdapter.updateDonations(donations)
//                // Check if the list is now empty
                if (donationsList.isEmpty()) {
                    // Show an empty view or a message indicating there are no donations
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
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
//            recyclerView.adapter?.notifyDataSetChanged()
//        }
    }

    fun showDeliveryTimeBuilderDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Deliver Within")

        val deliveryTime = EditText(requireContext())
        builder.setView(deliveryTime)

        // Set up the buttons
        builder.setPositiveButton("OK") { _, _ ->
            val textDeliveryTime = deliveryTime.text.toString()
            // Do something with the deliver within input, such as store it
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

}



