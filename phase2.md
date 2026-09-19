# Phase 2 — Home + Podcast Detail (Week 2)

> **အရေးကြီး** — ဒါက **plan guide** ပါ။ Code တွေ ဘယ်လို ရေးရမလဲ ဆိုတဲ့ **လမ်းညွှန်** ဖြစ်ပြီး၊
> အတိအကျ copy-paste လုပ်ဖို့ မဟုတ်ပါ။ Phase 1 မှာ တည်ဆောက်ထားတဲ့ အပေါ်မှာပဲ ဆက်တည်ဆောက်ပါ။

---

## Phase 2 — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **ရည်ရွယ်ချက်** | Firebase data ကို Home နဲ့ Detail screen မှာ တကယ် ပြနိုင်ရန် |
| **ကြာချိန်** | ၅ ရက် (Week 2) |
| **အဖွဲ့ဝင်** | A (UI), B (Data/ViewModel), C (Firestore data + Week 4 ကြိုပြင်) |
| **အဆုံးမှာ ရရမယ့် ရလဒ်** | Home မှာ podcast ၅ ခု ပေါ်မယ်။ တစ်ခုကို နှိပ်ရင် Detail ပွင့်ပြီး episode ၄ ခု ပေါ်မယ်။ Loading / Error / Empty ၃ မျိုးလုံး မှန်ကန်စွာ ပြမယ်။ |

### Phase 2 မှာ လုပ်မယ့် အလုပ် ၅ ခု

```
၁။ Firestore data စစ် + Utility formatter ရေး
၂။ HomeViewModel ကို category filter နဲ့ ဖြည့်စွက်
၃။ HomeScreen ကို Firebase data နဲ့ အပြည့်အဝ ချိတ်
၄။ PodcastDetailViewModel + PodcastDetailScreen
၅။ Edge case စစ် + Unit test + Merge
```

---

## Phase 1 ကနေ ဆက်ယူလာတဲ့ အခြေအနေ

Phase 2 မစခင် ဒါတွေ ရှိပြီးသား ဖြစ်ရမယ် (Phase 1 မှာ ပြီးသွားပြီ):

| အခြေအနေ | ဖိုင် |
|---|---|
| ✅ Theme + Typography (Zawgyi/Unicode swap ပါ) | `ui/theme/` |
| ✅ Navigation + Bottom Nav ၅ ခု + route ၈ ခု | `ui/navigation/` |
| ✅ Reusable component ၁၃ ခု | `ui/components/` |
| ✅ Repository ၂ ခု (document ID mapping မှန်ပြီ) | `data/repository/` |
| ✅ HomeScreen + HomeViewModel (အခြေခံ) | `ui/home/` |
| ✅ SettingsScreen (Zawgyi + Dark toggle) | `ui/settings/` |
| ⬜ PodcastDetailScreen | Placeholder ပဲ ရှိသေး |
| ⬜ PodcastDetailViewModel | အလွတ် ဖိုင် |
| ⬜ `domain/util/` formatter ၃ ခု | အလွတ် ဖိုင် |

---

## Day 1 — Firestore Data စစ်ဆေး + Utility ရေးခြင်း

### ရက် ၁ ရဲ့ ရည်မှန်းချက်
- Firestore မှာ data တကယ် ရှိလား၊ field name မှန်လား စစ်
- `DurationFormatter`, `DateFormatter` ရေး

### Step 1: Firestore Data စစ်ဆေးခြင်း (Member B, C)

App ကို run ပြီး Home screen မှာ podcast မပေါ်ရင် ဒီအချက်တွေ လိုက်စစ်ပါ:

| စစ်ရမယ့် အချက် | ဘယ်လို ဖြစ်ရမလဲ |
|---|---|
| Collection name | `podcasts`, `episodes`, `news` — **အငယ်စာလုံး၊ အများကိန်း** |
| Field name | Firestore field နဲ့ data class property **တစ်လုံးမကျန် တူရမယ်** |
| `episodes.podcastId` | Podcast ရဲ့ **document ID အစစ်** ဖြစ်ရမယ် (title မဟုတ်) |
| `publishedAt` type | `timestamp` ဖြစ်ရမယ် (`string` မဟုတ်) |
| `duration` type | `number` ဖြစ်ရမယ် (စက္ကန့်နဲ့) |
| Security rules | `allow read: if true;` ဖြစ်ရမယ် |

