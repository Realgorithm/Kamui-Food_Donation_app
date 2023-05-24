package com.kamui.fooddonation.ngo

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.volunteer.DonationAdapter

class RecordFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerViewClaimed: RecyclerView
    private lateinit var donationListAdapter: DonationAdapter
    private lateinit var emptyView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)

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
}