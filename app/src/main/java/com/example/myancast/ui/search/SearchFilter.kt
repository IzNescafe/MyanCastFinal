// ui/search/SearchFilter.kt
package com.example.myancast.ui.search

import com.example.myancast.domain.model.Podcast

/**
 * Podcast ကို title (သို့) category နဲ့ ရှာတယ်။
 *
 * Firestore မှာ "contains" query မရှိလို့ (prefix ပဲ ရတယ်) client-side မှာပဲ filter လုပ်တယ် —
 * podcast ၅ ခုပဲ ရှိတော့ စွမ်းဆောင်ရည် ပြဿနာ မရှိဘူး၊ index လည်း မလိုဘူး။
 * Pure function မို့ emulator မလိုဘဲ test လုပ်လို့ရတယ် (Phase 2 ရဲ့ `CategoryFilter` ပုံစံ)။
 *
 * Query အလွတ် ဆိုရင် **အကုန် မပြဘူး** — "ရှာလိုသည်ကို ရိုက်ပါ" idle state ပြဖို့။
 */
fun searchPodcasts(podcasts: List<Podcast>, query: String): List<Podcast> {
    val q = query.trim()
    if (q.isBlank()) return emptyList()
    return podcasts.filter { podcast ->
        podcast.title.contains(q, ignoreCase = true) ||
            podcast.category.contains(q, ignoreCase = true)
    }
}
