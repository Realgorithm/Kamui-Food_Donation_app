package com.kamui.fooddonation.ngo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.kamui.fooddonation.R

class AddEmployee : AppCompatActivity() {
    // Declare ImageView
    private lateinit var imageView: ImageView

    // Create Activity Result Launcher for selecting images
    private val imageSelectionLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageView.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Find ImageView and set default image
        imageView = findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.mercy_04);

        // Find Select Image Button and set click listener to launch image selection activity
        val selectImageButton: Button = findViewById(R.id.select_image_button)
        selectImageButton.setOnClickListener {
            // Launch image selection activity
            imageSelectionLauncher.launch("image/*")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Finish activity if back button is pressed
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
