package com.example.cashierapp.viewmodel

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
class SplashActivityViewModel @Inject constructor(private val userUtil: UserUtil) : ViewModel() {

    private val _loginStatus = MutableLiveData(false)
    val loginStatus: LiveData<Boolean> = _loginStatus

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun checkCurrentUser(activity: Activity) {
        if (auth.currentUser != null) {
            setDB()
        } else {
            signInAnon(activity)
        }
    }

    private fun signInAnon(activity: Activity) {
        auth.signInAnonymously()
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success")
                    setDB()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setDB() {
        val user: HashMap<String, String> = HashMap()
        user[Constants.MESSAGE_TOKEN] = userUtil.getMessageToken()
        user[Constants.PROGRESS] = userUtil.getProgress()
        user[Constants.USER_TYPE] = ""

        auth.uid?.let {
            db.collection(Constants.USERS).document(it).set(user).addOnSuccessListener {
                _loginStatus.value = true
            }.addOnFailureListener {
                addDB()
            }
        }
    }

    private fun addDB() {

        val user: HashMap<String, String> = HashMap()
        user[Constants.MESSAGE_TOKEN] = userUtil.getMessageToken()
        user[Constants.PROGRESS] = userUtil.getProgress()
        user[Constants.USER_TYPE] = ""

        db.collection(Constants.USERS).add(user).addOnSuccessListener {
            Log.d(TAG, "db signup success")
        }.addOnFailureListener {
            Log.d(TAG, "signUpForDB failed !")
        }
    }


    companion object {
        private val TAG = SplashActivityViewModel::class.java.simpleName
    }
}