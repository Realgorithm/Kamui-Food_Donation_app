package com.kamui.fooddonation

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.Employee
import com.kamui.fooddonation.data.NgoData
import java.io.ByteArrayOutputStream
import java.util.Date

class FireStoreClass:BaseActivity() {

    // Collection names
    private val donationCollection = "donations"
    private val usersCollectionId ="r23fKsDlCbMnWap4xJZ2FUQmhnq2"
    private val ngoCollection ="ngo"
    private val employeeCollection ="employees"

    // Firebase Firestore instance
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storageInstance= FirebaseStorage.getInstance()

    /**
     * Store a new donation object in Firestore.
     */
//    fun addDonation(donation: Donation, onComplete: () -> Unit) {
//        firestoreInstance.collection(donationCollection)
//            .add(donation) // adds the donation to Firestore with a generated document ID
//            .addOnSuccessListener { documentReference ->
//                val donationId = documentReference.id
//                // set the generated ID as the donation ID
//                donation.donationId = donationId
//                // update the donation in Firestore with the new ID
//                firestoreInstance.collection(donationCollection)
//                    .document(donationId)
//                    .set(donation)
//                    .addOnSuccessListener { onComplete() }
//                    .addOnFailureListener { error -> Log.e("Firestore", "Error updating donation", error) }
//            }
//            .addOnFailureListener { error -> Log.e("Firestore", "Error adding donation", error) }
//    }