> **သတိ — Composite Index လိုအပ်ချက်**
>
> `PodcastRepository.getEpisodes()` က `whereEqualTo("podcastId")` **နဲ့**
> `orderBy("publishedAt")` ကို တွဲသုံးတယ်။ Firestore က ဒီလို query အတွက်
> **composite index** တောင်းတယ်။ ပထမဆုံး run တဲ့အခါ Logcat မှာ ဒီလို ပေါ်လာမယ်:
>
> ```
> FAILED_PRECONDITION: The query requires an index.
> You can create it here: https://console.firebase.google.com/...
> ```
>
> အဲဒီ **link ကို နှိပ်ပြီး "Create index" နှိပ်လိုက်ရုံပါပဲ**။ ၁–၂ မိနစ် စောင့်ရင် အလုပ်လုပ်မယ်။
> ဒါကို ကြိုမသိရင် တစ်နေကုန် ရှာနေရတတ်တယ်။

### Step 2: `DurationFormatter.kt` ရေးခြင်း (Member B)

**ဘာလုပ်မလဲ** — စက္ကန့် (`Int`) ကို လူဖတ်လို့ရတဲ့ စာသား ပြောင်း။

| Input | Output |
|---|---|
| `1470` | `"24:30"` |
| `3725` | `"1:02:05"` |
| `45` | `"0:45"` |
| `0` | `"0:00"` |

**လမ်းညွှန်:**
- `seconds / 3600` → နာရီ၊ `(seconds % 3600) / 60` → မိနစ်၊ `seconds % 60` → စက္ကန့်
- နာရီ ၀ ဖြစ်ရင် နာရီ မပြနဲ့
- မိနစ်/စက္ကန့်ကို `padStart(2, '0')` နဲ့ ၂ လုံး ဖြစ်အောင် ထား
- အနုတ်ကိန်း (negative) ဝင်လာရင် `"0:00"` ပြန်ပေး — crash မဖြစ်အောင်

**Function ၂ ခု အကြံပြုချက်:**
```
fun formatDuration(seconds: Int): String        // "24:30"      — progress bar အတွက်
fun formatDurationLabel(seconds: Int): String   // "၂၄ မိနစ်"   — list row အတွက်
```

### Step 3: `DateFormatter.kt` ရေးခြင်း (Member B)

**ဘာလုပ်မလဲ** — Firestore `Timestamp` ကို relative date ပြောင်း။

| Input | Output |
|---|---|
| ၃၀ စက္ကန့် အရင်က | `"ခုလေးတင်"` |
| ၂ နာရီ အရင်က | `"၂ နာရီ အရင်က"` |
| မနေ့က | `"မနေ့က"` |
| ၅ ရက် အရင်က | `"၅ ရက် အရင်က"` |
| ၁ လ ကျော် | `"၁၅ စက်တင်ဘာ ၂၀၂၅"` |

**လမ်းညွှန်:**
- `Timestamp?` ကို လက်ခံပါ — `null` ဖြစ်ရင် `""` ပြန်ပေး (crash မဖြစ်အောင်)
- `timestamp.toDate().time` နဲ့ `System.currentTimeMillis()` ကို နုတ်
- `< 1 မိနစ်` → `"ခုလေးတင်"`, `< 1 နာရီ` → မိနစ်, `< 24 နာရီ` → နာရီ, `< 30 ရက်` → ရက်
- ကျန်ရင် `SimpleDateFormat("d MMMM yyyy", Locale("my"))`

### Step 4: `EncodingUtil.kt` — ဆုံးဖြတ်ချက် လိုတယ် (Member C)

