package com.kamui.fooddonation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.kamui.fooddonation.data.AdminData
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.data.ReceiverData
import com.kamui.fooddonation.data.RestaurantData
import com.kamui.fooddonation.data.VolunteerData
import java.io.ByteArrayOutputStream

class EditProfile : BaseActivity() {
    private lateinit var imageView: ImageView
    private lateinit var locationIcon: EditText
    private lateinit var userName: EditText
    private lateinit var userEmailID: EditText
    private lateinit var userPhone: EditText
    private lateinit var userAddress: EditText
    private var selectedBitmap: Bitmap? = null
    private val storageInstance= FirebaseStorage.getInstance()

    // Create Activity Result Launcher for selecting images
    private val imageSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedBitmap = getBitmapFromUri(uri)
                imageView.setImageBitmap(selectedBitmap)
            }
        }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val childCollection: String
        val childData: Class<*>
        val userUid = FireStoreClass().getCurrentUserID()
        val addImage = findViewById<Button>(R.id.select_image_button)
        userName = findViewById(R.id.et_user_name)
        val userNameLabel = findViewById<TextView>(R.id.tv_user_name)
        userEmailID = findViewById(R.id.et_user_email)
        val userEmailIDLabel = findViewById<TextView>(R.id.tv_user_email)
        userPhone = findViewById(R.id.et_user_phone)
        val userPhoneLabel = findViewById<TextView>(R.id.tv_user_phone)
        userAddress = findViewById(R.id.et_user_address)
        val userAddressLabel = findViewById<TextView>(R.id.tv_user_address)
        locationIcon = findViewById(R.id.location_icon)
        val submitButton = findViewById<Button>(R.id.submit)

        setOnFocusChangeListener(userName, "Name", userNameLabel)
        setOnFocusChangeListener(userEmailID, "Email ID", userEmailIDLabel)
        setOnFocusChangeListener(userAddress, "Address", userAddressLabel)
        setOnFocusChangeListener(userPhone, "Mobile Number", userPhoneLabel)

        // Get the location icon drawable
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_location)

        // Set the drawable to the left of the EditText
        locationIcon.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        imageView = findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.mercy_04)
        addImage.setOnClickListener {
            // Launch image selection activity
            imageSelectionLauncher.launch("image/*")
        }
        Log.d("previousActivity", intent.getStringExtra("previousActivity").toString())

        when (intent.getStringExtra("previousActivity")) {
            "restaurant" -> {
                childCollection = "restaurant"
                childData = RestaurantData::class.java
            }

            "volunteer" -> {
                childCollection = "volunteer"
                childData = VolunteerData::class.java
            }

            "admin" -> {
                childCollection = "admin"
                childData = AdminData::class.java
            }

            "ngo" -> {
                childCollection = "ngo"
                childData = NgoData::class.java
            }

            "receiver" -> {
                childCollection = "receiver"
                childData = ReceiverData::class.java
            }

            else -> {
                // handle invalid previousActivity value
                return
            }
        }

        FireStoreClass().getUserData(childCollection, childData,userUid) { userData ->
            // Set the profile information
            when (userData) {
                is VolunteerData -> {
                    userName.text = userData.name?.toEditable()
                    userEmailID.text = userData.email?.toEditable()
                    userPhone.text = userData.phone?.toEditable()
                    userAddress.text = userData.address?.toEditable()
                    Glide.with(this).load(userData.imageUri).into(imageView)
                }

                is RestaurantData -> {
                    userName.text = userData.name?.toEditable()
                    userEmailID.text = userData.email?.toEditable()
                    userPhone.text = userData.phone?.toEditable()
                    userAddress.text = userData.address?.toEditable()
                    Glide.with(this).load(userData.imageUri).into(imageView)
                }

                is AdminData -> {
                    userName.text = userData.name?.toEditable()
                    userEmailID.text = userData.email?.toEditable()
                    userPhone.text = userData.phone?.toEditable()
                    userAddress.text = userData.address?.toEditable()
                    Glide.with(this).load(userData.imageUri).into(imageView)
                }

                is NgoData -> {
                    userName.text = userData.name?.toEditable()
                    userEmailID.text = userData.email?.toEditable()
                    userPhone.text = userData.phone?.toEditable()
                    userAddress.text = userData.address?.toEditable()
                    Glide.with(this).load(userData.imageUri).into(imageView)
                }

                is ReceiverData -> {
                    userName.text = userData.name?.toEditable()
                    userEmailID.text = userData.email?.toEditable()
                    userPhone.text = userData.phone?.toEditable()
                    userAddress.text = userData.address?.toEditable()
//                    FirebaseStorage.getInstance()
//                        .getReference("receiver_images/${userData.imageUri}")
//                        .downloadUrl
//                        .addOnSuccessListener { uri ->
                            Glide.with(this).load(userData.imageUri).into(imageView)
//                        }
                }
            }
            submitButton.setOnClickListener {
                showProgressDialog(resources.getString(R.string.please_wait))

                when (intent.getStringExtra("previousActivity")) {
                    "volunteer" -> {
                        // Upload image to Firebase Storage
                        val imageRef =
                            storageInstance.reference.child("volunteer_images/$userUid.jpg")
                        val baos = ByteArrayOutputStream()
                        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        // Check if the image is larger than 4MB
                        if (data.size > 4 * 1024 * 1024) {
                            Toast.makeText(
                                this,
                                "Image size must be less than 4MB!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                            return@setOnClickListener
                        }
                        val uploadTask = imageRef.putBytes(data)

                        uploadTask.addOnSuccessListener {
                            // Get download URL of uploaded image
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()

                                // Create VolunteerData object with image URL
                                val volunteerData = hashMapOf(
                                    "name" to userName.text.toString(),
                                    "email" to userEmailID.text.toString(),
                                    "phone" to userPhone.text.toString(),
                                    "address" to userAddress.text.toString(),
                                    "imageUri" to imageUrl
                                )
                                // Update user data in Firestore with image URL
                                FireStoreClass().updateUserData(userUid,childCollection, volunteerData) {
                                    finish()
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("Firestore", "Error uploading image", exception)
                            hideProgressDialog()
                        }
                    }

                    "restaurant" -> {
                        // Upload image to Firebase Storage
                        val imageRef =
                            storageInstance.reference.child("restaurant_images/$userUid.jpg")
                        val baos = ByteArrayOutputStream()
                        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        // Check if the image is larger than 4MB
                        if (data.size > 4 * 1024 * 1024) {
                            Toast.makeText(
                                this,
                                "Image size must be less than 4MB!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                            return@setOnClickListener
                        }
                        val uploadTask = imageRef.putBytes(data)
                        uploadTask.addOnSuccessListener {
                            // Get download URL of uploaded image
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()

                                // Create VolunteerData object with image URL
                                val restaurantData = hashMapOf(
                                    "name" to userName.text.toString(),
                                    "email" to userEmailID.text.toString(),
                                    "phone" to userPhone.text.toString(),
                                    "address" to userAddress.text.toString(),
                                    "imageUri" to imageUrl
                                )

                                // Update user data in Firestore with image URL
                                FireStoreClass().updateUserData(userUid,childCollection, restaurantData) {
                                    Log.d("RHomePage", "uploaded data")
                                    hideProgressDialog()
                                    finish()
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("Firestore", "Error uploading image", exception)
                            Toast.makeText(
                                this,
                                "Error uploading Image try again!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                        }
                    }

                    "admin" -> {
                        // Upload image to Firebase Storage
                        val imageRef =
                            storageInstance.reference.child("volunteer_images/$userUid.jpg")
                        val baos = ByteArrayOutputStream()
                        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        // Check if the image is larger than 4MB
                        if (data.size > 4 * 1024 * 1024) {
                            Toast.makeText(
                                this,
                                "Image size must be less than 4MB!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                            return@setOnClickListener
                        }
                        val uploadTask = imageRef.putBytes(data)

                        uploadTask.addOnSuccessListener {
                            // Get download URL of uploaded image
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()

                                val adminData = hashMapOf(
                                    "name" to userName.text.toString(),
                                    "email" to userEmailID.text.toString(),
                                    "phone" to userPhone.text.toString(),
                                    "address" to userAddress.text.toString(),
                                    "imageUri" to imageUrl
                                )

                                // Update user data in Firestore with image URL
                                FireStoreClass().updateUserData(userUid, childCollection, adminData) {
                                    hideProgressDialog()
                                    finish()
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("Firestore", "Error uploading image", exception)
                            Toast.makeText(
                                this,
                                "Error uploading Image try again!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                        }
                    }

                    "NHomePage" -> {
                        // Upload image to Firebase Storage
                        val imageRef =
                            storageInstance.reference.child("volunteer_images/$userUid.jpg")
                        val baos = ByteArrayOutputStream()
                        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        // Check if the image is larger than 4MB
                        if (data.size > 4 * 1024 * 1024) {
                            Toast.makeText(
                                this,
                                "Image size must be less than 4MB!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                            return@setOnClickListener
                        }
                        val uploadTask = imageRef.putBytes(data)

                        uploadTask.addOnSuccessListener {
                            // Get download URL of uploaded image
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                val ngoData = hashMapOf(
                                    "name" to userName.text.toString(),
                                    "email" to userEmailID.text.toString(),
                                    "phone" to userPhone.text.toString(),
                                    "address" to userAddress.text.toString(),
                                    "imageUri" to imageUrl
                                )
                                // Update user data in Firestore with image URL
                                FireStoreClass().updateUserData(userUid, childCollection, ngoData) {
                                    hideProgressDialog()
                                    finish()
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("Firestore", "Error uploading image", exception)
                            Toast.makeText(
                                this,
                                "Error uploading Image try again!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                        }
                    }

                    "RcHomePage" -> {
                        // Upload image to Firebase Storage
                        val imageRef =
                            storageInstance.reference.child("volunteer_images/$userUid.jpg")
                        val baos = ByteArrayOutputStream()
                        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        // Check if the image is larger than 4MB
                        if (data.size > 4 * 1024 * 1024) {
                            Toast.makeText(
                                this,
                                "Image size must be less than 4MB!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                            return@setOnClickListener
                        }
                        val uploadTask = imageRef.putBytes(data)

                        uploadTask.addOnSuccessListener {
                            // Get download URL of uploaded image
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()

                        val receiverData = hashMapOf(
                            "name" to userName.text.toString(),
                            "email" to userEmailID.text.toString(),
                            "phone" to userPhone.text.toString(),
                            "address" to userAddress.text.toString(),
                            "imageUri" to imageUrl
                        )
                                // Update user data in Firestore with image URL
                                FireStoreClass().updateUserData(userUid, childCollection, receiverData) {
                                    hideProgressDialog()
                                    finish()
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("Firestore", "Error uploading image", exception)
                            Toast.makeText(
                                this,
                                "Error uploading Image try again!",
                                Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                        }
                    }
                }
            }
        }
}
    // Extension function to convert a String to Editable
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}
