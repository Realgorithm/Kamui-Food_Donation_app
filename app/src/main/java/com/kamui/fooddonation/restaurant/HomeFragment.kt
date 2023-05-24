package com.kamui.fooddonation.restaurant

import android.annotation.SuppressLint
import android.content.Intent
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
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.ngo.NgoAdapter
import com.kamui.fooddonation.volunteer.DonationAdapter

class HomeFragment : BassFragment() {

    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerViewDonations: RecyclerView
    private lateinit var donationListAdapter: DonationAdapter

    private var ngosList= ArrayList<NgoData>()
    private lateinit var ngoAdapter: NgoAdapter
    private lateinit var recyclerViewNgo: RecyclerView
    private lateinit var emptyView: TextView

    // Companion object to create a new instance of the fragment
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        emptyView = view.findViewById(R.id.empty_view)
        // Initialize the RecyclerView and the adapter
        recyclerViewNgo = view.findViewById(R.id.recycler_view_ngo)
        ngoAdapter = NgoAdapter(requireContext(), ngosList, this)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNgo.layoutManager = layoutManager
        recyclerViewNgo.adapter = ngoAdapter

        getNgoData()

        recyclerViewDonations = view.findViewById(R.id.recycler_view_donations)
        showProgressDialog("Fetching Data")
        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager2: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerViewDonations.layoutManager = layoutManager2

        donationListAdapter = DonationAdapter(requireContext(), donationsList,this)
        recyclerViewDonations.adapter = donationListAdapter

        getRecentDonation()

        // Set up the donate button
        val donateBtn = view.findViewById<Button>(R.id.donate)
        donateBtn.setOnClickListener {
            val intent = Intent(requireContext(), RAddRequest::class.java)
            startActivity(intent)
        }

        // Return the view
        return view
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getNgoData(){
        val currentNgoId = FireStoreClass().getCurrentUserID()
        FireStoreClass().getAllNGOsExceptCurrentNGO(currentNgoId,
            onSuccess = { ngos ->
                ngosList.clear()
                ngosList.addAll(ngos)
                ngoAdapter.notifyDataSetChanged()
                Log.d("ngoList",ngosList.toString())
                Log.d("NgoList",ngosList.isNotEmpty().toString())
                if(ngosList.isNotEmpty()){
                    recyclerViewNgo.visibility=View.VISIBLE
                    emptyView.visibility=View.GONE
                }
                else{
                    recyclerViewNgo.visibility=View.GONE
                    emptyView.visibility =View.VISIBLE
                }
                hideProgressDialog()
            },
            onFailure = { exception ->
                // Handle the failure case
                Toast.makeText(requireContext()," Error $exception", Toast.LENGTH_SHORT).show()

            }
        )

    }

    @SuppressLint("SetTextI18n")
    private fun getRecentDonation(){
        val currentUserId = FireStoreClass().getCurrentUserID()
        hideProgressDialog()
        FireStoreClass().getRecentDonation(currentUserId,
        onUpdate = { donations ->
            donationsList.clear()
            donationsList.addAll(donations)
            donationListAdapter.updateDonations(donations)
            if(ngosList.isNotEmpty()){
                recyclerViewNgo.visibility=View.VISIBLE
                emptyView.visibility=View.GONE
            }
            else{
                recyclerViewNgo.visibility=View.GONE
                emptyView.visibility =View.VISIBLE
                emptyView.text="No recent donation"
            }
            Log.d("RecentDonation",donationsList.toString())
            Log.d("RecentDonation",donationsList.isEmpty().toString())
        },
        onError = {exception ->
            Toast.makeText(requireContext()," Error $exception", Toast.LENGTH_SHORT).show()
            Log.e("RecentDonation", "Error $exception")
        })
    }
}
