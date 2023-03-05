package com.kamui.fooddonation.volunteer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kamui.fooddonation.TrackFragment
import com.kamui.fooddonation.VolHomeFragment
import com.kamui.fooddonation.restaurant.AccountFragment
import com.kamui.fooddonation.restaurant.HistoryFragment
import com.kamui.fooddonation.restaurant.HomeFragment

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