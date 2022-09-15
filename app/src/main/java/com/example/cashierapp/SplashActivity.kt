package com.example.cashierapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cashierapp.viewmodel.SplashActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import okhttp3.internal.wait

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val TAG = SplashActivity::class.java.simpleName

    private val splashActivityViewModel : SplashActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashActivityViewModel.loginStatus.observe(this){
            if(it){
                goToHomePage()
            }
        }

        splashActivityViewModel.checkCurrentUser(this)

    }


    private fun goToHomePage(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)

    }


}