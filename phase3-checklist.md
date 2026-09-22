# ✅ Phase 3 — Final Checklist (Player)

> `phase3.md`၊ `memberC-phase3.md`၊ `memberC-next.md` တို့နဲ့ ချိန်ထိုး စစ်ဆေးပြီး။
> Code ကို 2026-09-23 (branch `pmh` = `main` @ `bd769b1` + `ce119dd` + fix ၂ ခု) မှာ စစ်ထားတယ်။

## အနှစ်ချုပ်

| အပိုင်း | အခြေအနေ |
|---|---|
| Build (`assembleDebug`) | ✅ Pass · warning ၀ ခု |
| Unit test (`testDebugUnitTest`) | ✅ **52 / 52 pass** (Phase 2 မှာ 25 → +27) |
| Day 0 Contract (`PlaybackState` + `PlayerController`) | ✅ တစ်ပတ်လုံး မပြောင်းရ |
| Slice A: Mini Player | ✅ Code ပြီး |
| Slice B: Engine (Service + Media3) | ✅ Code ပြီး |
| Slice C: Full Player | ✅ Code ပြီး |
| **Device test** | 🟡 **၉ / ၁၉** — C အားလုံး ပြီး၊ **B နဲ့ A ကျန်** |
| **Cross-review** | 🟡 C → B ဖတ်ပြီး၊ **A → C, B → A ကျန်** |
| **Phase 3 ပြီးမှု** | **~၈၅%** — code ပြီး၊ device test နဲ့ review ကျန် |

---

## 0. 📜 Day 0 — Contract

- [x] `domain/model/PlaybackState.kt` — field ၉ ခု + တွက်ထုတ် property ၅ ခု
- [x] `player/PlayerController.kt` — interface + function ၈ ခု
- [x] App တစ်ခုလုံးမှာ instance တစ်ခုတည်း (`MyanCastApp.playerController`)
- [x] `FakePlayerController` (test) — A ရော C ရော သုံး
- [x] `DemoPlayerController` (ယာယီ) — B ရဲ့ engine ဝင်ပြီးရင် ဖျက်ပြီး ✅
- [x] **Contract ကို Day 0 ကတည်းက တစ်ခါမှ မပြောင်းရ** — design မှန်ကြောင်း အထင်ရှားဆုံး အထောက်အထား

---

## 1. 🅰️ Slice A — Mini Player

### Model
- [x] `MiniPlayerUiState` — field ၇ ခု (visible, title, subtitle, cover, progress, isPlaying, hasNext)
- [x] `MiniPlayerViewModel` — `PlaybackState` → UI state (pure mapping)
- [x] `stateIn(WhileSubscribed(5_000))`
- [x] ViewModel ထဲ `android.*` / `media3` မရှိ

### UI
- [x] `MyanCastNavHost` — MiniPlayer ကို bottom nav **အပေါ်** မှာ ထား
- [x] Player screen မှာ **ဖျောက်** (`currentRoute != Screen.Player.route`)
- [x] Episode ရှိမှ ပေါ် (`miniState.visible`)
- [x] နှိပ်ရင် Full Player ဖွင့် (`launchSingleTop = true`)
- [x] `MiniPlayer.kt` font bug ပြင်ပြီး (`labelSmall` → `bodySmall`)
- [x] `hasNext = false` ဆိုရင် ⏭ ခလုတ် disable

### Test
- [x] `MiniPlayerViewModelTest` — ၃ test (ဖျောက်/ပြ/toggle)

---

## 2. 🅱️ Slice B — Playback Engine

### Engine / Manifest
- [x] `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_MEDIA_PLAYBACK` + `POST_NOTIFICATIONS`
- [x] `<service android:foregroundServiceType="mediaPlayback">` + `MediaSessionService` intent-filter
- [x] `PlaybackService` — ExoPlayer + `AudioAttributes(USAGE_MEDIA, SPEECH)` + audio focus
- [x] `setHandleAudioBecomingNoisy(true)` — headphone ဖြုတ်ရင် pause
- [x] Notification နှိပ်ရင် app ပြန်ဖွင့် (`setSessionActivity`)
- [x] `onTaskRemoved` — မဖွင့်နေရင်သာ service ရပ် (Day 0 ဆုံးဖြတ်ချက်)
- [x] `onDestroy` မှာ player + session release

