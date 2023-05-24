package com.kamui.fooddonation.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kamui.fooddonation.AccountFragment
import com.kamui.fooddonation.R

class AdminDonationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_donation)
        if(intent.getStringExtra("previousActivity")=="Profile") {
            val fragment2 = AccountFragment()
            val bundle = Bundle()
            bundle.putString("previousActivity", "AHomePage")
            fragment2.arguments = bundle
            // Create intent to start AHomePage activity and start the activity
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment2)
                .commit()
        }
        else{
            val fragment = AdminDonationFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}