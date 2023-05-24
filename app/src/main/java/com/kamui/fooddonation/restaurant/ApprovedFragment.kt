package com.kamui.fooddonation.restaurant

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.volunteer.DonationAdapter

class ApprovedFragment : BassFragment() {
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerViewDonations: RecyclerView
    private lateinit var donationListAdapter: DonationAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    companion object {
        fun newInstance(): ApprovedFragment {
            return ApprovedFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.history_child_fragment, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        recyclerViewDonations = view.findViewById(R.id.recycler_view_donations)
        showProgressDialog("Fetching Data")
        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerViewDonations.layoutManager = layoutManager

        val currentUserUid = FireStoreClass().getCurrentUserID()
        FirebaseFirestore.getInstance().collection("donations")
            .whereEqualTo("donorId", currentUserUid)
            .whereEqualTo("status", "approved")
            .get()
            .addOnSuccessListener {
                Log.d("PendingData", currentUserUid)
                getDonationList()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error querying donations", e)
            }

        swipeRefreshLayout.setOnRefreshListener {
            showProgressDialog("Fetching Data")
            // Refresh your data here
            getDonationList()
            // Hide the refresh indicator
            swipeRefreshLayout.isRefreshing = false
        }
        donationListAdapter = DonationAdapter(requireContext(), donationsList,this)
        recyclerViewDonations.adapter = donationListAdapter

        return view
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getDonationList() {
        FireStoreClass().listenForDonationUpdates("approved",
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