### Model
- [x] `Media3PlayerController` — `PlayerController` ကို implement
- [x] Service မချိတ်ရသေးခင် `play()` ခေါ်ရင် `pendingPlay` မှာ မှတ်ထား
- [x] `Player.Listener.onEvents` → `PlaybackState` ပြန်ဆောက်
- [x] Position က event နဲ့ မလာလို့ — ဖွင့်နေတုန်း 500ms ticker၊ **pause ဆိုရင် ရပ်**
- [x] `C.TIME_UNSET` → `0` (duration မသိရသေး)
- [x] Error message မြန်မာလို
- [x] `MyanCastApp.playerController` = `Media3PlayerController(this)`

### UI (Detail → Play)
- [x] `PodcastDetailViewModel` က `PlayerController` ကို constructor ကနေ ယူ
- [x] `playEpisode(id)` — ဖွင့်လို့ရမှ `true` ပြန်၊ Player ဖွင့်
- [x] ဖွင့်လို့ မရရင် snackbar `"ဒီအပိုင်းကို ဖွင့်လို့ မရပါ"`
- [x] `"▶ အားလုံး ဖွင့်"` အလုပ်လုပ်
- [x] `nowPlayingId` — လက်ရှိ ဖွင့်နေတဲ့ episode row ကို highlight

### Test
- [x] `PodcastDetailViewModelTest` — ၆ test (load ၂ + play ၄)

---

## 3. 🅲 Slice C — Full Player

### Data
- [x] `PlayerQueue.from()` — `audioUrl` မရှိတဲ့ episode ဖယ်
- [x] နှိပ်တဲ့ episode ဖွင့်လို့ မရရင် **`EMPTY`** ပြန် (index 0 မဟုတ် — episode ၃ နှိပ်ပြီး ၁ မကြားရအောင်)
- [x] `skipBackTarget` / `skipForwardTarget` / `nextSpeed` — **top-level pure function**
- [x] `SKIP_BACK_MS` / `SKIP_FORWARD_MS` / `PLAYBACK_SPEEDS` constant
- [x] မသိတဲ့ speed → `1f` ပြန်စ

### Model
- [x] `PlayerUiState` + `PlaybackState.toUiState()` (pure)
- [x] Duration မသိရသေးရင် Firestore ရဲ့ `episode.duration` ကို fallback
- [x] `formatSpeed()` (`"1x"` / `"1.25x"`) + `formatMs()` (`"24:30"`)
- [x] `seekToFraction` / `cycleSpeed` က `controller.state.value` ကို ဖတ် (stale state မဟုတ်)
- [x] `stateIn(WhileSubscribed(5_000))` + initial value အလွတ် မဟုတ်
- [x] ViewModel ထဲ `android.*` / `media3` မရှိ

### UI
- [x] `FullPlayerScreen` — NavHost အတွက် signature မပြောင်း
- [x] Cover 280dp + episode title (`headlineSmall`) + podcast title (`bodyMedium`)
- [x] Seek bar — **drag နေတုန်း drag value ပြ**၊ လွှတ်မှ seek
- [x] Drag နေတုန်း ဘယ်ဘက် အချိန်က လက်ကို လိုက်
- [x] `durationMs == 0` ဆိုရင် slider disable
- [x] `PlayerControls` — `isBuffering` spinner + `hasNext`/`hasPrevious` disable
- [x] `contentDescription` မြန်မာလို (TalkBack)
- [x] `Icons.AutoMirrored.Filled.Forward` (deprecated warning ၀)
- [x] ဖွင့်ထားတာ မရှိရင် `EmptyView("ဖွင့်ထားတာ မရှိသေးပါ")`
- [x] Error ကို slider အောက်မှာ ပြ (screen တစ်ခုလုံး မဖုံး)
- [x] Landscape အတွက် `verticalScroll`
- [x] `@Preview` ၃ ခု (playing / buffering+error / empty)