Phase 1 မှာ **font** ပဲ ပြောင်းထားတယ်၊ **စာသား ကိုယ်တိုင်** မပြောင်းသေးဘူး။
Firestore data က Unicode ဖြစ်ရင် Zawgyi font နဲ့ ပြတဲ့အခါ စာလုံး ပုံပျက်မယ်။

နည်းလမ်း ၂ ခု:

| နည်းလမ်း | အလုပ်ပမာဏ | ရလဒ် |
|---|---|---|
| **၁။ Unicode ပဲ သုံး** (အကြံပြု) | Settings က Zawgyi toggle ကို ဖျောက် | ၅ မိနစ် |
| **၂။ တကယ် ပြောင်း** | Rabbit converter library ထည့် | ၁ ရက် |

Course project အတွက် **နည်းလမ်း ၁ ကို အကြံပြုပါတယ်**။ Toggle ကို ချန်ထားပြီး
"Phase 5 မှာ ထည့်မယ်" လို့ မှတ်ထားလိုက်ပါ။

### Day 1 ရဲ့ Deliverable

- ✅ Firestore data ၃၅ ခု တကယ် ရှိပြီး field name မှန်ကြောင်း စစ်ပြီး
- ✅ Composite index ဖန်တီးပြီး
- ✅ `DurationFormatter`, `DateFormatter` ရေးပြီး

---

## Day 2 — HomeViewModel ဖြည့်စွက်ခြင်း

### ရက် ၂ ရဲ့ ရည်မှန်းချက်
- Category filter တကယ် အလုပ်လုပ်အောင် လုပ်
- ViewModel ကို test လုပ်လို့ရအောင် ပြင်

### Step 1: လက်ရှိ `HomeViewModel` ရဲ့ ချို့ယွင်းချက် ၃ ခု

| # | ပြဿနာ | ဘာကြောင့် အရေးကြီးလဲ |
|---|---|---|
| 1 | `selectCategory()` က state ပဲ ပြောင်းတယ်၊ **list ကို filter မလုပ်ဘူး** | Chip နှိပ်လည်း ဘာမှ မဖြစ် |
| 2 | `categories` က **hard-code** (`"All", "News", "Tech"...`)၊ Firestore က `"သတင်း"` | ဘယ်တော့မှ မကိုက်လို့ list အလွတ် ဖြစ်မယ် |
| 3 | `refresh()` က `loadPodcasts()` ထပ်ခေါ် → **listener ၂ ခု** | Memory leak + data ၂ ခါ ဝင် |

### Step 2: State ပုံစံ ပြင်ဆင်ခြင်း (Member B)

**လမ်းညွှန်** — `HomeUiState` ထဲမှာ list ၂ မျိုး ခွဲထား:

```
allPodcasts     : List<Podcast>   ← Firestore ကလာတဲ့ အကုန်လုံး (မပြောင်း)
podcasts        : List<Podcast>   ← Filter ပြီးသား (UI က ဒါကို ပြ)
categories      : List<String>    ← allPodcasts ကနေ တွက်ထုတ်
selectedCategory: String          ← Default က "အားလုံး"
```

**Category တွက်ထုတ်နည်း:**
```
listOf("အားလုံး") + allPodcasts.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
```

**Filter logic:**
```
if (selected == "အားလုံး") allPodcasts
else allPodcasts.filter { it.category == selected }
```

> **အရေးကြီး** — Filter ကို ViewModel ထဲမှာပဲ လုပ်ပါ။ Composable ထဲမှာ `filter { }`
> ခေါ်ရင် recomposition တိုင်း ပြန်တွက်နေမယ် — စွမ်းဆောင်ရည် ကျမယ်။

### Step 3: Job ထိန်းချုပ်မှု ထည့်ခြင်း

**လမ်းညွှန်:**
- `private var loadJob: Job? = null` ကြေညာ
- `loadPodcasts()` အစမှာ `loadJob?.cancel()` ခေါ်
- `loadJob = viewModelScope.launch { ... }` လို့ သတ်မှတ်

### Step 4: ViewModel Factory ထည့်ခြင်း (Member B)

