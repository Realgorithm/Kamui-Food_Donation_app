package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kamui.fooddonation.admin.AHomePage
import com.kamui.fooddonation.ngo.NHomePage
import com.kamui.fooddonation.receiver.RcHomePage
import com.kamui.fooddonation.restaurant.RHomePage
import com.kamui.fooddonation.volunteer.VHomePage

class SignInActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val signInButton = findViewById<Button>(R.id.button1)
        signInButton.setOnClickListener {
            when (intent.getStringExtra("previousActivity")) {
                "RHomePage" -> {
                    val rHomeIntent = Intent(this, RHomePage::class.java)
                    startActivity(rHomeIntent)
                    finish()
                }
                "NHomePage" -> {
                    val nHomeIntent = Intent(this, NHomePage::class.java)
                    startActivity(nHomeIntent)
                    finish()
                }
                "AHomePage" -> {
                    val aHomeIntent = Intent(this, AHomePage::class.java)
                    startActivity(aHomeIntent)
                    finish()
                }
                "RcHomePage" -> {
                    val rrHomeIntent = Intent(this, RcHomePage::class.java)
                    startActivity(rrHomeIntent)
                    finish()
                }
                "VHomePage" -> {
                    val vHomeIntent = Intent(this, VHomePage::class.java)
                    startActivity(vHomeIntent)
                    finish()
                }
            }
        }
    }
}