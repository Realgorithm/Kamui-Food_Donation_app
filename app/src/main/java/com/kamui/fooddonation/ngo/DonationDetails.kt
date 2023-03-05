package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kamui.fooddonation.Example
import com.kamui.fooddonation.R

class DonationDetails : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_details)
        // Set the title and enable the back button
        supportActionBar?.title = "Donations Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val volunteer = findViewById<Button>(R.id.add_volunteer)
        volunteer.setOnClickListener{
            val intent=Intent(this,Example::class.java)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // If the home button is pressed, finish the activity
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}