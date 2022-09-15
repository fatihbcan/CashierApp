package com.example.cashierapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashierapp.utils.Constants
import com.example.cashierapp.utils.EventLogger
import com.example.cashierapp.utils.UserTypes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CashierViewModel @Inject constructor(private val eventLogger: EventLogger): ViewModel() {

    private val _customerToken = MutableLiveData("")
    val customerToken : LiveData<String> = _customerToken

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun sendQRReadedLog(){
        eventLogger.sendLogWithBundle("QR_OPERATION","READED")
    }

    fun updateToken(token: String){
        _customerToken.value = token
    }

    fun updateData(progress: String){
        val user: HashMap<String, String> = HashMap()
        user[Constants.PROGRESS] = progress


        auth.uid?.let {
            db.collection(Constants.USERS).document(customerToken.value!!).update(user as Map<String, Any>).addOnSuccessListener {
            }.addOnFailureListener {
            }
        }
    }
}