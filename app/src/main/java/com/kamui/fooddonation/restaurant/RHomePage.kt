package com.kamui.fooddonation.restaurant


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.RestaurantData
import java.util.Locale

class RHomePage : BaseActivity() {

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rhome_page)

        onRestaurantLoginSuccess()

        val userUid =FireStoreClass().getCurrentUserID()
        FireStoreClass().getUserData("restaurant", RestaurantData::class.java,userUid){ userData ->
            val restaurantName= userData?.name.toString()
            // Set the title of the activity
            // Set the title and enable the back button
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayShowCustomEnabled(true)
            supportActionBar?.setCustomView(R.layout.action_bar_title)

            val title = findViewById<TextView>(R.id.action_bar_title)
            title.text = "Welcome ${restaurantName.uppercase(Locale.ROOT)}"

            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

        }

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
    // Function to update the shared preferences for Restaurant login status
    private fun onRestaurantLoginSuccess() {
        // Call updateLoggedInRestaurantStatus() to set the boolean flag to true
        updateLoggedInModuleStatus("Restaurant",true)
    }
    override fun onBackPressed() {
        doubleBackToExit()
    }

}
