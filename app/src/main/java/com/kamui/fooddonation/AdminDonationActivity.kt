package com.kamui.fooddonation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kamui.fooddonation.admin.AdminDonationFragment

class AdminDonationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_donation)
        val fragment = AdminDonationFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}