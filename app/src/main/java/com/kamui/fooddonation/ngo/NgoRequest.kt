package com.kamui.fooddonation.ngo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kamui.fooddonation.R

class NgoRequest : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: ViewPagerAdapter3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_ngo_request, container, false)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        adapter = ViewPagerAdapter3(childFragmentManager, lifecycle);
        viewPager.adapter = adapter

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "NGO REQUESTS"
                1 -> tab.text = "VOLUNTEERS"
            }
        }.attach()

        return view
    }
}