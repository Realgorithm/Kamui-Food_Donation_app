package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kamui.fooddonation.data.AdminData
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.data.ReceiverData
import com.kamui.fooddonation.data.RestaurantData
import com.kamui.fooddonation.data.VolunteerData

class AccountFragment() : BassFragment() {

    companion object {
        fun newInstance(previousActivity: String): AccountFragment {
            val fragment = AccountFragment()
            val args = Bundle()
            args.putString("previousActivity", previousActivity)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.account_fragment, container, false)
        val nameTextView = view.findViewById<TextView>(R.id.name_text_view)
        val contactTextView = view.findViewById<TextView>(R.id.contact_text_view)
        val roleTextView = view.findViewById<TextView>(R.id.role_text_view)
        val profileImageView =view.findViewById<ImageView>(R.id.profile_image)
        val editProfileButton = view.findViewById<Button>(R.id.edit_profile_button)
        val rewardPoint =view.findViewById<TextView>(R.id.rewards_point)
        val notificationSettingsButton = view.findViewById<Button>(R.id.notification_settings_button)
        val logoutButton = view.findViewById<Button>(R.id.logout_button)
        val deleteAccountButton = view.findViewById<Button>(R.id.delete_account_button)
        val userUid=FireStoreClass().getCurrentUserID()
        var keyValue = ""

        val (childCollection, childData) = when (arguments?.getString("previousActivity")) {
            "RHomePage" -> "restaurant" to RestaurantData::class.java
            "VHomePage" -> "volunteer" to VolunteerData::class.java
            "AHomePage" -> "admin" to AdminData::class.java
            "RcHomePage" -> "receiver" to ReceiverData::class.java
            "NHomePage" -> "ngo" to NgoData::class.java

            else -> throw IllegalArgumentException("Invalid previousActivity")
        }

        // Get the reward values for donation claimed
        FirebaseFirestore.getInstance().collection("donations")
            .whereEqualTo("volunteerId", userUid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var totalRewardPoints = 0
                for (document in querySnapshot.documents) {
                    val donation = document.toObject(Donation::class.java)
                    donation?.let {
                        totalRewardPoints = it.reward
                    }
                }
                rewardPoint.text = "Your Reward: ${(totalRewardPoints*10)} points"
            }

        // Get the reward values for made donation
        FirebaseFirestore.getInstance().collection("donations")
            .whereEqualTo("donorId", userUid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var totalRewardPoints = 0
                for (document in querySnapshot.documents) {
                    val donation = document.toObject(Donation::class.java)
                    donation?.let {
                        totalRewardPoints = it.reward
                    }
                }
                rewardPoint.text = "Your Reward: ${(totalRewardPoints*10)} points"
            }

        // Get the reward values for Claimed by donation
        FirebaseFirestore.getInstance().collection("donations")
            .whereEqualTo("receiverId", userUid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var totalRewardPoints = 0
                for (document in querySnapshot.documents) {
                    val donation = document.toObject(Donation::class.java)
                    donation?.let {
                        totalRewardPoints = it.reward
                    }
                }
                rewardPoint.text = "Your Reward: ${(totalRewardPoints*5)} points"
            }
        FireStoreClass().getUserData(childCollection,childData,userUid){userData ->
            // Set the profile information
            when(userData) {
                is VolunteerData -> {
                    keyValue ="volunteer"
                    nameTextView.text = "Hello ${userData.name}!"
                    contactTextView.text = userData.email
                    roleTextView.text = userData.role
                    Glide.with(this).load(userData.imageUri).into(profileImageView)


                }
                is RestaurantData -> {
                    keyValue="restaurant"
                    nameTextView.text = "Hello ${userData.name}!"
                    contactTextView.text = userData.email
                    roleTextView.text = userData.role
                    Glide.with(this).load(userData.imageUri).into(profileImageView)

                }
                is AdminData -> {
                    keyValue ="admin"
                    nameTextView.text = "Hello ${userData.name}!"
                    contactTextView.text = userData.email
                    roleTextView.text = userData.role
                    Glide.with(this).load(userData.imageUri).into(profileImageView)
                }
                is NgoData -> {
                    keyValue="ngo"
                    nameTextView.text = "Hello ${userData.name}!"
                    contactTextView.text = userData.email
                    roleTextView.text = userData.role
                    Glide.with(this).load(userData.imageUri).into(profileImageView)

                }
                is ReceiverData -> {
                    keyValue="receiver"
                    nameTextView.text = "Hello ${userData.name}!"
                    contactTextView.text = userData.email
                    roleTextView.text = userData.role
                    Glide.with(this).load(userData.imageUri).into(profileImageView)
                }
            }

            // Handle the edit profile button click
            editProfileButton.setOnClickListener {
                val intent =Intent(requireContext(),EditProfile::class.java)
                intent.putExtra("previousActivity",keyValue)
                startActivity(intent)
            }

            // Handle the delete account button click
            deleteAccountButton.setOnClickListener {
                // Prompt the user to enter their password
                val passwordEditText = EditText(requireContext())
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirm your password")
                    .setMessage("Enter your password to confirm account deletion:")
                    .setView(passwordEditText)
                    .setPositiveButton("Delete") { _, _ ->
                        val user = Firebase.auth.currentUser!!
                        val credential = EmailAuthProvider.getCredential(user.email!!, passwordEditText.text.toString())

                        // Re-authenticate the user with their current credentials
                        user.reauthenticate(credential)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    // Delete the user's account
                                    user.delete()
                                        .addOnCompleteListener { deleteTask ->
                                            if (deleteTask.isSuccessful) {
                                                FireStoreClass().deleteUser(keyValue, userUid)
                                                val intent = Intent(requireContext(), OnboardScreen::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                requireActivity().finish()
                                                Log.d(TAG, "User account deleted.")
                                            } else {
                                                // Handle the error
                                                Log.e(TAG, "Error deleting user account", deleteTask.exception)
                                                Toast.makeText(requireContext(), "Error deleting user account", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    // Handle the error
                                    Log.e(TAG, "Error authenticating user", authTask.exception)
                                    Toast.makeText(requireContext(), "Error authenticating user", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()

                // Set the input type of the password field to PASSWORD
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

        }

        // Handle the notification settings button click
        notificationSettingsButton.setOnClickListener {
            // Start the notification settings activity
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            startActivity(intent)
        }


        // Handle the logout button click
        logoutButton.setOnClickListener {
            updateLoggedInModuleStatus("Restaurant",false)
            updateLoggedInModuleStatus("Volunteer",false)
            // Logout the user
            FirebaseAuth.getInstance().signOut()
            // Finish the current activity
            val intent = Intent(requireContext(), OnboardScreen::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
        }

}

