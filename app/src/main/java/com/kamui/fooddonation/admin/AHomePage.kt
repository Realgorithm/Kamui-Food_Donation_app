package com.kamui.fooddonation.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.cardview.widget.CardView
import com.kamui.fooddonation.R

class AHomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahome_page)

        // Show the up arrow for navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize UI components
        val cardView1: CardView = findViewById(R.id.cardview1)
        val cardView2: CardView = findViewById(R.id.cardview2)
        val logout:Button=findViewById(R.id.logout)

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
            finish()
        }
    }

    // Handle navigation up arrow press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
