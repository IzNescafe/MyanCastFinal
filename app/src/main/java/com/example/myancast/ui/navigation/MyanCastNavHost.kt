// ui/navigation/MyanCastNavHost.kt
package com.example.myancast.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myancast.ui.theme.AppConfig

@Composable
fun MyanCastNavHost(
    navController: NavHostController,
    config: AppConfig,
    onConfigChange: (AppConfig) -> Unit
) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            // ─── Week 1: placeholder screens ───
            composable("home") {
                PlaceholderScreen("Home", currentRoute)
            }
            composable("news") {
                PlaceholderScreen("News", currentRoute)
            }
            composable("search") {
                PlaceholderScreen("Search", currentRoute)
            }
            composable("library") {
                PlaceholderScreen("Library", currentRoute)
            }
            composable("settings") {
                PlaceholderScreen("Settings", currentRoute)
            }
            composable("player") {
                PlaceholderScreen("Player", currentRoute)
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String, route: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$name Screen\n(route: $route)")
    }
}