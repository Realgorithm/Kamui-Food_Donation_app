package com.kamui.fooddonation.restaurant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.kamui.fooddonation.R

class HomeFragment : Fragment() {

    // Companion object to create a new instance of the fragment
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        // Set up the donate button
        val donateBtn = view.findViewById<Button>(R.id.donate)
        donateBtn.setOnClickListener {
            val intent = Intent(requireContext(), RAddRequest::class.java)
            startActivity(intent)
        }

        // Return the view
        return view
    }

}
