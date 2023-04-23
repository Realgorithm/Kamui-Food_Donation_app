package com.kamui.fooddonation

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kamui.fooddonation.databinding.DialogProgressBinding

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog:Dialog
    // Declare a late init variable for the binding
    private lateinit var binding: DialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}