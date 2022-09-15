package com.example.cashierapp.utils

import javax.inject.Inject

class UserUtil @Inject constructor() {

    private var messagingToken = ""
    private var progress = Progress.IDLE.value

    fun getMessageToken() : String = messagingToken
    fun getProgress() : String = progress

    fun setMessageToken(token: String) {
        this.messagingToken = token
    }

    fun setProgress(progress: String) {
        this.progress = progress
    }
}