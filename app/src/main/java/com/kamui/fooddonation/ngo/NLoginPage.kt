package com.kamui.fooddonation.ngo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kamui.fooddonation.R
import com.kamui.fooddonation.SignInActivity
import com.kamui.fooddonation.SignupActivity

class NLoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        // Initialize and set click listener for sign-in button
        val signInButton = findViewById<Button>(R.id.button1)
        signInButton.setOnClickListener {
            // Start sign-in activity and pass the previous activity's name as an extra
            val intent = Intent(this, SignInActivity::class.java)
            intent.putExtra("previousActivity", "NHomePage")
            startActivity(intent)
        }

        // Initialize and set click listener for sign-up button
        val signUpButton = findViewById<Button>(R.id.button2)
        signUpButton.setOnClickListener {
            // Start sign-up activity
            val intent = Intent(this, SignupActivity::class.java)
            intent.putExtra("previousActivity", "NHomePage")
            startActivity(intent)
        }
    }
}