### Test
- [x] `PlayerQueueTest` — ၁၂ test
- [x] `PlayerViewModelTest` — ၈ test

---

## 4. 🤝 Team စည်းကမ်း (code ထဲ စစ်ပြီး)

- [x] `app/src/main` မှာ `!!` မရှိ
- [x] ViewModel တွေ `PlayerController` ကိုပဲ သိ — `androidx.media3.*` မသိ
- [x] Media3 type တွေ `player/` အပြင် မထွက်
- [x] အချိန် / speed format ကို mapping function မှာပဲ (composable ထဲ မဟုတ်)
- [x] မြန်မာစာအပေါ် `label*` (Poppins) style မရှိ၊ ASCII (`"24:30"`, `"1.5x"`) မှာပဲ သုံး
- [x] `collectAsStateWithLifecycle()` သုံး
- [x] Route တွေ `Screen.kt` ကနေပဲ
- [x] `@Preview` က private stateless content ပေါ်မှာပဲ
- [x] Build warning ၀ ခု

---

## 5. ⏸️ တမင်ဆိုင်းငံ့ထားတာ

| အချက် | ဘာကြောင့် | ဘယ်တော့ |
|---|---|---|
| `MainActivity.kt`: commented-out `EPISODE_TEST` | Team ဆုံးဖြတ်ချက် | Final submission မတိုင်မီ |
| Sleep timer · Queue screen · Download | `fullscope.md` မှာ nice-to-have | အချိန်ရရင် |
| `lastPlayed` / "Continue Listening" | Room history လိုတယ် | Phase 4 |
| `"သိမ်း"` (subscribe) ခလုတ် | Room လိုတယ် | Phase 4 |
| `PlaybackStateTest` (`hasNext`/`progress` edge) | ViewModel test တွေက သွယ်ဝိုက် ဖုံးထားပြီး | Optional (B) |
| Process death ပြီးရင် queue/podcastTitle ပြန်မရ | Memory ထဲပဲ သိမ်းထား | Course project အတွက် လက်ခံ |

### 📌 Device test မှာ တွေ့ခဲ့တာ — Firestore `duration` မကိုက်

Stream ရဲ့ တကယ့် အလျား (`7:05`) နဲ့ Firestore ရဲ့ `duration` (`18:00`) မတူဘူး။
**Code မှန်တယ်** — fallback က အလုပ်လုပ်တာ — ဒါပေမဲ့ Phase 1 မှာ ထည့်ထားတဲ့ ကိန်းဂဏန်းက
တကယ့် MP3 နဲ့ မကိုက်ဘူး။ `EpisodeRow` မှာ ၇ မိနစ် audio ကို `"၂၄ မိနစ်"` လို့ ပြနေမယ်
(list က stream ကို မမြင်လို့ ဘယ်တော့မှ ပြန်မပြင်ဘူး)။

| နည်းလမ်း | အလုပ် | ရလဒ် |
|---|---|---|
| ⭐ Console မှာ `duration` ပြင် (SoundHelix ~၃၀၀–၄၈၀ စက္ကန့်) | ၂၀ document | နေရာတိုင်း ကိုက် |
| Demo လုပ်မယ့် podcast ၄ ပိုင်းပဲ ပြင် | ၅ မိနစ် | မြင်ရမယ့် နေရာ မှန် |
| မပြင်ဘူး | ၀ | List မှာ မှားပြ၊ Player က ပြန်ပြင် |

> Code ပြဿနာ မဟုတ်လို့ **Phase 3 ကို မပိတ်ဆို့ဘူး** — ဒါပေမဲ့ data က C ရဲ့ တာဝန်၊
> demo မှာ Home နဲ့ Detail မှာ မြင်ရမယ်။

---

## 6. ⬜ ကျန်ရှိနေသေးတာ (code မဟုတ် — လူလိုတာ)

### 6.1 Device Test — ၉ / ၁၉ ပြီး

