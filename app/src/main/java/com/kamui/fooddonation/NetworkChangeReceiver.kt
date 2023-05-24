package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class NetworkChangeReceiver : BroadcastReceiver() {

    private var noInternetLayout: View? = null

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (!isConnected(context)) {
            showNoInternetLayout(context)
        } else {
            hideNoInternetLayout(context)
        }
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showNoInternetLayout(context: Context) {
        val view = (context as AppCompatActivity).window.decorView.rootView
        val mainLayout = view.findViewById<ViewGroup>(android.R.id.content)

        // Remove any existing no internet layout
        hideNoInternetLayout(context)

        // Inflate the no internet layout and add it to the main layout
        noInternetLayout = LayoutInflater.from(context)
            .inflate(R.layout.no_internet_layout, mainLayout, false)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        mainLayout.addView(noInternetLayout, layoutParams)
    }

    private fun hideNoInternetLayout(context: Context) {
        val view = (context as AppCompatActivity).window.decorView.rootView
        val mainLayout = view.findViewById<ViewGroup>(android.R.id.content)
        if (noInternetLayout != null) {
            mainLayout.removeView(noInternetLayout)
            noInternetLayout = null
        }
    }
}


