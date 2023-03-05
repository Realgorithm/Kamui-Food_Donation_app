package com.kamui.fooddonation.slider

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kamui.fooddonation.slider.FragmentFirst
import com.kamui.fooddonation.slider.FragmentSecond
import com.kamui.fooddonation.slider.FragmentThird


class SliderAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentFirst.newInstance()
            1 -> FragmentSecond.newInstance()
            2 -> FragmentThird.newInstance()
            else -> FragmentFirst.newInstance()
        }
    }
}