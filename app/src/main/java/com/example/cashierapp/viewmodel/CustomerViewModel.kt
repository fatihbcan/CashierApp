package com.example.cashierapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashierapp.utils.Constants
import com.example.cashierapp.utils.EventLogger
import com.example.cashierapp.utils.Progress
import com.example.cashierapp.utils.UserTypes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val eventLogger: EventLogger): ViewModel() {

    private val _status = MutableLiveData<String>(Progress.IDLE.value)
    val status : LiveData<String> = _status

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun sendQRGeneraterLog(){
        eventLogger.sendLogWithBundle("QR_OPERATION","GENERATED")
    }

    fun getAuthToken(): String = auth.uid!!

    fun eventListener(){
        db.collection(Constants.USERS).whereEqualTo(Constants.USER_TYPE, UserTypes.CUSTOMER.value).addSnapshotListener { value, error ->
            if(error != null){
                return@addSnapshotListener
            }


            if( value != null ){
                for (document in value.documents){
                    _status.value = document.getString(Constants.PROGRESS)
                }
            }
        }
    }
}