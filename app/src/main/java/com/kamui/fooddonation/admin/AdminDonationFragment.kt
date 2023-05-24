package com.kamui.fooddonation.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.volunteer.DonationAdapter


class AdminDonationFragment : BassFragment() {
    private var donationsList = ArrayList<Donation>()
    private lateinit var recyclerViewDonations: RecyclerView
    private lateinit var donationListAdapter:DonationAdapter
    private lateinit var btnApproveAll: Button
    private lateinit var emptyView: TextView
    private var hasDonations: Boolean = false
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_donation, container, false)

        showProgressDialog("Fetching data")
        // Initialize UI elements
        recyclerViewDonations = view.findViewById(R.id.recyclerViewDonations)
        emptyView = view.findViewById(R.id.empty_view)
        btnApproveAll = view.findViewById(R.id.btnApproveAll)
        btnApproveAll.isEnabled = hasDonations
        // Set up RecyclerView with LinearLayoutManager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerViewDonations.layoutManager = layoutManager

        // Create a list of sample donations (replace with actual data from Firestore)
        getDonationList()

        // Create an instance of DonationListAdapter and set it to RecyclerView
        donationListAdapter = DonationAdapter(requireContext(), donationsList,this)
        recyclerViewDonations.adapter = donationListAdapter

        btnApproveAll.isEnabled = donationsList.isNotEmpty()
        // Set click listener for approve all button
        btnApproveAll.setOnClickListener {
            // Perform approval logic here
            // Example: iterate through the list of donations and update their status to "approved" in Firestore
            // You can also show a confirmation dialog before approving all donations
            // Show confirmation dialog
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm Approval")
            builder.setMessage("Are you sure you want to approve all donations?")
            builder.setPositiveButton("Yes") { _, _ ->
                // Get a reference to the Firestore collection
                val donationsRef = FirebaseFirestore.getInstance().collection("donations")
                // Query for all donations with status "unapproved"
                donationsRef.whereEqualTo("status", "unapproved").get()
                    .addOnSuccessListener { documents ->
                        // Iterate through the list of donations and update their status to "approved"
                        for (document in documents) {
                            FireStoreClass().updateDonationStatus(document.id,"approved"){
                                Log.d(TAG, "Donation ${document.id} approved.")
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error querying donations", e)
                    }
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.show()
        }
        return view
    }


    /**
     * Function to get a list of sample donations (replace with actual data from Firestore)
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun getDonationList() {
        FireStoreClass().listenForDonationUpdates("unapproved",
            onUpdate = { donations ->
                donationsList.clear()
                donationsList.addAll(donations)
                // Update the adapter with the new donations data
                donationListAdapter.updateDonations(donations)
                // Check if the list is now empty
                if (donationsList.isEmpty()) {
                    // Show an empty view or a message indicating there are no donations
                    recyclerViewDonations.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerViewDonations.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
                hasDonations = donations.isNotEmpty()
                btnApproveAll.isEnabled = hasDonations
                hideProgressDialog()
            },
            onError = { error ->
                // Handle the error, for example by displaying an error message
                Toast.makeText(requireContext(), "Error listening for donation updates", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error listening for donation updates", error)
                hasDonations = false
                btnApproveAll.isEnabled = false
                hideProgressDialog()
            }
        )
    }
}