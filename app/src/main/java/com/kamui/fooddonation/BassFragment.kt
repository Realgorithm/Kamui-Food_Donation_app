package com.kamui.fooddonation

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.kamui.fooddonation.databinding.DialogProgressBinding

open class BassFragment: Fragment() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog: Dialog
    private lateinit var binding: DialogProgressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // Check if user is logged in for Volunteer
    fun checkUserLoggedInModules(name:String): Boolean {
        // Use SharedPreferences or any other persistent storage to get the boolean flag
        // that indicates whether the user is logged in for Module or not
        val sharedPrefs = requireContext().getSharedPreferences("${name}Prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedIn${name}", false)
    }
    fun updateLoggedInModuleStatus(name: String, isLoggedIn: Boolean) {
        // Use SharedPreferences or any other persistent storage to update the boolean flag
        // that indicates whether the user is logged in for Admin or not
        val sharedPrefs = requireContext().getSharedPreferences("${name}Prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isLoggedIn${name}", isLoggedIn)
        editor.apply()
    }
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireContext())

        binding = DialogProgressBinding.inflate(LayoutInflater.from(requireContext()))
        mProgressDialog.setContentView(binding.root)

        binding.tvProgressText.text = text

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        if (::mProgressDialog.isInitialized && mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }

    fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                R.color.red_200
            )
        )
        snackBar.show()
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

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.requireActivity().onBackPressed()
            requireActivity().finishAffinity()
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            requireContext(),
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
