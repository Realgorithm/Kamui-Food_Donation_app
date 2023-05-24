package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamui.fooddonation.admin.AHomePage
import com.kamui.fooddonation.data.AdminData
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.data.ReceiverData
import com.kamui.fooddonation.data.RestaurantData
import com.kamui.fooddonation.data.VolunteerData
import com.kamui.fooddonation.ngo.NHomePage
import com.kamui.fooddonation.receiver.RcHomePage
import com.kamui.fooddonation.restaurant.RHomePage
import com.kamui.fooddonation.volunteer.VHomePage

class SignInActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private var userNotFound: Toast? =null
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        // Initialize Firebase Auth
        auth = Firebase.auth
        userNotFound = Toast.makeText(this, "User not found", Toast.LENGTH_LONG)
        emailInput =findViewById(R.id.sign_in_email_input)
        passwordInput=findViewById(R.id.sign_in_password_input)

        val signInButton = findViewById<Button>(R.id.button1)

        signInButton.setOnClickListener {
            signInRegisteredUser()
        }
    }

    private fun validateForm(textEmail:String,textPassword:String): Boolean {
        return when {
            TextUtils.isEmpty(textEmail)->{
                showErrorSnackBar("Enter your email")
                emailInput.requestFocus()
                false
            }
            TextUtils.isEmpty(textPassword)->{
                showErrorSnackBar("Please enter your password")
                passwordInput.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SuspiciousIndentation")
    // ...
    private fun signInRegisteredUser() {
        val textEmail = emailInput.text.toString()
        val textPassword = passwordInput.text.toString()
        if (validateForm(textEmail, textPassword)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(textEmail, textPassword)
                .addOnCompleteListener(this) { task ->
                    val userUid=FireStoreClass().getCurrentUserID()
                    if (task.isSuccessful) {
                        val (childCollection, childData) = when (intent.getStringExtra("previousActivity")) {
                            "RHomePage" -> "restaurant" to RestaurantData::class.java
                            "NHomePage" -> "ngo" to NgoData::class.java
                            "ALoginPage" -> "admin" to AdminData::class.java
                            "RcHomePage" -> "receiver" to ReceiverData::class.java
                            "VHomePage" -> "volunteer" to VolunteerData::class.java
                            else -> throw IllegalArgumentException("Invalid previousActivity")
                        }

                        val userId = auth.currentUser?.uid
                        val email = auth.currentUser?.email
                        if (userId != null) {
                            FireStoreClass().getUserData(childCollection, childData,userUid) { userData ->
                                val emailMatch = userData?.let {
                                    when (userData) {
                                        is RestaurantData -> userData.email == email
                                        is NgoData -> userData.email == email
                                        is AdminData -> userData.email == email
                                        is ReceiverData -> userData.email == email
                                        is VolunteerData -> userData.email == email

                                        else -> throw IllegalArgumentException("Invalid userData class")
                                    }
                                } ?:false
                                Log.d("emailMatch",emailMatch.toString())
                                Log.d("email",email.toString())
                                if (emailMatch) {
                                    val intentClass = when (intent.getStringExtra("previousActivity")) {
                                            "RHomePage" -> RHomePage::class.java
                                            "NHomePage" -> NHomePage::class.java
                                            "ALoginPage" -> AHomePage::class.java
                                            "RcHomePage" -> RcHomePage::class.java
                                            "VHomePage" -> VHomePage::class.java
                                            else -> throw IllegalArgumentException("Invalid previousActivity")
                                        }
                                    startActivity(Intent(this, intentClass))
                                    finish()
                                }
                                else{
                                    userNotFound?.show()
                                }
                            }
                        }
                        else {
                            userNotFound?.show()
                        }
                        hideProgressDialog()
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        hideProgressDialog()
                    }
                }
        }
    }
}