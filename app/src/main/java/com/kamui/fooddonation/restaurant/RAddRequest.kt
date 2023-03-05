package com.kamui.fooddonation.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.kamui.fooddonation.R

class RAddRequest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radd_request)

        // Set the title and enable the back button
        supportActionBar?.title = "Add Food Request"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the listeners for the food item edit text fields
        val foodItemEditText = findViewById<EditText>(R.id.et_food_item)
        val foodItemTextView = findViewById<TextView>(R.id.tv_food_item)

        foodItemEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                // If the focus is lost, show the hint text and hide the label
                foodItemEditText.hint = "Food items"
                foodItemTextView.visibility = View.GONE;
            } else {
                // If the focus is gained, hide the hint text and show the label
                foodItemEditText.hint = ""
                foodItemTextView.visibility = View.VISIBLE;
            }
        }

        // Set up the listeners for the feed count edit text fields
        val feedCountEditText = findViewById<EditText>(R.id.et_feed_count)
        val feedCountTextView = findViewById<TextView>(R.id.tv_feed_count)

        feedCountEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // If the focus is gained, hide the hint text and show the label
                feedCountEditText.hint = ""
                feedCountTextView.visibility = View.VISIBLE;
            } else {
                // If the focus is lost, show the hint text and hide the label
                feedCountEditText.hint = "Feed Counts"
                feedCountTextView.visibility = View.GONE;
            }
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
