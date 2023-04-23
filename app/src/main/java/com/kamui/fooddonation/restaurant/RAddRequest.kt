package com.kamui.fooddonation.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.kamui.fooddonation.R
import com.kamui.fooddonation.volunteer.Donation

class RAddRequest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radd_request)

        // Set the title and enable the back button
        supportActionBar?.title = "Add Food Request"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the listeners for the food item edit text fields
        val foodTypeInput = findViewById<EditText>(R.id.foodTypeInput)
        val foodTypeLabel = findViewById<TextView>(R.id.foodTypeLabel)
        val quantityInput = findViewById<EditText>(R.id.quantityInput)
        val quantityLabel = findViewById<TextView>(R.id.quantityLabel)
        val expirationDateInput = findViewById<EditText>(R.id.expirationDateInput)
        val expirationDateLabel = findViewById<TextView>(R.id.expirationDateLabel)

        val addDonationBtn = findViewById<Button>(R.id.uploadBtn)
        addDonationBtn.setOnClickListener {
            val foodType = foodTypeInput.text.toString()
            val quantity = quantityInput.text.toString()
            val expirationDate = expirationDateInput.text.toString()

            // Create a new food donation object
//            val donation = Donation(foodType, quantity, expirationDate)

            foodTypeInput.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    // If the focus is lost, show the hint text and hide the label
                    foodTypeInput.hint = "Food items"
                    foodTypeLabel.visibility = View.GONE;
                } else {
                    // If the focus is gained, hide the hint text and show the label
                    foodTypeInput.hint = ""
                    foodTypeLabel.visibility = View.VISIBLE;
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

