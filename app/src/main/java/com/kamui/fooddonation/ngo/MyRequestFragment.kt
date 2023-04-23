package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.kamui.fooddonation.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MyRequestFragment : Fragment() {

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
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.my_request_fragment, container, false)

        // Find views by id
        val calendarSpinner = view.findViewById<Spinner>(R.id.calendar_spinner)
        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)

        // Set up adapter for calendar Spinner
        calendarSpinner.adapter = calendarAdapter

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
}