**✅ ပြီးသွားပြီ (real phone)**

| # | ဘယ်သူ | စမ်းချက် | ရလဒ် |
|---|---|---|---|
| 4 | B | Headphone ဖြုတ် | ✅ Pause ဖြစ် |
| 6 | B | Wi-Fi + cellular ပိတ် | ✅ `"ဖွင့်လို့ မရပါ — အင်တာနက် ချိတ်ဆက်မှု စစ်ကြည့်ပါ"` |
| 7 | C | `audioUrl` အလွတ် episode နှိပ် | ✅ Snackbar ပြ၊ **တခြား episode မဖွင့်** |
| 8 | C | Seek bar ဆွဲ | ✅ Thumb နဲ့ အချိန် လက်ကို လိုက်၊ လွှတ်မှ ခုန် |
| 9 | C | ↺15 / 30↻ အစွန်း | ✅ 0:00 / ဆုံး၊ crash မဖြစ် |
| 10 | C | Speed ၆ ချက် | ✅ Cycle ပြီး **အသံ တကယ် ပြောင်း** |
| 11 | C | ⏭ နောက်ဆုံး / ⏮ ပထမဆုံး | ✅ Disable (မီးခိုး) |
| 12 | C | ဖွင့်နေစဉ် rotate | ✅ အသံ ဆက်၊ landscape scroll၊ position မပျောက် |
| + | C | MiniPlayer နဲ့ bottom padding | ✅ အခွာ နည်းနည်း၊ နေရာလွတ် ကြီး မရှိ |
| + | C | GPRS (slow network) | ✅ Play ခလုတ်နေရာမှာ spinner |

**🚩 ကျန်သေးတယ် — B နဲ့ A စမ်းပေးရန်**

| # | ဘယ်သူ | စမ်းချက် | မျှော်လင့်ရမယ့် ရလဒ် | ☐ |
|---|---|---|---|---|
| 1 | 🅱️ **B** | Episode နှိပ် | အသံ စဖွင့်၊ Full Player ပွင့် | ☐ |
| 2 | 🅱️ **B** | `"▶ အားလုံး ဖွင့်"` | ပထမ episode ကနေ စ၊ ⏭ က ၂ ခုမြောက်ကို သွား | ☐ |
| 3 | 🅱️ **B** | **App background / lock screen** | အသံ ဆက်ဖွင့်၊ **notification + lock screen control အလုပ်လုပ်** ← ⚠️ အရေးကြီးဆုံး | ☐ |
| 5 | 🅱️ **B** | ဖုန်းဝင် / တခြား app က အသံဖွင့် | Pause (audio focus) | ☐ |
| 13 | 🅰️ **A** | Home / Detail / News / Settings မှာ mini player | Bottom nav အပေါ်မှာ ပေါ် | ☐ |
| 14 | 🅰️ **A** | Full Player မှာ mini player | **မပေါ်ရ** | ☐ |
| 15 | 🅰️ **A** | Mini player ၂ ချက် မြန်မြန် နှိပ် | Back stack မှာ Player ၁ ခုပဲ | ☐ |
| 16 | 🅰️ **A** | Full Player ကနေ back | အရင် screen ပြန်၊ mini player ပေါ် | ☐ |
| 17 | 🤝 all | X ဖွင့်ပြီး တခြား podcast က Y ဖွင့် | Y ကို ပြောင်း၊ mini player update | ☐ |
| 18 | 🤝 all | Dark ↔ Light | Player screen တွေ ဖတ်လို့ရ | ☐ |
| 19 | 🤝 all | Recents ကနေ swipe ဖယ် | မဖွင့်နေရင်သာ ရပ် (Day 0 ဆုံးဖြတ်ချက်) | ☐ |

### 6.2 Cross-Review (Day 5) — လှည့်ပုံ: **A → C · B → A · C → B**

| Review | အခြေအနေ |
|---|---|
| **C → B** | 🟡 Code ဖတ်ပြီး — မေးခွန်း ၃ ခု B ကို ပို့ရန် (အောက်မှာ) |
| **A → C** | ☐ A လုပ်ရန် |
| **B → A** | ☐ B လုပ်ရန် |

