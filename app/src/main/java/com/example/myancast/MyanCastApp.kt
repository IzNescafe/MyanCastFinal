package com.example.myancast

import android.app.Application
import com.example.myancast.data.local.AppDatabase
import com.example.myancast.player.DemoPlayerController
import com.example.myancast.player.PlayerController
import com.google.firebase.FirebaseApp

class MyanCastApp : Application() {

    val playerController: PlayerController by lazy { DemoPlayerController() }
    val database by lazy { AppDatabase.get(this) }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
