# 📋 Phase 2 — Complete To-Do List (All Members)

> **Vertical Slice** — Member တစ်ယောက်စီ DB → Model → UI အားလုံး လုပ်
> **ရည်ရွယ်ချက်** — Firebase data ကို Home + Detail မှာ မြင်ရအောင်

---

## 📌 Phase 2 — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **ကြာချိန်** | ၅ ရက် (Week 2) |
| **Slice ၃ ခု** | A=Podcast list, B=Episode detail, C=Category+format |
| **လိုတဲ့ အလုပ်** | ၃ ယောက်လုံး DB → Model → UI လုပ် |
| **ရလဒ်** | Home → Detail → Back လမ်းကြောင်း အလုပ်လုပ် |

---

# 🅰️ Member A — Podcast စာရင်း

> **Slice A** — `podcasts` collection → Home screen

## 📅 Day 1 — 🗄️ Database Layer

### ⏰ မနက် (၁ နာရီ) — ၃ ယောက်လုံး အတူ
- [ ] Firebase Console ဖွင့် — `podcasts` collection စစ်
- [ ] Field name စစ် — `id`, `title`, `description`, `coverUrl`, `category`, `episodeCount`
- [ ] Data အရေအတွက် — ၅ ခု ရှိလား
- [ ] Category field — မြန်မာလို တစ်ပုံစံတည်း ဖြစ်လား
- [ ] Security rules — `allow read: if true;` ဖြစ်လား

### ⏰ နေ့လယ် (၂ နာရီ) — A ကိုယ်တိုင်
- [ ] `PodcastRepository.kt` ဖွင့် — `getPodcasts()` ရှိလား
- [ ] **Log ထည့်** (ယာယီ):
  ```kotlin
  android.util.Log.d("REPO", "podcasts=${list.size}")
  ```
- [ ] App run — Logcat မှာ `podcasts=5` ထွက်လား
- [ ] **မထွက်ရင် စစ်**:
  - `0` ထွက် → collection name (သို့) rules
  - `PERMISSION_DENIED` → Security rules
  - Crash → `google-services.json`
- [ ] **Log ဖျက်**
- [ ] **Build စစ်** — `./gradlew assembleDebug`
- [ ] **Push** — `git commit -m "feat(db): verify getPodcasts returns 5 items"`
- [ ] **အဖွဲ့ကို ပြော** — "A push ပြီးပြီ၊ B pull ဆွဲလို့ရပြီ"

### ✅ Day 1 Deliverable
- [ ] `getPodcasts()` ၅ ခု ပြန်ပေး
- [ ] Log ဖျက် + Push

---

## 📅 Day 2 — 🧠 Model Layer

### ⏰ မနက် (၃ နာရီ) — `HomeViewModel`

#### ၁။ State အသစ် (၃၀ မိနစ်)
- [ ] `HomeUiState` update:
  ```kotlin
  data class HomeUiState(
      val allPodcasts: List<Podcast> = emptyList(),
      val podcasts: List<Podcast> = emptyList(),
      val categories: List<String> = listOf("အားလုံး"),
      val selectedCategory: String = "အားလုံး",
      val lastPlayed: Episode? = null,
      val isLoading: Boolean = true,
      val error: String? = null
  )
  ```

#### ၂။ Job ထိန်းချုပ်မှု (၃၀ မိနစ်)
- [ ] `private var loadJob: Job? = null`
- [ ] `loadPodcasts()` အစမှာ `loadJob?.cancel()`
- [ ] `loadJob = viewModelScope.launch { ... }`

#### ၃။ Refresh leak ဖြေရှင်း (၁၅ မိနစ်)
- [ ] `refresh()` — `loadPodcasts()` က `loadJob?.cancel()` ပါ

#### ၄။ ViewModel Factory (၄၅ မိနစ်)
- [ ] Constructor ပြင်:
  ```kotlin
  class HomeViewModel(private val repo: PodcastRepository) : ViewModel() {
      companion object {
          fun factory(repo: PodcastRepository = PodcastRepository()) = viewModelFactory {
              initializer { HomeViewModel(repo) }
          }
      }
  }
  ```
- [ ] Import ထည့် — `viewModelFactory`, `initializer`
- [ ] `HomeScreen.kt` မှာ `viewModel(factory = HomeViewModel.factory())`