လက်ရှိမှာ `HomeViewModel(repo = PodcastRepository())` ဆိုပြီး **default parameter** နဲ့
repository ကို တိုက်ရိုက် ဆောက်နေတယ်။ `viewModel()` နဲ့ အလုပ်လုပ်ပေမဲ့ **test မလုပ်လို့ရဘူး**။

**လမ်းညွှန်:**
```
class HomeViewModel(private val repo: PodcastRepository) : ViewModel() {
    companion object {
        fun factory(repo: PodcastRepository = PodcastRepository()) = viewModelFactory {
            initializer { HomeViewModel(repo) }
        }
    }
}
```
Screen မှာ: `viewModel(factory = HomeViewModel.factory())`

### Day 2 ရဲ့ Deliverable

- ✅ Category filter တကယ် အလုပ်လုပ်
- ✅ Category list က Firestore data ကနေ တွက်ထုတ်
- ✅ `refresh()` က listener မပွားတော့ဘူး
- ✅ ViewModel Factory နဲ့ test လုပ်လို့ရပြီ

---

## Day 3 — HomeScreen အပြည့်အစုံ

### ရက် ၃ ရဲ့ ရည်မှန်းချက်
- HomeScreen ကို data နဲ့ တကယ် ချိတ်
- State ၃ မျိုး (Loading / Error / Empty) မှန်ကန်စွာ ပြ

### Step 1: လက်ရှိ `HomeScreen` ရဲ့ ချို့ယွင်းချက် ၄ ခု

| # | ပြဿနာ | ဖြေရှင်းနည်း |
|---|---|---|
| 1 | `LoadingView` / `ErrorView` မှာ **`padding` မသုံးထားဘူး** → TopAppBar အောက် ဝင်နေမယ် | `Modifier.padding(padding)` ထည့် |
| 2 | `state.error!!` — **`!!` သုံးထားတယ်** → race condition မှာ crash | `state.error?.let { }` သုံး |
| 3 | **Empty state မရှိဘူး** — data မရှိရင် အဖြူ screen | `EmptyView` ထည့် |
| 4 | `SectionHeader(onSeeAll = { /* TODO */ })` — နှိပ်လို့ရပေမဲ့ ဘာမှ မဖြစ် | Search ကို ပို့ (သို့) ဖျောက် |

### Step 2: State Handling ပုံစံ (Member A)

**လမ်းညွှန်** — `when` ရဲ့ အစီအစဉ်က အရေးကြီးတယ်:

```
when {
    state.isLoading           -> LoadingView(padding နဲ့)
    state.error != null       -> ErrorView(padding နဲ့, retry နဲ့)
    state.podcasts.isEmpty()  -> EmptyView(padding နဲ့)      ← အသစ် ထည့်ရမယ်
    else                      -> HomeContent(...)
}
```

> **သတိ** — Loading ကို အရင် စစ်ပါ။ မဟုတ်ရင် ပထမဆုံး frame မှာ `podcasts` က
> အလွတ် ဖြစ်နေလို့ "ဘာမှ မရှိပါ" ဆိုတာ တစ်ချက် လျှပ်ပြပြီး ပျောက်သွားမယ်။

### Step 3: MiniPlayer နေရာ ချန်ထားခြင်း

`HomeContent` မှာ `contentPadding = PaddingValues(bottom = 96.dp)` ရှိပြီးသား —
ဒါက Phase 3 မှာ ထည့်မယ့် MiniPlayer အတွက် ကြိုချန်ထားတာ။ **မဖျက်ပါနဲ့**။

### Step 4: Preview ထည့်ခြင်း (Member A)

Firebase မလိုဘဲ UI ကို စမ်းလို့ရအောင် `@Preview` ထည့်ပါ:

**လမ်းညွှန်:**
- `HomeContent` (private) ကို `@Preview` နဲ့ fake data ပေးပြီး စမ်း
- `HomeScreen` (public) ကို preview **မလုပ်ပါနဲ့** — ViewModel လိုလို့ crash ဖြစ်မယ်
- `MyanCastTheme { }` နဲ့ ပတ်ပါ၊ မဟုတ်ရင် font/color မမှန်ဘူး
- `@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)` သုံးပါ

