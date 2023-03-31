package com.kamui.fooddonation.receiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kamui.fooddonation.R
import com.kamui.fooddonation.restaurant.AccountFragment

class RcHomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Initialize drawerLayout and navigationView
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rchome_page)
        supportActionBar?.hide()

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        // Add drawer listener to drawerLayout
        drawerLayout.addDrawerListener(toggle)
        // Sync state of toggle with drawerLayout
        toggle.syncState()

        // Disable the default action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Show the navigation icon
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


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        item.isChecked = true
        drawerLayout.closeDrawers()

        when(item.itemId){
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, RcNgoFragment())
                    .commit()
            }
            R.id.nav_recieved -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, RcRestaurantFragment())
                    .commit()
            }
            R.id.nav_profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, AccountFragment())
                    .commit()
            }
            R.id.nav_logout -> {
                finish()
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}


