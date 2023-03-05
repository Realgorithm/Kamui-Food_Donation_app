package com.kamui.fooddonation.restaurant


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kamui.fooddonation.R

class RHomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rhome_page)

        // Set the title of the activity
        supportActionBar?.title = "Restaurant Name"

        // Set up the ViewPager to display the restaurant menu items
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        // Set up the BottomNavigationView to switch between menu item categories
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_1 -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.menu_item_2 -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.menu_item_3 -> {
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
}
