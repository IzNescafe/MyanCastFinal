package com.example.myancast.player

import com.example.myancast.domain.model.Episode

/**
 * ဖွင့်မယ့် episode စာရင်း — audioUrl မရှိတဲ့ episode တွေကို ဖြုတ်။
 */
data class PlayerQueue(
    val episodes: List<Episode>,
    val startIndex: Int
) {
    val isEmpty: Boolean get() = episodes.isEmpty()
    val size: Int get() = episodes.size

    companion object {
        /**
         * @param episodes — Firestore က episode စာရင်း
         * @param startEpisodeId — စဖွင့်မယ့် episode ID (null = ပထမဆုံး)
         * @return ဖွင့်လို့ရတဲ့ queue၊ နှိပ်တဲ့ episode မှာ audio မရှိရင် EMPTY
         *
         * ID မတွေ့ရင် index 0 ကို မပြန်ဘူး — episode ၃ နှိပ်ပြီး ၁ ကို ကြားရရင်
         * user က bug လို့ ထင်မယ်။ ဘာမှ မဖွင့်ဘဲ caller က message ပြတာ ပိုရိုးသားတယ်။
         */
        fun from(episodes: List<Episode>, startEpisodeId: String?): PlayerQueue {
            val playable = episodes.filter { it.audioUrl.isNotBlank() }
            if (playable.isEmpty()) return EMPTY
            if (startEpisodeId == null) return PlayerQueue(playable, 0)

            val index = playable.indexOfFirst { it.id == startEpisodeId }
            return if (index >= 0) PlayerQueue(playable, index) else EMPTY
        }

        val EMPTY = PlayerQueue(emptyList(), -1)
    }
}

// ─── Skip / Speed — fullscope: 15s back / 30s forward, 0.5x – 2.0x ───
// Queue instance မလိုဘူး — pure function တွေ

const val SKIP_BACK_MS = 15_000L
const val SKIP_FORWARD_MS = 30_000L
val PLAYBACK_SPEEDS = listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)

/** ၁၅ စက္ကန့် နောက်ပြန် — 0 အောက် မဆင်း */
fun skipBackTarget(positionMs: Long): Long =
    (positionMs - SKIP_BACK_MS).coerceAtLeast(0L)

/** ၃၀ စက္ကန့် ရှေ့ — duration ကျော် မသွား (duration မသိရင် ပေါင်းရုံ) */
fun skipForwardTarget(positionMs: Long, durationMs: Long): Long {
    val target = positionMs + SKIP_FORWARD_MS
    return if (durationMs > 0) target.coerceAtMost(durationMs) else target
}

/** 0.5 → 0.75 → 1 → 1.25 → 1.5 → 2 → 0.5၊ မသိတဲ့ speed ဆို 1x ပြန်စ */
fun nextSpeed(current: Float): Float {
    val i = PLAYBACK_SPEEDS.indexOf(current)
    return if (i == -1) 1f else PLAYBACK_SPEEDS[(i + 1) % PLAYBACK_SPEEDS.size]
}
