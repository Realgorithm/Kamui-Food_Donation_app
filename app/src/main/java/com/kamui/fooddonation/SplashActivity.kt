package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import com.kamui.fooddonation.admin.AHomePage
import com.kamui.fooddonation.ngo.NHomePage
import com.kamui.fooddonation.receiver.RcHomePage
import com.kamui.fooddonation.restaurant.RHomePage
import com.kamui.fooddonation.slider.SliderActivity
import com.kamui.fooddonation.volunteer.VHomePage
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val delayTime: Long = 2000
        val videoUri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.app_logo)
        videoView.setVideoURI(videoUri)

        videoView.setOnCompletionListener { startMainActivity(videoView) }

        videoView.start()

        // Check if user is logged in for Admin
        val isLoggedInRestaurant = checkUserLoggedInModules("restaurant")

        // Check if user is logged in for Admin
        val isLoggedInAdmin = checkUserLoggedInModules("Admin")

        // Check if user is logged in for NGO
        val isLoggedInNgo = checkUserLoggedInModules("ngo")

        // Check if user is logged in for NGO
        val isLoggedInReceiver = checkUserLoggedInModules("receiver")

        // Check if user is logged in for Volunteer
        val isLoggedInVolunteer = checkUserLoggedInModules("volunteer")

        // Redirect to the appropriate activity based on the user's login and internet connection
        if (isLoggedInAdmin) {
            redirectToHomePage(delayTime, Intent(this@SplashActivity, AHomePage::class.java)) // Admin homepage
        } else if (isLoggedInNgo) {
            redirectToHomePage(2000, Intent(this@SplashActivity, NHomePage::class.java)) // Ngo homepage
        } else if (isLoggedInReceiver) {
            redirectToHomePage(2000, Intent(this@SplashActivity, RcHomePage::class.java)) // Receiver homepage
        } else if (isLoggedInVolunteer) {
            redirectToHomePage(2000, Intent(this@SplashActivity, VHomePage::class.java)) // Volunteer homepage
        } else if (isLoggedInRestaurant) {
            redirectToHomePage(2000, Intent(this@SplashActivity, RHomePage::class.java)) // Restaurant homepage
        } else {
            redirectToHomePage(3000, Intent(this@SplashActivity, SliderActivity::class.java))
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun redirectToHomePage(delayMillis: Long, intent: Intent) {
        GlobalScope.launch(Dispatchers.Main) {
            delay(delayMillis)
            startActivity(intent)
            finish()
        }
    }

    private fun startMainActivity(videoView: VideoView) {
        // Implement the logic for starting the main activity here
        videoView.resume()
    }
}