### Day 3 ရဲ့ Deliverable

- ✅ Home မှာ podcast ၅ ခု တကယ် ပေါ်
- ✅ Category chip နှိပ်ရင် list ပြောင်း
- ✅ Loading spinner → data ပြောင်းလဲမှု ချောမွေ့
- ✅ Internet ဖြုတ်ရင် Error + "ထပ်စမ်းကြည့်" ခလုတ် ပေါ်
- ✅ `@Preview` အလုပ်လုပ်

---

## Day 4 — PodcastDetailViewModel + Screen

### ရက် ၄ ရဲ့ ရည်မှန်းချက်
- Podcast တစ်ခုရဲ့ episode list ပြ
- Navigation argument တကယ် သုံး

### Step 1: `PodcastDetailViewModel` ရေးခြင်း (Member B)

**State ပုံစံ အကြံပြုချက်:**
```
data class PodcastDetailUiState(
    val podcast  : Podcast?      = null,
    val episodes : List<Episode> = emptyList(),
    val isLoading: Boolean       = true,
    val error    : String?       = null
)
```

**`podcastId` ကို ဘယ်လို ရမလဲ — နည်း ၂ မျိုး:**

| နည်းလမ်း | အားသာချက် | အားနည်းချက် |
|---|---|---|
| **A. `SavedStateHandle`** | Process death မှာလည်း မပျောက် | Factory ရေးရ ခက်နည်းနည်း |
| **B. Screen ကနေ parameter ပို့** | ရိုးရှင်း | Rotation မှာ ပြန်ဆွဲ |

Course project အတွက် **နည်းလမ်း B** လုံလောက်ပါတယ်:
```
viewModel(factory = PodcastDetailViewModel.factory(podcastId))
```

**Repository မှာ ထပ်ထည့်ရမယ့် function:**
```
suspend fun getPodcast(id: String): Podcast?
```
`db.collection("podcasts").document(id).get()` သုံးပြီး
`toObjectWithId()` (**Phase 1 မှာ ရေးထားပြီးသား**) နဲ့ map လုပ်ပါ။

> **လိုအပ်တဲ့ dependency** — `.await()` သုံးချင်ရင်
> `kotlinx-coroutines-play-services` ကို `libs.versions.toml` မှာ ထပ်ထည့်ရမယ်။
> မထည့်ချင်ရင် `addOnSuccessListener` + `suspendCancellableCoroutine` နဲ့လည်း ရတယ်။

### Step 2: Flow ၂ ခု ပေါင်းခြင်း

Podcast အချက်အလက် (one-shot) နဲ့ Episode list (real-time flow) ကို
တစ်ချိန်တည်း load ရမယ်။

**လမ်းညွှန်:**
- `viewModelScope.launch { }` တစ်ခုထဲမှာ
- အရင် `getPodcast(id)` ကို `suspend` ခေါ်ပြီး state ထဲ ထည့်
- ပြီးမှ `getEpisodes(id).collect { }` နဲ့ episode တွေ စောင့်
- Error ကို `runCatching { }` (သို့) `try/catch` နဲ့ ဖမ်း
- Podcast က `null` ပြန်လာရင် `error = "Podcast ရှာမတွေ့ပါ"` သတ်မှတ်

### Step 3: `PodcastDetailScreen` ရေးခြင်း (Member A)

**Layout အကြံပြုချက် (အပေါ်ကနေ အောက်):**

```
┌──────────────────────────────┐
│ ← (back)                     │  TopAppBar (transparent)
├──────────────────────────────┤
│                              │
│        [Cover 200dp]         │  AsyncImage, rounded 16dp
│                              │
│        Podcast Title         │  displaySmall
│        category · ၄ ပိုင်း    │  bodySmall, TextLo
│                              │
│   [▶ အားလုံး ဖွင့်] [+ သိမ်း] │  Button + OutlinedButton
│                              │
│  Description text...         │  bodyMedium, maxLines=3 + "ပိုမို"
├──────────────────────────────┤
│  ပိုင်းများ (၄)               │  SectionHeader
│  ┌────────────────────────┐  │
│  │ EpisodeRow             │  │  ← Phase 1 component ပြန်သုံး
│  │ EpisodeRow             │  │
│  └────────────────────────┘  │
└──────────────────────────────┘
```

