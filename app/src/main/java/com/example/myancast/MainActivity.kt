// MainActivity.kt
package com.example.myancast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myancast.ui.navigation.MyanCastNavHost
import com.example.myancast.ui.theme.AppConfig
import com.example.myancast.ui.theme.MyanCastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()   // status bar + nav bar edge-to-edge

        setContent {
            MyanCastRoot()
        }
    }
}

/**
 * Root composable — config state ကို ဒီမှာ ထား
 * Settings က ပြောင်းလိုက်တာနဲ့ app တစ်ခုလုံး recompose ဖြစ်
 */
@Composable
private fun MyanCastRoot() {
    // ─── App config state (persists across recomposition) ───
    var config by remember { mutableStateOf(AppConfig(zawgyi = false, darkMode = true)) }

    MyanCastTheme(config = config) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            MyanCastNavHost(
                navController = navController,
                config = config,
                onConfigChange = { config = it }
            )
        }
    }
}