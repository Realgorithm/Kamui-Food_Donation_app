package com.kamui.fooddonation.ngo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import com.kamui.fooddonation.R

class EmployeeFragment : Fragment() {

    companion object {
        // Static function to create a new instance of this fragment
        fun newInstance(): EmployeeFragment {
            return EmployeeFragment()
        }
    }

    // Called when the fragment should create its UI
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.employee_fragment, container, false)

        // Get references to the Add Employee button and the Employee CardView
        val addEmp = view.findViewById<Button>(R.id.add_emp)
        val cardView = view.findViewById<CardView>(R.id.cardview)

        // Set a click listener for the Employee CardView
        cardView.setOnClickListener{
            // Open the UpdateEmployee activity when the card view is clicked
            val intent = Intent(requireContext(), UpdateEmployee::class.java)
            startActivity(intent)
        }

        // Set a click listener for the Add Employee button
        addEmp.setOnClickListener {
            // Open the AddEmployee activity when the button is clicked
            val intent = Intent(requireContext(), AddEmployee::class.java)
            startActivity(intent)
        }

        // Return the fragment's view
        return view
    }
}