#### ၅။ Category filter ချိတ် (၁ နာရီ) — C နဲ့ အတူ
- [ ] `selectCategory()` ပြင်:
  ```kotlin
  fun selectCategory(category: String) {
      _state.update {
          it.copy(
              selectedCategory = category,
              podcasts = filterByCategory(it.allPodcasts, category)
          )
      }
  }
  ```
- [ ] `loadPodcasts()` မှာ `allPodcasts` + `categories` ထည့်
- [ ] C ရဲ့ `buildCategories()` + `filterByCategory()` import

### ⏰ နေ့လယ် (၁ နာရီ) — Test
- [ ] `HomeViewModelTest.kt` ဖန်တီး
- [ ] Test ၁ — Loading state
- [ ] Test ၂ — Data ဝင်ပြီး ၅ ခု

### ✅ Day 2 Deliverable
- [ ] Job fix + Factory + state အသစ်
- [ ] `selectCategory()` filter
- [ ] Test ၂ ခု
- [ ] Push

---

## 📅 Day 3 — 🎨 UI Layer

### ⏰ မနက် (၃ နာရီ) — `HomeScreen`

#### ၁။ Padding ထည့် (၃၀ မိနစ်)
- [ ] `LoadingView(Modifier.padding(padding))`
- [ ] `ErrorView(..., modifier = Modifier.padding(padding))`
- [ ] `EmptyView(..., modifier = Modifier.padding(padding))`
- [ ] `HomeContent(..., modifier = Modifier.padding(padding))`

#### ၂။ `!!` ဖျက် (၁၅ မိနစ်)
- [ ] `state.error!!` → `state.error ?: ""` (သို့) `?.let { }`

#### ၃။ Empty state ထည့် (၁၅ မိနစ်)
- [ ] `when` block မှာ `state.podcasts.isEmpty()` case
- [ ] `EmptyView.kt` မှာ `modifier: Modifier = Modifier` ရှိလား

#### ၄။ `SectionHeader` onSeeAll (၃၀ မိနစ်)
- [ ] Option A — ဖျောက်
- [ ] Option B — Search ပို့

#### ၅။ `@Preview` (၁ နာရီ)
- [ ] `HomeContent` preview — fake data
- [ ] `HomeContentLoading` preview
- [ ] `MyanCastTheme { }` နဲ့ ပတ်
- [ ] `backgroundColor = 0xFF0E0D0B`

#### ၆။ `contentPadding` စစ် (၁၅ မိနစ်)
- [ ] `bottom = 96.dp` ရှိလား
- [ ] MiniPlayer နေရာ မဖျက်

### ⏰ နေ့လယ် (၂ နာရီ) — စမ်း
- [ ] App run — podcast ၅ ခု ပေါ်
- [ ] Loading state စမ်း
- [ ] Error state — internet ဖြုတ်
- [ ] Empty state — data ဖျက်
- [ ] Refresh — retry နှိပ်
- [ ] Preview — Android Studio

### ✅ Day 3 Deliverable
- [ ] podcast ၅ ခု ပေါ်
- [ ] State ၃ မျိုး မှန်
- [ ] `@Preview` ၂ ခု
- [ ] Push

---

## 📅 Day 4 — 🔗 Integration

### ⏰ မနက် (၁ နာရီ) — Merge
- [ ] Push — `git push origin feature/phase2-slice-a`
- [ ] `dev` pull — `git checkout dev && git pull origin dev`
- [ ] A merge:
  ```bash
  git merge feature/phase2-slice-a
  ./gradlew assembleDebug
  ```

### ⏰ နေ့လယ် (၂ နာရီ) — Edge Case
- [ ] Internet ဖြုတ်ပြီး app ဖွင့် → Error + retry
- [ ] Internet ပြန်ဖွင့် + retry → Data ပြန်ရ
- [ ] Firestore data ပြင် → App real-time ပြောင်း

### ⏰ ညနေ (၁ နာရီ) — Data Flow
- [ ] Data flow ရှင်းပြနိုင်အောင် လေ့ကျင့်

### ✅ Day 4 Deliverable
- [ ] Merge + build အောင်
- [ ] Edge case ၃ ခု

---

## 📅 Day 5 — 🔄 Cross-Review + Test

### ⏰ မနက် (၂ နာရီ) — B ရဲ့ code review
- [ ] `!!` မရှိ
- [ ] ViewModel ထဲ `android.*` မရှိ
- [ ] `Modifier` — ပထမ optional param
- [ ] `collectAsStateWithLifecycle()` သုံး
- [ ] မေးခွန်း — "ရှင်းပြနိုင်လား"

