package com.kamui.fooddonation.slider

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.kamui.fooddonation.OnboardScreen
import com.kamui.fooddonation.R

class FragmentThird : Fragment() {

    companion object {
        fun newInstance(): FragmentThird {
            return FragmentThird()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_third, container, false)

        val nextButton = view.findViewById<Button>(R.id.btn_next)

        nextButton.setOnClickListener {
            val intent = Intent(requireContext(), OnboardScreen::class.java)
            startActivity(intent)
        }

        return view
    }
}