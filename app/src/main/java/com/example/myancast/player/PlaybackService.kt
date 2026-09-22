package com.example.myancast.player

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.myancast.MainActivity

/**
 * Background မှာ အသံ ဆက်ဖွင့်ပေးတဲ့ service — ExoPlayer ကို ဒီထဲမှာပဲ ကိုင်တယ်။
 * Notification နဲ့ lock screen controls ကို Media3 က MediaSession ကနေ အလိုလို ဆောက်ပေးတယ်။
 * App ဘက်က Media3PlayerController (MediaController) နဲ့ ချိတ်ပြီး ထိန်းတယ်။
 */
class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                    .build(),
                /* handleAudioFocus = */ true
            )
            // Headphone ဖြုတ်ရင် pause
            .setHandleAudioBecomingNoisy(true)
            .build()

        // Notification ကို နှိပ်ရင် app ကို ပြန်ဖွင့်
        val openApp = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(openApp)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    /** Recent apps ကနေ swipe ဖယ်ရင် — ဖွင့်မနေဘူးဆိုမှ service ကို ရပ် */
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player
        if (player == null ||
            !player.playWhenReady ||
            player.mediaItemCount == 0 ||
            player.playbackState == Player.STATE_ENDED
        ) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }
}
