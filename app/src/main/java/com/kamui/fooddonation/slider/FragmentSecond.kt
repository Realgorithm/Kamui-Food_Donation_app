package com.kamui.fooddonation.slider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.kamui.fooddonation.R

class FragmentSecond : Fragment() {

    companion object {
        fun newInstance(): FragmentSecond {
            return FragmentSecond()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_second, container, false)

        val nextButton = view.findViewById<Button>(R.id.btn_next)

        nextButton.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
            viewPager.currentItem = 2
        }

        return view
    }
}