**လမ်းညွှန်:**
- `LazyColumn` သုံး — header ကို `item { }`, episode တွေကို `items(state.episodes) { }`
- `EpisodeRow` က Phase 1 မှာ ရှိပြီးသား — **အသစ် မရေးပါနဲ့**
- Episode နှိပ်ရင် Phase 3 အထိ ဘာမှ မလုပ်သေးဘူး — `onEpisodeClick` ကို ချန်ထား
- "သိမ်း" ခလုတ်က Phase 4 (Room) အထိ မအလုပ်လုပ်သေးဘူး — UI ပဲ ဆောက်ထား
- Episode မရှိရင် `EmptyView("ပိုင်း မရှိသေးပါ")` ပြ

### Step 4: Navigation ချိတ်ခြင်း

`MyanCastNavHost.kt` မှာ placeholder ကို တကယ့် screen နဲ့ အစားထိုးပါ။
**Route နဲ့ argument က Phase 1 မှာ ရေးပြီးသား** — `Screen.PodcastDetail.create(id)`
ကို ပဲ ဆက်သုံးပါ။ Route string အသစ် **မရေးပါနဲ့**။

### Day 4 ရဲ့ Deliverable

- ✅ Home က podcast နှိပ်ရင် Detail ပွင့်
- ✅ Detail မှာ cover, title, description ပေါ်
- ✅ Episode ၄ ခု စာရင်း ပေါ်
- ✅ Back နှိပ်ရင် Home ကို ပြန်ရောက်
- ✅ Bottom nav က Detail မှာ **မပေါ်** (Phase 1 မှာ စီစဉ်ပြီးသား)

---

## Day 5 — စစ်ဆေးခြင်း + Test + သန့်ရှင်းရေး

### ရက် ၅ ရဲ့ ရည်မှန်းချက်
- Edge case တွေ စစ်
- Unit test ရေး
- Code review + merge

### Step 1: Edge Case စစ်ဆေးခြင်း (အားလုံး)

| စမ်းရမယ့် အခြေအနေ | မျှော်လင့်ရမယ့် ရလဒ် |
|---|---|
| Internet ဖြုတ်ပြီး app ဖွင့် | Error view + retry ခလုတ် |
| Internet ပြန်ဖွင့်ပြီး retry နှိပ် | Data ပြန်ရ |
| Podcast မရှိတဲ့ ID နဲ့ Detail ဖွင့် | "ရှာမတွေ့ပါ" (crash မဖြစ်ရ) |
| Episode မရှိတဲ့ podcast | "ပိုင်း မရှိသေးပါ" empty view |
| Screen လှည့် (rotate) | State မပျောက်ရ |
| Detail ကနေ back မြန်မြန် ၅ ချက် နှိပ် | Crash မဖြစ်ရ |
| Firestore data ကို Console က ပြင် | App မှာ **ချက်ချင်း ပြောင်း** (real-time) |
| Settings မှာ Dark → Light ပြောင်း | Screen အားလုံး ပြောင်း၊ စာ ဖတ်လို့ရနေရမယ် |

### Step 2: Unit Test ရေးခြင်း (Member B)

Phase 1 မှာ `junit` နဲ့ `kotlinx-coroutines-test` ထည့်ပြီးသား —
`app/src/test/java/` ထဲမှာ ရေးပါ။

| Test | ဘာစစ်မလဲ |
|---|---|
| `formatDuration(1470)` | `"24:30"` ဖြစ်ရမယ် |
| `formatDuration(3725)` | `"1:02:05"` ဖြစ်ရမယ် |
| `formatDuration(0)` | `"0:00"` ဖြစ်ရမယ် (crash မဖြစ်ရ) |
| `HomeViewModel` category filter | `"သတင်း"` ရွေးရင် သတင်း podcast ပဲ ကျန်ရမယ် |
| `HomeViewModel` "အားလုံး" | Podcast အကုန်လုံး ပြန်ပေါ်ရမယ် |

