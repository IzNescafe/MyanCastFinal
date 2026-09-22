package com.example.myancast.player

import com.example.myancast.domain.model.PlaybackState
import kotlinx.coroutines.flow.StateFlow

/**
 * Player ကို ထိန်းတဲ့ API — ViewModel တွေက ဒါကိုပဲ သိတယ် (Media3 ကို မသိဘူး)။
 * App တစ်ခုလုံးမှာ instance တစ်ခုတည်း (MyanCastApp.playerController)။
 */
interface PlayerController {
    val state: StateFlow<PlaybackState>

    /** queue.isEmpty ဆိုရင် ဘာမှ မလုပ်ဘူး */
    fun play(queue: PlayerQueue, podcastTitle: String)
    fun togglePlayPause()
    fun seekTo(positionMs: Long)
    fun skipBack()        // -15s
    fun skipForward()     // +30s
    fun next()
    fun previous()
    fun setSpeed(speed: Float)
}