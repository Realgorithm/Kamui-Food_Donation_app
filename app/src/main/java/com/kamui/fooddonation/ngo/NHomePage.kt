package com.kamui.fooddonation.ngo


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.AccountFragment
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.OnboardScreen
import com.kamui.fooddonation.R


class NHomePage : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    // Initialize drawerLayout and navigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView:NavigationView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nhome_page)

        onNgoLoginSuccess()


        val userUid= FireStoreClass().getCurrentUserID()
        // Set drawerLayout, navigationView using findViewById
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Get the header view from the NavigationView
        val headerView = navigationView.getHeaderView(0)

        // Retrieve the user's name and image URI from Firestore
        FirebaseFirestore.getInstance().collection("users")
            .document("r23fKsDlCbMnWap4xJZ2FUQmhnq2")
            .collection("ngo")
            .document(userUid).get().addOnSuccessListener { documentSnapshot ->
            val name = documentSnapshot.getString("name")
            val imageUri = documentSnapshot.getString("imageUri")

            // Get references to the views in the header view
            val profileImage = headerView.findViewById<ImageView>(R.id.profile_image)
            val profileName = headerView.findViewById<TextView>(R.id.profile_name)

            // Load the user's image into the ImageView using Glide
            Glide.with(this).load(imageUri).into(profileImage)

            // Set the user's name in the TextView
            profileName.text = name
        }

        // Set an OnClickListener on the header view
        headerView.setOnClickListener {
            // Your code here
            val accountFragment = AccountFragment()
            val bundle =Bundle()
            bundle.putString("previousActivity","NHomePage")
            accountFragment.arguments=bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, accountFragment)
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
        if(item.isChecked && !isDarkMode()){
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(Color.BLACK, Color.RED)
            )
            navigationView.itemTextColor = colorStateList
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