### ⏰ နေ့လယ် (၂ နာရီ) — Unit Test
- [ ] `HomeViewModelTest.kt` ပြီး
- [ ] Test ၂ ခု pass
- [ ] `./gradlew testDebugUnitTest`

### ⏰ ညနေ (၁ နာရီ) — Merge
- [ ] `dev` pull + build + test
- [ ] Push — `git push origin dev`

### ✅ Day 5 Deliverable
- [ ] B review ပြီး
- [ ] Test ၂ ခု pass
- [ ] `dev` merge

---

## ✅ Member A — Final Checklist

### DB
- [ ] `getPodcasts()` ၅ ခု
- [ ] Field name မှန် + rules မှန်

### Model
- [ ] `allPodcasts` / `podcasts` ခွဲ
- [ ] `categories` Firestore ကနေ
- [ ] `refresh()` leak fix
- [ ] `factory()` ထည့်
- [ ] `selectCategory()` filter
- [ ] `android.*` မရှိ

### UI
- [ ] podcast ၅ ခု ပေါ်
- [ ] Loading / Error / Empty
- [ ] `!!` မရှိ
- [ ] `@Preview` ၂ ခု
- [ ] `collectAsStateWithLifecycle()`

### Test
- [ ] Test ၂ ခု pass

### Team
- [ ] နေ့စဉ် push
- [ ] Build အောင်မှ push
- [ ] Data flow ရှင်းပြနိုင်

---

# 🅱️ Member B — Episode အသေးစိတ်

> **Slice B** — `episodes` collection → Detail screen

## 📅 Day 1 — 🗄️ Database Layer

### ⏰ မနက် (၁ နာရီ) — ၃ ယောက်လုံး အတူ
- [ ] Firebase Console ဖွင့် — `episodes` collection စစ်
- [ ] Field name စစ် — `id`, `podcastId`, `title`, `description`, `audioUrl`, `coverUrl`, `duration`, `publishedAt`
- [ ] `podcastId` — document ID အစစ် ဖြစ်လား
- [ ] `duration` — number (seconds) ဖြစ်လား
- [ ] `publishedAt` — timestamp ဖြစ်လား

### ⏰ နေ့လယ် (၃ နာရီ) — B ကိုယ်တိုင်

#### ၁။ A push ပြီးမှ `git pull`
- [ ] `git checkout feature/phase2-slice-b`
- [ ] `git pull origin dev`

#### ၂။ Composite Index (၁ နာရီ)
- [ ] `getEpisodes()` run — Logcat မှာ `FAILED_PRECONDITION` ပေါ်လား
- [ ] Link ကို နှိပ် → "Create index"
- [ ] ၁-၂ မိနစ် စောင့်
- [ ] **အဖွဲ့ကို ပြော** — "index ဖန်တီးပြီး"

#### ၃။ `getEpisodes()` စစ် (၁ နာရီ)
- [ ] Log ထည့် — `episodes=${list.size}`
- [ ] App run — ၄ ခု ထွက်လား
- [ ] မထွက်ရင် — `podcastId` မှန်/မမှန်

#### ၄။ `getPodcast(id)` အသစ် (၁ နာရီ)
- [ ] Function ရေး:
  ```kotlin
  suspend fun getPodcast(id: String): Podcast? {
      return try {
          val doc = db.collection("podcasts").document(id).get().await()
          doc.toObjectWithId(Podcast::class.java)
      } catch (e: Exception) {
          null
      }
  }
  ```
- [ ] `import kotlinx.coroutines.tasks.await`
- [ ] `kotlinx-coroutines-play-services` dependency ရှိလား စစ်
- [ ] Document မရှိရင် `null` ပြန်
- [ ] **Build စစ်** + push

### ✅ Day 1 Deliverable
- [ ] Composite index ဖန်တီး
- [ ] `getEpisodes()` ၄ ခု
- [ ] `getPodcast(id)` ရေး
- [ ] Push

---

## 📅 Day 2 — 🧠 Model Layer

### ⏰ မနက် (၃ နာရီ) — `PodcastDetailViewModel`

#### ၁။ State (၃၀ မိနစ်)
- [ ] `PodcastDetailUiState`:
  ```kotlin
  data class PodcastDetailUiState(
      val podcast: Podcast? = null,
      val episodes: List<Episode> = emptyList(),
      val isLoading: Boolean = true,
      val error: String? = null
  )
  ```