**C က B ကို မေးရမယ့် ၃ ခု:**
1. Process death ပြီးရင် `queue` / `podcastTitle` ပြန်မရဘူး — mini player အလွတ် ပြန်လာမလား?
2. `POST_NOTIFICATIONS` ကြေညာထားပေမဲ့ runtime မတောင်းဘူး — Android 13+ device မှာ notification တကယ် ပေါ်လား?
3. (ပြီးပြီ) `EpisodeRow` pause icon — C က ပြင်ပြီး၊ B အတည်ပြုပေးပါ

### 6.3 Merge

- [ ] C ရဲ့ PR (`pmh` → `main`) — `ce119dd` + fix ၂ ခု
- [ ] `main` မှာ build + test ၅၂ ခု pass
- [ ] Device test အားလုံး ပြီးကြောင်း group မှာ အတည်ပြု

---

## 7. 🎯 Phase 3 — Success Criteria

Phase 3 ပြီးရင် **၃ ယောက်လုံး** ဒါတွေ လုပ်နိုင်/ရှင်းပြနိုင်ရမယ်:

| # | အချက် | A | B | C |
|---|---|---|---|---|
| 1 | Contract (interface + fake) ဘာကြောင့် Day 0 မှာ ချရလဲ ရှင်းပြနိုင် | ☐ | ☐ | ☐ |
| 2 | `MediaController` ↔ `PlaybackService` ဘယ်လို ချိတ်လဲ | ☐ | ☐ | ☐ |
| 3 | `Player.Listener` event → `PlaybackState` → UI လမ်းကြောင်း | ☐ | ☐ | ☐ |
| 4 | Position က event နဲ့ မလာလို့ ticker လိုတာ ဘာကြောင့်လဲ | ☐ | ☐ | ☐ |
| 5 | `WhileSubscribed` နဲ့ `Eagerly` ကွာခြားချက် | ☐ | ☐ | ☐ |
| 6 | Seek bar မှာ drag state သီးသန့် ထားရတာ ဘာကြောင့်လဲ | ☐ | ☐ | ☐ |
| 7 | Foreground service + `mediaPlayback` type ဘာကြောင့် လိုလဲ | ☐ | ☐ | ☐ |
| 8 | သူများရဲ့ code ကို review လုပ်နိုင် | ☐ | ☐ | ☐ |

---

## 8. ➡️ Phase 3 ပြီးရင် — Phase 4 (News + Library + Room)

`fullscope.md` Week 4။ ခုထိ ဘာမှ မစရသေး — `NewsViewModel`, `LibraryViewModel`,
`SearchViewModel`, `LibraryRepository` အလွတ်၊ News / NewsDetail / Library / Search screen
တွေက `PlaceholderScreen` ပဲ ရှိသေးတယ်။

| Member | Slice (အကြံပြု) | ဖိုင် |
|---|---|---|
| A | Library (Room) | `LibraryEntity`, `LibraryDao`, `LibraryRepository`, `LibraryScreen` |
| B | News | `NewsViewModel`, `NewsScreen`, `NewsDetailScreen` |
| C | Search + Continue Listening | `SearchViewModel`, `SearchScreen`, Room history + `PlaybackState` |

**ဆက်လက်သယ်ဆောင်ရန် (Poppins on မြန်မာစာ):** `NewsCard.kt:68`,
`ContinueListeningCard.kt:57-59` (+ `"Continue Listening"` ဘာသာပြန်), `SectionHeader.kt:37`

> Phase 3 ရဲ့ သင်ခန်းစာ — **Day 0 contract က အလုပ်ဖြစ်တယ်**။ တစ်ပတ်လုံး contract မပြောင်းရဘဲ
> ၃ ယောက်လုံး ပြိုင်တူ လုပ်နိုင်ခဲ့တယ်။ Phase 4 မှာလည်း Room history type ကို Day 0 မှာ ချပါ။
