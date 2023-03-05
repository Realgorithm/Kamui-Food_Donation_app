package com.kamui.fooddonation.volunteer

import android.annotation.SuppressLint
import android.os.Bundle
<<<<<<< HEAD
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kamui.fooddonation.R
import com.kamui.fooddonation.ngo.EmployeeFragment
import com.kamui.fooddonation.ngo.NgoRequest
import com.kamui.fooddonation.ngo.RRequest
import com.kamui.fooddonation.ngo.RecordFragment
import com.kamui.fooddonation.restaurant.AccountFragment

class VHomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
=======
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.kamui.fooddonation.R

class VHomePage : AppCompatActivity() {
>>>>>>> 80a599b (day33 commit)

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_home_page)

<<<<<<< HEAD
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Get the header view from the NavigationView
        val headerView = navigationView.getHeaderView(0)

        // Set an OnClickListener on the header view
        headerView.setOnClickListener {
            // Your code here
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, AccountFragment())
                .commit()
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener(this)

        val menu = navigationView.menu
        val requestsItem = menu.findItem(R.id.r_requests)
        requestsItem.isChecked = true
        navigationView.setCheckedItem(R.id.records)
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, RRequest())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        for (i in 0 until navigationView.menu.size()) {
            val menuItem = navigationView.menu.getItem(i)
            menuItem.isChecked = false
        }

        item.isChecked = true

        when (item.itemId) {
            R.id.r_requests -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, RRequest())
                    .commit()
            }
            R.id.n_requests -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, NgoRequest())
                    .commit()
            }
            R.id.m_employee -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, EmployeeFragment())
                    .commit()
            }
            R.id.records -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, RecordFragment())
                    .commit()
            }
            R.id.logout -> {
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
=======
        // Set the title of the activity
        supportActionBar?.title = "Hello Volunteer"

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
                R.id.track ->{
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
>>>>>>> 80a599b (day33 commit)
    }
}
