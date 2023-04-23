package com.kamui.fooddonation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Example : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        // Set drawerLayout, navigationView and toolbar using findViewById
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Initialize toggle to open and close drawer
            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, R.string.open, R.string.close
            )
        // Add drawer listener to drawerLayout
        drawerLayout.addDrawerListener(toggle)
        // Sync state of toggle with drawerLayout
        toggle.syncState()
        // Show the navigation icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set navigation item selected listener to navigationView
        navigationView.setNavigationItemSelectedListener(this)
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
    // Override onBackPressed to handle drawer close action
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Override onNavigationItemSelected to handle navigation item clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Highlight the selected item
        item.isChecked = true

        when (item.itemId) {
            // handle navigation item clicks here


        }
        // Close the drawer
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}