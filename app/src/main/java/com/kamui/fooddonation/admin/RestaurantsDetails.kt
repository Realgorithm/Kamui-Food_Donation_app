package com.kamui.fooddonation.admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.kamui.fooddonation.R

class RestaurantsDetails : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants_details)

        // Enable the back button on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Find the card view, delete icon and add button in the layout
        val cardView = findViewById<CardView>(R.id.cardview)
        val deleteIcon = cardView.findViewById<ImageView>(R.id.delete_icon)
        val addRes = findViewById<Button>(R.id.add_res)

        // Set a click listener on the delete icon to show a confirmation dialog before deleting the card view
        deleteIcon.setOnClickListener {
            showConfirmationDialog(cardView)
        }

        // Set a click listener on the add button to start the AddRestaurant activity
        addRes.setOnClickListener {
            val intent= Intent(this, AddRestaurant::class.java)
            startActivity(intent)
        }
    }

    // Enable the back button to finish the activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Show a confirmation dialog before deleting the card view
    private fun showConfirmationDialog(cardView: View) {
        AlertDialog.Builder(this@RestaurantsDetails)
            .setMessage("Are you sure you want to delete this card?")
            .setPositiveButton("Delete") { _, _ ->
                // Delete the cardView
                (cardView.parent as ViewGroup).removeView(cardView)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
