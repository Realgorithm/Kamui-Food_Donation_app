package com.kamui.fooddonation.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.kamui.fooddonation.R

class AccountFragment : Fragment() {

    companion object {
        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.account_fragment, container, false)
        val nameTextView = view.findViewById<TextView>(R.id.name_text_view)
        val contactTextView = view.findViewById<TextView>(R.id.contact_text_view)
        val editProfileButton = view.findViewById<Button>(R.id.edit_profile_button)
        val notificationSettingsButton = view.findViewById<Button>(R.id.notification_settings_button)
        val logoutButton = view.findViewById<Button>(R.id.logout_button)
        val deleteAccountButton = view.findViewById<Button>(R.id.delete_account_button)

        // Set the profile information
        nameTextView.text = "John Doe"
        contactTextView.text = "johndoe@email.com"

        // Handle the edit profile button click
        editProfileButton.setOnClickListener {
            // Start the edit profile activity
        }

        // Handle the notification settings button click
        notificationSettingsButton.setOnClickListener {
            // Start the notification settings activity
        }

        // Handle the logout button click
        logoutButton.setOnClickListener {
            // Logout the user
            activity?.onBackPressed()
        }

        // Handle the delete account button click
        deleteAccountButton.setOnClickListener {
            // Delete the user's account
        }
        return view
        }
    }

