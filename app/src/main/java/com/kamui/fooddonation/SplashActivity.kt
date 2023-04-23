package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
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
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.app_logo)
        videoView.setVideoURI(videoUri)

        videoView.setOnCompletionListener { startMainActivity(videoView) }

        videoView.start()

        // Check if user is logged in for Admin
        val isLoggedInRestaurant = checkUserLoggedInRestaurant()

        // Check if user is logged in for Admin
        val isLoggedInAdmin = checkUserLoggedInModules("Admin")

        // Check if user is logged in for NGO
        val isLoggedInNgo = checkUserLoggedInNgo()

        // Check if user is logged in for NGO
        val isLoggedInReceiver = checkUserLoggedInReceiver()

        // Check if user is logged in for Volunteer
        val isLoggedInVolunteer = checkUserLoggedInVolunteer()

//         If user is logged in for Admin, redirect to Admin homepage
        if (isLoggedInAdmin) {
            redirectToAdminHomePage()
        }
        // If user is logged in for NGO, redirect to NGO homepage
        else if(isLoggedInNgo){
            redirectToNgoHomePage()
        }
        // If user is logged in for NGO, redirect to NGO homepage
        else if(isLoggedInReceiver){
            redirectToReceiverHomePage()
        }
        else if(isLoggedInVolunteer){
            redirectToVolunteerHomePage()
        }
        else if(isLoggedInRestaurant){
            redirectToRestaurantHomePage()
        }
        else{
            showSliderScreens()
        }

    }
    // Check if user is logged in for Restaurant
    private fun checkUserLoggedInRestaurant(): Boolean {
        // Use SharedPreferences or any other persistent storage to get the boolean flag
        // that indicates whether the user is logged in for Restaurant or not
        val sharedPrefs = getSharedPreferences("RestaurantPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedInRestaurant", false)
    }

    // Check if user is logged in for NGO
    private fun checkUserLoggedInNgo(): Boolean {
        // Use SharedPreferences or any other persistent storage to get the boolean flag
        // that indicates whether the user is logged in for Module1 or not
        val sharedPrefs = getSharedPreferences("NgoPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedInNgo", false)
    }

    // Check if user is logged in for Admin
//    private fun checkUserLoggedInAdmin(): Boolean {
//        // Use SharedPreferences or any other persistent storage to get the boolean flag
//        // that indicates whether the user is logged in for Module1 or not
//        val sharedPrefs = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
//        return sharedPrefs.getBoolean("isLoggedInAdmin", false)
//    }

    // Check if user is logged in for Receiver
    private fun checkUserLoggedInReceiver(): Boolean {
        // Use SharedPreferences or any other persistent storage to get the boolean flag
        // that indicates whether the user is logged in for Module1 or not
        val sharedPrefs = getSharedPreferences("ReceiverPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedInReceiver", false)
    }

    // Check if user is logged in for Volunteer
    private fun checkUserLoggedInVolunteer(): Boolean {
        // Use SharedPreferences or any other persistent storage to get the boolean flag
        // that indicates whether the user is logged in for Restaurant or not
        val sharedPrefs = getSharedPreferences("VolunteerPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedInVolunteer", false)
    }


    // Redirect to Ngo homepage
    @OptIn(DelicateCoroutinesApi::class)
    private fun redirectToNgoHomePage() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(4000)
            startActivity(Intent(this@SplashActivity, NHomePage::class.java))
            finish()
        }
    }
    // Redirect to Admin homepage
    @OptIn(DelicateCoroutinesApi::class)
    private fun redirectToAdminHomePage() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(4000)
            startActivity(Intent(this@SplashActivity, AHomePage::class.java))
            finish()
        }
    }
    // Redirect to Receiver homepage
    @OptIn(DelicateCoroutinesApi::class)
    private fun redirectToReceiverHomePage() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            startActivity(Intent(this@SplashActivity, RcHomePage::class.java))
            finish()
        }
    }
    // Show login screens
    @OptIn(DelicateCoroutinesApi::class)
    private fun showSliderScreens() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(4000)
            startActivity(Intent(this@SplashActivity, SliderActivity::class.java))
//            startActivity(Intent(this@SplashActivity, ALoginPage::class.java))
            finish()
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun redirectToRestaurantHomePage() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(4000)
            startActivity(Intent(this@SplashActivity, RHomePage::class.java))
//            startActivity(Intent(this@SplashActivity, ALoginPage::class.java))
            finish()
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun redirectToVolunteerHomePage() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(4000)
            startActivity(Intent(this@SplashActivity, VHomePage::class.java))
//            startActivity(Intent(this@SplashActivity, ALoginPage::class.java))
            finish()
        }
    }
    private fun startMainActivity(videoView: VideoView) {

    }
}
