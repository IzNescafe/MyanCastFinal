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
         * @return — Playable queue
         */
        fun from(episodes: List<Episode>, startEpisodeId: String?): PlayerQueue {
            val playable = episodes.filter { it.audioUrl.isNotBlank() }
            val startIdx = if (startEpisodeId != null) {
                playable.indexOfFirst { it.id == startEpisodeId }.coerceAtLeast(0)
            } else 0
            return PlayerQueue(playable, startIdx)
        }
    }

    /** Skip back 15s — 0 အောက် မဆင်း */
    fun skipBackTarget(currentMs: Long): Long = (currentMs - 15_000L).coerceAtLeast(0L)

    /** Skip forward 30s — duration ကျော် မဆန် */
    fun skipForwardTarget(currentMs: Long, durationMs: Long): Long =
        if (durationMs > 0) (currentMs + 30_000L).coerceAtMost(durationMs)
        else currentMs + 30_000L

    /** Speed cycle — 0.5 → 0.75 → 1 → 1.25 → 1.5 → 2 → 0.5 */
    fun nextSpeed(current: Float): Float {
        val speeds = listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)
        val idx = speeds.indexOfFirst { it == current }.coerceAtLeast(0)
        return speeds[(idx + 1) % speeds.size]
    }
}