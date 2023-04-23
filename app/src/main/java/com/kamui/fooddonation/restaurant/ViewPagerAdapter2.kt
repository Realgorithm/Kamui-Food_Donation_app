package com.kamui.fooddonation.restaurant

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

// Adapter for the view pager that handles the three fragments
class ViewPagerAdapter2(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3 // Return the number of fragments
    }

    override fun createFragment(position: Int): Fragment {
        // Return the corresponding fragment based on the position
        return when (position) {
            0 -> PendingFragment()
            1 -> ApprovedFragment()
            2 -> AcceptedFragment()
            else -> Fragment() // Return a default empty fragment for an invalid position
        }
    }
}
