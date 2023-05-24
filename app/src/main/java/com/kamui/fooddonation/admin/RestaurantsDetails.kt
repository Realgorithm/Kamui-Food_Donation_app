package com.kamui.fooddonation.admin

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.RestaurantData

class RestaurantsDetails : AppCompatActivity() {

    private lateinit var restaurantImage: ImageView
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants_details)

        // Enable the back button on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Find the card view, delete icon and add button in the layout
        val parentView = findViewById<LinearLayout>(R.id.parent_view)

        FirebaseFirestore.getInstance().collection("users")
            .document("r23fKsDlCbMnWap4xJZ2FUQmhnq2")
            .collection("restaurant")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObjects(RestaurantData::class.java)
                for (userData in data) {
                    val cardView =LayoutInflater.from(this).inflate(R.layout.restaurant_cardview,null)
                    val restaurantName = cardView.findViewById<TextView>(R.id.restaurant_name)
                    val restaurantEmail = cardView.findViewById<TextView>(R.id.restaurant_email)
                    val restaurantAddress = cardView.findViewById<TextView>(R.id.restaurant_address)
                    restaurantImage = cardView.findViewById(R.id.restaurant_image)
                    val deleteIcon = cardView.findViewById<ImageView>(R.id.delete_icon)

                    restaurantName.text = userData?.name.toString()
                    restaurantEmail.text = userData?.email.toString()
                    restaurantAddress.text = userData?.address.toString()
                    Glide.with(this).load(userData?.imageUri).into(restaurantImage)
                    val restaurantId = userData?.id
                    // Access other fields as needed

                    // Set a click listener on the delete icon to show a confirmation dialog before deleting the card view
                    deleteIcon.setOnClickListener {
                        if (restaurantId != null) {
                            showConfirmationDialog(restaurantId, cardView)
                        }
                    }
                    // Add the new card view to the parent view
                    parentView.addView(cardView)
                }
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error getting user data", error) }


        // Set a click listener on the add button to start the AddRestaurant activity
//        addRes.setOnClickListener {
//            val intent= Intent(this, AddRestaurant::class.java)
//            startActivity(intent)
//        }
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
    private fun showConfirmationDialog(restaurantId: String, cardView: View) {
        AlertDialog.Builder(this@RestaurantsDetails)
            .setMessage("Are you sure you want to delete this card?")
            .setPositiveButton("Delete") { _, _ ->
                // Delete the cardView
                FireStoreClass().deleteUser("restaurant",restaurantId)
                (cardView.parent as ViewGroup).removeView(cardView)


            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
