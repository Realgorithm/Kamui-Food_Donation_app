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

class NgoDetails : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cardView = findViewById<CardView>(R.id.cardview)
        val deleteIcon = cardView.findViewById<ImageView>(R.id.delete_icon)
        val addNgo = findViewById<Button>(R.id.add_ngo)

        deleteIcon.setOnClickListener {
//            (cardView.parent as ViewGroup).removeView(cardView)
            showConfirmationDialog(cardView)
        }
        addNgo.setOnClickListener {
            val intent= Intent(this, AddNgo::class.java)
            startActivity(intent)
        }

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

    private fun showConfirmationDialog(cardView: View) {
        AlertDialog.Builder(this@NgoDetails)
            .setMessage("Are you sure you want to delete this card?")
            .setPositiveButton("Delete") { _, _ ->
                // Delete the cardView
                (cardView.parent as ViewGroup).removeView(cardView)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}