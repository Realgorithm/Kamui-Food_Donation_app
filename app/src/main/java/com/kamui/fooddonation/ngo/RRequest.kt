package com.kamui.fooddonation.ngo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.kamui.fooddonation.R
import com.kamui.fooddonation.restaurant.HomeFragment

class RRequest : Fragment() {

    // Companion object to create a new instance of the fragment
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_r_request, container, false)
        val pickup = view.findViewById<Button>(R.id.btnAction)
        pickup.setOnClickListener{
            val intent = Intent(requireContext(), DonationDetails::class.java)
            startActivity(intent)
        }
        val spinner = view.findViewById<Spinner>(R.id.spinner)

        // Set the adapter for the spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dropdown_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set the listener for when an item is selected
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle selection here
                // for example, you can show a toast to show the selected item
                Toast.makeText(requireContext(), parent?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing if no item is selected
            }
        }
        return view
    }

}
