package com.kamui.fooddonation.admin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kamui.fooddonation.R
import com.kamui.fooddonation.SignInActivity
import com.kamui.fooddonation.SignupActivity

class ALoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)

        button1.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            intent.putExtra("previousActivity", "ALoginPage")
            startActivity(intent)
//            onAdminLoginSuccess()
        }
        button2.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            intent.putExtra("previousActivity", "ALoginPage")
            startActivity(intent)
        }

    }
}