#### ၂။ ViewModel (၂ နာရီ)
- [ ] `PodcastDetailViewModel` ရေး:
  ```kotlin
  class PodcastDetailViewModel(
      private val podcastId: String,
      private val repo: PodcastRepository
  ) : ViewModel() {
      private val _state = MutableStateFlow(PodcastDetailUiState())
      val state: StateFlow<PodcastDetailUiState> = _state.asStateFlow()

      init { load() }

      private fun load() {
          viewModelScope.launch {
              // Podcast (one-shot)
              val podcast = repo.getPodcast(podcastId)
              if (podcast == null) {
                  _state.update { it.copy(isLoading = false, error = "Podcast ရှာမတွေ့ပါ") }
                  return@launch
              }
              _state.update { it.copy(podcast = podcast) }

              // Episodes (real-time)
              repo.getEpisodes(podcastId)
                  .catch { e ->
                      _state.update { it.copy(isLoading = false, error = e.message) }
                  }
                  .collect { list ->
                      _state.update { it.copy(episodes = list, isLoading = false) }
                  }
          }
      }

      companion object {
          fun factory(podcastId: String) = viewModelFactory {
              initializer { PodcastDetailViewModel(podcastId, PodcastRepository()) }
          }
      }
  }
  ```

#### ၃။ Factory (၃၀ မိနစ်)
- [ ] Import ထည့် — `viewModelFactory`, `initializer`

### ⏰ နေ့လယ် (၁ နာရီ) — Test
- [ ] `PodcastDetailViewModelTest.kt` ဖန်တီး
- [ ] Test ၁ — Podcast + Episodes load
- [ ] Test ၂ — Podcast မရှိရင် error

### ✅ Day 2 Deliverable
- [ ] `PodcastDetailViewModel` + state
- [ ] Flow ၂ ခု ပေါင်း
- [ ] Test ၂ ခု
- [ ] Push

---

## 📅 Day 3 — 🎨 UI Layer

### ⏰ မနက် (၃ နာရီ) — `PodcastDetailScreen`

#### Layout:
```
┌──────────────────────────────┐
│ ← (back)                     │
├──────────────────────────────┤
│        [Cover 200dp]         │
│        Podcast Title         │
│        category · ၄ ပိုင်း    │
│   [▶ အားလုံး ဖွင့်] [+ သိမ်း] │
│  Description...              │
├──────────────────────────────┤
│  ပိုင်းများ (၄)               │
│  ┌────────────────────────┐  │
│  │ EpisodeRow             │  │
│  └────────────────────────┘  │
└──────────────────────────────┘
```

#### Tasks:
- [ ] `PodcastDetailScreen.kt` ရေး
- [ ] TopAppBar (back button)
- [ ] `LazyColumn`:
  - [ ] `item { CoverHeader }`
  - [ ] `item { TitleSection }`
  - [ ] `item { ActionButtons }`
  - [ ] `item { Description }`
  - [ ] `item { SectionHeader("ပိုင်းများ (${episodes.size})") }`
  - [ ] `items(episodes) { EpisodeRow }`
- [ ] Loading / Error / Empty ၃ မျိုး
- [ ] Episode မရှိရင် `EmptyView("ပိုင်း မရှိသေးပါ")`
- [ ] Episode နှိပ် — `onEpisodeClick` (Phase 3 အထိ no-op)
- [ ] "သိမ်း" ခလုတ် — UI ပဲ (Phase 4)
- [ ] `@Preview` — `PodcastDetailContent` fake data နဲ့

### ⏰ နေ့လယ် (၂ နာရီ) — စမ်း
- [ ] Detail ဖွင့်ရင် cover + title + episodes ပေါ်
- [ ] Back နှိပ် → Home ပြန်
- [ ] Podcast မရှိ ID → "ရှာမတွေ့ပါ"
- [ ] Episode မရှိ podcast → Empty

### ✅ Day 3 Deliverable
- [ ] Detail screen ပြီး
- [ ] Back အလုပ်လုပ်
- [ ] Push

---

## 📅 Day 4 — 🔗 Integration

### ⏰ မနက် (၁ နာရီ) — Merge
- [ ] Push — `git push origin feature/phase2-slice-b`
- [ ] `dev` pull
- [ ] B merge — **A + C ပြီးမှ**

