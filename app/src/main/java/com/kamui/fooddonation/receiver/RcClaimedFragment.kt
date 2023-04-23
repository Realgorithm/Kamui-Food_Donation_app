package com.kamui.fooddonation.receiver

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.admin.BassFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.volunteer.DonationAdapter

class RcClaimedFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerViewClaimed: RecyclerView
    private lateinit var donationListAdapter: DonationAdapter
    private lateinit var emptyView: TextView
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rc_claimed, container, false)
        emptyView = view.findViewById(R.id.empty_view)
        recyclerViewClaimed = view.findViewById(R.id.recycler_view_rc_claimed)
        showProgressDialog("Fetching Data")
        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerViewClaimed.layoutManager = layoutManager

        val currentUserUid = FireStoreClass().getCurrentUserID()
        FirebaseFirestore.getInstance().collection("donations")
            .whereEqualTo("receiverId", currentUserUid)
            .whereIn("status", listOf("availableForVol", "claimedByVol", "delivered"))
            .get()
            .addOnSuccessListener {
                Log.d("PendingData", currentUserUid)
                // Update the adapter with the received donations
                donationsList.addAll(it.toObjects(Donation::class.java))
                donationListAdapter.updateDonations(donationsList)
                if (donationsList.isEmpty()) {
                    // Show an empty view or a message indicating there are no donations
                    recyclerViewClaimed.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                } else {
                    recyclerViewClaimed.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
                hideProgressDialog()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error querying donations", e)
            }

        donationListAdapter = DonationAdapter(requireContext(), donationsList,this)
        recyclerViewClaimed.adapter = donationListAdapter

        return view
    }
    private fun getDonationList(status:String) {

        FireStoreClass().listenForDonationUpdates(status,
            onUpdate = { donations ->
                // Update the adapter with the new donations data
//                donationsList.clear()
                donationsList.addAll(donations)
                donationListAdapter.updateDonations(donations)
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