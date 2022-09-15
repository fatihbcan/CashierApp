package com.example.cashierapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashierapp.utils.Constants
import com.example.cashierapp.utils.UserUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val userUtil: UserUtil) : ViewModel() {

    private val TAG= HomeFragmentViewModel::class.java.simpleName

    private val _navigationDecider = MutableLiveData("")
    val navigationDecider: LiveData<String> = _navigationDecider

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun processFirebaseActionsAndNavigate(userType: String) {
        db.collection(Constants.USERS).whereEqualTo(Constants.USER_TYPE, userType).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && task.result.documents.size > 0) {
                    setUserType(userType)
                } else {
                    signUpForDB(userType)
                }
            }
    }

    fun setNavDefault(){
        _navigationDecider.value = ""
    }

    private fun setUserType(userType: String) {
        val user: HashMap<String, String> = HashMap()
        user[Constants.USER_TYPE] = userType
        user[Constants.MESSAGE_TOKEN] = userUtil.getMessageToken()

        auth.uid?.let {
            db.collection(Constants.USERS).document(it).update(user as Map<String, Any>)
                .addOnSuccessListener { _navigationDecider.value = userType }
                .addOnFailureListener { it->  Log.e(TAG, "update failed$it") }
        }
    }

    private fun signUpForDB(userType: String) {

        val user: HashMap<String, String> = HashMap()
        user[Constants.MESSAGE_TOKEN] = userUtil.getMessageToken()
        user[Constants.PROGRESS] = userUtil.getProgress()
        user[Constants.USER_TYPE] = userType

        db.collection(Constants.USERS).add(user).addOnSuccessListener {
            Log.d(TAG, "db signup success")
            _navigationDecider.value = userType
        }.addOnFailureListener {
            Log.d(TAG, "signUpForDB failed !")
        }
    }

}