package com.example.cashierapp.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class EventLogger @Inject constructor() {

    private val analytics: FirebaseAnalytics = Firebase.analytics


    fun sendLogWithBundle(key: String, value: String){

        val tempBundle = Bundle()
        tempBundle.putString(key, value)

        analytics.logEvent(key, tempBundle)
    }
}