### ⏰ နေ့လယ် (၂ နာရီ) — Navigation ချိတ်
- [ ] `MyanCastNavHost.kt` ဖွင့်
- [ ] `PodcastDetailScreen` placeholder ကို အစားထိုး:
  ```kotlin
  composable("podcast/{id}") { entry ->
      val id = entry.arguments?.getString("id").orEmpty()
      PodcastDetailScreen(
          podcastId = id,
          onBack = { navController.popBackStack() }
      )
  }
  ```
- [ ] `Screen.PodcastDetail.create(id)` ဆက်သုံး
- [ ] Home → Detail → Back စမ်း

### ⏰ ညနေ (၁ နာရီ) — Edge Case
- [ ] Podcast မရှိ ID → "ရှာမတွေ့ပါ" (crash မဖြစ်)
- [ ] Episode မရှိ podcast → "ပိုင်း မရှိသေးပါ"
- [ ] Detail back မြန်မြန် ၅ ချက် → crash မဖြစ်

### ✅ Day 4 Deliverable
- [ ] Merge + build
- [ ] Nav ချိတ်
- [ ] Edge case ၃ ခု

---

## 📅 Day 5 — 🔄 Cross-Review + Test

### ⏰ မနက် (၂ နာရီ) — C ရဲ့ code review
- [ ] `!!` မရှိ
- [ ] ViewModel ထဲ `android.*` မရှိ
- [ ] `Modifier` — ပထမ optional param
- [ ] `formatDuration` test ရှိလား
- [ ] မေးခွန်း — "ရှင်းပြနိုင်လား"

### ⏰ နေ့လယ် (၂ နာရီ) — Unit Test
- [ ] Test ၂ ခု pass
- [ ] `./gradlew testDebugUnitTest`

### ⏰ ညနေ (၁ နာရီ) — Merge
- [ ] `dev` pull + build + test
- [ ] Push

### ✅ Day 5 Deliverable
- [ ] C review ပြီး
- [ ] Test ၂ ခု pass
- [ ] `dev` merge

---

## ✅ Member B — Final Checklist

### DB
- [ ] Composite index ဖန်တီး
- [ ] `getEpisodes()` ၄ ခု
- [ ] `getPodcast(id)` — `null` handle

### Model
- [ ] `PodcastDetailViewModel` + state
- [ ] Flow ၂ ခု ပေါင်း
- [ ] `factory(podcastId)` ထည့်
- [ ] `android.*` မရှိ

### UI
- [ ] `PodcastDetailScreen`
- [ ] Cover + Title + Episodes
- [ ] Back အလုပ်လုပ်
- [ ] State ၃ မျိုး
- [ ] `@Preview`

### Navigation
- [ ] `MyanCastNavHost` ချိတ်
- [ ] `Screen.PodcastDetail.create(id)` သုံး

### Test
- [ ] Test ၂ ခု pass

### Team
- [ ] နေ့စဉ် push
- [ ] Build အောင်မှ push
- [ ] Data flow ရှင်းပြနိုင်

---

# 🅲 Member C — Category + Formatter

> **Slice C** — Category သန့်ရှင်းရေး + format function ၃ ခု

## 📅 Day 1 — 🗄️ Database Layer

### ⏰ မနက် (၁ နာရီ) — ၃ ယောက်လုံး အတူ
- [ ] Firebase Console ဖွင့်
- [ ] podcast ၅ ခုရဲ့ `category` field စစ်
- [ ] **မှန်ရမယ့် ပုံစံ**:
  ```
  "သတင်း"
  "နည်းပညာ"
  "ဇာတ်လမ်း"
  "ကျန်းမာရေး"
  "စီးပွားရေး"
  ```
- [ ] **မှားနေတဲ့ ပုံစံ**:
  - `"သတင်း "` — space ပါ
  - `"News"` — အင်္ဂလိပ်
  - `""` — အလွတ်

### ⏰ နေ့လယ် (၂ နာရီ) — C ကိုယ်တိုင်
- [ ] category ၅ ခု တစ်ပုံစံတည်း ဖြစ်အောင် ပြင်
- [ ] space ဖျက် + အင်္ဂလိပ် → မြန်မာ
- [ ] category list စာရွက်မှာ ချရေး
- [ ] **Zawgyi ဆုံးဖြတ်ချက်** — Unicode ပဲ သုံး၊ Settings toggle ကို Phase 5
- [ ] **အဖွဲ့ကို ပြော** — category ၅ မျိုး ပြီး

