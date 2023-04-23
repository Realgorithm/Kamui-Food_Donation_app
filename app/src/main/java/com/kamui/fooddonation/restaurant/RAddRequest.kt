
package com.kamui.fooddonation.restaurant

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.RestaurantData
import java.util.Locale

class RAddRequest : BaseActivity() {

    private val datePattern = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/\\d{4}$"
    private val validFoodTypes = listOf("veg", "non-veg", "perishable", "non-perishable")
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radd_request)

        // Set the title and enable the back button
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_title)

        val title = findViewById<TextView>(R.id.action_bar_title)
        title.text = "Add Food Request"

        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)

//        supportActionBar?.title = "Add Food Request"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the listeners for the food item edit text fields
        val foodNameInput = findViewById<EditText>(R.id.food_name_input)
        val foodNameLabel = findViewById<TextView>(R.id.foodNameLabel)
        val foodTypeInput = findViewById<EditText>(R.id.foodTypeInput)
        val foodTypeLabel = findViewById<TextView>(R.id.foodTypeLabel)
        val quantityInput = findViewById<EditText>(R.id.quantityInput)
        val quantityLabel = findViewById<TextView>(R.id.quantityLabel)
        val foodQuantitySpinner = findViewById<Spinner>(R.id.foodQuantitySpinner)
        val expirationDateInput = findViewById<EditText>(R.id.expirationDateInput)
        val expirationDateLabel = findViewById<TextView>(R.id.expirationDateLabel)

        foodNameInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // If the focus is lost, show the hint text and hide the label
                foodNameInput.hint = "Food Name"
                foodNameLabel.visibility = View.GONE
            } else {
                // If the focus is gained, hide the hint text and show the label
                foodNameInput.hint = ""
                foodNameLabel.visibility = View.VISIBLE;
            }
        }
        foodTypeInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // If the focus is lost, show the hint text and hide the label
                foodTypeInput.hint = "Food Type"
                foodTypeLabel.visibility = View.GONE
            } else {
                // If the focus is gained, hide the hint text and show the label
                foodTypeInput.hint = ""
                foodTypeLabel.visibility = View.VISIBLE;
            }
        }
        quantityInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // If the focus is gained, hide the hint text and show the label
                quantityInput.hint = ""
                quantityLabel.visibility = View.VISIBLE;
            } else {
                // If the focus is lost, show the hint text and hide the label
                quantityInput.hint = "Quantity"
                quantityLabel.visibility = View.GONE;
            }
        }
        expirationDateInput.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // If the focus is gained, hide the hint text and show the label
                expirationDateInput.hint = ""
                expirationDateLabel.visibility = View.VISIBLE;
            } else {
                // If the focus is lost, show the hint text and hide the label
                expirationDateInput.hint = "Expiry Date"
                expirationDateLabel.visibility = View.GONE;
            }
        }

        val foodTypes = arrayOf("kg", "litre", "piece", "slices")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, foodTypes)

        foodQuantitySpinner.adapter = adapter


        val addDonationBtn = findViewById<Button>(R.id.uploadBtn)

        addDonationBtn.setOnClickListener {
            // create an instance of FirestoreClass
            val fireStoreClass = FireStoreClass()
            val textFoodName = foodNameInput.text.toString()
            val textFoodType = foodTypeInput.text.toString()
            val textFoodQuantity = quantityInput.text.toString()
            val textExpirationDate = expirationDateInput.text.toString()
            val moduleData = RestaurantData::class.java
            // replace with the actual user ID
            val moduleCollection = "restaurant" // replace with the actual module collection name
            // Clear the input fields after the donation has been added
            if (validateForm(textFoodName, textFoodType, textFoodQuantity, textExpirationDate)) {
                foodNameInput.text.clear()
                foodTypeInput.text.clear()
                quantityInput.text.clear()
                expirationDateInput.text.clear()
                fireStoreClass.getUserData(moduleCollection, moduleData) { userData ->
                    // handle the user data here
                    val restaurantName = userData?.restaurantName.toString()
                    val pickUpAddress = userData?.location!!

                    // Use the new ID to create the donation
                    val donation = Donation(
                        donor = restaurantName,
                        donorId = fireStoreClass.getCurrentUserID(),
                        pickupAddress = pickUpAddress,
                        status = "unapproved",
                        foodName = textFoodName,
                        foodQuantity = textFoodQuantity,
                        foodType = textFoodType,
                        expiryDate = textExpirationDate
                    )
                    // call the addDonation function with the Donation object and a completion block
                    fireStoreClass.addDonation(donation) {
                        // handle completion here
                        Toast.makeText(
                            this,
                            "Donation added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
    private fun validateForm(textFoodName:String,textFoodType:String,textFoodQuantity:String,textExpirationDate:String): Boolean {
        return when {
            ((TextUtils.isEmpty(textFoodName))||(TextUtils.isEmpty(textFoodType))||(TextUtils.isEmpty(textFoodQuantity))||(TextUtils.isEmpty(textExpirationDate)))->{
                showErrorSnackBar("Please filled every field")
                false
            }
            (!validFoodTypes.contains(textFoodType.toLowerCase(Locale.ROOT))) -> {
                showErrorSnackBar("please enter valid food type eg.(veg, non-veg)")
                false
            }
            (!textExpirationDate.matches(Regex(datePattern)))->{
                showErrorSnackBar("Please enter date in correct format eg.(dd/mm/yyyy)")
                false
            }
            else -> {
                true
            }
        }
    }
}

