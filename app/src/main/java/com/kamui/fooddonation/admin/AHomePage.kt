package com.kamui.fooddonation.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.kamui.fooddonation.AdminDonationActivity
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.OnboardScreen
import com.kamui.fooddonation.R
import kotlinx.coroutines.handleCoroutineException


class AHomePage : BaseActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahome_page)
        onAdminLoginSuccess()

//        // Show the navigation
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Initialize UI components
        val approveDonation: Button = findViewById(R.id.approve_donation)
        val cardView1: CardView = findViewById(R.id.cardview1)
        val cardView2: CardView = findViewById(R.id.cardview2)
        val logout:Button=findViewById(R.id.logout)

        approveDonation.setOnClickListener{
            val intent = Intent(this, AdminDonationActivity::class.java)
            startActivity(intent)
        }

        // Launch RestaurantsDetails activity when cardView1 is clicked
        cardView1.setOnClickListener {
            val intent = Intent(this, RestaurantsDetails::class.java)
            startActivity(intent)
        }

        // Launch NgoDetails activity when cardView2 is clicked
        cardView2.setOnClickListener {
            val intent = Intent(this, NgoDetails::class.java)
            startActivity(intent)
        }

        // Exit the activity when the logout button is clicked
        logout.setOnClickListener {
            // Update the shared preferences to indicate that the user is not logged in
            updateLoggedInModuleStatus("Admin",false)
            FirebaseAuth.getInstance().signOut()
            // Finish the current activity
            val intent = Intent(this,OnboardScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onAdminLoginSuccess() {
        // Call updateLoggedInAdminStatus() to set the boolean flag to true
        updateLoggedInModuleStatus("Admin",true)
    }
    override fun onBackPressed() {
        doubleBackToExit()
    }
}
