package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kamui.fooddonation.admin.AHomePage
import com.kamui.fooddonation.databinding.DialogProgressBinding
import com.kamui.fooddonation.ngo.NHomePage
import com.kamui.fooddonation.receiver.RcHomePage
import com.kamui.fooddonation.restaurant.RHomePage
import com.kamui.fooddonation.volunteer.VHomePage


class SignupActivity : AppCompatActivity() {
    private lateinit var mProgressDialog:Dialog
    // Declare a late init variable for the binding
    private lateinit var binding: DialogProgressBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signUpButton = findViewById<Button>(R.id.button2)

//        common fields
        val tvFirst =findViewById<TextView>(R.id.tv_firstText)
        val tvSecond=findViewById<TextView>(R.id.tv_second)
        val emailInput=findViewById<EditText>(R.id.email_input)
        val passwordInput=findViewById<EditText>(R.id.password_input)
        val confirmPasswordInput=findViewById<EditText>(R.id.confirm_password_input)
        val fullNameInput=findViewById<EditText>(R.id.full_name_input)
        val addressInput =findViewById<EditText>(R.id.address_input)
        val phoneInput=findViewById<EditText>(R.id.phone_input)
//        Admin controls
        val adminDesignation=findViewById<EditText>(R.id.admin_designation)
        val radioGroupAccess=findViewById<RadioGroup>(R.id.access_level_radio)

//        Restaurants controls
        val restaurantName=findViewById<EditText>(R.id.restaurant_name_input)
        val radioGroupResCuisine=findViewById<RadioGroup>(R.id.res_cuisine_radio)
        val radioGroupResFoodType=findViewById<RadioGroup>(R.id.res_food_type_radio)
        val radioGroupResFoodPickup=findViewById<RadioGroup>(R.id.res_food_pickup_radio)

//        NGO controls
        val ngoName=findViewById<EditText>(R.id.ngo_name_input)
        val radioGroupNgoFoodType=findViewById<RadioGroup>(R.id.ngo_food_type_radio)
        val radioGroupNgoOrganization=findViewById<RadioGroup>(R.id.ngo_organization_radio)
        val radioGroupNgoFoodPickup=findViewById<RadioGroup>(R.id.ngo_food_pickup_radio)
        val ngoRegistration=findViewById<EditText>(R.id.ngo_registration_no)

        //Receiver controls
        val revPickupTime=findViewById<EditText>(R.id.rev_pickup_time)

        //Volunteer controls
        val radioGroupVolAvailability=findViewById<RadioGroup>(R.id.vol_availability_radio)
        val radioGroupVolPreferences=findViewById<RadioGroup>(R.id.vol_pref_radio)

        // all Five Fields
        val restaurantFields = findViewById<LinearLayout>(R.id.restaurant_fields)
        val receiverFields= findViewById<LinearLayout>(R.id.receiver_fields)
        val ngoFields= findViewById<LinearLayout>(R.id.ngo_fields)
        val volunteerFields= findViewById<LinearLayout>(R.id.volunteer_fields)
        val adminFields= findViewById<LinearLayout>(R.id.admin_fields)