### ✅ Day 1 Deliverable
- [ ] Category ၅ မျိုး တစ်ပုံစံတည်း
- [ ] Zawgyi ဆုံးဖြတ်ချက် ချ

---

## 📅 Day 2 — 🧠 Model Layer

### ⏰ မနက် (၃ နာရီ) — Pure Function ၃ ခု

#### ၁။ `CategoryFilter.kt` (၁ နာရီ)
- [ ] File ဖန်တီး — `ui/home/CategoryFilter.kt`
- [ ] `const val ALL_CATEGORY = "အားလုံး"` ကြေညာ
- [ ] `buildCategories()` ရေး:
  ```kotlin
  fun buildCategories(podcasts: List<Podcast>): List<String> {
      return listOf(ALL_CATEGORY) + podcasts
          .map { it.category }
          .filter { it.isNotBlank() }
          .distinct()
          .sorted()
  }
  ```
- [ ] `filterByCategory()` ရေး:
  ```kotlin
  fun filterByCategory(podcasts: List<Podcast>, category: String): List<Podcast> {
      return if (category == ALL_CATEGORY) podcasts
      else podcasts.filter { it.category == category }
  }
  ```

#### ၂။ `DurationFormatter.kt` (၁ နာရီ)
- [ ] File ဖန်တီး — `domain/util/DurationFormatter.kt`
- [ ] `formatDuration()` — "24:30"
  ```kotlin
  fun formatDuration(seconds: Int): String {
      if (seconds <= 0) return "0:00"
      val h = seconds / 3600
      val m = (seconds % 3600) / 60
      val s = seconds % 60
      return if (h > 0) "%d:%02d:%02d".format(h, m, s)
             else "%d:%02d".format(m, s)
  }
  ```
- [ ] `formatDurationLabel()` — "၂၄ မိနစ်"
  ```kotlin
  fun formatDurationLabel(seconds: Int): String {
      if (seconds <= 0) return ""
      val minutes = seconds / 60
      return "$minutes မိနစ်"
  }
  ```

#### ၃။ `DateFormatter.kt` (၁ နာရီ)
- [ ] File ဖန်တီး — `domain/util/DateFormatter.kt`
- [ ] `formatRelativeDate(timestamp: Timestamp?)`:
  ```kotlin
  fun formatRelativeDate(timestamp: Timestamp?): String {
      if (timestamp == null) return ""
      val diff = System.currentTimeMillis() - timestamp.toDate().time
      val sec = diff / 1000
      val min = sec / 60
      val hour = min / 60
      val day = hour / 24

      return when {
          sec < 60 -> "ခုလေးတင်"
          min < 60 -> "$min မိနစ် အရင်က"
          hour < 24 -> "$hour နာရီ အရင်က"
          day == 1L -> "မနေ့က"
          day < 30 -> "$day ရက် အရင်က"
          else -> SimpleDateFormat("d MMMM yyyy", Locale("my"))
              .format(timestamp.toDate())
      }
  }
  ```

### ⏰ နေ့လယ် (၁ နာရီ) — Unit Test
- [ ] `DurationFormatterTest.kt` ဖန်တီး
- [ ] Test ၁ — `formatDuration(1470) == "24:30"`
- [ ] Test ၂ — `formatDuration(0) == "0:00"` + `(-5) == "0:00"`
- [ ] `DateFormatterTest.kt` ဖန်တီး
- [ ] Test — `null` → `""`

### ✅ Day 2 Deliverable
- [ ] `CategoryFilter.kt`
- [ ] `DurationFormatter.kt`
- [ ] `DateFormatter.kt`
- [ ] Test ၂ ခု
- [ ] Push

---

## 📅 Day 3 — 🎨 UI Layer

### ⏰ မနက် (၃ နာရီ) — UI ချိတ်

#### ၁။ `CategoryChips` — HomeScreen မှာ ချိတ် (၁ နာရီ)
- [ ] **A ကို အရင်ပြော** — A push ပြီးမှ pull
- [ ] `HomeScreen.kt` ဖွင့်
- [ ] `CategoryChips` ရှိပြီးသား ဖြစ်လား
- [ ] Chip နှိပ် → `onCategorySelect` ခေါ်
- [ ] **A နဲ့ အတူ ၁၅ မိနစ်** — `selectCategory()` ချိတ်
- [ ] Chip နှိပ် → list ပြောင်း စမ်း

