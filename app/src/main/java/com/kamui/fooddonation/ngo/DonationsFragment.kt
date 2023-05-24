package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.volunteer.DonationAdapter
import java.util.Calendar
import java.util.Date

class DonationsFragment : BassFragment() {
    private lateinit var selectedDate: Date
    private val donationsList = ArrayList<Donation>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter
    private lateinit var emptyView: TextView

    // Initialize adapter for calendar Spinner
    private val calendarAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArrayList<String>().apply { add("Select Date") }
        ).apply {
            setDropDownViewResource(R.layout.calendar_dropdown_item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation", "ClickableViewAccessibility",
        "NotifyDataSetChanged"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.my_request_fragment, container, false)

        selectedDate = Date()

        val userUid = FireStoreClass().getCurrentUserID()
        // Find views by id
        val calendarSpinner = view.findViewById<Spinner>(R.id.calendar_spinner)
        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)

        emptyView = view.findViewById(R.id.empty_view)
        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        donationAdapter = DonationAdapter(requireContext(), donationsList, this)


        // Set up adapter for calendar Spinner
        calendarSpinner.adapter = calendarAdapter

        // Get the current date
        val currentDate = Calendar.getInstance()

        // Set the Spinner text to show the current date
        val dateValue = "${currentDate.get(Calendar.DAY_OF_MONTH)}/${currentDate.get(Calendar.MONTH) + 1}/${currentDate.get(Calendar.YEAR)}"
        calendarAdapter.clear()
        calendarAdapter.add(dateValue)
        calendarAdapter.notifyDataSetChanged()

        // Get donations for the current date
        getDonationList(currentDate.time)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = donationAdapter
        // Hide the calendar view
        calendarView.visibility = View.GONE

        // Set listener for when Spinner is clicked
        calendarSpinner.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                calendarView.visibility =
                    if (calendarView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                Log.d("calendars","Value ${calendarView.visibility}")
                true
            } else {
                false
            }
        }

        // Set listener for when date is selected in calendar
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Update the selected date
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            // Update the Spinner text to show the selected date
            val dateString = "$dayOfMonth/${month + 1}/$year"
            calendarAdapter.clear()
            calendarAdapter.add(dateString)
            calendarAdapter.notifyDataSetChanged()

            // Hide the calendar view
            calendarView.visibility = View.GONE

            showProgressDialog("Fetching Data")
            // Get donations for selected date
            getDonationList(selectedDate.time)
        }

        // Set the listener for the "Claim" button in the adapter
        donationAdapter.setOnClaimClickListener(object : DonationAdapter.OnClaimClickListener {
            @RequiresApi(Build.VERSION_CODES.P)
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
                    FireStoreClass().getUserData("ngo", NgoData::class.java,userUid) { userData ->
                        val receiverName = userData?.name.toString()
                        val destLocation = userData?.location
                        val ngoAddress = userData?.ngoAddress.toString()

                        Log.d("receiverAddressBefore",ngoAddress.trim())
                        selectedDonation.donationId?.let {
                            FireStoreClass().updateDonationStatus(it, "availableForVol") {
                                if(ngoAddress!="") {
                                    FireStoreClass().updateReceiverInfo(
                                        selectedDonation.donationId!!, receiverName, destLocation,
                                        receiverId, ngoAddress
                                    ) {
                                        Log.d("receiverAddress", ngoAddress)
                                        // Update the status of the donation to "availableForVol"
                                        selectedDonation.status = "availableForVol"
                                        Toast.makeText(
                                            requireContext(),
                                            "You have successfully claimed donation",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        // Update the UI
                                        donationAdapter.notifyItemChanged(position)
                                    }
                                }
                                Log.d("receiverAddressAfter",ngoAddress.trim())
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
    @SuppressLint("NotifyDataSetChanged")
    private fun getDonationList(selectedDates: Date){
        val startDate = Calendar.getInstance()
        startDate.time = selectedDates
        startDate.set(Calendar.HOUR_OF_DAY, 0)
        startDate.set(Calendar.MINUTE, 0)
        startDate.set(Calendar.SECOND, 0)
        val endDate = Calendar.getInstance()
        endDate.time = selectedDates
        endDate.set(Calendar.HOUR_OF_DAY, 23)
        endDate.set(Calendar.MINUTE, 59)
        endDate.set(Calendar.SECOND, 59)

        FireStoreClass().getDonationsByDateRange(startDate.time, endDate.time,){ donations ->
            if (donations != null) {
                donationsList.clear()
                donationsList.addAll(donations)
                donationAdapter.updateDonations(donations)
                donationAdapter.notifyDataSetChanged()
                Log.d("donationsList",donationsList.isNotEmpty().toString())
                if(donationsList.isNotEmpty()) {
                    emptyView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }else{
                    emptyView.visibility =View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                hideProgressDialog()
            }
            else{
                Toast.makeText(requireContext(),"Error occured while trying to get donations",Toast.LENGTH_SHORT).show()
            }

        }
    }
}
