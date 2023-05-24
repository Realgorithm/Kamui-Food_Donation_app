package com.kamui.fooddonation.volunteer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kamui.fooddonation.AccountFragment
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.VolunteerData

class VHomePage : BaseActivity() {

        @RequiresApi(Build.VERSION_CODES.P)
        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_v_home_page)
            onVolunteerLoginSuccess()
            val userUid=FireStoreClass().getCurrentUserID()
            FireStoreClass().getUserData("volunteer", VolunteerData::class.java,userUid){ userData ->
                val volunteerName= userData?.name.toString()
                // Set the title of the activity
                supportActionBar?.title = "Welcome $volunteerName"

                val accountFragment = AccountFragment()
                val bundle =Bundle()
                bundle.putString("previousActivity","VHomePage")
                accountFragment.arguments=bundle

            }

            // Set up the ViewPager to display the restaurant menu items
            val viewPager = findViewById<ViewPager2>(R.id.viewPager)
            viewPager.adapter = ViewPagerAdapterVolunteer(this)

            // Set up the BottomNavigationView to switch between menu item categories
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        viewPager.currentItem = 0
                        true
                    }
                    R.id.track -> {
                        viewPager.currentItem = 1
                        true
                    }
                    R.id.acc -> {
                        viewPager.currentItem = 2

                        true
                    }
                    else -> false
                }
            }

            // Set up a callback to update the BottomNavigationView when the ViewPager is swiped
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    bottomNavigationView.menu.getItem(position).isChecked = true
                }
            })
        }
    private fun onVolunteerLoginSuccess() {
        // Call updateLoggedInNgoStatus() to set the boolean flag to true
        updateLoggedInModuleStatus("Volunteer",true)
    }
    }


