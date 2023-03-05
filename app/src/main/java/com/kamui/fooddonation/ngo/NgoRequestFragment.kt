package com.kamui.fooddonation.ngo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import com.kamui.fooddonation.R

class NgoRequestFragment : Fragment() {

    // Companion object to create a new instance of the fragment
    companion object {
        fun newInstance(): NgoRequestFragment {
            return NgoRequestFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.ngo_request_fragment, container, false)

        // Get a reference to the CalendarView
        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)

        // Set a listener to handle date changes
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Do something with the selected date
            // For example, show the selected date in a toast message
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(requireContext(), "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}
