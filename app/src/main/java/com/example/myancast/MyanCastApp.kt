package com.example.myancast

import android.app.Application
import com.google.firebase.FirebaseApp
import com.example.myancast.data.local.AppDatabase
import kotlin.getValue

class MyanCastApp : Application() {
    val database by lazy { AppDatabase.get(this) }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}