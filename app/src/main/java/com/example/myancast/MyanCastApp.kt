package com.example.myancast

import android.app.Application
import com.example.myancast.data.local.AppDatabase
import com.example.myancast.data.repository.LibraryRepository
import com.example.myancast.data.repository.RoomLibraryRepository
import com.example.myancast.player.HistoryRecorder
import com.example.myancast.player.Media3PlayerController
import com.example.myancast.player.PlayerController
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MyanCastApp : Application() {

    val playerController: PlayerController by lazy { Media3PlayerController(this) }
    val database by lazy { AppDatabase.get(this) }

    // ★ Phase 4 Contract
    val libraryRepository: LibraryRepository by lazy {
        RoomLibraryRepository(database.libraryDao())
    }

    /**
     * ★ Phase 4 (C) — playback ကို နားထောင်ပြီး history သိမ်းသူ။
     * App scope မို့ screen ပိတ်လည်း (background playback) ဆက်သိမ်းတယ်။
     */
    private val historyRecorder by lazy {
        HistoryRecorder(
            player = playerController,
            library = libraryRepository,
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        historyRecorder.start()
    }
}
