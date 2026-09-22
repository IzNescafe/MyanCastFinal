package com.example.myancast.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.myancast.ui.theme.BgDark
import com.example.myancast.ui.theme.GoldPrimary
import com.example.myancast.ui.theme.TextHi

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onSkipBack: () -> Unit,
    onSkipForward: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    modifier: Modifier = Modifier,
    isBuffering: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onSkipBack) {
            Icon(Icons.Filled.Replay, "back", tint = TextHi, modifier = Modifier.size(28.dp))
        }
        IconButton(onClick = onPrev) {
            Icon(Icons.Filled.SkipPrevious, "prev", tint = TextHi, modifier = Modifier.size(36.dp))
        }
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(GoldPrimary)
                .clickable(onClick = onPlayPause),
            contentAlignment = Alignment.Center
        ) {
            if (isBuffering) {
                // Stream load နေတုန်း — play ခလုတ်နေရာမှာ spinner
                CircularProgressIndicator(
                    color = BgDark,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = BgDark,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        IconButton(onClick = onNext) {
            Icon(Icons.Filled.SkipNext, "next", tint = TextHi, modifier = Modifier.size(36.dp))
        }
        IconButton(onClick = onSkipForward) {
            Icon(Icons.Filled.Forward, "fwd", tint = TextHi, modifier = Modifier.size(28.dp))
        }
    }
}