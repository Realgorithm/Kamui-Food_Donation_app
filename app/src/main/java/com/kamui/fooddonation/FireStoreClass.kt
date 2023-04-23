package com.kamui.fooddonation

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.kamui.fooddonation.data.AdminData
import com.kamui.fooddonation.data.Donation
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.data.ReceiverData
import com.kamui.fooddonation.data.RestaurantData
import com.kamui.fooddonation.data.VolunteerData
import java.lang.Exception

class FireStoreClass:BaseActivity() {

    private lateinit var listenerRegistration: ListenerRegistration
    // Collection names
    private val donationCollection = "donations"
    private val usersCollectionId ="r23fKsDlCbMnWap4xJZ2FUQmhnq2"

    // Firebase Firestore instance
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    /**
     * Store a new donation object in Firestore.
     */
//    fun addDonation(donation: Donation, onComplete: () -> Unit) {
//        firestoreInstance.collection(donationCollection).document()
//            .set(donation)
//            .addOnSuccessListener {
//                onComplete() }
//            .addOnFailureListener { error -> Log.e("Firestore", "Error adding donation", error) }
//    }
    fun addDonation(donation: Donation, onComplete: () -> Unit) {
        firestoreInstance.collection(donationCollection)
            .add(donation) // adds the donation to Firestore with a generated document ID
            .addOnSuccessListener { documentReference ->
                val donationId = documentReference.id
                // set the generated ID as the donation ID
                donation.donationId = donationId
                // update the donation in Firestore with the new ID
                firestoreInstance.collection(donationCollection)
                    .document(donationId)
                    .set(donation)
                    .addOnSuccessListener { onComplete() }
                    .addOnFailureListener { error -> Log.e("Firestore", "Error updating donation", error) }
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error adding donation", error) }
    }


    fun updateReceiverInfo(
        donationId: String, receiverName: String,
        destAddress: GeoPoint?, receiverId: String,
        onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("donations").document(donationId)

        docRef.update(
            mapOf(
                "receiver" to receiverName,
                "receiverId" to receiverId,
                "destAddress" to destAddress
            )
        )
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }


    /**
     * Get a list of all donations from Firestore.
     */
    fun getAllDonations(onComplete: (List<Donation>) -> Unit) {
        firestoreInstance.collection(donationCollection)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val donations = querySnapshot.toObjects(Donation::class.java)
                onComplete(donations)
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error getting all donations", error) }
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
                    val donations = querySnapshot.toObjects(Donation::class.java)
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
//    fun <T : Any> getUserData(
//        moduleCollection: String,
//        clazz: Class<out T>?, // constraint on T: T must have a role variable of type String
//        onComplete: (userData: T?) -> Unit
//    ) {
//        firestoreInstance.collection("users")
//            .document(usersCollectionId)
//            .collection(moduleCollection)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if(querySnapshot.documents.isNotEmpty()) {
//                    for(document in querySnapshot.documents) {
//                        val userData = document.toObject(clazz)
//                        Log.d("Firestore", "userData class: ${userData?.javaClass?.simpleName}")
//                        onComplete(userData)
//                    }
//                }
//            }
//            .addOnFailureListener { error -> Log.e("Firestore", "Error getting user data", error) }
//    }
    fun <T : Any> getUserData(
        moduleCollection: String,
        clazz: Class<out T>, // updated type parameter with out variance modifier
        onComplete: (userData: T?) -> Unit
    ) {
        firestoreInstance.collection("users")
            .document(usersCollectionId)
            .collection(moduleCollection)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if(querySnapshot.documents.isNotEmpty()) {
                    for(document in querySnapshot.documents) {
                        val userData = document.toObject(clazz)
                        Log.d("Firestore", "userData class: ${userData?.javaClass?.simpleName}")
                        onComplete(userData)
                    }
                }
            }
            .addOnFailureListener { error -> Log.e("Firestore", "Error getting user data", error) }
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

    companion object {
        fun registerUser(activity: SignupActivity, userInfo: NgoData, userid: String) {
            // Handle registration for Ngo user
            val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userid)
                .collection("ngo").document()
            userDocRef.set(userInfo)
                .addOnSuccessListener {
                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegisteredSuccess("ngo")
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error writing document",
                        e
                    )
                }
        }

        fun registerUser(activity: SignupActivity, userInfo: RestaurantData, userid: String) {
            // Handle registration for Restaurant user
            val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userid)
                .collection("restaurant").document()
            userDocRef.set(userInfo)
                .addOnSuccessListener {
                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegisteredSuccess("restaurant")
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error writing document",
                        e
                    )
                }
        }

        fun registerUser(activity: SignupActivity, userInfo: AdminData, userid: String) {
            // Handle registration for Admin user
            val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userid)
                .collection("admin").document()
            userDocRef.set(userInfo)
                .addOnSuccessListener {
                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegisteredSuccess("admin")
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error writing document",
                        e
                    )
                }
        }

        fun registerUser(activity: SignupActivity, userInfo: ReceiverData, userid: String) {
            // Handle registration for Receiver user
            val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userid)
                .collection("receiver").document()
            userDocRef.set(userInfo)
                .addOnSuccessListener {
                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegisteredSuccess("receiver")
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error writing document",
                        e
                    )
                }
        }

        fun registerUser(activity: SignupActivity, userInfo: VolunteerData, userid: String) {
            // Handle registration for Volunteer user
            val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userid)
                .collection("volunteer").document()
            userDocRef.set(userInfo)
                .addOnSuccessListener {
                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegisteredSuccess("volunteer")
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error writing document",
                        e
                    )
                }

        }
    }
}


