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
import com.kamui.fooddonation.data.NgoData

class NgoDetails : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val parentView = findViewById<LinearLayout>(R.id.parent_view)

        FirebaseFirestore.getInstance().collection("users")
            .document("r23fKsDlCbMnWap4xJZ2FUQmhnq2")
            .collection("ngo")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObjects(NgoData::class.java)
                for (userData in data) {
                    // Create a new card view for each ngo
                    val newCardView = LayoutInflater.from(this).inflate(R.layout.ngo_cardview, null)
                    val ngoName = newCardView.findViewById<TextView>(R.id.ngo_name)
                    val ngoEmail = newCardView.findViewById<TextView>(R.id.ngo_email)
                    val ngoAddress = newCardView.findViewById<TextView>(R.id.ngo_address)
                    val ngoImage = newCardView.findViewById<ImageView>(R.id.ngo_image)
                    val deleteIcon = newCardView.findViewById<ImageView>(R.id.delete_icon)
                    // Set the text and image for each ngo
                    ngoName.text = userData?.name.toString()
                    ngoEmail.text = userData?.email.toString()
                    ngoAddress.text = userData?.address.toString()
                    Glide.with(this).load(userData?.imageUri).into(ngoImage)
                    val ngoId = userData?.ngoId
                    // Access other fields as needed

                    // Set a click listener on the delete icon to show a confirmation dialog before deleting the card view
                    deleteIcon.setOnClickListener {
                        if (ngoId != null) {
                            showConfirmationDialog(ngoId,newCardView)
                        }
                    }
                    // Add the new card view to the parent view
                    parentView.addView(newCardView)
                }
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error getting user data", error) }

//        addNgo.setOnClickListener {
//            val intent= Intent(this, AddNgo::class.java)
//            startActivity(intent)
//        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog(ngoId:String,cardView: View) {
        AlertDialog.Builder(this@NgoDetails)
            .setMessage("Are you sure you want to delete this card?")
            .setPositiveButton("Delete") { _, _ ->
                // Delete the cardView
                FireStoreClass().deleteUser("ngo",ngoId)
                (cardView.parent as ViewGroup).removeView(cardView)

            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}