**လမ်းညွှန်** — Repository ကို fake လုပ်ဖို့:

> `PodcastRepository` က ခု `class` ဖြစ်နေတယ် — subclass မလုပ်လို့ရဘူး။
> **`interface PodcastRepository` + `PodcastRepositoryImpl` ခွဲတာကို အကြံပြုပါတယ်**။
> သန့်တယ်၊ Phase 4 (Room) မှာလည်း အသုံးဝင်မယ်။
>
> ```
> interface PodcastRepository {
>     fun getPodcasts(): Flow<List<Podcast>>
>     fun getEpisodes(podcastId: String): Flow<List<Episode>>
>     suspend fun getPodcast(id: String): Podcast?
> }
> ```
> Test မှာ: `class FakePodcastRepository : PodcastRepository { ... flowOf(fakeData) }`

### Step 3: Code Review Checklist

Merge မလုပ်ခင် ဒါတွေ စစ်ပါ:

- [ ] `!!` (not-null assertion) တစ်ခုမှ မကျန်ဘူးလား
- [ ] Composable ထဲမှာ `filter`, `sortedBy`, `map` မရှိဘူးလား (ViewModel မှာ လုပ်ရမယ်)
- [ ] `Modifier` က composable ရဲ့ **ပထမဆုံး optional parameter** လား
- [ ] ViewModel ထဲမှာ `android.*` import မရှိဘူးလား (Context မသုံးရ)
- [ ] Flow collect က `collectAsStateWithLifecycle()` လား (`collectAsState()` မဟုတ်)
- [ ] Route string ကို `Screen.kt` ကနေပဲ ယူလား (hard-code မရှိဘူးလား)
- [ ] Build warning ဘယ်နှစ်ခု ရှိလဲ — ၀ ဖြစ်အောင် လုပ်

### Step 4: Merge လုပ်ခြင်း

```bash
git checkout dev
git pull origin dev
git merge feature/phase2-home-detail
./gradlew assembleDebug     # merge ပြီး build ထပ်စစ်
git push origin dev
```

### Day 5 ရဲ့ Deliverable

- ✅ Edge case ၈ ခုလုံး စစ်ပြီး
- ✅ Unit test ၅ ခု အနည်းဆုံး ရေးပြီး၊ အားလုံး pass
- ✅ Code review ပြီး
- ✅ `dev` branch ကို merge ပြီး

---

## Phase 2 — Final Checklist

| # | စစ်ရမယ့် အချက် | ပြီး/မပြီး |
|---|---|---|
| 1 | Firestore composite index ဖန်တီးပြီး | ☐ |
| 2 | `DurationFormatter` ရေးပြီး + test pass | ☐ |
| 3 | `DateFormatter` ရေးပြီး | ☐ |
| 4 | Zawgyi ဆုံးဖြတ်ချက် ချပြီး | ☐ |
| 5 | HomeViewModel category filter အလုပ်လုပ် | ☐ |
| 6 | Category list က Firestore ကနေ တွက်ထုတ် | ☐ |
| 7 | `refresh()` က listener မပွား | ☐ |
| 8 | ViewModel Factory ထည့်ပြီး | ☐ |
| 9 | Home မှာ podcast ၅ ခု ပေါ် | ☐ |
| 10 | Loading / Error / Empty ၃ မျိုးလုံး မှန် | ☐ |
| 11 | `@Preview` အလုပ်လုပ် | ☐ |
| 12 | `PodcastDetailViewModel` ရေးပြီး | ☐ |
| 13 | `PodcastDetailScreen` ရေးပြီး | ☐ |
| 14 | Episode ၄ ခု ပေါ် | ☐ |
| 15 | Back navigation မှန် | ☐ |
| 16 | Rotate လုပ်လည်း state မပျောက် | ☐ |
| 17 | `!!` တစ်ခုမှ မကျန် | ☐ |
| 18 | Unit test ၅ ခု pass | ☐ |
| 19 | `dev` branch ကို merge ပြီး | ☐ |

