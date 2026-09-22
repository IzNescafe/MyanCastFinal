package com.example.myancast

import android.app.Application
import com.example.myancast.data.local.AppDatabase
import com.example.myancast.player.Media3PlayerController
import com.example.myancast.player.PlayerController
import com.google.firebase.FirebaseApp

class MyanCastApp : Application() {

    val playerController: PlayerController by lazy { Media3PlayerController(this) }
    val database by lazy { AppDatabase.get(this) }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
