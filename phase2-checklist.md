# ✅ Phase 2 — Final Checklist

> `phase-2 Detailed.md`, `memberA.md`, `memberB.md`, `memberC.md` နဲ့ `memberC-next.md` တို့နဲ့ ချိန်ထိုး စစ်ဆေးပြီး။
> Code ကို 2026-09-22 (local working tree, `main` @ `1e27a2e` အပေါ်) မှာ စစ်ဆေးထားတယ်။

## အနှစ်ချုပ်

| အပိုင်း | အခြေအနေ |
|---|---|
| Build (`assembleDebug`) | ✅ Pass |
| Unit test (`testDebugUnitTest`) | ✅ **25 / 25 pass** (အရင် 9) |
| Slice A: Podcast စာရင်း (Home) | ✅ Code ပြီး |
| Slice B: Episode အသေးစိတ် | ✅ Code ပြီး |
| Slice C: Category + formatter | ✅ Code ပြီး |
| Team: Repository interface refactor | ✅ ပြီး |
| **Code မဟုတ်၊ လူလိုတာ** | ⬜ Commit + PR၊ device edge case၊ Console စစ် (section 6) |

---

## 1. 🅰️ Slice A: Podcast စာရင်း (Home)

### Database
- [x] `getPodcasts()` က document ID ပါတဲ့ podcast တွေ ပြန်တယ် (`toListWithIds`)
- [x] Podcast မှာ `orderBy` မသုံးလို့ document တွေ တိတ်တဆိတ် မပျောက်ဘူး
- [x] Security rules — read-only

### Model: `HomeViewModel.kt`
- [x] `allPodcasts` / `podcasts` ခွဲထား (အကုန်လုံး vs. filter ပြီးသား)
- [x] `categories` ကို data ကနေ `buildCategories()` နဲ့ တွက် (hard-code list မရှိ)
- [x] `selectCategory()` က `filterByCategory()` နဲ့ တကယ် filter လုပ်
- [x] `loadJob?.cancel()` ပါလို့ `refresh()` က listener မပွား
- [x] `factory()` — default က `PodcastRepositoryImpl()`
- [x] `selectedCategory` default က `ALL_CATEGORY`
- [x] `android.*` import မရှိ

### UI: `HomeScreen.kt`
- [x] `collectAsStateWithLifecycle()`
- [x] `when` အစီအစဉ် — loading → error → empty → content
- [x] `LoadingView` / `ErrorView` / `EmptyView` အားလုံး `Modifier.padding(padding)` ရတယ်
- [x] `!!` မရှိ
- [x] **Empty state ၂ မျိုး** — data မရှိ (`allPodcasts.isEmpty()`) → screen အပြည့်၊ filter ရလဒ် မရှိ → inline `"ဒီအမျိုးအစားမှာ မရှိသေးပါ"`၊ chip တွေ ဆက်ပေါ်
- [x] Trending ကနေ "See all" ခလုတ် ဖျက်ပြီး
- [x] အင်္ဂလိပ် heading တွေ မြန်မာ ပြောင်းပြီး
- [x] Preview က `ALL_CATEGORY` သုံး၊ hard-code `"အားလုံး"` မရှိ
- [x] ထပ်နေတဲ့ `EmptyView` import ဖျက်ပြီး
- [x] `@Preview` ၃ ခု — private content ပေါ်မှာပဲ (content / loading / empty)
- [x] MiniPlayer အတွက် `contentPadding = PaddingValues(bottom = 96.dp)` ထားရှိ

### UI: Components
- [x] `PodcastListItem.kt`: `"၁၂ ပိုင်း • နည်းပညာ"` + `bodySmall`
- [x] `PodcastCard.kt` (Trending): `"၂၀ ပိုင်း"` + `bodySmall` (အရင် `"20 episodes"` Poppins)

### Tests
- [x] `HomeViewModelTest`: စတင်ချိန် loading state
- [x] `HomeViewModelTest`: load ပြီးရင် podcast ၅ ခု၊ `isLoading = false`၊ `error = null`

---

## 2. 🅱️ Slice B: Episode အသေးစိတ်

### Database
- [x] Composite index `episodes: podcastId ↑ + publishedAt ↓` (app မှာ အလုပ်လုပ်)
- [x] `getPodcast(id)` — `.await()` သုံး၊ document မရှိရင် `null` ပြန်
- [x] `kotlinx-coroutines-play-services` version catalog ထဲ ပါ

### Model: `PodcastDetailViewModel.kt`
- [x] `PodcastDetailUiState` data class တစ်ခုတည်း (+ `canRetry`)
- [x] One-shot `getPodcast()` ပြီးမှ live `getEpisodes()` — coroutine တစ်ခုထဲ
- [x] Flow ပေါ်မှာ `.catch {}` (`collect` ပတ်လည် `try/catch` မဟုတ်)
- [x] `CancellationException` ကို rethrow
- [x] retry မှာ `loadJob?.cancel()`
- [x] Episode cover က podcast cover ကို fallback
- [x] `"ရှာမတွေ့ပါ"` မှာ retry ခလုတ် မရှိ (`canRetry = false`)
- [x] `factory(podcastId)`

### UI: `PodcastDetailScreen.kt`
- [x] NavHost အတွက် signature မပြောင်း
- [x] Loading / error / empty padding ပါ၊ `!!` မရှိ
- [x] Episode မရှိတဲ့ podcast မှာလည်း header ပေါ် + inline `"ပိုင်း မရှိသေးပါ"`
- [x] `contentPadding` bottom 96dp ထားရှိ
- [x] `@Preview` ၄ ခု

