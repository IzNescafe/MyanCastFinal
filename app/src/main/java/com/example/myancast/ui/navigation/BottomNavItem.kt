// ui/navigation/BottomNavItem.kt
package com.example.myancast.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route,     "ပင်မ",         Icons.Filled.Home),
    BottomNavItem(Screen.News.route,     "သတင်း",        Icons.Outlined.Newspaper),
    BottomNavItem(Screen.Search.route,   "ရှာဖွေ",        Icons.Filled.Search),
    BottomNavItem(Screen.Library.route,  "စာကြည့်တိုက်",  Icons.Outlined.Bookmarks),
    BottomNavItem(Screen.Settings.route, "ဆက်တင်",       Icons.Filled.Settings)
)

/** Bottom bar ကို ဒီ route တွေမှာပဲ ပြမယ် (detail/player မှာ မပြဘူး)။ */
val bottomNavRoutes: Set<String> = bottomNavItems.map { it.route }.toSet()