        when(intent.getStringExtra("previousActivity")){
            "RHomePage" -> {
                restaurantFields.visibility = View.VISIBLE
            }
            "RcHomePage" -> {
                receiverFields.visibility = View.VISIBLE
            }
            "NHomePage" -> {
                ngoFields.visibility = View.VISIBLE
            }
            "AHomePage" -> {
                adminFields.visibility = View.VISIBLE
            }
            "VHomePage" -> {
                volunteerFields.visibility = View.VISIBLE
            }
        }
        signUpButton.setOnClickListener {

            val selectedAdminAccessId = radioGroupAccess.checkedRadioButtonId
            val radioButtonAdminAccess=findViewById<RadioButton>(selectedAdminAccessId)

            val selectedResCuisineId = radioGroupResCuisine.checkedRadioButtonId
            val radioButtonResCuisine=findViewById<RadioButton>(selectedResCuisineId)
            val selectedResFoodTypeId = radioGroupResFoodType.checkedRadioButtonId
            val radioButtonResFoodType =findViewById<RadioButton>(selectedResFoodTypeId)
            val selectedResFoodPickupId = radioGroupResFoodPickup.checkedRadioButtonId
            val radioButtonResFoodPickup =findViewById<RadioButton>(selectedResFoodPickupId)

            val selectedNgoFoodTypeId = radioGroupNgoFoodType.checkedRadioButtonId
            val radioButtonNgoFoodType=findViewById<RadioButton>(selectedNgoFoodTypeId)
            val selectedNgoOrganizationId = radioGroupNgoOrganization.checkedRadioButtonId
            val radioButtonNgoOrganization=findViewById<RadioButton>(selectedNgoOrganizationId)
            val selectedNgoFoodPickupId = radioGroupNgoFoodPickup.checkedRadioButtonId
            val radioButtonNgoFoodPickup=findViewById<RadioButton>(selectedNgoFoodPickupId)

            val selectedVolAvailabilityId = radioGroupVolAvailability.checkedRadioButtonId
            val radioButtonVolAvailability=findViewById<RadioButton>(selectedVolAvailabilityId)
            val selectedVolPreferencesId = radioGroupVolPreferences.checkedRadioButtonId
            val radioButtonVolPreferences=findViewById<RadioButton>(selectedVolPreferencesId)

            //obtain the entered data
            val textFullName = fullNameInput.text.toString()
            val textEmail = emailInput.text.toString()
            val textPassword = passwordInput.text.toString()
            val textConfirmPwd = confirmPasswordInput.text.toString()
            val textPhone = phoneInput.text.toString()
            val textAddress = addressInput.text.toString()
            val textAdminDesignation = adminDesignation.text.toString()
            val textRestaurantName = restaurantName.text.toString()
            val textNgoName = ngoName.text.toString()
            val textNgoRegistration = ngoRegistration.text.toString()
            val textRevPickupTime = revPickupTime.text.toString()

            val textAdminAccess: String?

            if(TextUtils.isEmpty(textFullName)){
                showErrorSnackBar("Enter your full name")
                fullNameInput.requestFocus()
            }else if(TextUtils.isEmpty(textEmail)){
                showErrorSnackBar("Enter your email")
                emailInput.requestFocus()
            }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                showErrorSnackBar("Re-enter your email")
                emailInput.requestFocus()
            }else if(TextUtils.isEmpty(textAddress)){
                showErrorSnackBar("Enter your Address")
                addressInput.requestFocus()
            }else if (TextUtils.isEmpty(textPhone)){
                showErrorSnackBar("Please Enter your phone number")
                phoneInput.requestFocus()
            }else if(textPhone.length!=10){
                showErrorSnackBar("Phone no should be 10 digits")
                phoneInput.requestFocus()
            }else if(TextUtils.isEmpty(textPassword)){
                showErrorSnackBar("Please enter your password")
                passwordInput.requestFocus()
            }else if(textPassword.length<8){
                showErrorSnackBar("Password should be at least 8 digits")
                passwordInput.requestFocus()
            }else if(TextUtils.isEmpty(textConfirmPwd)){
                showErrorSnackBar("Please confirm your password")
                confirmPasswordInput.requestFocus()
            }
            else if(textPassword != textConfirmPwd){
                Toast.makeText(this, "Please enter same Password", Toast.LENGTH_LONG).show()
                showErrorSnackBar("Password Confirmation is required")
                confirmPasswordInput.requestFocus()
                //clear the entered passwords
                passwordInput.clearComposingText()
                confirmPasswordInput.clearComposingText()
            }
            else {
                showProgressDialog(resources.getString(R.string.please_wait))
                textAdminAccess = radioButtonAdminAccess.text.toString()
                registerUser(textFullName,textEmail,textAddress,textPhone,textPassword,textConfirmPwd,textAdminAccess)

                when (intent.getStringExtra("previousActivity")) {
                    "RHomePage" -> {
                        val rHomeIntent = Intent(this, RHomePage::class.java)
                        startActivity(rHomeIntent)
                        finish()
                    }

                    "NHomePage" -> {
                        val nHomeIntent = Intent(this, NHomePage::class.java)
//                        nHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP , Intent.FLAG_ACTIVITY_CLEAR_TASK , Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(nHomeIntent)
                        finish()
                    }

                    "AHomePage" -> {
                        if (selectedAdminAccessId == -1){
                            showErrorSnackBar("Please select your access level")
                        }
                        val aHomeIntent = Intent(this, AHomePage::class.java)
                        startActivity(aHomeIntent)
                        finish()
                    }

                    "RcHomePage" -> {
                        val rrHomeIntent = Intent(this, RcHomePage::class.java)
                        startActivity(rrHomeIntent)
                        finish()
                    }

                    "VHomePage" -> {
                        val vHomeIntent = Intent(this, VHomePage::class.java)
                        startActivity(vHomeIntent)
                        finish()
                    }
                }
            }
        }
    }

    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    private fun showProgressDialog(text: String) {
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
        mProgressDialog.dismiss()
    }
    private fun showErrorSnackBar(message: String) {
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
    private fun registerUser(textFullName: String, textEmail: String, textAddress: String, textPhone: String, textPassword: String, textConfirmPwd: String, textAdminAccess: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(textEmail, textPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User account created successfully
                    Toast.makeText(
                        this,
                        "User registered Successfully",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    auth.currentUser?.sendEmailVerification()
                } else {
                    // Handle the error
                    // For example, display an error message to the user
                    try {
                        throw task.exception!!
                    } catch (e: Exception) {
                        Log.e("logging", "error:$e.getMessage")

                    }
                    // or log the error to the console
                }
            }
    }
}