### Navigation
- [x] `MyanCastNavHost` → တကယ့် `PodcastDetailScreen`၊ route က `Screen.PodcastDetail` ကနေ
- [x] Back က လက်ရှိ entry ကိုပဲ pop လုပ်၊ မြန်မြန် နှိပ်ရင် Home ကို မရောက်

### Tests
- [x] `PodcastDetailViewModelTest`: အောင်မြင်တဲ့ load
- [x] `PodcastDetailViewModelTest`: podcast မရှိရင် → error

---

## 3. 🅲 Slice C: Category + Formatter

### Database
- [x] Zawgyi ဆုံးဖြတ်ချက် — **Unicode ပဲ သုံး၊ Zawgyi ကို Phase 5 ရွှေ့**
- [ ] Console: category ၅ မျိုး တစ်ပုံစံတည်း၊ နောက်က space မပါ *(လက်နဲ့ စစ်၊ section 6 ကြည့်)*

### Model
- [x] `CategoryFilter.kt`: `ALL_CATEGORY` const၊ `buildCategories()`၊ `filterByCategory()`
- [x] `MyanmarDigits.kt`: `Int` နဲ့ `Long` အတွက် `toMyanmarDigits()`
- [x] `formatDuration()`: `"24:30"`၊ `"1:02:05"`၊ 0/-5 → `"0:00"`
- [x] `formatDurationLabel()`: `"၂၄ မိနစ်"`၊ `"၁ နာရီ ၂ မိနစ်"`၊ `"၁ နာရီ"`၊ 45s → `"၁ မိနစ် အောက်"`၊ 0/-5 → `""`
- [x] `formatRelativeDate()`: null → `""`၊ အနာဂတ် → `"ခုလေးတင်"`၊ မြန်မာ ဂဏန်း
- [x] ရက်စွဲ အပြည့်အစုံ — built-in မြန်မာ လ စာရင်း (`Locale("my")` မမှီခို)
- [x] Logic ကို `formatRelativeMillis(epochMillis, now)` ခွဲထား — test လုပ်ရလွယ်

### UI
- [x] `EpisodeRow.kt`: formatter တွေ ချိတ်ပြီး၊ blank အပိုင်း filter၊ `" · "` join
- [x] `EpisodeRow.kt`: meta line `bodySmall` (အရင် `labelSmall` = Poppins)
- [x] `CategoryChips.kt`: `bodyMedium` (အရင် `labelMedium` = Poppins)
- [x] Chip တွေ `HomeViewModel` နဲ့ ချိတ်ပြီး
- [x] `SettingsScreen.kt`: Zawgyi toggle disabled၊ subtitle `"Phase 5 တွင် ထည့်သွင်းမည်"`
- [x] `EncodingUtil.kt`: ဆုံးဖြတ်ချက်နဲ့ အကြောင်းရင်း comment + Phase 5 TODO

### Tests
- [x] `DurationFormatterTest`: 3 test
- [x] `DateFormatterTest`: 8 test
- [x] `CategoryFilterTest`: 9 test (ဖျက်ခံခဲ့ရတဲ့ `selectCategory` test ကို အစားထိုး)

---

## 4. 🤝 Team စည်းကမ်း (code ထဲ စစ်ပြီး)

- [x] `app/src/main` တစ်ခုလုံးမှာ `!!` မရှိ
- [x] `"အားလုံး"` က `ALL_CATEGORY` အနေနဲ့ပဲ ရှိ
- [x] Screen composable ထဲမှာ list logic (`filter` / `sortedBy`) မရှိ (အသေးစား ချွင်းချက် — `EpisodeRow` က meta စာ ၂ ခု join လုပ်၊ `memberC.md` ကိုယ်တိုင် အကြံပြုထားတာ)
- [x] ViewModel ထဲ `android.*` မရှိ
- [x] Screen အားလုံး `collectAsStateWithLifecycle()` သုံး
- [x] Route တွေ `Screen.kt` ကနေပဲ
- [x] Phase 2 screen တွေမှာ မြန်မာစာအပေါ် `label*` (Poppins) style မရှိ
- [x] `PodcastRepository` interface + `PodcastRepositoryImpl` + test အတွက် `FakePodcastRepository`

---

## 5. ⏸️ တမင်ဆိုင်းငံ့ထားတာ

| အချက် | ဘာကြောင့် | ဘယ်တော့ |
|---|---|---|
| `MainActivity.kt`: commented-out `EPISODE_TEST` block | Team က ခု ထားဖို့ ဆုံးဖြတ် | Final submission မတိုင်မီ ဖျက် |
| Zawgyi စာသား ပြောင်းလဲမှု (Rabbit) | အမှတ်ပေးစရာ မဟုတ်၊ ~၁ ရက် အလုပ် | Phase 5 |
| `lastPlayed` / "Continue Listening" | Room history လိုတယ် | Phase 4 |
| `"သိမ်း"` (subscribe) ခလုတ် | Room လိုတယ် | Phase 4 |

### ဆက်လက်သယ်ဆောင်ရန် — နောက် phase component တွေမှာ Poppins

ဒါတွေ Phase 2 screen တွေ မဟုတ်ပေမဲ့ သူတို့ phase စတဲ့အခါ ပြင်ပါ:

| ဖိုင် | လိုင်း | စာသား | Phase |
|---|---|---|---|
| `NewsCard.kt` | 68 | `news.category` (မြန်မာ) | 4 (News) |
| `MiniPlayer.kt` | 87 | `subtitle` (မြန်မာ ဖြစ်နိုင်) | 3 (Player) |
| `ContinueListeningCard.kt` | 57-59 | `"Continue Listening"`: ဘာသာပြန် + `bodySmall` | 4 |
| `SectionHeader.kt` | 37 | `"See all"`: ပြန်ပေါ်ရင် ဘာသာပြန် | — |

---