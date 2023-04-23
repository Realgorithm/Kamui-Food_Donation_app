package com.kamui.fooddonation.ngo


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.OnboardScreen
import com.kamui.fooddonation.R
import com.kamui.fooddonation.restaurant.AccountFragment


class NHomePage : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    // Initialize drawerLayout and navigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView:NavigationView
    private lateinit var profile:ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nhome_page)

        onNgoLoginSuccess()
        // Get the menu item color from resources
        val menuItemColor = ContextCompat.getColorStateList(this, R.color.red_200)
        // Get the selected menu item color from resources
        val selectedMenuItemColor = ContextCompat.getColorStateList(this, R.color.black)

        // Set drawerLayout, navigationView using findViewById
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Get the header view from the NavigationView
        val headerView = navigationView.getHeaderView(0)

        // Set an OnClickListener on the header view
        headerView.setOnClickListener {
            // Your code here
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame,AccountFragment())
                .commit()
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Select the item with ID R.id.r_requests
        val menu = navigationView.menu
        val requestsItem = menu.findItem(R.id.ngo_home)
        requestsItem.isChecked = true
        navigationView.setCheckedItem(R.id.records)
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, NgoHomeFragment())
            .commit()

        // Initialize toggle to open and close drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open, R.string.close
        )
        // Add drawer listener to drawerLayout
        drawerLayout.addDrawerListener(toggle)
        // Sync state of toggle with drawerLayout
        toggle.syncState()

        // Set hamburger icon color based on app theme
        if (isDarkMode()) {
            toggle.drawerArrowDrawable.color = Color.WHITE
        } else {
            toggle.drawerArrowDrawable.color = Color.RED
            navigationView.itemTextColor = ColorStateList.valueOf(Color.RED)
        }

        // Show the navigation icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set navigation item selected listener to navigationView
        navigationView.setNavigationItemSelectedListener(this)

    }

    // Override onOptionsItemSelected to handle drawer open and close actions
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // If drawer is open, close it, otherwise open it
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


    // Override onBackPressed to handle drawer close action
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    // Override onNavigationItemSelected to handle navigation item clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // De-highlight all other items
        for (i in 0 until navigationView.menu.size()) {
            val menuItem = navigationView.menu.getItem(i)
            menuItem.isChecked = false
        }

        // Highlight the selected item
        item.isChecked = true

        when (item.itemId) {
            // handle navigation item clicks here
            R.id.ngo_home -> {
                // Create intent to start AHomePage activity and start the activity
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, NgoHomeFragment())
                    .commit()
            }
            R.id.m_employee -> {
                // Create intent to start AHomePage activity and start the activity
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, EmployeeFragment())
                    .commit()
            }
            R.id.records -> {
                // Create intent to start AHomePage activity and start the activity
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, RecordFragment())
                    .commit()
            }
            R.id.logout -> {
                // Update the shared preferences to indicate that the user is not logged in
                updateLoggedInModuleStatus("Ngo",false)
                FirebaseAuth.getInstance().signOut()
                // Finish the current activity
                val intent = Intent(this, OnboardScreen::class.java)
                startActivity(intent)
                finish()
            }
        }
        // Close the drawer
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun onNgoLoginSuccess() {
        // Call updateLoggedInNgoStatus() to set the boolean flag to true
        updateLoggedInModuleStatus("Ngo",true)
    }
}






