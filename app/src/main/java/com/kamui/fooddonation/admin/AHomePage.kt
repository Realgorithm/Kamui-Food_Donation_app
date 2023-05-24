package com.kamui.fooddonation.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.OnboardScreen
import com.kamui.fooddonation.R


class AHomePage : BaseActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
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
        val resCount :TextView =findViewById(R.id.res_count)
        val ngoCount :TextView =findViewById(R.id.ngo_count)
        val profile: Button = findViewById(R.id.profile)
        val logout:Button=findViewById(R.id.logout)


        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            // Send the token to your server so it can send FCM messages to this app
        }
        FirebaseMessaging.getInstance().subscribeToTopic("Admin-app")
        approveDonation.setOnClickListener{
            val intent = Intent(this, AdminDonationActivity::class.java)
            startActivity(intent)
        }

        FirebaseFirestore.getInstance().collection("users").document("r23fKsDlCbMnWap4xJZ2FUQmhnq2").collection("restaurant")
            .get()
            .addOnSuccessListener {querySnapshot ->
                val count = querySnapshot.size()
                resCount.text = count.toString()
            }

        FirebaseFirestore.getInstance().collection("users").document("r23fKsDlCbMnWap4xJZ2FUQmhnq2").collection("ngo")
            .get()
            .addOnSuccessListener {querySnapshot ->
                val count = querySnapshot.size()
                ngoCount.text = count.toString()
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

        profile.setOnClickListener{
            val intent = Intent(this, AdminDonationActivity::class.java)
            intent.putExtra("previousActivity","Profile")
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
