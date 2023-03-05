package com.kamui.fooddonation



import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class SignupActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val tvFirst =findViewById<TextView>(R.id.tv_firstText)
        val tvSecond=findViewById<TextView>(R.id.tv_second)

    }
}