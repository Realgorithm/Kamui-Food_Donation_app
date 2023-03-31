package com.kamui.fooddonation.volunteer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kamui.fooddonation.restaurant.AccountFragment

class ViewPagerAdapterVolunteer(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VolHomeFragment()
            1 -> TrackFragment()
            2 -> AccountFragment()
            else -> Fragment()
        }
    }

}