    fun addDonation(donation: Donation, image: Bitmap?, onComplete: () -> Unit) {
        firestoreInstance.collection(donationCollection)
            .add(donation) // adds the donation to Firestore with a generated document ID
            .addOnSuccessListener { documentReference ->
                val donationId = documentReference.id
                // set the generated ID as the donation ID
                donation.donationId = donationId
                // upload the image to Firestore Storage
                if (image != null) {
                    val imageRef = storageInstance.reference.child("donation_images/$donationId.jpg")
                    val baos = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask
                        .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error uploading image", exception)
                    }
                        .addOnSuccessListener {
                        // get the image download URL and add it to the donation object
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            donation.imageUri = uri.toString()
                            // update the donation in Firestore with the new ID and image URL
                            firestoreInstance.collection(donationCollection)
                                .document(donationId)
                                .set(donation)
                                .addOnSuccessListener { onComplete() }
                                .addOnFailureListener { error -> Log.e("Firestore", "Error updating donation", error) }
                        }
                    }
                } else {
                    // if there is no image, update the donation in Firestore with the new ID
                    firestoreInstance.collection(donationCollection)
                        .document(donationId)
                        .set(donation)
                        .addOnSuccessListener { onComplete() }
                        .addOnFailureListener { error -> Log.e("Firestore", "Error updating donation", error) }
                }
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error adding donation", error) }
    }



    fun updateReceiverInfo(
        donationId: String, receiverName: String,
        destAddress: GeoPoint?, receiverId: String,
        receiverAddress : String,
        onComplete: () -> Unit) {
        firestoreInstance.collection("donations").document(donationId)
        .update(
            mapOf(
                "receiver" to receiverName,
                "receiverId" to receiverId,
                "destAddress" to destAddress,
                "receiverAddress" to receiverAddress
            )
        )
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }


    /**
     * Get a list of all employees from Firestore.
     */
    fun getAllEmployees(ngoId:String, onSuccess: (List<Employee>) -> Unit) {
        firestoreInstance.collection(employeeCollection)
            .whereEqualTo("ngoId",ngoId)
            .get()
            .addOnSuccessListener { result ->
                val employees = mutableListOf<Employee>()
                for (document in result) {
                    val employee = document.toObject(Employee::class.java)
                    employees.add(employee)
                }
                onSuccess(employees)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }



    fun getDonationsByDateRange( startDate: Date, endDate: Date, callback: (List<Donation>?) -> Unit, ) {
        firestoreInstance.collection(donationCollection)
            .whereGreaterThanOrEqualTo("pickupDate", startDate)
            .whereLessThanOrEqualTo("pickupDate", endDate)
            .get()
            .addOnSuccessListener { result ->
                val donations = mutableListOf<Donation>()
                for (document in result) {
                    val donation = document.toObject(Donation::class.java)
                    donations.add(donation)
                }
                callback(donations)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                callback(null)
            }
    }


    fun listenForDonationUpdates(status: String, onUpdate: (List<Donation>) -> Unit, onError: (Exception) -> Unit) {
        firestoreInstance.collection(donationCollection)
            .whereEqualTo("status", status) // add a filter to only fetch donations with the specified status
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error listening for donation updates", error)
                    onError(error)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val donations = mutableListOf<Donation>()
                    for (document in querySnapshot.documents) {
                        val donation = document.toObject(Donation::class.java)
                        donation?.let {
                            it.pickupDate = document.getTimestamp("pickupDate")?.toDate() // Convert Timestamp to Date
                            donations.add(it)
                        }
                    }
                    onUpdate(donations)
                }
            }
    }

    fun getRecentDonation(userId: String,onUpdate: (List<Donation>) -> Unit, onError: (Exception) -> Unit) {
        firestoreInstance.collection(donationCollection)
            .whereEqualTo("donorId", userId)
            .orderBy("pickupDate", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error listening for donation updates", error)
                    onError(error)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val donations = mutableListOf<Donation>()
                    for (document in querySnapshot.documents) {
                        val donation = document.toObject(Donation::class.java)
                        donation?.let {
                            it.pickupDate = document.getTimestamp("pickupDate")?.toDate() // Convert Timestamp to Date
                            donations.add(it)
                        }
                    }
                    onUpdate(donations)
                }
            }
    }


    /**
     * Get a specific donation from Firestore.
     */
    fun getDonationById(id: String, onComplete: (DocumentReference) -> Unit) {
        firestoreInstance.collection(donationCollection)
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    onComplete(documentSnapshot.reference)
                } else {
                    Log.e("Firestore", "Donation with id $id not found")
                }
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error getting donation by id", error) }
    }

    /**
     * Update a donation's status in Firestore.
     */
    fun updateDonationStatus(id: String, newStatus: String, onComplete: () -> Unit) {
        firestoreInstance.collection(donationCollection)
            .document(id)
            .update("status", newStatus)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { error -> Log.e("Firestore", "Error updating donation status", error) }
    }
    fun updateVolunteerId(id: String, volunteerId: String, onComplete: () -> Unit) {
        firestoreInstance.collection(donationCollection)
            .document(id)
            .update("volunteerId", volunteerId)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { error -> Log.e("Firestore", "Error updating donation status", error) }
    }

    /**
     * Update a donation's claimed by volunteer name in Firestore.
     */
    fun updateDonationClaimedBy(id: String, volunteerName: String, onComplete: () -> Unit) {
        firestoreInstance.collection(donationCollection)
            .document(id)
            .update("claimedBy", volunteerName)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { error -> Log.e("Firestore", "Error updating donation claimed by", error) }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun <T : Any> getUserData(
        moduleCollection: String,
        clazz: Class<out T>, // updated type parameter with out variance modifier
        userId: String,
        onComplete: (userData: T?) -> Unit
    ) {
        firestoreInstance.collection("users")
            .document(usersCollectionId)
            .collection(moduleCollection)
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(clazz)
                    Log.d("Firestore", "userData class: ${userData?.javaClass?.simpleName}")
                    onComplete(userData)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error getting user data", error) }
    }


    fun <T : Any> updateUserData(
        userId: String,
        moduleCollection: String,
        userData: T,
        onComplete: () -> Unit
    ) {
        firestoreInstance.collection("users")
            .document(usersCollectionId)
            .collection(moduleCollection)
            .document(userId)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Firestore", "User data updated successfully")
                onComplete()
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error updating user data", error) }

    }

    fun getAllNGOsExceptCurrentNGO(currentNgoId: String, onSuccess: (List<NgoData>) -> Unit, onFailure: (Exception) -> Unit) {
        firestoreInstance.collection("users")
            .document(usersCollectionId)
            .collection(ngoCollection)
            .whereNotEqualTo("ngoId", currentNgoId) // Exclude the current NGO by ID
            .get()
            .addOnSuccessListener { result ->
                val ngos = result.toObjects(NgoData::class.java)
                onSuccess(ngos)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun deleteUser(moduleCollection: String,userId:String){
        firestoreInstance.collection("users")
            .document(usersCollectionId)
            .collection(moduleCollection).document(userId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
    fun <T : Any> addUserData(
        moduleCollection: String,
        userId: String,
        userData: T,
        onComplete: () -> Unit
    ) {
        firestoreInstance.collection("users")
            .document(usersCollectionId)
            .collection(moduleCollection)
            .document(userId)
            .set(userData)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { error -> Log.e("Firestore", "Error adding user data", error) }
    }


    /**
     * A function for getting the user id of current logged user.
     */
    override fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}


