package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import com.kamui.fooddonation.admin.AloginPage
import com.kamui.fooddonation.slider.SliderActivity
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.app_logo)
        videoView.setVideoURI(videoUri)

        videoView.setOnCompletionListener { startMainActivity(videoView) }

        videoView.start()

        GlobalScope.launch(Dispatchers.Main) {
            delay(4000)
            startActivity(Intent(this@SplashActivity, SliderActivity::class.java))
//            startActivity(Intent(this@SplashActivity, AloginPage::class.java))
            finish()
        }
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
    }

    private fun startMainActivity(videoView: VideoView) {

    }
}
