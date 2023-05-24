package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kamui.fooddonation.BaseActivity
import com.kamui.fooddonation.FireStoreClass
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.Employee
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddEmployee : BaseActivity() {
    // Declare ImageView
    private lateinit var imageView: ImageView
    private lateinit var empName: EditText
    private lateinit var empContactNo: EditText

    private var imageSelectionUri: Uri? = null

    // Create Activity Result Launcher for selecting images
    private val imageSelectionLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageView.setImageURI(uri)
            imageSelectionUri = uri
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        empName = findViewById(R.id.et_emp_name)
        empContactNo = findViewById(R.id.et_contact_no)

        val empNameLabel = findViewById<TextView>(R.id.tv_emp_name)
val empContactLabel= findViewById<TextView>(R.id.tv_contact_no)

        // Find ImageView and set default image
        imageView = findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.mercy_04)

        setOnFocusChangeListener(empName,"Employee Name",empNameLabel)
        setOnFocusChangeListener(empContactNo,"Contact Number",empContactLabel)

        // Find Select Image Button and set click listener to launch image selection activity
        val selectImageButton: Button = findViewById(R.id.select_image_button)
        selectImageButton.setOnClickListener {
            // Launch image selection activity
            imageSelectionLauncher.launch("image/*")
        }

        val addButton: Button = findViewById(R.id.submit)
        addButton.setOnClickListener {
            val textEmpName = empName.text.toString()
            val textEmpContactNo = empContactNo.text.toString()

            if (TextUtils.isEmpty(textEmpName)){
                showErrorSnackBar("Please Enter Employee Name")
            }
            else if(TextUtils.isEmpty(textEmpContactNo)){
                showErrorSnackBar("please enter employee contact")
            }
            else if(textEmpContactNo.length!=10){
                showErrorSnackBar("please enter correct number")
            }
            else{
            addEmployeeToFirestore(textEmpName, textEmpContactNo, imageSelectionUri)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Finish activity if back button is pressed
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun addEmployeeToFirestore(empName: String, contact: String, imageUri: Uri?) {
        // Get a reference to the employees collection in Firestore
        val db = FirebaseFirestore.getInstance()
        val employeesCollection = db.collection("employees")
        val currentUserId =FireStoreClass().getCurrentUserID()
        // Create a new employee object with the provided details
        val employee = Employee(
            name = empName,
            phone = contact
        )

        // If an image was selected, upload it to Firebase Storage and add the download URL to the employee object
        imageUri?.let {
            val inputStream = contentResolver.openInputStream(it)
            inputStream?.use { input ->
                // Check if image size is less than or equal to 2MB
                if (input.available() > 2 * 1024 * 1024) {
                    Toast.makeText(this, "Image size should be less than or equal to 2MB", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            val storageRef = FirebaseStorage.getInstance().reference.child("employee_images/${UUID.randomUUID()}")

            // Compress the image and upload its bytes to Firebase Storage
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            storageRef.putBytes(data)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        employee.imageUrl = uri.toString()

                        // Get the current number of employees to generate a unique ID for the new employee
                        employeesCollection.get()
                            .addOnSuccessListener { querySnapshot ->
                                val id = querySnapshot.size() + 1
                                employee.id = id.toString()
                                employee.ngoId =currentUserId

                                // Add the employee object to Firestore
                                employeesCollection.add(employee)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                                        // Show success message
                                        Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show()
                                        // Finish activity
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Firestore", "Error adding document", e)
                                        // Show error message
                                        Toast.makeText(this, "Error adding employee", Toast.LENGTH_SHORT).show()
                                    }
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error uploading image", e)
                    // Show error message
                    Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            // If no image was selected, add the employee object to Firestore without an image
            employeesCollection.get()
                .addOnSuccessListener { querySnapshot ->
                    val id = querySnapshot.size() + 1
                    employee.id = id.toString()
                    employee.ngoId =currentUserId

                    employeesCollection.add(employee)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                            // Show success message
                            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show()
                            // Finish activity
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error adding document", e)
                            // Show error message
                            Toast.makeText(this, "Error adding employee", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }

}
