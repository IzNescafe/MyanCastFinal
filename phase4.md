# Phase 4 — News + Library + Search (Week 4)

> **အရေးကြီး** — ဒါက **plan guide** ပါ။ Code ဘယ်လို ဖွဲ့ရမလဲ ဆိုတဲ့ လမ်းညွှန်ဖြစ်ပြီး
> အတိအကျ copy-paste လုပ်ဖို့ မဟုတ်ပါ။ Phase 1–3 အပေါ်မှာပဲ ဆက်တည်ဆောက်ပါ။
>
> အခြေခံ — `fullscope.md` (feature #8–13)၊ `phase3.md` ရဲ့ Day 0 contract ပုံစံ၊
> နဲ့ 2026-09-23 က `main` ပေါ်က code အခြေအနေ။

---

## Phase 4 — Overview

| အချက် | အသေးစိတ်                                                                                              |
|---|-------------------------------------------------------------------------------------------------------|
| **ရည်ရွယ်ချက်** | Bottom nav ၅ ခုလုံး တကယ် အလုပ်လုပ်စေရန် — News, Library, Search ပြီးအောင် + Home မှာ "ဆက်နားထောင်ရန်" |
| **ကြာချိန်** | Day 0 (၁ နာရီ) + ၅ ရက်                                                                                |
| **အလုပ်ခွဲဝေပုံ** | **Vertical slice** ဆက်သုံး — တစ်ယောက်စီ Data → Model → UI                                             |
| **အဆုံးမှာ ရရမယ့် ရလဒ်** | သတင်း ဖတ်/နားထောင်လို့ရ၊ podcast သိမ်းလို့ရ၊ ရှာလို့ရ၊ ရပ်ထားတဲ့ episode ကို Home ကနေ ဆက်ဖွင့်လို့ရ   |

### `fullscope.md` ရဲ့ ဘယ် feature တွေလဲ

| # | Feature | Screen | ပိုင်ရှင် |
|---|---|---|---|
| 8 | News List | News | 🅱️ B |
| 9 | News Detail (+ optional audio) | News | 🅱️ B |
| 10 | Subscribe / Unsubscribe | Detail | 🅰️ A |
| 11 | Library (Subscribed + History) | Library | 🅰️ A |
| 12 | Search | Search | 🅲 C |
| 2 | Continue Listening | Home | 🅲 C |

**ဒီ phase မှာ မလုပ်ဘူး** — Download, Sleep timer, Favorites, Queue screen (nice-to-have)၊
Zawgyi converter (Phase 5)၊ FCM notification (out of scope)။

---

## Phase 4 က ဘာအပေါ်မှာ စတည်လဲ (code ထဲ စစ်ပြီး)

| အခြေအနေ | ဘာ | ဖိုင် |
|---|---|---|
| ✅ | Room database + `AppDatabase.get(context)` (version 1) | `data/local/AppDatabase.kt` |
| ✅ | `LibraryEntity(id, type)` + `LibraryDao` (sub ၃ function) | `data/local/` |
| ✅ | `NewsRepository.getNews()` — Firestore real-time, limit ၅၀ | `data/repository/NewsRepository.kt` |
| ✅ | `NewsItem(id, headline, body, imageUrl, audioUrl?, category, publishedAt)` | `domain/model/NewsItem.kt` |
| ✅ | `FeaturedNewsCard` + `NewsListItem` composable | `ui/components/NewsCard.kt` |
| ✅ | `ContinueListeningCard(episode, progress, onClick)` | `ui/components/ContinueListeningCard.kt` |
| ✅ | `PlayerController` + `PlaybackState` (Phase 3 contract) | `player/` |
| ✅ | Route ၈ ခု + callback တွေ NavHost မှာ ချိတ်ပြီးသား | `ui/navigation/` |
| ⬜ | `NewsViewModel`, `LibraryViewModel`, `SearchViewModel` | **အလွတ် ၁ ကြောင်း** |
| ⬜ | `LibraryRepository` | **အလွတ် ၁ ကြောင်း** |
| ⬜ | News / NewsDetail / Library / Search screen | `PlaceholderScreen` ပဲ |
| ⬜ | `HomeUiState.lastPlayed` | အမြဲ `null` |
| ⬜ | Detail ရဲ့ `"+ သိမ်း"` ခလုတ် | `onSave = { /* TODO */ }` |

> ⚠️ **Phase 3 ကျန်တာတွေ အရင် ပြီးအောင် လုပ်ပါ** (`phase3-checklist.md` section 6) —
> device test ၁၁ ခု နဲ့ review ၂ ခု။ `main` အစိမ်း မဖြစ်ဘဲ Phase 4 မစပါနဲ့။

---

## ⭐ Day 0 — Contract (၃ ယောက်လုံး၊ ၁ နာရီ)

### ဘာကြောင့် ထပ်လုပ်ရလဲ

Phase 3 မှာ contract က အလုပ်ဖြစ်ခဲ့တယ် — တစ်ပတ်လုံး **တစ်ခါမှ မပြောင်းရဘဲ**
၃ ယောက်လုံး ပြိုင်တူ လုပ်နိုင်ခဲ့တယ်။ Phase 4 မှာလည်း မျှသုံးတဲ့ အပိုင်း ရှိတယ် —
**Room database**။ A က subscribe အတွက်၊ C က history အတွက် — **table တစ်ခုတည်း**။

```
                ┌───────────────────────────────────┐
                │  CONTRACT (Day 0 — A က ရေး)       │
                │  LibraryEntity (schema v2)        │
                │  PlaybackProgress (domain model)  │
                │  LibraryRepository (interface)    │
                └────┬──────────────────────┬───────┘
      implements     │                      │   သုံး
   ┌─────────────────┘                      └──────────────────┐
   ▼                                                            ▼
A: RoomLibraryRepository                        C: HistoryRecorder + HomeViewModel
   + LibraryViewModel + LibraryScreen              + ContinueListeningCard
   + Detail "သိမ်း" ခလုတ်                          (test: FakeLibraryRepository)

🅱️ B က Room မလိုဘူး — Firestore ပဲ သုံးလို့ Day 0 ပြီးတာနဲ့ ချက်ချင်း စလို့ရတယ်
```

### Contract ဖိုင် ၁ — `domain/model/PlaybackProgress.kt` (အသစ်)

```kotlin
package com.example.myancast.domain.model

/**
 * Episode တစ်ခုကို ဘယ်အထိ နားထောင်ထားလဲ — Room မှာ သိမ်း၊ Home နဲ့ Library မှာ ပြ။
 * Firestore နဲ့ မဆိုင်လို့ `is` prefix သုံးလို့ရတယ်။
 */
data class PlaybackProgress(
    val episodeId: String,
    val podcastId: String,
    val positionMs: Long,
    val durationMs: Long,
    val updatedAt: Long          // System.currentTimeMillis()
) {
    val fraction: Float
        get() = if (durationMs > 0) (positionMs.toFloat() / durationMs).coerceIn(0f, 1f) else 0f

    /** ၉၅% ကျော် နားထောင်ပြီးရင် "ပြီးပြီ" — Continue Listening မှာ မပြတော့ဘူး */
    val isFinished: Boolean get() = fraction >= 0.95f
}
```

### Contract ဖိုင် ၂ — `data/local/LibraryEntity.kt` (schema v2)

```kotlin
@Entity(tableName = "library")
data class LibraryEntity(
    @PrimaryKey val id: String,        // sub → podcastId ၊ history → episodeId
    val type: String,                  // TYPE_SUB (သို့) TYPE_HISTORY
    val podcastId: String = "",        // history က ဘယ် podcast လဲ (sub ဆို ကိုယ့်ID)
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val updatedAt: Long = 0L
) {
    companion object {
        const val TYPE_SUB = "sub"
        const val TYPE_HISTORY = "history"
    }
}
```

> **⚠️ Room schema ပြောင်းရင် app က crash ဖြစ်မယ်** —
> `AppDatabase` မှာ `version = 2` တင်ပြီး၊ course project မို့ migration မရေးဘဲ
> `.fallbackToDestructiveMigration()` ထည့်ပါ (အဟောင်း data ပျက်မယ် — ခု ဘာမှ မရှိသေးလို့ အဆင်ပြေ)။
> ထည့်မရင် `IllegalStateException: Room cannot verify the data integrity` တက်မယ်။

### Contract ဖိုင် ၃ — `data/repository/LibraryRepository.kt` (interface)

```kotlin
interface LibraryRepository {
    // ─── Subscribe (A) ───
    fun subscribedIds(): Flow<Set<String>>
    suspend fun subscribe(podcastId: String)
    suspend fun unsubscribe(podcastId: String)

    // ─── History (C) ───
    fun history(): Flow<List<PlaybackProgress>>       // နောက်ဆုံး ထိတာ အရင်
    fun lastPlayed(): Flow<PlaybackProgress?>         // Home ရဲ့ Continue Listening
    suspend fun saveProgress(progress: PlaybackProgress)
}
```

| ဆုံးဖြတ်ချက် | ဘာကြောင့် |
|---|---|
| `Flow<Set<String>>` (List မဟုတ်) | `id in subscribedIds` စစ်ရတာ O(1) |
| History က `PlaybackProgress` ပြန် (Entity မဟုတ်) | Room type တွေ UI ဆီ မရောက်ရ — `PodcastRepository` နဲ့ အတူတူ |
| `saveProgress` က upsert | Episode တစ်ခုကို row တစ်ခုပဲ ထား |
| Interface + `RoomLibraryRepository` + `FakeLibraryRepository` | Phase 2/3 နဲ့ တူတူ — test လုပ်လို့ရအောင် |

### Contract ဖိုင် ၄ — `MyanCastApp` မှာ ၂ ကြောင်း

```kotlin
val libraryRepository: LibraryRepository by lazy { RoomLibraryRepository(database.libraryDao()) }
// C က Day 2 မှာ ထည့်မယ် — playback ကို နားထောင်ပြီး history ရေးသူ
// val historyRecorder by lazy { HistoryRecorder(playerController, libraryRepository) }
```

### Day 0 မှာ ဆုံးဖြတ်ရမယ့် အချက် ၄ ခု

| မေးခွန်း | အကြံပြု အဖြေ |
|---|---|
| History ကို ဘယ်နှစ်ခု သိမ်းမလဲ | နောက်ဆုံး ၂၀ — Library ရဲ့ History tab မှာ ပြ |
| ဘယ်အချိန် သိမ်းမလဲ | ၁၀ စက္ကန့် တစ်ခါ + pause/episode ပြောင်းချိန် (write များရင် battery ကုန်) |
| `isFinished` (၉၅%) ဖြစ်ရင် Continue Listening မှာ ပြမလား | **မပြဘူး** — ပြီးသွားတာကို ဆက်နားထောင်ခိုင်းလို့ မဖြစ် |
| Search က ဘာကို ရှာမလဲ | Podcast title + category (Episode/News နောက်မှ) |

### ✅ Day 0 Deliverable
- [ ] ၃ ယောက်လုံး contract ၄ ခု သဘောတူ (ပြောင်းစရာ ရှိရင် **ဒီနေ့ပဲ** ပြောင်း)
- [ ] 🅰️ **A** က `feature/phase4-contract` မှာ commit → `main` ကို merge
- [ ] `.\gradlew assembleDebug` + app run ပြီး Room crash မဖြစ်ကြောင်း အတည်ပြု
- [ ] ၃ ယောက်လုံး အဲဒီ `main` ကနေ branch ဆောက်

> **Day 0 ပြီးရင် contract ပြောင်းဖို့ ၃ ယောက်လုံး သဘောတူရမယ်။**
> Field အသစ် (default value နဲ့) ထည့်တာ အန္တရာယ် မရှိ — နာမည်ပြောင်း/ဖျက်တာက ကျန် ၂ ယောက် ပျက်မယ်။

---

## ⭐ Slice ၃ ခု

#### 🅰️ Slice A — Library + Subscribe

| Layer | အလုပ် | ဖိုင် |
|---|---|---|
| **Data** | Contract ၄ ခု (Day 0) + `LibraryDao` အသစ် (sub + history query) + `RoomLibraryRepository` | `data/local/`, `data/repository/` |
| **Model** | `LibraryViewModel` + `LibraryUiState` (subscribed podcast list + history list + tab) | `ui/library/` |
| **UI** | `LibraryScreen` — tab ၂ ခု (သိမ်းထားသော / မှတ်တမ်း) + Detail ရဲ့ `"+ သိမ်း"` ခလုတ် အလုပ်လုပ်အောင် | `ui/library/`, `ui/details/` |

#### 🅱️ Slice B — News

| Layer | အလုပ် | ဖိုင် |
|---|---|---|
| **Data** | Console မှာ news ၁၀ ခု စစ် (`imageUrl`, `audioUrl`, `category`, `publishedAt`) + `NewsRepository` ကို interface ခွဲ | Firebase Console, `data/repository/NewsRepository.kt` |
| **Model** | `NewsViewModel` + `NewsUiState` (list + category filter) · `NewsDetailViewModel` | `ui/news/` |
| **UI** | `NewsScreen` (featured ၁ + list) · `NewsDetailScreen` (headline, body, audio ရှိရင် ဖွင့်ခလုတ်) | `ui/news/` |

#### 🅲 Slice C — Search + Continue Listening

| Layer | အလုပ် | ဖိုင် |
|---|---|---|
| **Data** | `HistoryRecorder` — `PlayerController.state` ကို နားထောင်ပြီး `saveProgress()` (၁၀ စက္ကန့် တစ်ခါ) | `player/HistoryRecorder.kt` (အသစ်) |
| **Model** | `SearchViewModel` + `SearchUiState` (query, results, empty/idle) + `searchPodcasts()` pure function · `HomeViewModel` မှာ `lastPlayed` ချိတ် | `ui/search/`, `ui/home/` |
| **UI** | `SearchScreen` (search field + result list + empty state) · Home ရဲ့ `ContinueListeningCard` ချိတ် (+ font ပြင်) | `ui/search/`, `ui/home/`, `ui/components/` |

### ဖိုင် ပိုင်ဆိုင်မှု

| ဖိုင် | ပိုင်ရှင် | မှတ်ချက် |
|---|---|---|
| `domain/model/PlaybackProgress.kt`, `data/local/*`, `data/repository/LibraryRepository.kt` | **Contract** | Day 0 ပြီးရင် ၃ ယောက် သဘောတူမှ ပြောင်း |
| `data/repository/RoomLibraryRepository.kt`, `ui/library/*` | **A** | |
| `ui/details/PodcastDetailScreen.kt` + `ViewModel` | **A** (Phase 4 မှာ) | `"သိမ်း"` ခလုတ် အတွက် — B ကို ပြော |
| `data/repository/NewsRepository.kt`, `ui/news/*` | **B** | |
| `ui/components/NewsCard.kt` | **B** | Poppins bug (line 68) ပါ ပြင် |
| `player/HistoryRecorder.kt`, `ui/search/*` | **C** | |
| `ui/home/HomeViewModel.kt` + `HomeScreen.kt` | **C** (Phase 4 မှာ) | `lastPlayed` အတွက် — A ကို ပြော (Phase 2 က A ပိုင်) |
| `ui/components/ContinueListeningCard.kt` | **C** | Poppins bug (line 57-59) ပါ ပြင် |
| `MyanCastApp.kt` | **A** (Day 0) → **C** (Day 2 မှာ recorder ၁ ကြောင်း) | |
| `ui/navigation/MyanCastNavHost.kt` | **B** | Search → Detail route ထပ်ချိတ်ဖို့ လိုရင် |

---

# Day 1 — 🗄️ DATA layer

> **ရည်မှန်းချက်** — ၃ ယောက်လုံး ကိုယ့် data source ကနေ data ထွက်လာကြောင်း အတည်ပြု။

### 🔰 ၃ ယောက်လုံး အတူ (မနက် ၄၅ မိနစ်) — Firestore `news` collection စစ်

| စစ်ရမယ့် အချက် | ဘာဖြစ်ရမလဲ |
|---|---|
| Document အရေအတွက် | ၁၀ ခု |
| `headline`, `body`, `category` | မြန်မာလို၊ အလွတ် မရှိ |
| `imageUrl` | `https://` နဲ့ စ (Phase 1 မှာ မထည့်မိရင် ခု ထည့်) |
| `audioUrl` | တချို့မှာပဲ ရှိလည်း ရ (optional) — ရှိရင် `.mp3` |
| `publishedAt` | `timestamp` type (`string` မဟုတ်) |

> **Phase 2 ရဲ့ သင်ခန်းစာ ပြန်သတိရပါ** — `orderBy("publishedAt")` သုံးထားလို့
> field မပါတဲ့ document တွေ **တိတ်တဆိတ် ပျောက်**မယ်။ News မပေါ်ရင် အရင် ဒါကို စစ်ပါ။

### 🅰️ A — Room v2
1. Contract ၄ ခု commit (Day 0 က မပြီးရင် ဒီနေ့ မနက် ပြီးအောင်)
2. `LibraryDao` ဖြည့်:
   ```kotlin
   @Query("SELECT * FROM library WHERE type = :type ORDER BY updatedAt DESC")
   fun byType(type: String): Flow<List<LibraryEntity>>

   @Query("SELECT * FROM library WHERE type = 'history' ORDER BY updatedAt DESC LIMIT 1")
   fun lastPlayed(): Flow<LibraryEntity?>

   @Upsert suspend fun upsert(entity: LibraryEntity)
   @Query("DELETE FROM library WHERE id = :id AND type = :type") suspend fun delete(id: String, type: String)
   ```
3. `RoomLibraryRepository` — Entity ↔ `PlaybackProgress` map
4. **Day 1 ညနေ push** ⏰ — C စောင့်နေတယ်

### 🅱️ B — News data
- `NewsRepository` ကို `interface NewsRepository` + `FirestoreNewsRepository` ခွဲ (Phase 2 ရဲ့ `PodcastRepository` ပုံစံ)
- `FakeNewsRepository` (test) ရေး
- ယာယီ log နဲ့ `news = 10` ထွက်မထွက် စစ်ပြီး **log ပြန်ဖျက်**

### 🅲 C — Search နည်းလမ်း ဆုံးဖြတ် + HistoryRecorder ပုံစံ

**⚠️ Firestore က "contains" ရှာလို့ မရဘူး** — `whereGreaterThanOrEqualTo` နဲ့ prefix ပဲ ရတယ်၊
မြန်မာစာ + case နဲ့ ဆိုရင် ပိုရှုပ်တယ်။

| နည်းလမ်း | အားသာချက် | အားနည်းချက် |
|---|---|---|
| ⭐ **Client-side filter** — `getPodcasts()` (၅ ခုပဲ) ကို memory ထဲမှာ `contains` | ရိုးရှင်း၊ အလယ်စာလုံးလည်း ရှာလို့ရ | Data များရင် မတော်ဘူး (၅ ခုအတွက် ပြဿနာ မရှိ) |
| Firestore prefix query | Server-side | Prefix ပဲ ရ၊ index လို |

**→ နည်းလမ်း ၁ ရွေးပါ။** Pure function တစ်ခုပဲ — test လွယ်တယ် (Phase 2 ရဲ့ `CategoryFilter` နဲ့ အတူတူ):
```kotlin
fun searchPodcasts(podcasts: List<Podcast>, query: String): List<Podcast>
// query blank → emptyList (idle state)
// title (သို့) category မှာ contains (ignoreCase) → ပါ
// query ကို trim လုပ် — user က space ပါ ရိုက်တတ်တယ်
```

### ✅ Day 1 Deliverable
| ဘယ်သူ | ပြီးရမယ့် အရာ |
|---|---|
| ၃ ယောက် | Firestore `news` ၁၀ ခု field မှန်ကြောင်း စစ်ပြီး |
| A | Room v2 + DAO + `RoomLibraryRepository` **push ပြီး** ⏰ |
| B | News repository interface + fake |
| C | `searchPodcasts()` + test (emulator မလို) |

---

# Day 2 — 🧠 MODEL layer

> **ရည်မှန်းချက်** — ViewModel ၃ ခု + history ရေးတဲ့ logic။

### 🔰 စည်းကမ်း (Phase 2/3 နဲ့ အတူတူ)
1. ViewModel ထဲ `android.*` / Room type / Firestore type မရှိရ
2. State က `data class` တစ်ခုတည်း (`isLoading` + `error` ပါ)
3. Logic က pure function (သို့) ViewModel — composable ထဲ မရေးရ

### 🅰️ A — `LibraryViewModel`
```kotlin
data class LibraryUiState(
    val tab: LibraryTab = LibraryTab.SUBSCRIBED,     // enum: SUBSCRIBED / HISTORY
    val subscribed: List<Podcast> = emptyList(),     // ID → Podcast ပြန်ရှာပြီးသား
    val history: List<HistoryRow> = emptyList(),     // episode + progress
    val isLoading: Boolean = true,
    val error: String? = null
)
```
- `combine(podcastRepo.getPodcasts(), libraryRepo.subscribedIds())` → subscribed list
- History row အတွက် episode အချက်အလက် လိုတယ် — `PlaybackProgress` မှာ `episodeId` ပဲ ရှိလို့
  **ဆုံးဖြတ်ရန်**: (က) Firestore ကနေ episode ပြန်ဆွဲ (ခ) Room မှာ title/cover ပါ သိမ်း
  → ⭐ (ခ) က ရိုးရှင်းတယ် (offline လည်း ရ) ဒါပေမဲ့ contract ပြောင်းရမယ် → **Day 0 မှာ ဆုံးဖြတ်ပါ**
- Detail ရဲ့ subscribe: `PodcastDetailViewModel` မှာ `isSubscribed: StateFlow<Boolean>` + `toggleSubscribe()`

### 🅱️ B — `NewsViewModel`
- `NewsUiState(news, categories, selectedCategory, isLoading, error)`
- Category filter — Phase 2 ရဲ့ `CategoryFilter.kt` ပုံစံ ပြန်သုံး (pure function အသစ် `filterNews()`)
- `loadJob?.cancel()` မမေ့နဲ့ (Phase 2 ရဲ့ bug)
- `NewsDetailScreen` အတွက် — list ထဲက item ကို ID နဲ့ ရှာ (သို့) `getNews(id)` ထပ်ရေး

### 🅲 C — `SearchViewModel` + `HistoryRecorder`

**SearchViewModel:**
```kotlin
data class SearchUiState(
    val query: String = "",
    val results: List<Podcast> = emptyList(),
    val isIdle: Boolean = true,        // query အလွတ် — "ရှာလိုသည်ကို ရိုက်ပါ"
    val isLoading: Boolean = true,
    val error: String? = null
)
```
- Podcast list ကို တစ်ခါ collect ထားပြီး query ပြောင်းတိုင်း `searchPodcasts()` ပြန်ခေါ်

**HistoryRecorder** (ဒါက Phase 4 ရဲ့ အခက်ဆုံး အပိုင်း):
```kotlin
class HistoryRecorder(
    private val player: PlayerController,
    private val library: LibraryRepository,
    private val scope: CoroutineScope
) {
    fun start() { /* player.state ကို collect */ }
}
```
- `MyanCastApp.onCreate()` မှာ `start()` ခေါ် (app တစ်သက်လုံး)
- **၁၀ စက္ကန့် တစ်ခါပဲ** ရေး — `positionMs / 10_000` ပြောင်းမှ သိမ်း (သို့) `sample(10_000)`
- Pause ဖြစ်ချိန် / episode ပြောင်းချိန် ချက်ချင်း သိမ်း
- `durationMs == 0` (မသိသေး) ဆိုရင် မသိမ်းနဲ့ — fraction မှားမယ်
- `isFinished` ဆိုရင်လည်း သိမ်း (Library history မှာ ပြဖို့) ဒါပေမဲ့ Home မှာ မပြ

### ✅ Day 2 Deliverable
| ဘယ်သူ | ပြီးရမယ့် အရာ |
|---|---|
| A | `LibraryViewModel` + Detail subscribe logic + test ၂ |
| B | `NewsViewModel` (+ detail) + test ၂ |
| C | `SearchViewModel` + `HistoryRecorder` + test ၃ |

---

# Day 3 — 🎨 UI layer

### 🔰 စည်းကမ်း
- `collectAsStateWithLifecycle()` · `Modifier` က ပထမ optional param · `!!` မရှိ
- **မြန်မာစာမှာ `label*` (Poppins) မသုံးရ** — ဒီ phase မှာ ပြင်ရမယ့် ၂ နေရာ ရှိ
- Loading → Error → Empty → Content အစီအစဉ်
- `@Preview` က private stateless content ပေါ်မှာပဲ

### 🅰️ A — `LibraryScreen`
```
┌──────────────────────────────┐
│  စာကြည့်တိုက်                 │  TopAppBar
│  [သိမ်းထားသော] [မှတ်တမ်း]     │  TabRow ၂ ခု
├──────────────────────────────┤
│  PodcastListItem             │  ← Phase 2 component ပြန်သုံး
│  PodcastListItem             │
└──────────────────────────────┘
```
- Empty ၂ မျိုး — `"သိမ်းထားတာ မရှိသေးပါ"` / `"နားထောင်ထားတာ မရှိသေးပါ"`
- History tab မှာ progress bar (`LinearProgressIndicator(progress.fraction)`)
- Detail ရဲ့ ခလုတ်: သိမ်းထားရင် `"✓ သိမ်းပြီး"` (OutlinedButton) ၊ မသိမ်းရင် `"+ သိမ်းမည်"`

### 🅱️ B — `NewsScreen` + `NewsDetailScreen`
```
News:                          NewsDetail:
┌────────────────────┐         ┌────────────────────┐
│ [FeaturedNewsCard] │         │ ←                  │
│  CategoryChips     │         │  [ပုံ 16:9]        │
│  NewsListItem      │         │  Headline (display)│
│  NewsListItem      │         │  category · ၂ ရက်  │
└────────────────────┘         │  [▶ နားထောင်မည်]   │ ← audioUrl ရှိမှ
                               │  Body ...          │
                               └────────────────────┘
```
- `NewsCard.kt:68` — `labelSmall` → `bodySmall` (မြန်မာ category)
- Audio ရှိရင် `PlayerController.play()` နဲ့ ဖွင့် — **News ကို episode အဖြစ် ပြောင်းရမယ်**
  (`Episode(id = news.id, title = news.headline, audioUrl = news.audioUrl!!, coverUrl = news.imageUrl)`)
- Audio မရှိရင် ခလုတ် **လုံးဝ မပြနဲ့** (Phase 2 ရဲ့ "See all" သင်ခန်းစာ)

### 🅲 C — `SearchScreen` + Continue Listening
```
┌──────────────────────────────┐
│  [🔍 ရှာဖွေရန်...        ✕ ] │  TextField (Myanmar font!)
├──────────────────────────────┤
│  idle    → "ရှာလိုသည်ကို ရိုက်ပါ"
│  ရလဒ်မရှိ → "ရှာမတွေ့ပါ"
│  ရလဒ်ရှိ  → PodcastListItem × n
└──────────────────────────────┘
```
- `TextField` မှာ `textStyle = MaterialTheme.typography.bodyLarge` (Myanmar family)
- `singleLine = true` + `ImeAction.Search` + ✕ ခလုတ်နဲ့ query ရှင်း
- Home: `state.lastPlayed?.let { ContinueListeningCard(...) }` ကို တကယ့် data နဲ့ ချိတ် —
  နှိပ်ရင် **အဲဒီ episode ကို ရပ်ထားတဲ့ နေရာကနေ ဆက်ဖွင့်** (`play()` ပြီး `seekTo(positionMs)`)
- `ContinueListeningCard.kt:57-59` — `"Continue Listening"` → `"ဆက်နားထောင်ရန်"` + `bodySmall`

### ✅ Day 3 Deliverable
| ဘယ်သူ | ပြီးရမယ့် အရာ |
|---|---|
| A | Library tab ၂ ခု + Detail သိမ်းခလုတ် အလုပ်လုပ် + `@Preview` |
| B | News list + detail (+ audio) + `@Preview` |
| C | Search အလုပ်လုပ် + Home မှာ Continue Listening ပေါ် + `@Preview` |

---

# Day 4 — 🔗 INTEGRATION (A ဦးဆောင်)

### Step 1: Merge အစီအစဉ် **A → C → B**
```bash
git checkout main && git pull origin main
git merge feature/phase4-slice-a  && ./gradlew assembleDebug   # Room contract ပိုင်ရှင်
git merge feature/phase4-slice-c  && ./gradlew assembleDebug   # A ရဲ့ repo ကို သုံး
git merge feature/phase4-slice-b  && ./gradlew assembleDebug   # လွတ်လပ် (News)
./gradlew testDebugUnitTest
```

### Step 2: အစအဆုံး လမ်းကြောင်း (၃ ယောက်လုံး ရှင်းပြနိုင်ရမယ်)
```
Episode ဖွင့်                                   (Phase 3)
      ↓ PlayerController.state (500ms)
HistoryRecorder — ၁၀ စက္ကန့် တစ်ခါ               (C)
      ↓ saveProgress(PlaybackProgress)
LibraryRepository → Room "library" table       (A — contract)
      ↓ lastPlayed() / history()
      ├─→ HomeViewModel.lastPlayed → ContinueListeningCard   (C)
      └─→ LibraryViewModel.history → Library History tab     (A)
              ↓ နှိပ်
      play(queue) + seekTo(positionMs) → ရပ်ထားတဲ့ နေရာက ဆက်  (C)
```

### Step 3: Edge case ၁၅ ခု

| # | စမ်းချက် | မျှော်လင့်ရမယ့် ရလဒ် | ပိုင်ရှင် |
|---|---|---|---|
| 1 | Podcast သိမ်း → Library သွား | ချက်ချင်း ပေါ် | A |
| 2 | Unsubscribe → Library | ချက်ချင်း ပျောက် | A |
| 3 | App ပိတ်ပြီး ပြန်ဖွင့် | သိမ်းထားတာ ကျန်နေ (Room) | A |
| 4 | Library အလွတ် | Empty ၂ မျိုး မှန်ကန်စွာ ပြ | A |
| 5 | News list | ၁၀ ခု ပေါ်၊ ပုံ ပါ | B |
| 6 | News category chip | Filter မှန် | B |
| 7 | News detail — audio ရှိ | ခလုတ် ပေါ်၊ နှိပ်ရင် ဖွင့်၊ mini player ပေါ် | B |
| 8 | News detail — audio မရှိ | ခလုတ် **မပေါ်ရ** | B |
| 9 | Search `"နည်း"` ရိုက် | နည်းပညာ podcast ပေါ် | C |
| 10 | Search ရလဒ် မရှိ | `"ရှာမတွေ့ပါ"` | C |
| 11 | Search ရှင်း (✕) | Idle state ပြန်ရောက် | C |
| 12 | Episode ၃၀ စက္ကန့် ဖွင့် → Home | Continue Listening ပေါ်၊ progress မှန် | C |
| 13 | Continue Listening နှိပ် | **ရပ်ထားတဲ့ နေရာက** ဆက်ဖွင့် | C |
| 14 | Episode ပြီးအောင် နားထောင် | Continue Listening မှာ **မပေါ်တော့** (`isFinished`) | C |
| 15 | Rotate — screen ၄ ခုလုံး | State မပျောက် | ၃ ယောက် |

### ✅ Day 4 Deliverable
- [ ] Slice ၃ ခု merge ပြီး build + test pass
- [ ] Bottom nav ၅ ခုလုံး တကယ့် screen ပြ (placeholder မကျန်)
- [ ] Edge case ၁၅ ခု စစ်ပြီး
- [ ] ၃ ယောက်လုံး data flow ရှင်းပြနိုင်

---

# Day 5 — 🔄 TEST + CROSS-REVIEW

### Cross-review လှည့်ပုံ (ထပ်လှည့် — အသစ် ဖတ်ရအောင်)
```
C → A   ·   A → B   ·   B → C
```

**Review checklist (၃ ယောက်လုံး):**
- [ ] `!!` မရှိ · ViewModel ထဲ `android.*` / Room / Firestore type မရှိ
- [ ] `loadJob?.cancel()` ပါ (Flow collect လုပ်တဲ့ ViewModel တိုင်း)
- [ ] မြန်မာစာမှာ `label*` style မရှိ
- [ ] Empty / Error / Loading ၃ မျိုး ပါ
- [ ] Room write က coroutine ထဲမှာ (main thread မှာ မဟုတ်)
- [ ] Contract ဖိုင် ၄ ခု Day 0 ကတည်းက မပြောင်းရ (ပြောင်းရင် ၃ ယောက် သဘောတူပြီးလား)

### Test ပစ်မှတ် (unit test စုစုပေါင်း ၆၅+ ဖြစ်သင့်)

| ဘယ်သူ | Test file | ဘာစစ် |
|---|---|---|
| A | `RoomLibraryRepositoryTest` (သို့ `LibraryViewModelTest`) | subscribe/unsubscribe flow၊ history mapping |
| A | `LibraryViewModelTest` | tab ပြောင်း၊ empty state |
| B | `NewsViewModelTest` | load ၁၀၊ category filter၊ error |
| B | `NewsDetailViewModelTest` | ID မတွေ့ရင် error |
| C | `SearchPodcastsTest` (pure) | query အလွတ်၊ case၊ မတွေ့၊ category match |
| C | `SearchViewModelTest` | query ပြောင်းရင် result ပြောင်း |
| C | `HistoryRecorderTest` | ၁၀ စက္ကန့် တစ်ခါပဲ ရေး၊ `durationMs == 0` ဆို မရေး |

```bash
./gradlew testDebugUnitTest
```

---

## Phase 4 — Final Checklist

| # | အချက် | ပိုင်ရှင် | ☐ |
|---|---|---|---|
| 1 | Day 0 contract ၄ ခု merge ပြီး | A | ☐ |
| 2 | Room v2 — app crash မဖြစ် | A | ☐ |
| 3 | Subscribe/unsubscribe + app ပိတ်ပြီး ကျန် | A | ☐ |
| 4 | Library tab ၂ ခု + empty ၂ မျိုး | A | ☐ |
| 5 | News ၁၀ ခု + ပုံ + category filter | B | ☐ |
| 6 | News detail + audio (ရှိမှ ခလုတ်ပြ) | B | ☐ |
| 7 | `NewsCard` Poppins bug ပြင်ပြီး | B | ☐ |
| 8 | Search — idle / ရလဒ် / မတွေ့ ၃ မျိုး | C | ☐ |
| 9 | History ၁၀ စက္ကန့် တစ်ခါ သိမ်း | C | ☐ |
| 10 | Continue Listening — ရပ်ထားတဲ့နေရာက ဆက်ဖွင့် | C | ☐ |
| 11 | `ContinueListeningCard` ဘာသာပြန် + font ပြင် | C | ☐ |
| 12 | Placeholder screen မကျန်တော့ | ၃ ယောက် | ☐ |
| 13 | Edge case ၁၅ ခု | ၃ ယောက် | ☐ |
| 14 | Unit test ၆၅+ pass · warning ၀ | ၃ ယောက် | ☐ |
| 15 | Cross-review C→A, A→B, B→C | ၃ ယောက် | ☐ |

---

## Phase 4 — Troubleshooting

| ပြဿနာ | Layer | ဖြေရှင်းနည်း |
|---|---|---|
| `Room cannot verify the data integrity` | Data | Schema ပြောင်းပြီး version မတင်ရသေး → `version = 2` + `fallbackToDestructiveMigration()` |
| `@Upsert` unresolved | Data | Room 2.6+ မှာပဲ ရှိ — catalog မှာ `room = "2.6.1"` ဟုတ်လား |
| Library အလွတ် (သိမ်းထားပေမဲ့) | Data | `type` string မတူ — `TYPE_SUB` constant သုံး၊ လက်နဲ့ မရိုက်နဲ့ |
| News မပေါ် | Data | `orderBy("publishedAt")` → field မပါတဲ့ doc ပျောက် (Phase 2 bug နဲ့ အတူတူ) |
| News ပုံ မပေါ် | Data/UI | `imageUrl` ရှိလား · `https://` လား |
| Continue Listening မပေါ် | Model | `durationMs == 0` ဖြစ်နေစဉ် သိမ်းမိလား · `isFinished` ဖြစ်နေလား |
| History row ထပ်နေ | Data | `@Upsert` သုံးလား၊ `@Insert` သုံးမိလား — key က `episodeId` |
| Battery/write များ | Model | Recorder က state တိုင်း ရေးနေတယ် → ၁၀ စက္ကန့် throttle ထည့် |
| Search မြန်မာစာ မတွေ့ | Model | `ignoreCase` + `trim()` · Console မှာ space ပါနေလား (Phase 2 cleanup) |
| Search field မှာ စာလုံး ပုံပျက် | UI | `TextField` ရဲ့ `textStyle` မသတ်မှတ်ရင် default (Poppins) ဖြစ်တတ် |
| Detail မှာ သိမ်းခလုတ် မပြောင်း | Model | `isSubscribed` က Flow ကနေ လာလား၊ တစ်ခါပဲ ဖတ်မိလား |

---

## Phase 4 — Git Workflow

```
main
  ↑  (Day 0) feature/phase4-contract   ← A: PlaybackProgress + LibraryEntity v2 + LibraryRepository + Fake
  ↑
  ├── feature/phase4-slice-a   (A: Library + Subscribe)
  ├── feature/phase4-slice-b   (B: News)
  └── feature/phase4-slice-c   (C: Search + Continue Listening)
```

**စည်းကမ်း ၄ ခု:**
1. Build မအောင်ရင် push မလုပ်နဲ့
2. သူများဖိုင် ထိရင် အရင်ပြော — contract ဆို ၃ ယောက်လုံး သဘောတူမှ
3. နေ့စဉ် အနည်းဆုံး ၁ ခါ push
4. `git add app/src` ပဲ — `git add .` မလုပ်နဲ့ (`.idea/`, `.kotlin/` ပါသွားမယ်)

---

## Phase 4 ပြီးရင် — Phase 5 (Week 5: Polish + Presentation)

`fullscope.md` Week 5 အတိုင်း:
- Bug fix · error handling · empty state အားလုံး ပြန်စစ်
- Unit test ဖြည့် · performance (startup, scroll)
- **ဆိုင်းငံ့ထားတာတွေ ဆုံးဖြတ်** — Zawgyi converter၊ `MainActivity` ရဲ့ comment-out code ဖျက်၊
  Firestore `duration` data ပြင် (`phase3-checklist.md` section 5)
- README ရေး · presentation slide · demo script · နောက်ဆုံး ပြန်လေ့ကျင့်

> **Demo အတွက် အကြံပြုချက်** — Phase 4 ပြီးရင် app က feature အပြည့် ဖြစ်သွားပြီ။
> Demo လမ်းကြောင်းကို ခုကတည်းက ရွေးထားပါ: Home → podcast ဖွင့် → mini player →
> Full player (speed/seek) → back → News → Search → Library → Continue Listening။
