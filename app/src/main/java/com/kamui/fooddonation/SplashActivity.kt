package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kamui.fooddonation.ngo.NloginPage
import com.kamui.fooddonation.slider.SliderActivity
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        // Display the splash screen for 2 seconds
//        Handler().postDelayed({
////            startActivity(Intent(this, SliderActivity::class.java))
//            startActivity(Intent(this, AHomePage::class.java))
//            finish()
//        }, 2000)
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            startActivity(Intent(this@SplashActivity, SliderActivity::class.java))
//            startActivity(Intent(this@SplashActivity, NloginPage::class.java))
            finish()
        }
    }
}