---

## Phase 2 — ဖြစ်နိုင်တဲ့ ပြဿနာများနဲ့ ဖြေရှင်းနည်း

| ပြဿနာ | ဖြေရှင်းနည်း |
|---|---|
| Home မှာ podcast မပေါ် | Logcat စစ် → `PERMISSION_DENIED` ဆိုရင် Firestore rules; အလွတ်ဆိုရင် collection name |
| Episode မပေါ် (podcast တော့ပေါ်) | ၁) Composite index ဖန်တီးပြီးလား ၂) `podcastId` က document ID အစစ်လား |
| Detail ဖွင့်ရင် ချက်ချင်း crash | `podcastId` က `""` ဖြစ်နေလား — Logcat မှာ route string စစ် |
| Cover image မပေါ် | ၁) `INTERNET` permission ၂) `coverUrl` က `https://` နဲ့ စလား |
| Category chip နှိပ်လည်း ဘာမှ မဖြစ် | Filter logic က ViewModel ထဲ ရောက်ပြီလား |
| Rotate ရင် data ပြန်ဆွဲ | `viewModel()` က Screen ထဲမှာလား၊ `remember` နဲ့ ဆောက်နေလား စစ် |
| `duration` က `0` ပဲ ပြ | Firestore မှာ `number` type လား `string` type လား စစ် |
| Timestamp `null` | Firestore မှာ `timestamp` type နဲ့ ထည့်ထားလား စစ် |
| Myanmar စာလုံး ကျဉ်းနေ/ထပ်နေ | `lineHeight` က `fontSize` ရဲ့ ၁.၈ ဆ ရှိလား စစ် |

---

## Phase 2 — Git Workflow

```
dev
  ↑
feature/phase2-home-detail
  ├── memberA: HomeScreen, PodcastDetailScreen, Preview
  ├── memberB: ViewModel, Repository, Formatter, Test
  └── memberC: Firestore data စစ်, EncodingUtil ဆုံးဖြတ်ချက်
```

**နေ့စဉ် ပုံမှန်:**
```bash
git checkout feature/phase2-home-detail
git pull origin feature/phase2-home-detail

# ... အလုပ် လုပ် ...

./gradlew assembleDebug          # push မလုပ်ခင် build စစ် (အမြဲ)
git add .
git commit -m "feat(home): add category filter to HomeViewModel"
git push origin feature/phase2-home-detail
```

> **စည်းကမ်း** — Build မအောင်တဲ့ code ကို **ဘယ်တော့မှ push မလုပ်ပါနဲ့**။
> အဖွဲ့ဝင် ၃ ယောက်လုံး ပိတ်မိသွားမယ်။

---

## Phase 2 ပြီးရင် — Phase 3 (Week 3: Player)

Phase 3 မှာ ဒါတွေ လုပ်မယ်:
- `PlaybackService` (Media3 `MediaSessionService`)
- `PlayerController` singleton
- `MiniPlayer` ကို Scaffold မှာ ချိတ် (Phase 1 က `bottom = 96.dp` နေရာ)
- `FullPlayerScreen` — cover, seek bar, controls
- `PlayerQueue` — episode list ကို queue အဖြစ် ဖွင့်

**Phase 3 အတွက် ကြိုပြင်ထားရမယ့် အရာ:**
- `AndroidManifest.xml` မှာ `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_MEDIA_PLAYBACK` permission
- `<service>` tag နဲ့ `MediaSessionService` intent-filter
- Android 13+ အတွက် `POST_NOTIFICATIONS` runtime permission

> **အကြံပြုချက်** — Phase 2 ပြီးတာနဲ့ Phase 3 အတွက် manifest ကို ကြိုထည့်ထားရင်
> Week 3 ရဲ့ ပထမနေ့ တစ်ဝက် သက်သာမယ်။
