package com.kamui.fooddonation

import android.app.Dialog
import android.content.Context
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kamui.fooddonation.databinding.DialogProgressBinding

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog:Dialog
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    // Declare a late init variable for the binding
    private lateinit var binding: DialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the NetworkChangeReceiver
        networkChangeReceiver = NetworkChangeReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
    }
    // Check if user is logged in for Volunteer
     fun checkUserLoggedInModules(name:String): Boolean {
        // Use SharedPreferences or any other persistent storage to get the boolean flag
        // that indicates whether the user is logged in for Restaurant or not
        val sharedPrefs = getSharedPreferences("${name}Prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedIn${name}", false)
    }
     fun updateLoggedInModuleStatus(name: String,isLoggedIn: Boolean) {
        // Use SharedPreferences or any other persistent storage to update the boolean flag
        // that indicates whether the user is logged in for Admin or not
        val sharedPrefs = getSharedPreferences("${name}Prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isLoggedIn${name}", isLoggedIn)
        editor.apply()
    }
    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /* Set the screen content from a layout resource.
   The resource will be inflated, adding all top-level views to the screen. */
        binding = DialogProgressBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(binding.root)

        binding.tvProgressText.text = text

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    open fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetLayout() {
        val mainLayout = findViewById<ViewGroup>(android.R.id.content)
        val noInternetLayout =
            LayoutInflater.from(this).inflate(R.layout.no_internet_layout, mainLayout, false)
        mainLayout.addView(noInternetLayout)
    }

    private fun hideNoInternetLayout() {
        val mainLayout = findViewById<ViewGroup>(android.R.id.content)
        val noInternetLayout = mainLayout.findViewById<View>(R.id.no_internet_layout)
        mainLayout.removeView(noInternetLayout)
    }
    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    fun hideProgressDialog() {
        if (::mProgressDialog.isInitialized && mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }
    open fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun isDarkMode(): Boolean {
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun setOnFocusChangeListener(input: EditText, hint: String, label: TextView) {
        input.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                input.hint = hint
                label.visibility = View.GONE
            } else {
                input.hint = ""
                label.visibility = View.VISIBLE
            }
        }
    }

    fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(this,
                R.color.red_200
            )
        )
        snackBar.show()
    }
    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity() // Finish all activities and exit the app
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
    
    override fun onPause() {
        super.onPause()
        hideProgressDialog()
    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgressDialog()
        // Unregister the NetworkChangeReceiver
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onResume() {
        super.onResume()

        // Check internet connectivity on resume
        val isConnected = isInternetAvailable(this)
        if (isConnected) {
            hideNoInternetLayout()
        } else {
            showNoInternetLayout()
        }
    }
}