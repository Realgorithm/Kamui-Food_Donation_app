package com.kamui.fooddonation.slider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.kamui.fooddonation.R

class SliderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        supportActionBar?.hide()
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        viewPager.adapter = SliderAdapter(this)


    }
}