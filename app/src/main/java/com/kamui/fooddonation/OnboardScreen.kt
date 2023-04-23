package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.kamui.fooddonation.admin.AloginPage
import com.kamui.fooddonation.ngo.NHomePage
import com.kamui.fooddonation.ngo.NLoginPage
import com.kamui.fooddonation.receiver.RcLoginPage
import com.kamui.fooddonation.restaurant.RloginPage
import com.kamui.fooddonation.volunteer.VLoginPage

class OnboardScreen : AppCompatActivity() {

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var scrollView: HorizontalScrollView

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)


        // Get references to the buttons
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)

        scrollView = findViewById(R.id.scroll_view)

        val textView = findViewById<TextView>(R.id.text_view)
        val textView2 = findViewById<TextView>(R.id.tv_change)

        val handler = Handler()
        val runnable = object : Runnable {
            var count = 0
            @SuppressLint("SetTextI18n")
            override fun run() {
                when (count % 3) {
                    0 -> {
                        textView.text = "40% of india's food ends up in the bin."
                        textView2.text = "When we waste food, we waste the opportunity to feed a hungry person."
                    }
                    1 -> {
                        textView.text = "Reflect, \nRethink,\nReconsider.\nWhy Food waste is major problem"
                        textView2.text = "Approximately 690 million people in the world go hungry, which is about 9% of the global population"
                    }
                    2 -> {
                        textView.text = "Wasting food is like stealing from the table of those who are poor and hungry"
                        textView2.text = "The amount of food wasted in wealthy countries is roughly equivalent to the entire net food production of sub-Saharan Africa"
                    }

                }
                count++
                handler.postDelayed(this, 4000) // Delay of 4 seconds (4000 milliseconds)
            }
        }

        // Start the first update after a delay
        handler.postDelayed(runnable, 1000) // Delay of 1 seconds (1000 milliseconds)


        scrollView.post {
            val center = scrollView.width / 2
            val position = center - (button3.width / 2)
            scrollView.smoothScrollTo(button3.left - position, 0)

            button1.isEnabled = false
            button2.isEnabled = false
            button4.isEnabled = false
            button3.isEnabled = true
            button5.isEnabled = false
        }
        scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            val center = scrollView.width / 2
            val position = scrollView.scrollX + center

            Log.d("tabish","value $position")

            if (position < button1.left) {
                scrollView.smoothScrollTo(button1.left, 0)
                return@setOnScrollChangeListener
            }
//
            if (position > button5.right) {
                scrollView.smoothScrollTo(button5.right - scrollView.width, 0)
                return@setOnScrollChangeListener
            }
            val button = when {
                position < button2.left -> button1
                position < button3.left -> button2
                position < button4.left -> button3
                position < button5.left -> button4
                else -> button5
            }

            button1.isEnabled = button == button1
            button2.isEnabled = button == button2
            button3.isEnabled = button == button3
            button4.isEnabled = button == button4
            button5.isEnabled = button == button5
        }

        // Fade in effect for the buttons
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 3000
        button1.startAnimation(fadeInAnimation)
        button2.startAnimation(fadeInAnimation)
        button3.startAnimation(fadeInAnimation)
        button4.startAnimation(fadeInAnimation)
        button5.startAnimation(fadeInAnimation)

        button1.setOnClickListener{
//            val intent = Intent(this, NLoginPage::class.java)
            val intent = Intent(this, NLoginPage::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener{
            val intent = Intent(this, RcLoginPage::class.java)
            startActivity(intent)
        }
        button3.setOnClickListener{
            val intent = Intent(this, VLoginPage::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener{
            val intent = Intent(this, RloginPage::class.java)
            startActivity(intent)
        }
        button5.setOnClickListener{
            val intent = Intent(this, AloginPage::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}