#### ၂။ `EpisodeRow` — Formatter ချိတ် (၁ နာရီ)
- [ ] `EpisodeRow.kt` ဖွင့်
- [ ] Duration ပြ:
  ```kotlin
  Text(
      text = formatDurationLabel(episode.duration),
      style = MaterialTheme.typography.labelSmall
  )
  ```
- [ ] Date ပြ:
  ```kotlin
  Text(
      text = formatRelativeDate(episode.publishedAt),
      ...
  )
  ```
- [ ] `duration == 0` → duration လုံးဝ မပြ
- [ ] `publishedAt == null` → date လုံးဝ မပြ

#### ၃။ Empty/Error component padding (၁ နာရီ)
- [ ] `EmptyView.kt` — `modifier: Modifier = Modifier` ရှိလား
- [ ] `ErrorView.kt` — အတူတူ
- [ ] `LoadingView.kt` — အတူတူ
- [ ] A နဲ့ B က `Modifier.padding(padding)` ပေးလို့ရကြောင်း အတည်ပြု

### ⏰ နေ့လယ် (၂ နာရီ) — စမ်း
- [ ] Chip အားလုံး တစ်ခုချင်း နှိပ် → list မှန်
- [ ] `duration = 0` → ကြာချိန် မပေါ်
- [ ] `publishedAt = null` → crash မဖြစ်

### ✅ Day 3 Deliverable
- [ ] Chip နှိပ် → list ပြောင်း
- [ ] EpisodeRow မှာ duration + date
- [ ] Empty/Error padding
- [ ] Push

---

## 📅 Day 4 — 🔗 Integration

### ⏰ မနက် (၁ နာရီ) — Merge
- [ ] Push — `git push origin feature/phase2-slice-c`
- [ ] **A ပြီးမှ C merge** — A ရဲ့ ဖိုင်တွေ ထိထားလို့
- [ ] `dev` pull + build

### ⏰ နေ့လယ် (၂ နာရီ) — Edge Case
- [ ] Category chip အကုန် တစ်ခုချင်း နှိပ် → list မှန်
- [ ] `duration = 0` episode → crash မဖြစ်
- [ ] `publishedAt = null` → crash မဖြစ်
- [ ] Myanmar font line-height မှန်

### ⏰ ညနေ (၁ နာရီ) — Data Flow
- [ ] `filterByCategory()` flow ရှင်းပြနိုင်
- [ ] Formatter flow ရှင်းပြနိုင်

### ✅ Day 4 Deliverable
- [ ] Merge + build
- [ ] Edge case ၃ ခု
- [ ] Data flow

---

## 📅 Day 5 — 🔄 Cross-Review + Test

### ⏰ မနက် (၂ နာရီ) — A ရဲ့ code review
- [ ] `!!` မရှိ
- [ ] ViewModel ထဲ `android.*` မရှိ
- [ ] `Modifier` — ပထမ optional param
- [ ] `collectAsStateWithLifecycle()`
- [ ] မေးခွန်း — "ရှင်းပြနိုင်လား"

### ⏰ နေ့လယ် (၂ နာရီ) — Unit Test
- [ ] `DurationFormatterTest` pass
- [ ] `DateFormatterTest` pass
- [ ] `./gradlew testDebugUnitTest`

### ⏰ ညနေ (၁ နာရီ) — Merge
- [ ] `dev` pull + build + test
- [ ] Push

### ✅ Day 5 Deliverable
- [ ] A review ပြီး
- [ ] Test ၂ ခု pass
- [ ] `dev` merge

---

## ✅ Member C — Final Checklist

### DB
- [ ] Category ၅ မျိုး တစ်ပုံစံတည်း
- [ ] Zawgyi ဆုံးဖြတ်ချက်

### Model
- [ ] `CategoryFilter.kt` — `buildCategories`, `filterByCategory`
- [ ] `ALL_CATEGORY` const
- [ ] `DurationFormatter` — `formatDuration`, `formatDurationLabel`
- [ ] `DateFormatter` — `formatRelativeDate`
- [ ] `null` handle
- [ ] `0` handle

### UI
- [ ] Chip နှိပ် → list ပြောင်း
- [ ] `EpisodeRow` — duration + date
- [ ] `EmptyView` / `ErrorView` — padding

### Test
- [ ] Formatter test ၂ ခု pass

