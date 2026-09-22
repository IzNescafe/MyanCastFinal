package com.example.myancast.ui.player

/**
 * MiniPlayer ရဲ့ UI state — ဒီ field ၇ ခုက MiniPlayer ကို ပြဖို့ လိုတဲ့ data အားလုံး။
 */
data class MiniPlayerUiState(
    val visible: Boolean = false,        // episode ရှိမှ ပြ
    val title: String = "",              // episode title
    val subtitle: String = "",           // podcast title
    val coverUrl: String = "",           // cover ပုံ URL
    val progress: Float = 0f,            // 0f..1f
    val isPlaying: Boolean = false,      // ဖွင့်နေလား
    val hasNext: Boolean = false         // နောက် episode ရှိလား
)