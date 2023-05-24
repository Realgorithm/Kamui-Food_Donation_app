package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R

class EmployeeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var employeeAdapter: EmployeeAdapter
    private lateinit var emptyView: TextView
    companion object {
        // Static function to create a new instance of this fragment
        fun newInstance(): EmployeeFragment {
            return EmployeeFragment()
        }
    }

    // Called when the fragment should create its UI
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.employee_fragment, container, false)

        // Get references to the Add Employee button and the Employee CardView
        val addEmp = view.findViewById<Button>(R.id.add_emp)
        emptyView = view.findViewById(R.id.empty_view)

        // Initialize the RecyclerView and the adapter
        recyclerView = view.findViewById(R.id.recycler_view)

        // Set the layout manager and the adapter for the RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val currentUserId =FireStoreClass().getCurrentUserID()

        FireStoreClass().getAllEmployees(currentUserId)
        {employees ->
            employeeAdapter = EmployeeAdapter(ArrayList(employees))
            recyclerView.adapter = employeeAdapter
            if (employees.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            }
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
