package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.BassFragment
import com.kamui.fooddonation.data.NgoData

class NgoRequestFragment : BassFragment() {
    private var ngosList= ArrayList<NgoData>()
    private lateinit var ngoAdapter: NgoAdapter
    private lateinit var recyclerView: RecyclerView
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

    // Companion object to create a new instance of the fragment
    companion object {
        fun newInstance(): NgoRequestFragment {
            return NgoRequestFragment()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.ngo_request_fragment, container, false)
        showProgressDialog("Fetching Data")
        // Find views by id
        val calendarSpinner = view.findViewById<Spinner>(R.id.calendar_spinner)
        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        emptyView = view.findViewById(R.id.empty_view)
        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        ngoAdapter = NgoAdapter(requireContext(), ngosList, this)
        // Set up adapter for calendar Spinner
        calendarSpinner.adapter = calendarAdapter

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ngoAdapter

        getNgoData()
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
            // Update the Spinner text to show the selected date
            val dateString = "$dayOfMonth/${month + 1}/$year"
            calendarAdapter.clear()
            calendarAdapter.add(dateString)
            calendarAdapter.notifyDataSetChanged()

            // Hide the calendar view
            calendarView.visibility = View.GONE
        }
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
                if(ngosList.isNotEmpty()){
                    recyclerView.visibility=View.VISIBLE
                    emptyView.visibility=View.GONE
                }
                else{
                    recyclerView.visibility=View.GONE
                    emptyView.visibility =View.VISIBLE
                }
                hideProgressDialog()
            },
            onFailure = { exception ->
                // Handle the failure case
                Toast.makeText(requireContext()," Error $exception",Toast.LENGTH_SHORT).show()

            }
        )

    }
}
