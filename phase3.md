# Phase 3: Player (Week 3)

> **Important:** this is a **plan guide**. It shows how the code should be structured; it is not
> meant for exact copy-paste. It builds on the Home → Detail flow from Phase 2.
>
> Based on: `fullscope.md` (features #4–7, #15), the Phase 3 table at the end of `phase2.md`,
> and the code on `main` as of 2026-09-22.

---

## Phase 3: Overview

| Item | Detail |
|---|---|
| **Goal** | Tap an episode → audio plays. Mini player on every screen, Full Player with seek/speed, keeps playing in the background with a notification |
| **Duration** | 5 days + Day 0 (1 hour) |
| **Split** | **Vertical slices again**: each member does Engine/Data → Model → UI for one part |
| **Result at the end** | Detail → tap episode → playback starts → Full Player opens → back → Mini player stays → app in background → notification controls work |

### fullscope features in this phase

| # | Feature | Screen | Owner |
|---|---|---|---|
| 4 | Play / Pause | Player | B (engine) + A + C (buttons) |
| 5 | Seek: 15s back / 30s forward | Player | B (engine) + C (UI) |
| 6 | Mini Player (persistent bar at the bottom) | All | **A** |
| 7 | Full Player (cover, title, seek bar, controls) | Player | **C** |
| 15 | Playback Speed 0.5x – 2.0x | Player | B (engine) + C (UI) |

**Not in this phase:** sleep timer, queue screen, downloads (nice-to-have); Continue Listening and history (Phase 4, needs Room); Zawgyi (Phase 5).

---

## What Phase 3 starts from (checked in code)

| Status | What | File |
|---|---|---|
| ✅ | Media3 1.5.0 (`exoplayer`, `ui`, `session`) in the version catalog | `gradle/libs.versions.toml` |
| ✅ | `MiniPlayer` composable (not connected) | `ui/components/MiniPlayer.kt` |
| ✅ | `PlayerControls` composable (not connected) | `ui/components/PlayerControls.kt` |
| ✅ | `Screen.Player` route + NavHost entry | `ui/navigation/` |
| ✅ | `formatDuration()` → `"24:30"` (tested) | `domain/util/DurationFormatter.kt` |
| ✅ | Episodes already carry the podcast cover (B's fallback) | `PodcastDetailViewModel.kt` |
| ✅ | 96dp bottom padding reserved on Home and Detail | `HomeScreen.kt`, `PodcastDetailScreen.kt` |
| ⬜ | `PlaybackState`, `PlayerController`, `PlayerQueue`, `PlaybackService`, `PlayerViewModel` | **empty files** |
| ⬜ | `FullPlayerScreen` | still `PlaceholderScreen` |
| ⬜ | Detail → play: `onEpisodeClick` only **navigates**, it doesn't say **which** episode to play | `MyanCastNavHost.kt:117` |
| ⬜ | "▶ အားလုံး ဖွင့်" button | `onPlayAll = { /* TODO: Phase 3 */ }` |
| ⬜ | Manifest: no `<service>`, no foreground permissions | `AndroidManifest.xml` |

> ⚠️ **Before starting:** Phase 2 fixes are still uncommitted on local `main` (see `phase2-checklist.md` section 6).
> Merge them first, so every Phase 3 branch starts from the same `main`.

---

## ⭐ Day 0: the shared contract (all 3 together, 1 hour)

### Why a contract first

In Phase 2, A and B **waited** for C's functions. Phase 3 has the same risk, only bigger:
A's Mini player and C's Full player **both** read from B's engine.

The fix: **agree on 2 small files on Day 0**, then everyone codes against them **at the same time**.
Until B's engine is ready, A and C use a `FakePlayerController`.

```
                    ┌──────────────────────────────┐
                    │  CONTRACT (Day 0, all agree) │
                    │  PlaybackState               │
                    │  PlayerController (interface)│
                    └──────┬───────────┬───────────┘
         implements        │           │        reads
   ┌───────────────────────┘           └──────────────────────────┐
   ▼                                                               ▼
B: Media3PlayerController                         A: MiniPlayerViewModel
   + PlaybackService (Media3)                     C: PlayerViewModel
                                                  (tests: FakePlayerController)
```

> Same idea as `PodcastRepository` → `PodcastRepositoryImpl` + `FakePodcastRepository` in Phase 2.

### Contract file 1: `domain/model/PlaybackState.kt`

```kotlin
package com.example.myancast.domain.model

/**
 * Player ရဲ့ လက်ရှိ အခြေအနေ — app တစ်ခုလုံး ဒီ class တစ်ခုတည်းကို ဖတ်တယ်။
 * PlayerController (B) က ထုတ်ပေး၊ MiniPlayer (A) နဲ့ FullPlayer (C) က ဖတ်။
 * Firestore နဲ့ map မလုပ်လို့ `is` prefix သုံးလို့ရတယ်။
 */
data class PlaybackState(
    val queue: List<Episode> = emptyList(),   // ဖွင့်မယ့် episode စာရင်း
    val currentIndex: Int = -1,               // queue ထဲက လက်ရှိ — -1 = ဘာမှ မဖွင့်ရသေး
    val podcastTitle: String = "",            // Mini player subtitle အတွက်
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,                // 0 = မသိရသေး (stream load နေဆဲ)
    val speed: Float = 1f,
    val error: String? = null
) {
    val currentEpisode: Episode? get() = queue.getOrNull(currentIndex)
    val hasEpisode: Boolean get() = currentEpisode != null
    val hasNext: Boolean get() = currentIndex in 0 until queue.lastIndex
    val hasPrevious: Boolean get() = currentIndex > 0
    val progress: Float
        get() = if (durationMs > 0) (positionMs.toFloat() / durationMs).coerceIn(0f, 1f) else 0f
}
```

**Design decisions to agree on:**

| Decision | Why |
|---|---|
| `currentEpisode` is **computed** from `queue` + `currentIndex`, not stored | One source of truth: they can never disagree |
| Times in **milliseconds (`Long`)** | That's what Media3 uses. Convert to seconds only for display: `formatDuration((ms / 1000).toInt())` |
| `durationMs = 0` means "unknown" | Streams report duration late. The UI falls back to `episode.duration` (seconds, from Firestore) |
| `error: String?` in Myanmar | Shown directly by the UI |
| No Media3 / Android types | `PlaybackState` stays in `domain/` and is testable on the JVM |

### Contract file 2: `player/PlayerController.kt`

```kotlin
package com.example.myancast.player

import com.example.myancast.domain.model.PlaybackState
import kotlinx.coroutines.flow.StateFlow

/**
 * Player ကို ထိန်းတဲ့ API — ViewModel တွေက ဒါကိုပဲ သိတယ် (Media3 ကို မသိဘူး)။
 * App တစ်ခုလုံးမှာ instance တစ်ခုတည်း (MyanCastApp.playerController)။
 */
interface PlayerController {
    val state: StateFlow<PlaybackState>

    /** queue.isEmpty ဆိုရင် ဘာမှ မလုပ်ဘူး */
    fun play(queue: PlayerQueue, podcastTitle: String)
    fun togglePlayPause()
    fun seekTo(positionMs: Long)
    fun skipBack()        // -15s
    fun skipForward()     // +30s
    fun next()
    fun previous()
    fun setSpeed(speed: Float)
}
```

| Decision | Why |
|---|---|
| An **interface**, with no Android types in it | ViewModels import it without `android.*` → unit-testable with a fake |
| **One** instance for the whole app, held by `MyanCastApp` | Mini player and Full player must show the **same** playback |
| `play()` takes a `PlayerQueue` (C) | The queue drops episodes with no `audioUrl` and finds the start episode by ID, before Media3 sees anything |
| Tapping an episode with no audio → **empty queue**, nothing plays | Otherwise the user taps episode 3 and hears episode 1 |

### How ViewModels get the controller (no DI framework)

```kotlin
// MyanCastApp.kt (B)
val playerController: PlayerController by lazy { Media3PlayerController(this) }

// Any ViewModel factory
initializer {
    val app = this[APPLICATION_KEY] as MyanCastApp
    PlayerViewModel(app.playerController)
}
```
(`APPLICATION_KEY` = `androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY`)

### What C commits on Day 0 (`feature/phase3-contract`)

| File | Why |
|---|---|
| `domain/model/PlaybackState.kt` | Contract |
| `player/PlayerController.kt` | Contract |
| `player/PlayerQueue.kt` (class + skip/speed helpers) | The contract's `play()` signature needs it |
| `player/DemoPlayerController.kt` | **Temporary** controller with no sound, so `MyanCastApp.playerController` exists and A's and C's factories compile before B's engine. Deleted on Day 4 |
| `MyanCastApp.kt`: `val playerController: PlayerController by lazy { DemoPlayerController() }` | B swaps in `Media3PlayerController(this)` on Day 2 |
| `app/src/test/.../FakePlayerController.kt` | Shared test fake (records calls) |

The full code is in `memberC-phase3.md` → Day 0.

### ✅ Day 0 Deliverable
- [ ] A, B and C read both contract files and agree (or change them **now**, not on Day 3)
- [ ] C commits the 6 files above → merged to `main`
- [ ] Everyone creates their branch **from that `main`**

> **After Day 0, changing the contract needs all 3 to agree.** Adding a field with a default value is
> safe; renaming or removing one breaks the other two slices.

---

## ⭐ Slices

#### 🅰️ Slice A: Mini Player (Member A)

| Layer | Work | File |
|---|---|---|
| **Engine/Data** | — (reads the contract only) | — |
| **Model** | `MiniPlayerViewModel` + `MiniPlayerUiState` (visible, title, subtitle, cover, progress, isPlaying, hasNext) | `ui/player/MiniPlayerViewModel.kt` (new) |
| **UI** | Connect `MiniPlayer`; place it in `MyanCastNavHost` **above** the bottom nav, on every screen except Player; tap → Full Player | `ui/components/MiniPlayer.kt`, `ui/navigation/MyanCastNavHost.kt` |

#### 🅱️ Slice B: Playback engine (Member B)

| Layer | Work | File |
|---|---|---|
| **Engine/Data** | `PlaybackService` (Media3 `MediaSessionService` + ExoPlayer), manifest service + permissions | `player/PlaybackService.kt`, `AndroidManifest.xml` |
| **Model** | `Media3PlayerController` implements `PlayerController` (MediaController, listener → `PlaybackState`, position updates) + `MyanCastApp.playerController` | `player/Media3PlayerController.kt` (new), `MyanCastApp.kt` |
| **UI** | Media notification + lock screen (Media3 builds them); Detail → `play()` + "▶ အားလုံး ဖွင့်" | `PodcastDetailViewModel.kt`, `PodcastDetailScreen.kt` |

#### 🅲 Slice C: Full Player (Member C)

| Layer | Work | File |
|---|---|---|
| **Engine/Data** | `PlayerQueue` (pure: playable filter, start index) + skip/speed helpers | `player/PlayerQueue.kt` |
| **Model** | `PlayerViewModel` + `PlayerUiState` + `PlaybackState.toUiState()` | `ui/player/PlayerViewModel.kt` |
| **UI** | `FullPlayerScreen`: cover, titles, seek slider, times, `PlayerControls`, speed button | `ui/player/FullPlayerScreen.kt`, `ui/components/PlayerControls.kt` |

---

### File ownership (to avoid merge conflicts)

| File | Owner | Note |
|---|---|---|
| `domain/model/PlaybackState.kt` | **Contract** | Changes need all 3 |
| `player/PlayerController.kt` | **Contract** | Changes need all 3 |
| `player/PlayerQueue.kt` | **C** | B calls it |
| `app/src/test/.../FakePlayerController.kt` | **C** | A and C use it |
| `player/PlaybackService.kt`, `player/Media3PlayerController.kt` | **B** | |
| `MyanCastApp.kt`, `AndroidManifest.xml` | **B** | |
| `ui/details/*` | **B** | Detail → play() |
| `ui/player/FullPlayerScreen.kt`, `ui/player/PlayerViewModel.kt` | **C** | |
| `ui/components/PlayerControls.kt` | **C** | |
| `ui/player/MiniPlayerViewModel.kt` (new) | **A** | |
| `ui/components/MiniPlayer.kt` | **A** | |
| `ui/navigation/MyanCastNavHost.kt` | **A** | ⚠️ B changes the Detail `onEpisodeClick` lambda **only on Day 4** |

> Phase 2 had 2 people in `PodcastRepository.kt`. In Phase 3 the shared part is the **contract**,
> and it's frozen after Day 0, so nobody should be editing the same file at the same time.

---

# Day 1: 🗄️ ENGINE / DATA layer

> **Goal:** B can make sound come out; C's queue is ready for B to call.

### 🔰 All 3 together (30 min): know the Media3 pieces

| Piece | What it is | Who touches it |
|---|---|---|
| `ExoPlayer` | Plays the audio stream | B (inside the service) |
| `MediaSessionService` | A service that keeps the player alive in the background + gives a notification | B |
| `MediaController` | The app's remote control for the service's player | B (inside `Media3PlayerController`) |
| `MediaItem` | One track: URI + metadata (title, artist, artwork) | B (built from `Episode`) |
| `PlayerController` / `PlaybackState` | **Our** contract: hides all of the above | Everyone reads it |

### 🅰️ A
- Read `MiniPlayer.kt`; list which contract fields feed which parameter
- Write `MiniPlayerUiState` (no ViewModel yet)
- ⚠️ `MiniPlayer.kt:87`: subtitle uses `labelSmall` (Poppins) but shows the Myanmar podcast title → `bodySmall`

### 🅱️ B
1. Manifest:
   ```xml
   <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
   <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
   <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

   <service
       android:name=".player.PlaybackService"
       android:exported="true"
       android:foregroundServiceType="mediaPlayback">
       <intent-filter>
           <action android:name="androidx.media3.session.MediaSessionService" />
       </intent-filter>
   </service>
   ```
   > `targetSdk = 35`, so `foregroundServiceType="mediaPlayback"` **and** `FOREGROUND_SERVICE_MEDIA_PLAYBACK` are both required, or the service crashes on start.
2. `PlaybackService : MediaSessionService`:
   - `ExoPlayer` with `setAudioAttributes(AudioAttributes(USAGE_MEDIA, AUDIO_CONTENT_TYPE_SPEECH), handleAudioFocus = true)`
   - `setHandleAudioBecomingNoisy(true)`, so unplugging headphones pauses playback
   - `onGetSession()` returns the session; release player + session in `onDestroy()`
3. Smoke test: play one hard-coded SoundHelix URL → hear sound

### 🅲 C
- `PlayerQueue.from(episodes, startEpisodeId)` + skip/speed helpers + tests (TDD, no emulator)
- **Push by Day 1 evening**: B calls it on Day 2

### ✅ Day 1 Deliverable
| Who | Done |
|---|---|
| A | `MiniPlayerUiState` + MiniPlayer font fix |
| B | Service + manifest; hard-coded URL plays |
| C | `PlayerQueue` + helpers + tests, **pushed** |

---

# Day 2: 🧠 MODEL layer

### 🔰 3 rules (same spirit as Phase 2)

| # | Rule | Why |
|---|---|---|
| 1 | ViewModels depend on **`PlayerController`**, never on Media3 classes | Testable + no `android.*` |
| 2 | UI state is **one data class** per screen, mapped from `PlaybackState` with a **pure function** | Test the mapping without coroutines |
| 3 | Time formatting happens in the **ViewModel mapping**, never in a composable | Recomposition runs several times a second while playing |

### 🅰️ A: `MiniPlayerViewModel`
- `state = controller.state.map { it.toMiniUiState() }.stateIn(viewModelScope, WhileSubscribed(5_000), MiniPlayerUiState())`
- Actions: `togglePlayPause()`, `next()`
- `visible = playback.hasEpisode`
- Tests with `FakePlayerController`: hidden when nothing is playing; title/subtitle mapped

### 🅱️ B: `Media3PlayerController`
- Connect: `SessionToken(context, ComponentName(context, PlaybackService::class.java))` → `MediaController.Builder(...).buildAsync()` → on ready, keep the controller
- `play(queue, podcastTitle)`: `setMediaItems(queue.episodes.map { it.toMediaItem(podcastTitle) }, queue.startIndex, 0L)` → `prepare()` → `play()`
- `Episode.toMediaItem()`: `mediaId = id`, `uri = audioUrl`, metadata title / artist = podcastTitle / artworkUri = coverUrl
- `Player.Listener.onEvents` → rebuild `PlaybackState` (index from `currentMediaItemIndex`, `isPlaying`, `playbackState == STATE_BUFFERING`, `playbackParameters.speed`, `onPlayerError` → Myanmar message)
- **Position does not arrive by events**: while `isPlaying`, update `positionMs` every **500 ms** in a coroutine; stop when paused
- Skips use C's `skipBackTarget()` / `skipForwardTarget()`
- If `play()` is called before the controller has connected → remember the request and run it when connected

### 🅲 C: `PlayerViewModel`
- `PlayerUiState` + `PlaybackState.toUiState()` (pure) + `formatSpeed()`
- Actions: play/pause, skip back/forward, next/previous, `seekToFraction(f)`, `cycleSpeed()`
- Factory through `APPLICATION_KEY`
- Tests: mapping (pure) + actions with `FakePlayerController`

### ✅ Day 2 Deliverable
| Who | Done |
|---|---|
| A | `MiniPlayerViewModel` + tests |
| B | `Media3PlayerController` + `MyanCastApp.playerController`; a hard-coded `play()` from anywhere plays audio |
| C | `PlayerViewModel` + `PlayerUiState` + tests |

---

# Day 3: 🎨 UI layer

### 🔰 3 rules

| # | Rule |
|---|---|
| 1 | `collectAsStateWithLifecycle()`, `Modifier` as the first optional param, no `!!` (same as Phase 2) |
| 2 | **Myanmar text → `body*` / `title*` styles.** Times (`"24:30"`) and speed (`"1.5x"`) are ASCII → `label*` is fine |
| 3 | Seek slider: while the finger is **dragging**, show the drag value, not the player position, or the thumb jumps back |

### 🅰️ A: Mini player on every screen
```
Scaffold(bottomBar = {
    Column {
        if (mini.visible && currentRoute != Screen.Player.route) MiniPlayer(...)   // tap → Player
        if (currentRoute in bottomNavRoutes) MyanCastBottomBar(...)
    }
})
```
- Detail has no bottom nav, but **does** show the mini player
- The Mini player now sits **inside** the Scaffold's bottom bar, so `padding` already makes room.
  Check whether the 96dp `contentPadding` on Home/Detail is now too much (reduce it to ~16dp if so)
- Open Player with `launchSingleTop = true`, so tapping the mini player twice doesn't stack 2 players

### 🅱️ B: Detail → play
- `PodcastDetailViewModel` gets `player: PlayerController` in its constructor; factory via `APPLICATION_KEY`
- `playEpisode(id): Boolean` → `PlayerQueue.from(state.episodes, id)`; if `queue.isEmpty` show "ဒီအပိုင်းကို ဖွင့်လို့ မရပါ" and return `false`, else `player.play(queue, podcast.title)` and return `true`
- `playAll()` → `PlayerQueue.from(state.episodes, null)` (starts at the first playable episode)
- Screen: `onPlay = { if (vm.playEpisode(ep.id)) onEpisodeClick(ep.id) }`, so it only opens the Player if something plays. **Signature unchanged**
- Update `PodcastDetailViewModelTest` for the new constructor param (use `FakePlayerController`)
- Optional: `EpisodeRow(isPlaying = ep.id == nowPlayingId)` for the currently playing row

### 🅲 C: `FullPlayerScreen`
```
┌──────────────────────────────┐
│ ⌄                            │  TopAppBar (collapse = onBack)
│                              │
│        [Cover ~280dp]        │  AsyncImage, rounded 16dp
│                              │
│  Episode title               │  headlineSmall, TextHi, max 2 lines
│  Podcast title               │  bodyMedium, TextLo
│                              │
│  ━━━━━━━━●──────────────     │  Slider
│  12:05                 24:30 │  labelMedium (ASCII)
│                              │
│   ↺15  ⏮   ( ▶ )   ⏭  30↻   │  PlayerControls
│                              │
│           [ 1x ]             │  speed chip → cycleSpeed()
└──────────────────────────────┘
```
- No episode yet → `EmptyView("ဖွင့်ထားတာ မရှိသေးပါ")`
- `error != null` → show the message under the slider (playback can continue to the next episode)
- `isBuffering` → small progress indicator in the play button area

### ✅ Day 3 Deliverable
| Who | Done |
|---|---|
| A | Mini player visible on Home/Detail/tabs, hidden on Player; tap opens Player |
| B | Tap episode / "▶ အားလုံး ဖွင့်" starts playback |
| C | Full Player works with real state + `@Preview`s |

**📣 Standup:** each member **plays audio on their phone** and shows their part.

---

# Day 4: 🔗 INTEGRATION (B leads)

> `phase2.md`: "B ရဲ့ slice မှာ UI မပါဘူး — B က notification layout နဲ့ Day 4 integration ကို ဦးဆောင်ရမယ်"

### Step 1: Merge order **B → C → A**

```bash
git checkout main && git pull origin main
git merge feature/phase3-slice-b   && ./gradlew assembleDebug   # engine first (everyone depends on it)
git merge feature/phase3-slice-c   && ./gradlew assembleDebug   # Full player
git merge feature/phase3-slice-a   && ./gradlew assembleDebug   # NavHost wiring last (uses both)
./gradlew testDebugUnitTest
```

> **Why this order:** A's NavHost wiring shows both the Mini player and the Full player, so it goes last.
> Conflicts → **all 3 resolve them together.**

### Step 2: The end-to-end path (each member must explain it)

```
EpisodeRow tapped                                           (C's component)
        ↓  onPlay
PodcastDetailViewModel.playEpisode(id)                      (B)
        ↓  PlayerQueue.from(episodes, id)                   (C — drops no-audio episodes)
PlayerController.play(queue, podcastTitle)                  (contract)
        ↓
Media3PlayerController → MediaController                    (B)
        ↓  setMediaItems + prepare + play
PlaybackService → ExoPlayer → 🔊  + notification            (B)
        ↓  Player.Listener + 500ms position ticker
StateFlow<PlaybackState>                                    (contract)
        ├─→ MiniPlayerViewModel → MiniPlayer (every screen)       (A)
        └─→ PlayerViewModel → toUiState() → FullPlayerScreen     (C)
                                  ↓ formatDuration()  "12:05 / 24:30"
```

### Step 3: Edge cases

| # | Test | Expected | Owner |
|---|---|---|---|
| 1 | Tap an episode | Audio starts, Full Player opens | B |
| 2 | "▶ အားလုံး ဖွင့်" | Starts from episode 1, ⏭ goes to 2 | B |
| 3 | App to background / lock screen | Keeps playing; notification + lock-screen controls work | B |
| 4 | Unplug headphones (or disconnect BT) | Pauses | B |
| 5 | Phone call / other app plays audio | Pauses (audio focus) | B |
| 6 | Internet off while playing | Error message, no crash | B + C |
| 7 | Episode with empty `audioUrl` | Tapping it plays nothing + shows a message; "play all" skips it; no crash | B + C |
| 8 | Drag the seek bar | Thumb follows the finger, audio jumps on release | C |
| 9 | ↺15 at 0:05 / 30↻ near the end | Stays at 0:00 / stops at the end, no crash | C |
| 10 | Speed chip ×6 | 0.5 → 0.75 → 1 → 1.25 → 1.5 → 2 → 0.5, audio speed really changes | C |
| 11 | ⏭ on the last episode / ⏮ on the first | Button disabled (or no-op), no crash | C |
| 12 | Rotate Full Player | Keeps playing, state kept | C |
| 13 | Mini player on Home, Detail, News, Settings | Visible above the bottom nav | A |
| 14 | Mini player on Full Player | **Hidden** | A |
| 15 | Tap Mini player twice fast | Only one Full Player on the back stack | A |
| 16 | Back from Full Player | Returns to the previous screen, mini player visible | A |
| 17 | Play episode X, then open another podcast and play Y | Switches to Y; mini player updates | All |
| 18 | Dark ↔ Light | Player screens readable | All |
| 19 | Swipe the app away from recents | Playback stops or continues **consistently** (decide as a team) | All |

### Step 4: Remove the Demo
After all 3 merges, `MyanCastApp` uses `Media3PlayerController(this)`. Delete `player/DemoPlayerController.kt` (C) and build again.

### ✅ Day 4 Deliverable
- [ ] 3 slices merged; build + tests pass
- [ ] `DemoPlayerController` deleted
- [ ] Edge cases 1–19 checked
- [ ] Each member can explain the path above

---

# Day 5: 🔄 CROSS-REVIEW + TEST

### Review rotation (changed from Phase 2 so everyone reads new code)

```
A → C's code
B → A's code
C → B's code
```

### Review checklist (everyone)
- [ ] No `!!`
- [ ] ViewModels import `PlayerController`, never `androidx.media3.*` or `android.*`
- [ ] No Media3 types outside `player/`
- [ ] Time/speed formatting in the mapping function, not in composables
- [ ] No `label*` style on Myanmar text
- [ ] Contract files unchanged since Day 0 (or changed with all 3 agreeing)
- [ ] Player/controller released in `onDestroy` (B)
- [ ] Position ticker stops when paused (B), so it doesn't waste battery

### Tests

| Who | Test file | What |
|---|---|---|
| A | `MiniPlayerViewModelTest` | hidden with no episode; visible + mapped fields; togglePlayPause calls controller |
| B | `PlaybackStateTest` | `currentEpisode`, `hasNext`/`hasPrevious` at edges, `progress` with 0 duration |
| B | `PodcastDetailViewModelTest` (update) | `playEpisode(id)` calls `play()` with the right start index |
| C | `PlayerQueueTest` | filter, start index, missing ID, empty list, skip helpers, speed cycle |
| C | `PlayerViewModelTest` | `toUiState()` mapping, duration fallback, `seekToFraction`, `cycleSpeed` |

```bash
./gradlew testDebugUnitTest
```

---

## Phase 3: Final checklist

| # | Item | ☐ |
|---|---|---|
| 1 | Contract agreed on Day 0 and merged first | ☐ |
| 2 | Service + manifest; plays in background with notification | ☐ |
| 3 | Tap episode / play all → plays | ☐ |
| 4 | Mini player on every screen except Player | ☐ |
| 5 | Full Player: cover, titles, seek, 15/30 skip, prev/next, speed | ☐ |
| 6 | Audio focus + noisy (headphones) handled | ☐ |
| 7 | 19 edge cases checked | ☐ |
| 8 | Tests pass (A 3+, B 3+, C 8+) | ☐ |
| 9 | No `!!`, no Media3 outside `player/`, no Poppins on Myanmar | ☐ |
| 10 | All 3 can explain the playback path | ☐ |

---

## Phase 3: Troubleshooting

| Problem | Layer | Fix |
|---|---|---|
| `ForegroundServiceTypeException` / crash on play | Engine | Manifest: `foregroundServiceType="mediaPlayback"` + `FOREGROUND_SERVICE_MEDIA_PLAYBACK` |
| No sound, no error | Engine | `prepare()` called? Is `audioUrl` a real `https://` MP3? Check emulator volume |
| `play()` does nothing the first time | Engine | MediaController not connected yet → queue the request until connected |
| Mini player and Full player show different things | Model | Two controllers created → use the **one** `MyanCastApp.playerController` |
| Seek bar never moves | Model | Position ticker missing: Media3 has no position events |
| Seek thumb jumps back while dragging | UI | Show the drag value while dragging; seek on `onValueChangeFinished` |
| Duration shows `0:00` at first | Model | Stream still loading → fall back to `episode.duration` |
| Notification doesn't appear (Android 13+) | Engine | Check the notification permission in the app settings. Media-session notifications have special rules; test on a real API 33+ device |
| `IllegalStateException: Method called on wrong thread` | Engine | Only call `MediaController` from the main thread |
| Unit test crash on `MediaItem` / `Uri` | Test | Media3 types need Android → keep them out of tested code (that's why the contract has none) |
| Mini player covers the last list item | UI | Bottom padding: Scaffold `padding` + `contentPadding` |
| Myanmar subtitle looks squashed | UI | `label*` style → `body*` |

---

## Phase 3: Git workflow

```
main
  ↑  (Day 0) feature/phase3-contract     ← C: PlaybackState + PlayerController + PlayerQueue + Demo/Fake controllers
  ↑
  ├── feature/phase3-slice-a   (A: Mini player)
  ├── feature/phase3-slice-b   (B: Engine)
  └── feature/phase3-slice-c   (C: Full player)
```

**3 rules (same as Phase 2):**
1. Never push code that doesn't build
2. Tell the owner before touching their file, **and never touch the contract alone**
3. Push at least once a day

> Stage `app/src` only (`git add app/src`), not `.idea/` or `.kotlin/`.

---

## After Phase 3: Phase 4 (News + Library + Room)

From `fullscope.md` Week 4: `NewsRepository` + `NewsScreen` + `NewsDetailScreen`, Room (`LibraryEntity`, DAO),
`LibraryScreen` (subscribed + history), `SearchScreen`, and the **Continue Listening** card on Home, which needs
Phase 3's `PlaybackState` + Room history.
