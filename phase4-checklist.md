# ✅ Phase 4 — Final Checklist (News + Library + Search)

> `phase4.md`၊ `phase4-memberC.md`၊ `fullscope.md` (feature #8–13) တို့နဲ့ ချိန်ထိုး စစ်ဆေးပြီး။
> Code ကို 2026-09-24 မှာ စစ်ထားတယ် — `main` @ `3de5fc9` (PR #23) + C ရဲ့ Slice C commit။

## အနှစ်ချုပ်

| အပိုင်း | အခြေအနေ |
|---|---|
| Build (`assembleDebug`) | ✅ Pass · **warning ၀ ခု** |
| Unit test (`testDebugUnitTest`) | ✅ **109 / 109 pass** (Phase 3 မှာ 52 → **+57**) |
| Day 0 Contract (Room + `LibraryRepository`) | ✅ တစ်ပတ်လုံး မပြောင်းရ |
| Slice A: Library + Subscribe | ✅ Code ပြီး (PR #19–22) |
| Slice B: News + News Detail | ✅ Code ပြီး (PR #23) |
| Slice C: Search + ဆက်နားထောင်ရန် | ✅ Code ပြီး |
| **`PlaceholderScreen` ကျန်** | ✅ **တစ်ခုမှ မကျန် — bottom nav ၅ ခုလုံး တကယ့် screen** |
| **Device test** | ⬜ ကျန် (section 6) |
| **Cross-review** | ⬜ ကျန် (section 6) |
| **Phase 4 ပြီးမှု** | **~၉၀%** — code ပြီး၊ device test နဲ့ review ကျန် |

---

## 0. 📜 Day 0 — Contract (A က ရေး)

- [x] `domain/model/PlaybackProgress.kt` — field ၈ ခု + `fraction` + `isFinished` (၉၅%)
- [x] `data/local/LibraryEntity.kt` v2 — `TYPE_SUB` / `TYPE_HISTORY` constant
- [x] `AppDatabase` — `version = 2` + `fallbackToDestructiveMigration()` (crash မဖြစ်)
- [x] `data/repository/LibraryRepository.kt` — interface (subscribe ၃ + history ၃)
- [x] `MyanCastApp.libraryRepository` = `RoomLibraryRepository(database.libraryDao())`
- [x] `FakeLibraryRepository` (test)
- [x] **Day 0 ဆုံးဖြတ်ချက်** — history row မှာ `episodeTitle` / `podcastTitle` / `coverUrl` **ပါ သိမ်း**
      → C က Home/Library မှာ ပြဖို့ Firestore ပြန်ဆွဲစရာ မလိုတော့ဘူး
- [x] Contract ကို Day 0 ကတည်းက **တစ်ခါမှ မပြောင်းရ** (Phase 3 နဲ့ အတူတူ — ၂ ပတ်ဆက်တိုက်)

---

## 1. 🅰️ Slice A — Library + Subscribe

### Data
- [x] `LibraryDao` — `byType()` · `lastPlayed()` · `@Upsert` · `delete(id, type)`
- [x] `RoomLibraryRepository` — Entity ↔ `PlaybackProgress` map (Room type UI ဆီ မရောက်)

### Model
- [x] `LibraryViewModel` — `combine` ၄ ခု (tab + podcasts + subscribedIds + history)
- [x] `LibraryUiState` + `LibraryTab` enum + `HistoryRow`
- [x] `catch` နဲ့ error ကိုင် · `stateIn(WhileSubscribed(5_000))`
- [x] `PodcastDetailViewModel` — `isSubscribed: StateFlow<Boolean>` + `toggleSubscribe()`

### UI
- [x] `LibraryScreen` — `TabRow` ၂ ခု (သိမ်းထားသော / မှတ်တမ်း)
- [x] Empty ၂ မျိုး — `"သိမ်းထားတာ မရှိသေးပါ"` / `"နားထောင်ထားတာ မရှိသေးပါ"`
- [x] History row မှာ `LinearProgressIndicator` (ဘယ်အထိ နားထောင်ပြီးလဲ)
- [x] `PodcastListItem` (Phase 2 component) ပြန်သုံး
- [x] Detail ရဲ့ ခလုတ် — သိမ်းပြီး/မသိမ်းရသေး အလိုက် ပြောင်း (`Check` / `Add` icon)
- [x] NavHost မှာ `LibraryViewModel` ချိတ်ပြီး

### Test
- [x] `RoomLibraryRepositoryTest` — ၁၁ test (Robolectric + in-memory Room)
- [x] `LibraryViewModelTest` — ၂ test

---

## 2. 🅱️ Slice B — News

### Data
- [x] `NewsRepository` → **interface** + `FirestoreNewsRepository` (Phase 2 ပုံစံ)
- [x] `getNewsItem(id)` — `.await()` + document မရှိရင် `null`
- [x] `FakeNewsRepository` (test)
- [x] `orderBy("publishedAt")` ရဲ့ အန္တရာယ်ကို comment နဲ့ မှတ်ထား (Phase 2 သင်ခန်းစာ)

### Model
- [x] `NewsFilter.kt` — `buildNewsCategories()` / `filterNews()` pure function
- [x] `ALL_CATEGORY` ကို hard-code မလုပ်ဘဲ ပြန်သုံး
- [x] `NewsViewModel` — `loadJob?.cancel()` · `allNews`/`news` ခွဲ · `featured` + `rest`
- [x] `NewsDetailViewModel` — `canRetry` · `playError` · `hasAudio`
- [x] `NewsItem.toEpisode()` — သတင်း audio ကို Phase 3 player နဲ့ ဖွင့်ဖို့ mapping (pure)
- [x] Audio မရှိရင် `PlayerQueue` က ဖယ်လိုက်တာကို အခြေခံပြီး `playError` ပြ

### UI
- [x] `NewsScreen` — `FeaturedNewsCard` ၁ ခု + `CategoryChips` + list
- [x] Empty ၂ မျိုး (data မရှိ / filter ရလဒ် မရှိ)
- [x] `NewsDetailScreen` — ပုံ + headline + category/ရက်စွဲ + body
- [x] Audio ရှိမှ `"▶ နားထောင်မည်"` ခလုတ် ပြ (မရှိရင် လုံးဝ မပြ)
- [x] `NewsCard.kt` Poppins bug ပြင်ပြီး (`labelSmall` → `bodySmall`)
- [x] NavHost မှာ `onPlay` → Player screen (`launchSingleTop`)
- [x] `@Preview` တွေ ရှိ

### Test
- [x] `NewsFilterTest` ၅ · `NewsViewModelTest` ၆ · `NewsDetailViewModelTest` ၈

---

## 3. 🅲 Slice C — Search + ဆက်နားထောင်ရန်

### Data
- [x] `ui/search/SearchFilter.kt` — `searchPodcasts()` pure (trim + ignoreCase + title/category)
- [x] Query အလွတ် ဆို `emptyList()` (idle state အတွက်)
- [x] Firestore မှာ contains query မရှိတာကို client-side filter နဲ့ ဖြေရှင်း (podcast ၅ ခုပဲ)

### Model
- [x] `SearchViewModel` — `combine(query, podcasts)` + `isIdle` + `WhileSubscribed`
- [x] `onQueryChange()` / `clearQuery()`
- [x] `player/HistoryRecorder.kt` — ၁၀ စက္ကန့် bucket throttle
- [x] `durationMs == 0` ဆို **မသိမ်း** (fraction မှားပြီး `isFinished` မဖြစ်အောင်)
- [x] Pause / episode ပြောင်းရင် ချက်ချင်း သိမ်း
- [x] App scope သုံး (`MyanCastApp` — screen ပိတ်လည်း ဆက်သိမ်း)
- [x] `HomeViewModel` — `lastPlayed: PlaybackProgress?` (`isFinished` ဆို null)
- [x] `resumeLastPlayed()` — ဖွင့်ထားပြီးသား ဆို **ပြန်မစ** · `toggleLastPlayed()` — ဖွင့်/ရပ်
- [x] `playingEpisodeId` + `isLastPlayedPlaying` — card ရဲ့ ▶/⏸ အတွက်
- [x] `resumeError` + `resumeErrorShown()` — တိတ်တဆိတ် မရပ်သွားအောင်

### UI
- [x] `SearchScreen` — placeholder အစား၊ state **၄ မျိုး** (loading / error / idle / မတွေ့)
- [x] `TextField` မှာ `textStyle` သတ်မှတ် (မသတ်မှတ်ရင် Poppins ဖြစ်မယ်)
- [x] ✕ ခလုတ် · `ImeAction.Search` · `singleLine`
- [x] `@Preview` ၃ ခု · signature မပြောင်း (NavHost မထိရ)
- [x] `ContinueListeningCard` — `Episode` အစား primitive · `"ဆက်နားထောင်ရန်"` + `bodySmall`
- [x] Card မှာ ⏸/▶ icon ပြောင်း + မြန်မာ `contentDescription`
- [x] Card body နှိပ် → Full Player · ခလုတ် နှိပ် → screen မပြောင်းဘဲ ဖွင့်/ရပ်
- [x] Home မှာ snackbar (ဆက်ဖွင့်လို့ မရရင် အကြောင်း ပြ)
- [x] NavHost — `onOpenPlayer` (`launchSingleTop`)

### Test
- [x] `SearchFilterTest` ၇ · `SearchViewModelTest` ၄ · `HistoryRecorderTest` ၅
- [x] `HomeViewModelTest` ၂ → **၁၁** (ဆက်နားထောင်ရန် ၉ ခု ထပ်ထည့်)

### 🐛 Device test မှာ တွေ့ခဲ့တဲ့ bug ၂ ခု (ဖြေရှင်းပြီး)

| # | Bug | အကြောင်းရင်း | ဖြေရှင်းချက် |
|---|---|---|---|
| 1 | App ပိတ်/ဖွင့်ပြီး card နှိပ်ရင် **အစကနေ ပြန်စ** | `play()` ပြီးမှ `seekTo()` ခေါ်တာ — service မချိတ်ရသေးရင် seek က တိတ်တဆိတ် ပျောက် | Contract မှာ `play(queue, title, startPositionMs = 0L)` **additive** ထည့် → `setMediaItems(items, index, startPositionMs)` |
| 2 | App ပိတ်/ဖွင့်ပြီး card **နှိပ်မရ** · mini player မပေါ် | `Media3PlayerController` က `init` မှာ **တစ်ခါပဲ** ချိတ်တယ် — မအောင်ရင် `controller` ထာဝရ `null`၊ `play()` တိုင်း `pendingPlay` ပဲ မှတ်ပြီး ဘာမှ မဖြစ် | `connect()` ခွဲထုတ်ပြီး `play()` / `togglePlayPause()` မှာ **retry** + `Log.e` |

> **ဘာကြောင့် ဒီ bug က Phase 3 မှာ မပေါ်လဲ** — အရင်က `playerController` ကို screen ဖွင့်မှ (foreground) ဖန်တီးတယ်။
> C ရဲ့ `historyRecorder.start()` က `Application.onCreate()` မှာ ဖြစ်လို့ **process စချိန်** (Activity မတက်ခင်) ချိတ်မိတယ်။
> ⚠️ ဒီ ၂ ခုက **B ရဲ့ ဖိုင်** (`PlayerController.kt` contract + `Media3PlayerController.kt`) — B ကို ပြောရမယ်။

---

## 4. 🤝 Team စည်းကမ်း (code ထဲ စစ်ပြီး)

- [x] `app/src/main` မှာ `!!` မရှိ
- [x] ViewModel တွေမှာ `android.*` / `androidx.media3.*` / Room type မရှိ
      (ချွင်းချက် ၁ — `PlaybackProgress.kt` ရဲ့ မလိုတဲ့ `android.icu` import၊ section 6 ကြည့်)
- [x] Media3 က `player/` အပြင် မထွက် · Room က `data/local/` အပြင် မထွက်
- [x] Pure logic က top-level function (`SearchFilter`, `NewsFilter`, `CategoryFilter`, `PlayerQueue`)
- [x] `collectAsStateWithLifecycle()` သုံး · `loadJob?.cancel()` ပါ
- [x] မြန်မာစာအပေါ် `label*` (Poppins) မရှိ — ကျန်တဲ့ ၄ နေရာက ASCII (`"1.5x"`, `"12:05"`) နဲ့ `"See all"` ပဲ
- [x] `ALL_CATEGORY` က နေရာတိုင်း တစ်ခုတည်း
- [x] `@Preview` က private stateless content ပေါ်မှာပဲ
- [x] Test fake ၄ ခု — `FakePodcastRepository` · `FakeNewsRepository` · `FakeLibraryRepository` · `FakePlayerController`
- [x] Build warning ၀ ခု

---

## 5. ⏸️ တမင်ဆိုင်းငံ့ထားတာ

| အချက် | ဘာကြောင့် | ဘယ်တော့ |
|---|---|---|
| Zawgyi စာသား ပြောင်းလဲမှု (Rabbit converter) | ~၁ ရက် · grading criteria မဟုတ် | **Phase 5 — ဆုံးဖြတ်ချက် ချရမယ်** |
| `MainActivity` ရဲ့ comment-out `EPISODE_TEST` | Team ဆုံးဖြတ်ချက် | Phase 5 (submission မတိုင်မီ) |
| Firestore `duration` data မကိုက်တာ | Code မှားတာ မဟုတ် · data ပြင်ရမယ် | Phase 5 (demo မတိုင်မီ) |
| `FirebaseSource.kt` · `SettingsViewModel.kt` အလွတ် stub | မလိုတော့ဘူး (`FirestoreExt` / `AppConfig` က တာဝန်ယူထား) | Phase 5 (ဖျက်) |
| Sleep timer · Favorites · Queue screen · Download · FCM | `fullscope.md` ရဲ့ nice-to-have | အချိန်ရရင် (Phase 5) |
| History ၂၀ limit | Day 0 ဆုံးဖြတ်ချက် — ခု limit မထား | Phase 5 (သို့ လက်ခံ) |
| Process death ပြီးရင် mini player အလွတ် | queue က memory ထဲပဲ | Course project အတွက် လက်ခံ |

### ✅ Poppins carry-over list — **ပြီးပြီ**

| ဖိုင် | အခြေအနေ |
|---|---|
| `MiniPlayer.kt` | ✅ A ပြင်ပြီး (Phase 3) |
| `NewsCard.kt` | ✅ B ပြင်ပြီး (Phase 4) |
| `ContinueListeningCard.kt` | ✅ C ပြင်ပြီး + မြန်မာပြန်ပြီး (Phase 4) |
| `SectionHeader.kt` (`"See all"`) | ⬜ အင်္ဂလိပ်စာမို့ အန္တရာယ် မရှိ — ပေါ်ရင် ဘာသာပြန် (Phase 5) |

---

## 6. ⬜ ကျန်ရှိနေသေးတာ (code မဟုတ် — လူလိုတာ)

### 6.1 🅰️ A ရဲ့ code မှာ ကျန်နေတဲ့ ၆ ချက် (C review လုပ်ပြီး — ခုထိ မပြင်ရသေး)

| # | တွေ့ချက် | ဖိုင် | အဆင့် | ☐ |
|---|---|---|---|---|
| 1 | `import android.icu.text.CaseMap` — မလိုတဲ့ auto-import၊ **domain model ထဲမှာ `android.*`** | `PlaybackProgress.kt:3` | 🔴 | ☐ |
| 2 | `exportSchema = false` ကျန်ခဲ့လို့ KSP warning | `AppDatabase.kt` | 🟡 | ☐ |
| 3 | Test function နာမည်မှာ `%` (Windows warning ၂ ခု) | `RoomLibraryRepositoryTest.kt:158,170` | 🟡 | ☐ |
| 4 | `coverURL` (Entity) vs `coverUrl` (model) နာမည် မညီ | `LibraryEntity.kt:16` | 🟡 | ☐ |
| 5 | History **၂၀ limit မထား** (Day 0 ဆုံးဖြတ်ချက်) | `LibraryDao.byType()` | 🟡 | ☐ |
| 6 | မလိုတဲ့ import `kotlinx.coroutines.flow.toSet` | `RoomLibraryRepository.kt:8` | 🟡 | ☐ |

### 6.2 Device Test — ၁၅ ခု

**✅ ပြီးသွားပြီ (C — real phone)**

| # | စမ်းချက် | ရလဒ် |
|---|---|---|
| 9 | Search `"နည်း"` ရိုက် | ✅ |
| 10 | Search ရလဒ် မရှိ → `"ရှာမတွေ့ပါ"` | ✅ |
| 11 | ✕ နှိပ် → idle | ✅ |
| 12 | Episode ၃၀ စက္ကန့် ဖွင့် → Home → card + progress | ✅ |
| 14 | ပြီးအောင် နားထောင် → card ပျောက် | ✅ |
| + | App ပိတ်/ဖွင့် → history ကျန် | ✅ |

**🚩 ကျန်သေး**

| # | စမ်းချက် | မျှော်လင့်ရမယ့် ရလဒ် | ဘယ်သူ | ☐ |
|---|---|---|---|---|
| 13 | Card နှိပ် → ရပ်ထားတဲ့နေရာက ဆက်ဖွင့် (**bug fix ၂ ခု ပြီးမှ ပြန်စမ်း**) | ၃၀ စက္ကန့်ကနေ စ · နှိပ်မရ မဖြစ်ရ | 🅲 C | ☐ |
| + | Card ⏸/▶ ခလုတ် | ဖွင့်/ရပ် ပြောင်း · screen မပြောင်းရ | 🅲 C | ☐ |
| 1 | Podcast သိမ်း → Library | ချက်ချင်း ပေါ် | 🅰️ A | ☐ |
| 2 | Unsubscribe | ချက်ချင်း ပျောက် | 🅰️ A | ☐ |
| 3 | App ပိတ်/ဖွင့် | သိမ်းထားတာ ကျန် (Room) | 🅰️ A | ☐ |
| 4 | Library အလွတ် | Empty ၂ မျိုး မှန် | 🅰️ A | ☐ |
| 5 | News list | ၁၀ ခု + ပုံ ပေါ် | 🅱️ B | ☐ |
| 6 | News category chip | Filter မှန် | 🅱️ B | ☐ |
| 7 | News detail — audio ရှိ | ခလုတ် ပေါ် · ဖွင့်ရ · mini player ပေါ် | 🅱️ B | ☐ |
| 8 | News detail — audio မရှိ | ခလုတ် **မပေါ်ရ** | 🅱️ B | ☐ |
| 15 | Screen ၄ ခုလုံး rotate | State မပျောက် | 🤝 all | ☐ |

### 6.3 Cross-Review — လှည့်ပုံ: **C → A · A → B · B → C**

| Review | အခြေအနေ |
|---|---|
| **C → A** | ✅ ပြီး — finding ၆ ချက် (section 6.1) |
| **A → B** | ☐ A လုပ်ရန် |
| **B → C** | ☐ B လုပ်ရန် — အထူးကြည့်ရန်: `HistoryRecorder` throttle · contract ပြောင်းလဲမှု ၂ ခု |

### 6.4 Merge

- [ ] C ရဲ့ PR (`pmh` → `main`) — Slice C + bug fix ၂ ခု + ဒီ checklist
- [ ] `main` မှာ build + test ၁၀၉ pass
- [ ] **B ကို ပြော** — `PlayerController.play()` မှာ `startPositionMs` ထပ်ထည့်ပြီး (additive) · `connect()` retry ထည့်ပြီး

---

## 7. 🎯 Phase 4 — Success Criteria

`fullscope.md` ရဲ့ MVP feature ၁၅ ခု — Phase 4 ပြီးရင် **၁၃ ခု** ပြီး:

| # | Feature | Phase | ☐ |
|---|---|---|---|
| 1 | Podcast List | 2 | ✅ |
| 2 | **Continue Listening** | **4** | ✅ |
| 3 | Podcast Detail | 2 | ✅ |
| 4 | Play / Pause | 3 | ✅ |
| 5 | Seek (15s/30s) | 3 | ✅ |
| 6 | Mini Player | 3 | ✅ |
| 7 | Full Player | 3 | ✅ |
| 8 | **News List** | **4** | ✅ |
| 9 | **News Detail** | **4** | ✅ |
| 10 | **Subscribe / Unsubscribe** | **4** | ✅ |
| 11 | **Library (Subscribed + History)** | **4** | ✅ |
| 12 | **Search** | **4** | ✅ |
| 13 | Zawgyi / Unicode Toggle | — | ⏸️ Phase 5 ဆုံးဖြတ်ချက် |
| 14 | Dark / Light Theme | 1 | ✅ |
| 15 | Playback Speed | 3 | ✅ |

၃ ယောက်လုံး ရှင်းပြနိုင်ရမယ့် အချက်:

| # | အချက် | A | B | C |
|---|---|---|---|---|
| 1 | Room ကနေ Flow ထုတ်ပြီး UI အထိ ရောက်တဲ့ လမ်းကြောင်း | ☐ | ☐ | ☐ |
| 2 | Contract (interface + fake) က ဘာကြောင့် ၃ ပတ်ဆက် အလုပ်ဖြစ်လဲ | ☐ | ☐ | ☐ |
| 3 | `combine` က query နဲ့ data ၂ ခုကို ဘယ်လို ပေါင်းလဲ | ☐ | ☐ | ☐ |
| 4 | History ကို ဘာကြောင့် throttle လုပ်ရလဲ | ☐ | ☐ | ☐ |
| 5 | Firestore မှာ contains query မရှိတာကို ဘယ်လို ဖြေရှင်းလဲ | ☐ | ☐ | ☐ |
| 6 | `play()` ပြီးမှ `seekTo()` ခေါ်လို့ မရတာ ဘာကြောင့်လဲ | ☐ | ☐ | ☐ |

---

## 8. ➡️ Phase 4 ပြီးရင် — Phase 5 (Polish + Presentation)

Feature အားလုံး ပြီးပြီ — Phase 5 က **သန့်စင်ခြင်း၊ စမ်းသပ်ခြင်း၊ တင်ပြခြင်း** ပါ (`phase5.md` ကြည့်)။

ကျန်နေတဲ့ အလုပ် (Phase 5 Day 1 မှာ စုပြီး ဆုံးဖြတ်မယ်):
- ဒီ checklist ရဲ့ section 6 အားလုံး (device test ၁၅၊ review ၂၊ A ရဲ့ ၆ ချက်)
- `phase3-checklist.md` section 6 က ကျန်နေတဲ့ device test ၁၁ ခု + review ၂ ခု
- Section 5 က ဆိုင်းငံ့ထားတာ အားလုံး — Zawgyi ဆုံးဖြတ်ချက်၊ Firestore `duration`၊ dead code
- `README.md` မရှိသေး (`fullscope.md` quality checklist မှာ ပါတယ်)
- `proguard-rules.pro` မရှိသေး (release build မှာ လိုမယ်)

> **Phase 4 ရဲ့ သင်ခန်းစာ** — Contract habit က ၂ ပတ်ဆက်တိုက် အလုပ်ဖြစ်တယ်။
> ဒါပေမဲ့ **contract ကို additive ပြောင်းရတာလည်း ဖြစ်တယ်** (`startPositionMs`) —
> default value နဲ့ ထည့်တာမို့ ကျန် ၂ ယောက် ဘာမှ မပြင်ရဘူး။ ဒါက "ကောင်းတဲ့" contract ပြောင်းလဲမှုပါ။
