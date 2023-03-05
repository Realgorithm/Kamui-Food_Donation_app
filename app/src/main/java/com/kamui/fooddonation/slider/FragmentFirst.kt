package com.kamui.fooddonation.slider

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.kamui.fooddonation.R

class FragmentFirst : Fragment() {

    companion object {
        fun newInstance(): FragmentFirst {
            return FragmentFirst()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        val nextButton = view.findViewById<Button>(R.id.btn_next)

        nextButton.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
            viewPager.currentItem = 1
        }

        return view
    }

    }