### Team
- [ ] နေ့စဉ် push
- [ ] Build အောင်မှ push
- [ ] Data flow ရှင်းပြနိုင်

---

# 🤝 ၃ ယောက်လုံး — Common Tasks

## 📅 Day 1 — Database
- [ ] Firebase Console — collection ၃ ခု စစ်
- [ ] Field name — data class နဲ့ တူ
- [ ] Security rules — read only
- [ ] Standup (၁၅ မိနစ်)

## 📅 Day 2 — Model
- [ ] ViewModel ထဲ `android.*` မရှိ
- [ ] State — `data class` တစ်ခုတည်း
- [ ] Logic — ViewModel မှာ
- [ ] Standup

## 📅 Day 3 — UI
- [ ] `collectAsStateWithLifecycle()` သုံး
- [ ] `Modifier` — ပထမ optional param
- [ ] `!!` မရှိ
- [ ] Standup — ဖုန်းပြ

## 📅 Day 4 — Integration
- [ ] Merge — **A → C → B**
- [ ] `./gradlew assembleDebug` အောင်မှ
- [ ] Edge case ၁၀ ခု စမ်း
- [ ] Data flow ရှင်းပြ

## 📅 Day 5 — Cross-Review
- [ ] A → B review
- [ ] B → C review
- [ ] C → A review
- [ ] Unit test ၆ ခု pass
- [ ] `dev` merge

---

# 📊 Edge Case Testing (Day 4)

| # | Case | Expected | Owner |
|---|---|---|---|
| 1 | Internet ဖြုတ် → app ဖွင့် | Error + retry | A |
| 2 | Internet ပြန် + retry | Data ပြန်ရ | A |
| 3 | Podcast မရှိ ID → Detail | "ရှာမတွေ့ပါ" | B |
| 4 | Episode မရှိ podcast | "ပိုင်းမရှိသေးပါ" | B |
| 5 | Category chip အကုန် | list မှန် | C |
| 6 | `duration = 0` | ကြာချိန် မပြ | C |
| 7 | Rotate screen | State မပျောက် | All |
| 8 | Detail back ၅ ချက် | Crash မဖြစ် | All |
| 9 | Firestore data ပြင် | App real-time | All |
| 10 | Dark ↔ Light | စာ ဖတ်လို့ရ | All |

---

# 🌿 Git Workflow

```bash
# Day 1
git checkout dev
git pull origin dev
git checkout -b feature/phase2-slice-a   # A
git checkout -b feature/phase2-slice-b   # B
git checkout -b feature/phase2-slice-c   # C

# နေ့စဉ်
git checkout feature/phase2-slice-a
git pull origin dev
# ... အလုပ် ...
./gradlew assembleDebug
git add .
git commit -m "feat(home): add category filter"
git push origin feature/phase2-slice-a

# Day 4 — Merge (A → C → B)
git checkout dev
git pull origin dev
git merge feature/phase2-slice-a
./gradlew assembleDebug
git merge feature/phase2-slice-c
./gradlew assembleDebug
git merge feature/phase2-slice-b
./gradlew assembleDebug
git push origin dev
```

**စည်းကမ်း ၃ ခု:**
1. Build မအောင်ရင် push မလုပ်
2. သူများဖိုင် ထိရင် အရင်ပြော
3. နေ့စဉ် အနည်းဆုံး ၁ ခါ push

---

# 🎯 Phase 2 — Success Criteria

Phase 2 ပြီးရင် **၃ ယောက်လုံး** ဒါတွေ လုပ်နိုင်ရမယ်:

1. **Firestore data ကို Logcat မှာ စစ်နိုင်**
2. **Flow → StateFlow ပြောင်းနိုင်** (`map`, `update`, `catch`)
3. **ViewModel မှာ Job ထိန်းချုပ်နိုင်**
4. **ViewModel Factory ရေးနိုင်**
5. **Compose မှာ state ၃ မျိုး ပြနိုင်**
6. **`@Preview` ရေးနိုင်**
7. **Data flow ကို အစအဆုံး ရှင်းပြနိုင်**
8. **သူများရဲ့ code ကို review လုပ်နိုင်**

---

# 📝 Daily Standup Template

```
1. မနေ့က ဘာလုပ်ပြီးလဲ?
   →

2. ဒီနေ့ ဘာလုပ်မလဲ?
   →

3. ဘာပြဿနာ ရှိလဲ?
   →
```

---
