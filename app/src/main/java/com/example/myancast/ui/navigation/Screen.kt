// ui/navigation/Screen.kt
package com.example.myancast.ui.navigation

/**
 * App တစ်ခုလုံးရဲ့ route အားလုံး။
 * Route string တွေကို ဒီတစ်နေရာတည်းမှာပဲ ထားတယ် — typo ကြောင့် crash မဖြစ်အောင်။
 */
sealed class Screen(val route: String) {

    // ─── Bottom nav destinations ───
    data object Home     : Screen("home")
    data object News     : Screen("news")
    data object Search   : Screen("search")
    data object Library  : Screen("library")
    data object Settings : Screen("settings")

    // ─── Detail destinations ───
    data object PodcastDetail : Screen("podcast/{$ARG_PODCAST_ID}") {
        fun create(podcastId: String) = "podcast/$podcastId"
    }

    data object NewsDetail : Screen("newsDetail/{$ARG_NEWS_ID}") {
        fun create(newsId: String) = "newsDetail/$newsId"
    }

    data object Player : Screen("player")

    companion object {
        const val ARG_PODCAST_ID = "podcastId"
        const val ARG_NEWS_ID = "newsId"
    }
}
