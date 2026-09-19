# Phase 2 — Home + Podcast Detail (Week 2)

> **အရေးကြီး** — ဒါက **plan guide** ပါ။ Code တွေ ဘယ်လို ရေးရမလဲ ဆိုတဲ့ **လမ်းညွှန်** ဖြစ်ပြီး၊
> အတိအကျ copy-paste လုပ်ဖို့ မဟုတ်ပါ။ Phase 1 မှာ တည်ဆောက်ထားတဲ့ အပေါ်မှာပဲ ဆက်တည်ဆောက်ပါ။

---

## Phase 2 — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **ရည်ရွယ်ချက်** | Firebase data ကို Home နဲ့ Detail screen မှာ တကယ် ပြနိုင်ရန် |
| **ကြာချိန်** | ၅ ရက် (Week 2) |
| **အလုပ်ခွဲဝေပုံ** | **အဖွဲ့ဝင် ၃ ယောက်လုံး Database + Model + UI ၃ ခုလုံး လုပ်မယ်** |
| **အဆုံးမှာ ရရမယ့် ရလဒ်** | Home မှာ podcast ၅ ခု ပေါ်မယ်။ တစ်ခုကို နှိပ်ရင် Detail ပွင့်ပြီး episode ၄ ခု ပေါ်မယ်။ Loading / Error / Empty ၃ မျိုးလုံး မှန်ကန်စွာ ပြမယ်။ |

---

## ⭐ အလုပ်ခွဲဝေမှု — Vertical Slice

### ဘာကြောင့် ဒီလို ခွဲတာလဲ

**အရင်နည်း (Layer အလိုက် ခွဲ) — မကောင်းဘူး:**

```
Member A  →  UI အားလုံး           ← Firebase ဘယ်လို အလုပ်လုပ်မှန်း မသိဘူး
Member B  →  Database + Model     ← Compose ဘယ်လို ရေးမှန်း မသိဘူး
Member C  →  Firestore data ပဲ    ← Code ကို လက်နဲ့ မထိရဘူး
```

**အခုနည်း (Feature အလိုက် ခွဲ) — ကောင်းတယ်:**

```
Member A  →  Database → Model → UI     (Podcast စာရင်း)
Member B  →  Database → Model → UI     (Episode အသေးစိတ်)
Member C  →  Database → Model → UI     (Category + ပုံစံ)
```

တစ်ယောက်ချင်းစီ **data က Firestore ကနေ ထွက်ပြီး မျက်လုံးနဲ့ မြင်ရတဲ့ အထိ
လမ်းကြောင်း တစ်ခုလုံး** ကို ကိုယ်တိုင် ရေးရမယ်။ ဒါမှ flow ကို နားလည်မယ်။

---

### Slice ၃ ခု

#### 🅰️ Slice A — Podcast စာရင်း (Member A)

| Layer | လုပ်ရမယ့် အလုပ် | ဖိုင် |
|---|---|---|
| **Database** | `podcasts` collection စစ် + `getPodcasts()` အလုပ်လုပ်မလုပ် အတည်ပြု | `data/repository/PodcastRepository.kt` |
| **Model** | `HomeViewModel` — load, loading/error state, `refresh()` leak fix, Factory | `ui/home/HomeViewModel.kt` |
| **UI** | `HomeScreen` — list ပြ, Loading/Error/Empty ၃ မျိုး, `@Preview` | `ui/home/HomeScreen.kt` |

#### 🅱️ Slice B — Episode အသေးစိတ် (Member B)

| Layer | လုပ်ရမယ့် အလုပ် | ဖိုင် |
|---|---|---|
| **Database** | `episodes` collection စစ် + **composite index** + `getEpisodes()` + `getPodcast()` အသစ် | `data/repository/PodcastRepository.kt` |
| **Model** | `PodcastDetailViewModel` + `PodcastDetailUiState` | `ui/details/PodcastDetailViewModel.kt` |
| **UI** | `PodcastDetailScreen` — cover, title, episode list | `ui/details/PodcastDetailScreen.kt` |

#### 🅲 Slice C — Category နှင့် ပုံစံ (Member C)

| Layer | လုပ်ရမယ့် အလုပ် | ဖိုင် |
|---|---|---|
| **Database** | Firestore မှာ `category` field တွေ **တစ်ပုံစံတည်း** ဖြစ်အောင် သန့်ရှင်းရေး | Firebase Console |
| **Model** | `CategoryFilter.kt` (pure function) + `DurationFormatter` + `DateFormatter` | `ui/home/CategoryFilter.kt`, `domain/util/` |
| **UI** | `CategoryChips` ချိတ် + `EpisodeRow` မှာ ကြာချိန်/ရက်စွဲ ပြ | `ui/components/CategoryChips.kt`, `EpisodeRow.kt` |

---

### ဖိုင် ပိုင်ဆိုင်မှု ဇယား (Merge conflict မဖြစ်အောင်)

| ဖိုင် | ပိုင်ရှင် | မှတ်ချက် |
|---|---|---|
| `ui/home/HomeViewModel.kt` | **A** | C က filter function ကို ခေါ်ဖို့ ၁ ကြောင်း ထည့်မယ် — A ကို အရင်ပြော |
| `ui/home/HomeScreen.kt` | **A** | C က CategoryChips နေရာ ၁ ခု ထည့်မယ် — A ကို အရင်ပြော |
| `ui/home/CategoryFilter.kt` | **C** | ဖိုင်အသစ် — C တစ်ယောက်တည်း |
| `ui/details/*` | **B** | B တစ်ယောက်တည်း |
| `domain/util/*` | **C** | C တစ်ယောက်တည်း |
| `ui/components/CategoryChips.kt`, `EpisodeRow.kt` | **C** | C တစ်ယောက်တည်း |
| `data/repository/PodcastRepository.kt` | **A + B** | ⚠️ **၂ ယောက် ထိတယ်** — အောက်က စည်းကမ်း ဖတ်ပါ |
| `ui/navigation/MyanCastNavHost.kt` | **B** | Day 4 မှာပဲ ထိရမယ် |

> **⚠️ `PodcastRepository.kt` စည်းကမ်း**
>
> A နဲ့ B ၂ ယောက်လုံး ဒီဖိုင်ကို ထိရမယ်။ Conflict မဖြစ်အောင်:
> 1. **Day 1 မနက်** — A က `getPodcasts()` ပိုင်း အရင်ပြီးအောင် လုပ်ပြီး **ချက်ချင်း push**
> 2. **Day 1 နေ့လယ်** — B က `git pull` ဆွဲပြီးမှ `getEpisodes()` / `getPodcast()` ထည့်
> 3. Function **အသစ်တွေကို ဖိုင်ရဲ့ အောက်ဆုံးမှာပဲ** ထည့်ပါ — အလယ်မှာ မထည့်ပါနဲ့

---

## Phase 1 ကနေ ဆက်ယူလာတဲ့ အခြေအနေ

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

# Day 1 — 🗄️ DATABASE Layer (၃ ယောက်လုံး)

> **ဒီနေ့ ရည်မှန်းချက်** — ၃ ယောက်လုံး **data layer** ကို ကိုင်မယ်။
> Firestore က data ဘယ်လို ထွက်လာလဲ၊ Repository က ဘာလုပ်ပေးလဲ နားလည်ရမယ်။

### 🔰 ၃ ယောက်လုံး အတူတူ လုပ်ရမယ့် အရာ (မနက် ၁ နာရီ)

Firebase Console ကို ၃ ယောက်လုံး ဖွင့်ပြီး ဒါတွေ **အတူတူ** စစ်ပါ:

| စစ်ရမယ့် အချက် | ဘယ်လို ဖြစ်ရမလဲ |
|---|---|
| Collection name | `podcasts`, `episodes`, `news` — **အငယ်စာလုံး၊ အများကိန်း** |
| Field name | Firestore field နဲ့ data class property **တစ်လုံးမကျန် တူရမယ်** |
| `episodes.podcastId` | Podcast ရဲ့ **document ID အစစ်** ဖြစ်ရမယ် (title မဟုတ်) |
| `publishedAt` type | `timestamp` ဖြစ်ရမယ် (`string` မဟုတ်) |
| `duration` type | `number` ဖြစ်ရမယ် (စက္ကန့်နဲ့) |
| Security rules | `allow read: if true;` ဖြစ်ရမယ် |

> **ဘာကြောင့် အတူတူ လုပ်ရလဲ** — Phase 2 ရဲ့ ပြဿနာ အများစုက code မှားလို့ မဟုတ်ဘူး၊
> **data မှားလို့** ဖြစ်တယ်။ ၃ ယောက်လုံး data ပုံစံ သိထားရင် နောက်ပိုင်း အချင်းချင်း ကူညီလို့ရမယ်။

---

### 🅰️ Member A — `getPodcasts()` အတည်ပြုခြင်း

**လုပ်ရမယ့် အရာ:**
1. `PodcastRepository.getPodcasts()` က တကယ် data ပြန်ပေးလား စစ်
2. Log ထည့်ပြီး ဘယ်နှစ်ခု ပြန်လာလဲ ကြည့်

**လမ်းညွှန်:**
- ယာယီ `.onEach { Log.d("REPO", "podcasts=${it.size}") }` ထည့်ပြီး Logcat စစ်
- `5` ထွက်ရမယ်။ `0` ထွက်ရင် → collection name (သို့) rules ပြဿနာ
- စစ်ပြီးရင် Log ကို **ပြန်ဖြုတ်ပါ**
- **ပြီးတာနဲ့ ချက်ချင်း push** (B စောင့်နေလို့)

---

### 🅱️ Member B — `getEpisodes()` + `getPodcast()` အသစ်

> **A push လုပ်ပြီးမှ စပါ** — `git pull` အရင် ဆွဲပါ။

**လုပ်ရမယ့် အရာ ၂ ခု:**

**၁။ Composite index ဖန်တီးခြင်း**

`getEpisodes()` က `whereEqualTo("podcastId")` **နဲ့** `orderBy("publishedAt")` ကို
တွဲသုံးတယ်။ Firestore က ဒီလို query အတွက် **composite index** တောင်းတယ်။
ပထမဆုံး run တဲ့အခါ Logcat မှာ ဒီလို ပေါ်လာမယ်:

```
FAILED_PRECONDITION: The query requires an index.
You can create it here: https://console.firebase.google.com/...
```

အဲဒီ **link ကို နှိပ်ပြီး "Create index" နှိပ်လိုက်ရုံပါပဲ**။ ၁–၂ မိနစ် စောင့်ရင် အလုပ်လုပ်မယ်။

> ဒါကို ကြိုမသိရင် တစ်နေကုန် ရှာနေရတတ်တယ်။ **Index ဖန်တီးပြီးကြောင်း အဖွဲ့ကို ပြောပါ** —
> A နဲ့ C လည်း ဒီ error တွေ့မယ်။

**၂။ `getPodcast(id)` function အသစ် ရေးခြင်း**

```
suspend fun getPodcast(id: String): Podcast?
```

**လမ်းညွှန်:**
- `db.collection("podcasts").document(id).get()` သုံး
- `toObjectWithId()` (**Phase 1 မှာ ရေးထားပြီးသား** — `data/firebase/FirestoreExt.kt`) နဲ့ map
- Document မရှိရင် `null` ပြန်ပေး (exception မပစ်နဲ့)
- `.await()` သုံးချင်ရင် `kotlinx-coroutines-play-services` ကို `libs.versions.toml` မှာ ထပ်ထည့်ရမယ်။
  မထည့်ချင်ရင် `addOnSuccessListener` + `suspendCancellableCoroutine` နဲ့လည်း ရတယ်။

---

### 🅲 Member C — Category Data သန့်ရှင်းရေး

**ပြဿနာ** — Firestore မှာ category တွေ ဒီလို ရောနေတတ်တယ်:

```
"သတင်း"      ← မြန်မာလို
"News"        ← အင်္ဂလိပ်လို
"သတင်း "     ← နောက်မှာ space ပါနေ
""            ← အလွတ်
```

ဒါဆို filter လုပ်တဲ့အခါ **ဘယ်တော့မှ မကိုက်ဘူး**။

**လုပ်ရမယ့် အရာ:**
1. Podcast ၅ ခုလုံးရဲ့ `category` ကို Console မှာ တစ်ခုချင်း ဖွင့်ကြည့်
2. **မြန်မာလို တစ်ပုံစံတည်း** ဖြစ်အောင် ပြင် (space မပါ၊ အလွတ် မရှိ)
3. ဘယ် category တွေ ရှိလဲ စာရွက်မှာ ချရေး — Day 2 မှာ လိုမယ်

**မှတ်ချက်** — Phase 1 မှာ ထည့်ထားတဲ့ category တွေက:
`သတင်း`, `နည်းပညာ`, `ဇာတ်လမ်း`, `ကျန်းမာရေး`, `စီးပွားရေး`

> **Zawgyi ဆုံးဖြတ်ချက်လည်း ဒီနေ့ ချပါ** — Phase 1 မှာ **font** ပဲ ပြောင်းထားတယ်၊
> **စာသား ကိုယ်တိုင်** မပြောင်းသေးဘူး။ Course project အတွက် **Unicode ပဲ သုံးပြီး
> Settings က Zawgyi toggle ကို "Phase 5" လို့ မှတ်ထားဖို့ အကြံပြုပါတယ်** (၅ မိနစ်)။
> တကယ် ပြောင်းချင်ရင် Rabbit converter library လိုမယ် (၁ ရက်)။

---

### Day 1 ရဲ့ Deliverable

| ဘယ်သူ | ပြီးရမယ့် အရာ |
|---|---|
| ၃ ယောက်လုံး | Firestore data ၃၅ ခု field name မှန်ကြောင်း စစ်ပြီး |
| A | `getPodcasts()` က ၅ ခု ပြန်ပေးကြောင်း အတည်ပြုပြီး + push ပြီး |
| B | Composite index ဖန်တီးပြီး + `getPodcast()` ရေးပြီး |
| C | Category ၅ မျိုး တစ်ပုံစံတည်း ဖြစ်ပြီး + Zawgyi ဆုံးဖြတ်ချက် ချပြီး |

**📣 နေ့ဆုံး Standup (၁၅ မိနစ်)** — တစ်ယောက်ချင်းစီ
"ငါ့ data က Repository ကနေ ဘယ်လို ထွက်လာလဲ" ကို ၂ မိနစ် ရှင်းပြပါ။

---

# Day 2 — 🧠 MODEL Layer (၃ ယောက်လုံး)

> **ဒီနေ့ ရည်မှန်းချက်** — ၃ ယောက်လုံး **ViewModel / logic layer** ကို ကိုင်မယ်။
> Repository က လာတဲ့ data ကို UI သုံးလို့ရတဲ့ ပုံစံ ဘယ်လို ပြောင်းလဲ နားလည်ရမယ်။

### 🔰 ၃ ယောက်လုံး နားလည်ထားရမယ့် စည်းကမ်း ၃ ခု

| # | စည်းကမ်း | ဘာကြောင့်လဲ |
|---|---|---|
| 1 | **Logic အားလုံး ViewModel မှာ** — Composable ထဲမှာ `filter`, `sortedBy`, `map` မရေးရ | Recomposition တိုင်း ပြန်တွက်နေမယ် → စွမ်းဆောင်ရည် ကျ |
| 2 | **ViewModel ထဲမှာ `android.*` import မရှိရ** | Context ဝင်လာရင် test မလုပ်လို့ရတော့ဘူး |
| 3 | **State က `data class` တစ်ခုတည်း** ဖြစ်ရမယ် | `StateFlow` ၅ ခု သုံးရင် UI က တစ်ခါတည်း update မဖြစ်ဘူး |

---

### 🅰️ Member A — `HomeViewModel` ပြင်ဆင်ခြင်း

**လက်ရှိ code ရဲ့ ချို့ယွင်းချက် ၃ ခု** (ဒါတွေ ဖြည့်ရမယ်):

| # | ပြဿနာ | ဘာကြောင့် အရေးကြီးလဲ |
|---|---|---|
| 1 | `selectCategory()` က state ပဲ ပြောင်းတယ်၊ **list ကို filter မလုပ်ဘူး** | Chip နှိပ်လည်း ဘာမှ မဖြစ် |
| 2 | `categories` က **hard-code** (`"All", "News", "Tech"...`)၊ Firestore က `"သတင်း"` | ဘယ်တော့မှ မကိုက်လို့ list အလွတ် ဖြစ်မယ် |
| 3 | `refresh()` က `loadPodcasts()` ထပ်ခေါ် → **listener ၂ ခု** | Memory leak + data ၂ ခါ ဝင် |

**၁။ State ပုံစံ ပြင်ဆင်ခြင်း**

`HomeUiState` ထဲမှာ list ၂ မျိုး ခွဲထား:

```
allPodcasts     : List<Podcast>   ← Firestore ကလာတဲ့ အကုန်လုံး (မပြောင်း)
podcasts        : List<Podcast>   ← Filter ပြီးသား (UI က ဒါကို ပြ)
categories      : List<String>    ← C ရဲ့ function က တွက်ပေးမယ်
selectedCategory: String          ← Default က "အားလုံး"
```

**၂။ Job ထိန်းချုပ်မှု ထည့်ခြင်း**

- `private var loadJob: Job? = null` ကြေညာ
- `loadPodcasts()` အစမှာ `loadJob?.cancel()` ခေါ်
- `loadJob = viewModelScope.launch { ... }` လို့ သတ်မှတ်

**၃။ ViewModel Factory ထည့်ခြင်း**

လက်ရှိမှာ `HomeViewModel(repo = PodcastRepository())` ဆိုပြီး **default parameter** နဲ့
repository ကို တိုက်ရိုက် ဆောက်နေတယ် → **test မလုပ်လို့ရဘူး**။

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

> **C ရဲ့ function ကို စောင့်ပါ** — filter နဲ့ category တွက်တဲ့ logic က C ရေးမယ်။
> C မပြီးသေးရင် ယာယီ `allPodcasts` ကိုပဲ ပြထားပြီး Day 3 မှာ ချိတ်ပါ။

---

### 🅱️ Member B — `PodcastDetailViewModel` ရေးခြင်း

**State ပုံစံ:**
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

**Flow ၂ ခု ပေါင်းနည်း:**

Podcast အချက်အလက် (one-shot) နဲ့ Episode list (real-time flow) ကို တစ်ချိန်တည်း load ရမယ်။

- `viewModelScope.launch { }` တစ်ခုထဲမှာ
- အရင် `getPodcast(id)` ကို `suspend` ခေါ်ပြီး state ထဲ ထည့်
- ပြီးမှ `getEpisodes(id).collect { }` နဲ့ episode တွေ စောင့်
- Error ကို `runCatching { }` (သို့) `try/catch` နဲ့ ဖမ်း
- Podcast က `null` ပြန်လာရင် `error = "Podcast ရှာမတွေ့ပါ"` သတ်မှတ်

---

### 🅲 Member C — Pure Function ၃ ခု ရေးခြင်း

> **C ရဲ့ အလုပ်က အရေးအကြီးဆုံး** — ဒီ function ၃ ခုက A နဲ့ B ၂ ယောက်လုံး သုံးမယ်။
> **Test အရင်ရေးပြီးမှ** function ရေးတာ အကောင်းဆုံး။

**၁။ `ui/home/CategoryFilter.kt` (ဖိုင်အသစ်)**

Function ၂ ခု — `class` မလိုဘူး၊ **pure function** ပဲ:

```
fun buildCategories(podcasts: List<Podcast>): List<String>
fun filterByCategory(podcasts: List<Podcast>, category: String): List<Podcast>
```

**လမ်းညွှန်:**
```
buildCategories:
    listOf("အားလုံး") + podcasts.map { it.category }
                                 .filter { it.isNotBlank() }
                                 .distinct()
                                 .sorted()

filterByCategory:
    if (category == "အားလုံး") podcasts
    else podcasts.filter { it.category == category }
```

> **"အားလုံး" ဆိုတဲ့ စာသားကို `const val` တစ်ခု လုပ်ပါ** — A ရော C ရော သုံးမယ်။
> Hard-code ၂ နေရာ ရေးရင် တစ်နေရာ ပြင်ဖို့ မေ့သွားမယ်။

**၂။ `domain/util/DurationFormatter.kt`**

| Input | Output |
|---|---|
| `1470` | `"24:30"` |
| `3725` | `"1:02:05"` |
| `45` | `"0:45"` |
| `0` | `"0:00"` |
| `-5` | `"0:00"` (crash မဖြစ်ရ) |

- `seconds / 3600` → နာရီ၊ `(seconds % 3600) / 60` → မိနစ်၊ `seconds % 60` → စက္ကန့်
- နာရီ ၀ ဖြစ်ရင် နာရီ မပြနဲ့
- မိနစ်/စက္ကန့်ကို `padStart(2, '0')` နဲ့ ၂ လုံး ဖြစ်အောင် ထား

Function ၂ ခု:
```
fun formatDuration(seconds: Int): String        // "24:30"      — progress bar အတွက်
fun formatDurationLabel(seconds: Int): String   // "၂၄ မိနစ်"   — list row အတွက်
```

**၃။ `domain/util/DateFormatter.kt`**

| Input | Output |
|---|---|
| ၃၀ စက္ကန့် အရင်က | `"ခုလေးတင်"` |
| ၂ နာရီ အရင်က | `"၂ နာရီ အရင်က"` |
| မနေ့က | `"မနေ့က"` |
| ၅ ရက် အရင်က | `"၅ ရက် အရင်က"` |
| ၁ လ ကျော် | `"၁၅ စက်တင်ဘာ ၂၀၂၅"` |

- `Timestamp?` ကို လက်ခံပါ — `null` ဖြစ်ရင် `""` ပြန်ပေး (crash မဖြစ်အောင်)
- `timestamp.toDate().time` နဲ့ `System.currentTimeMillis()` ကို နုတ်
- `< 1 မိနစ်` → `"ခုလေးတင်"`, `< 1 နာရီ` → မိနစ်, `< 24 နာရီ` → နာရီ, `< 30 ရက်` → ရက်
- ကျန်ရင် `SimpleDateFormat("d MMMM yyyy", Locale("my"))`

---

### Day 2 ရဲ့ Deliverable

| ဘယ်သူ | ပြီးရမယ့် အရာ |
|---|---|
| A | `HomeViewModel` — Job fix + Factory + state ပုံစံ အသစ် |
| B | `PodcastDetailViewModel` + `PodcastDetailUiState` |
| C | `CategoryFilter` + `DurationFormatter` + `DateFormatter` (+ unit test) |

**📣 နေ့ဆုံး Standup** — တစ်ယောက်ချင်းစီ
"Repository က data ကို ငါ ဘယ်လို ပုံစံ ပြောင်းလိုက်လဲ" ကို ၂ မိနစ် ရှင်းပြပါ။

---

# Day 3 — 🎨 UI Layer (၃ ယောက်လုံး)

> **ဒီနေ့ ရည်မှန်းချက်** — ၃ ယောက်လုံး **Compose UI** ကို ကိုင်မယ်။
> ViewModel က state ကို မျက်လုံးနဲ့ မြင်ရတဲ့ အထိ ဘယ်လို ပြောင်းလဲ နားလည်ရမယ်။

### 🔰 ၃ ယောက်လုံး နားလည်ထားရမယ့် စည်းကမ်း ၃ ခု

| # | စည်းကမ်း | ဘာကြောင့်လဲ |
|---|---|---|
| 1 | `collectAsStateWithLifecycle()` သုံး (`collectAsState()` မဟုတ်) | App background ရောက်ရင် Firestore listener ရပ်စေဖို့ |
| 2 | `Modifier` က composable ရဲ့ **ပထမဆုံး optional parameter** | Compose ရဲ့ စံနှုန်း — မဟုတ်ရင် ဖတ်ရ ခက် |
| 3 | `!!` **လုံးဝ မသုံးရ** | Race condition မှာ crash ဖြစ်မယ် |

---

### 🅰️ Member A — `HomeScreen` အပြည့်အစုံ

**လက်ရှိ code ရဲ့ ချို့ယွင်းချက် ၄ ခု:**

| # | ပြဿနာ | ဖြေရှင်းနည်း |
|---|---|---|
| 1 | `LoadingView` / `ErrorView` မှာ **`padding` မသုံးထားဘူး** → TopAppBar အောက် ဝင်နေမယ် | `Modifier.padding(padding)` ထည့် |
| 2 | `state.error!!` — **`!!` သုံးထားတယ်** | `state.error?.let { }` သုံး |
| 3 | **Empty state မရှိဘူး** — data မရှိရင် အဖြူ screen | `EmptyView` ထည့် |
| 4 | `SectionHeader(onSeeAll = { /* TODO */ })` — နှိပ်လို့ရပေမဲ့ ဘာမှ မဖြစ် | Search ကို ပို့ (သို့) ဖျောက် |

**State handling ပုံစံ — `when` ရဲ့ အစီအစဉ်က အရေးကြီးတယ်:**

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

**`@Preview` ထည့်ခြင်း:**
- `HomeContent` (private) ကို `@Preview` နဲ့ fake data ပေးပြီး စမ်း
- `HomeScreen` (public) ကို preview **မလုပ်ပါနဲ့** — ViewModel လိုလို့ crash ဖြစ်မယ်
- `MyanCastTheme { }` နဲ့ ပတ်ပါ၊ မဟုတ်ရင် font/color မမှန်ဘူး
- `@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)` သုံးပါ

> **MiniPlayer နေရာ မဖျက်ပါနဲ့** — `contentPadding = PaddingValues(bottom = 96.dp)` က
> Phase 3 မှာ ထည့်မယ့် MiniPlayer အတွက် ကြိုချန်ထားတာ။

---

### 🅱️ Member B — `PodcastDetailScreen`

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
│  │ EpisodeRow             │  │  ← C ရဲ့ component (ပြင်ပြီးသား)
│  │ EpisodeRow             │  │
│  └────────────────────────┘  │
└──────────────────────────────┘
```

**လမ်းညွှန်:**
- `LazyColumn` သုံး — header ကို `item { }`, episode တွေကို `items(state.episodes) { }`
- `EpisodeRow` က Phase 1 မှာ ရှိပြီးသား — **အသစ် မရေးပါနဲ့** (C က ပြင်နေတယ်)
- Episode နှိပ်ရင် Phase 3 အထိ ဘာမှ မလုပ်သေးဘူး — `onEpisodeClick` ကို ချန်ထား
- "သိမ်း" ခလုတ်က Phase 4 (Room) အထိ မအလုပ်လုပ်သေးဘူး — UI ပဲ ဆောက်ထား
- Episode မရှိရင် `EmptyView("ပိုင်း မရှိသေးပါ")` ပြ
- A နဲ့ အတူတူ — Loading / Error / Empty ၃ မျိုး ထည့်ရမယ်

---

### 🅲 Member C — `CategoryChips` + `EpisodeRow` ချိတ်ခြင်း

**၁။ `CategoryChips` ကို `HomeScreen` မှာ ချိတ်**

> **A ကို အရင်ပြောပါ** — `HomeScreen.kt` က A ပိုင်တဲ့ ဖိုင်။
> A push လုပ်ပြီးမှ pull ဆွဲပြီး ထည့်ပါ။

- Day 2 မှာ ရေးထားတဲ့ `buildCategories()` / `filterByCategory()` ကို
  `HomeViewModel` မှာ ချိတ် (A နဲ့ အတူတူ ထိုင်ပြီး လုပ်ပါ — ၁၅ မိနစ် လောက်ပဲ)
- Chip နှိပ်ရင် list တကယ် ပြောင်းလား စစ်

**၂။ `EpisodeRow` မှာ formatter ချိတ်**

- `formatDurationLabel(episode.duration)` → "၂၄ မိနစ်" ပြ
- `formatRelativeDate(episode.publishedAt)` → "၂ ရက် အရင်က" ပြ
- `duration` က `0` ဖြစ်ရင် ကြာချိန် **လုံးဝ မပြနဲ့** (— ဆိုပြီး မပြနဲ့)

**၃။ Empty/Error component ကို padding လက်ခံအောင် ပြင်**

`EmptyView`, `ErrorView`, `LoadingView` ၃ ခုလုံး `modifier: Modifier = Modifier`
parameter ရှိပြီးသား — A နဲ့ B က `Modifier.padding(padding)` ပေးလို့ရကြောင်း အတည်ပြုပါ။

---

### Day 3 ရဲ့ Deliverable

| ဘယ်သူ | ပြီးရမယ့် အရာ |
|---|---|
| A | Home မှာ podcast ၅ ခု ပေါ် + state ၃ မျိုး မှန် + `@Preview` |
| B | Detail မှာ cover/title/episode list ပေါ် |
| C | Chip နှိပ်ရင် list ပြောင်း + ကြာချိန်/ရက်စွဲ ပေါ် |

**📣 နေ့ဆုံး Standup** — တစ်ယောက်ချင်းစီ
**ဖုန်းပြပြီး** "ငါ့ဟာ ဒီလို ပေါ်တယ်" ဆိုပြီး ၂ မိနစ် ပြပါ။

---

# Day 4 — 🔗 INTEGRATION (၃ ယောက်လုံး အတူတူ)

> **ဒီနေ့ ရည်မှန်းချက်** — Slice ၃ ခုကို ပေါင်းစည်းပြီး **အစအဆုံး လမ်းကြောင်း** စမ်း။
> ဒီနေ့က ၃ ယောက်လုံး **တစ်နေရာတည်းမှာ ထိုင်ပြီး** လုပ်ပါ (သို့) video call ဖွင့်ထားပါ။

### Step 1: Merge လုပ်ခြင်း (မနက် ၁ နာရီ)

```bash
# ၃ ယောက်လုံး ကိုယ့် branch ကို push ပြီးပြီလား စစ်
git checkout dev
git pull origin dev
git merge feature/phase2-slice-a      # A အရင်
./gradlew assembleDebug               # build စစ်
git merge feature/phase2-slice-c      # ပြီးမှ C (A ရဲ့ ဖိုင်တွေ ထိလို့)
./gradlew assembleDebug
git merge feature/phase2-slice-b      # နောက်ဆုံး B
./gradlew assembleDebug
```

> **အစီအစဉ် A → C → B က အရေးကြီးတယ်** — C က A ရဲ့ ဖိုင်တွေ ထိထားလို့။
> Conflict ဖြစ်ရင် **၃ ယောက်လုံး အတူတူ ဖြေရှင်းပါ** — တစ်ယောက်တည်း မဖြေရှင်းပါနဲ့။

### Step 2: Navigation ချိတ်ခြင်း (Member B ဦးဆောင်)

`MyanCastNavHost.kt` မှာ `PodcastDetailScreen` ရဲ့ placeholder ကို
တကယ့် screen နဲ့ အစားထိုးပါ။

> **Route နဲ့ argument က Phase 1 မှာ ရေးပြီးသား** — `Screen.PodcastDetail.create(id)`
> ကို ပဲ ဆက်သုံးပါ။ Route string အသစ် **မရေးပါနဲ့**။

### Step 3: အစအဆုံး လမ်းကြောင်း စမ်းခြင်း (၃ ယောက်လုံး)

ဒီ လမ်းကြောင်းကို **တစ်ယောက်ချင်းစီ ကိုယ်တိုင် လိုက်ပြီး** ရှင်းပြပါ:

```
Firestore "podcasts" collection
        ↓  (Member A ရေးတဲ့ layer)
PodcastRepository.getPodcasts()  →  Flow<List<Podcast>>
        ↓  (Member A ရေးတဲ့ layer)
HomeViewModel  →  HomeUiState
        ↓  (Member C ရေးတဲ့ layer)
filterByCategory()  →  filtered list
        ↓  (Member A ရေးတဲ့ layer)
HomeScreen  →  PodcastListItem
        ↓  (နှိပ်လိုက်တယ်)
Screen.PodcastDetail.create(id)  →  navigate
        ↓  (Member B ရေးတဲ့ layer)
PodcastDetailViewModel  →  getPodcast() + getEpisodes()
        ↓  (Member B ရေးတဲ့ layer)
PodcastDetailScreen  →  EpisodeRow
        ↓  (Member C ရေးတဲ့ layer)
formatDurationLabel()  →  "၂၄ မိနစ်"
```

> **ဒါက Phase 2 ရဲ့ အဓိက ရည်ရွယ်ချက်ပါ** — ၃ ယောက်လုံး ဒီ လမ်းကြောင်း တစ်ခုလုံးကို
> ရှင်းပြနိုင်ရမယ်။ မရှင်းပြနိုင်သေးရင် ရေးထားတဲ့ code ကို ပြန်ဖတ်ပါ။

### Step 4: Edge Case စစ်ဆေးခြင်း

| စမ်းရမယ့် အခြေအနေ | မျှော်လင့်ရမယ့် ရလဒ် | ဘယ်သူ စမ်းမလဲ |
|---|---|---|
| Internet ဖြုတ်ပြီး app ဖွင့် | Error view + retry ခလုတ် | A |
| Internet ပြန်ဖွင့်ပြီး retry နှိပ် | Data ပြန်ရ | A |
| Podcast မရှိတဲ့ ID နဲ့ Detail ဖွင့် | "ရှာမတွေ့ပါ" (crash မဖြစ်ရ) | B |
| Episode မရှိတဲ့ podcast | "ပိုင်း မရှိသေးပါ" empty view | B |
| Category chip အကုန် တစ်ခုချင်း နှိပ် | List မှန်မှန် ပြောင်း | C |
| `duration` က `0` ဖြစ်တဲ့ episode | ကြာချိန် မပေါ်ရ (crash မဖြစ်ရ) | C |
| Screen လှည့် (rotate) | State မပျောက်ရ | ၃ ယောက်လုံး |
| Detail ကနေ back မြန်မြန် ၅ ချက် နှိပ် | Crash မဖြစ်ရ | ၃ ယောက်လုံး |
| Firestore data ကို Console က ပြင် | App မှာ **ချက်ချင်း ပြောင်း** (real-time) | ၃ ယောက်လုံး |
| Settings မှာ Dark → Light ပြောင်း | Screen အားလုံး ပြောင်း၊ စာ ဖတ်လို့ရနေရမယ် | ၃ ယောက်လုံး |

### Day 4 ရဲ့ Deliverable

- ✅ Slice ၃ ခုလုံး `dev` မှာ merge ပြီး build အောင်
- ✅ Home → Detail → Back လမ်းကြောင်း အလုပ်လုပ်
- ✅ Edge case ၁၀ ခုလုံး စစ်ပြီး
- ✅ ၃ ယောက်လုံး data flow ကို ရှင်းပြနိုင်ပြီ

---

# Day 5 — 🔄 CROSS-REVIEW + TEST

> **ဒီနေ့ ရည်မှန်းချက်** — **ကိုယ်မရေးခဲ့တဲ့ code** ကို ဖတ်ပြီး နားလည်အောင် လုပ်။
> ဒါမှ တစ်ယောက်ယောက် မလာနိုင်ရင် ကျန်တဲ့သူတွေ ဆက်လုပ်လို့ရမယ်။

### Step 1: Cross-Review (မနက် ၂ နာရီ)

**လှည့်ပြီး review လုပ်ပါ:**

```
A  →  B ရဲ့ code ကို review
B  →  C ရဲ့ code ကို review
C  →  A ရဲ့ code ကို review
```

**Review လုပ်တဲ့အခါ ဒါတွေ စစ်ပါ:**

- [ ] `!!` (not-null assertion) တစ်ခုမှ မကျန်ဘူးလား
- [ ] Composable ထဲမှာ `filter`, `sortedBy`, `map` မရှိဘူးလား
- [ ] `Modifier` က composable ရဲ့ **ပထမဆုံး optional parameter** လား
- [ ] ViewModel ထဲမှာ `android.*` import မရှိဘူးလား
- [ ] Flow collect က `collectAsStateWithLifecycle()` လား
- [ ] Route string ကို `Screen.kt` ကနေပဲ ယူလား (hard-code မရှိဘူးလား)
- [ ] Build warning ဘယ်နှစ်ခု ရှိလဲ — ၀ ဖြစ်အောင် လုပ်

> **Review ရဲ့ အဓိက မေးခွန်း** — "ဒီ code က ဘာလုပ်တာလဲ **ငါ ရှင်းပြနိုင်လား**?"
> မရှင်းပြနိုင်ရင် ရေးတဲ့သူကို မေးပါ။ ဒါက အရှက်ရစရာ မဟုတ်ဘူး — ဒါက ရည်ရွယ်ချက်ပါ။

### Step 2: Unit Test ရေးခြင်း (၃ ယောက်လုံး — နေ့လယ် ၂ နာရီ)

Phase 1 မှာ `junit` နဲ့ `kotlinx-coroutines-test` ထည့်ပြီးသား —
`app/src/test/java/` ထဲမှာ ရေးပါ။ **တစ်ယောက်ချင်းစီ ၂ ခု စီ** ရေးပါ:

| ဘယ်သူ | Test | ဘာစစ်မလဲ |
|---|---|---|
| **A** | `HomeViewModel` loading state | စတင်ချိန်မှာ `isLoading = true` |
| **A** | `HomeViewModel` data ဝင်ပြီး | `isLoading = false` + podcast ၅ ခု |
| **B** | `PodcastDetailViewModel` load | Podcast + episode ၂ ခုလုံး ဝင်လာ |
| **B** | Podcast မရှိရင် | `error != null` ဖြစ်ရမယ် |
| **C** | `formatDuration(1470)` | `"24:30"` ဖြစ်ရမယ် |
| **C** | `formatDuration(0)` နဲ့ `(-5)` | `"0:00"` ဖြစ်ရမယ် (crash မဖြစ်ရ) |

**Repository ကို fake လုပ်နည်း:**

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
>
> **ဒီ refactor ကို ၃ ယောက်လုံး အတူတူ လုပ်ပါ** — ၃ ယောက်လုံးရဲ့ code ကို ထိလို့။

### Step 3: နောက်ဆုံး Merge

```bash
git checkout dev
git pull origin dev
./gradlew assembleDebug     # build စစ်
./gradlew testDebugUnitTest # test စစ်
git push origin dev
```

### Day 5 ရဲ့ Deliverable

- ✅ ၃ ယောက်လုံး သူများရဲ့ code ကို review လုပ်ပြီး
- ✅ Unit test ၆ ခု ရေးပြီး၊ အားလုံး pass
- ✅ `dev` branch ကို merge ပြီး

---

## Phase 2 — Final Checklist (တစ်ယောက်ချင်းစီ)

### 🅰️ Member A — Podcast စာရင်း

| # | Layer | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|---|
| 1 | DB | `getPodcasts()` က ၅ ခု ပြန်ပေး | ☐ |
| 2 | Model | `refresh()` က listener မပွား | ☐ |
| 3 | Model | ViewModel Factory ထည့်ပြီး | ☐ |
| 4 | Model | State က `allPodcasts` / `podcasts` ခွဲထား | ☐ |
| 5 | UI | Home မှာ podcast ၅ ခု ပေါ် | ☐ |
| 6 | UI | Loading / Error / Empty ၃ မျိုးလုံး မှန် | ☐ |
| 7 | UI | `@Preview` အလုပ်လုပ် | ☐ |
| 8 | Test | ViewModel test ၂ ခု pass | ☐ |

### 🅱️ Member B — Episode အသေးစိတ်

| # | Layer | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|---|
| 1 | DB | Composite index ဖန်တီးပြီး | ☐ |
| 2 | DB | `getPodcast(id)` ရေးပြီး (`null` ကို ကိုင်တွယ်) | ☐ |
| 3 | DB | `getEpisodes()` က ၄ ခု ပြန်ပေး | ☐ |
| 4 | Model | `PodcastDetailViewModel` ရေးပြီး | ☐ |
| 5 | Model | Flow ၂ ခု ပေါင်းပြီး | ☐ |
| 6 | UI | `PodcastDetailScreen` ရေးပြီး | ☐ |
| 7 | UI | Episode ၄ ခု ပေါ် + Back မှန် | ☐ |
| 8 | Test | ViewModel test ၂ ခု pass | ☐ |

### 🅲 Member C — Category နှင့် ပုံစံ

| # | Layer | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|---|
| 1 | DB | Category ၅ မျိုး တစ်ပုံစံတည်း ဖြစ်ပြီး | ☐ |
| 2 | DB | Zawgyi ဆုံးဖြတ်ချက် ချပြီး | ☐ |
| 3 | Model | `CategoryFilter.kt` ရေးပြီး | ☐ |
| 4 | Model | `DurationFormatter` ရေးပြီး | ☐ |
| 5 | Model | `DateFormatter` ရေးပြီး | ☐ |
| 6 | UI | Chip နှိပ်ရင် list ပြောင်း | ☐ |
| 7 | UI | `EpisodeRow` မှာ ကြာချိန်/ရက်စွဲ ပေါ် | ☐ |
| 8 | Test | Formatter test ၂ ခု pass | ☐ |

### 🤝 ၃ ယောက်လုံး

| # | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|
| 1 | Edge case ၁၀ ခုလုံး စစ်ပြီး | ☐ |
| 2 | Cross-review ပြီး | ☐ |
| 3 | `!!` တစ်ခုမှ မကျန် | ☐ |
| 4 | Build warning ၀ ခု | ☐ |
| 5 | **Data flow ကို ၃ ယောက်လုံး ရှင်းပြနိုင်** | ☐ |
| 6 | `dev` branch ကို merge ပြီး | ☐ |

---

## Phase 2 — ဖြစ်နိုင်တဲ့ ပြဿနာများနဲ့ ဖြေရှင်းနည်း

| ပြဿနာ | ဘယ် layer လဲ | ဖြေရှင်းနည်း |
|---|---|---|
| Home မှာ podcast မပေါ် | DB | Logcat စစ် → `PERMISSION_DENIED` ဆိုရင် rules; အလွတ်ဆိုရင် collection name |
| Episode မပေါ် (podcast တော့ပေါ်) | DB | ၁) Composite index ဖန်တီးပြီးလား ၂) `podcastId` က document ID အစစ်လား |
| Detail ဖွင့်ရင် ချက်ချင်း crash | Model | `podcastId` က `""` ဖြစ်နေလား — Logcat မှာ route string စစ် |
| Cover image မပေါ် | UI | ၁) `INTERNET` permission ၂) `coverUrl` က `https://` နဲ့ စလား |
| Category chip နှိပ်လည်း ဘာမှ မဖြစ် | Model | Filter logic က ViewModel ထဲ ရောက်ပြီလား |
| Category chip မပေါ်ဘူး | DB | Firestore မှာ `category` field အလွတ် ဖြစ်နေလား |
| Rotate ရင် data ပြန်ဆွဲ | Model | `viewModel()` က Screen ထဲမှာလား၊ `remember` နဲ့ ဆောက်နေလား စစ် |
| `duration` က `0` ပဲ ပြ | DB | Firestore မှာ `number` type လား `string` type လား စစ် |
| Timestamp `null` | DB | Firestore မှာ `timestamp` type နဲ့ ထည့်ထားလား စစ် |
| Myanmar စာလုံး ကျဉ်းနေ/ထပ်နေ | UI | `lineHeight` က `fontSize` ရဲ့ ၁.၈ ဆ ရှိလား စစ် |

---

## Phase 2 — Git Workflow

```
dev
  ↑
  ├── feature/phase2-slice-a   (A: Podcast စာရင်း — DB + Model + UI)
  ├── feature/phase2-slice-b   (B: Episode အသေးစိတ် — DB + Model + UI)
  └── feature/phase2-slice-c   (C: Category + ပုံစံ — DB + Model + UI)
```

**နေ့စဉ် ပုံမှန်:**
```bash
git checkout feature/phase2-slice-a      # ကိုယ့် slice
git pull origin dev                       # dev က အပြောင်းအလဲ ဆွဲ

# ... အလုပ် လုပ် ...

./gradlew assembleDebug                   # push မလုပ်ခင် build စစ် (အမြဲ)
git add .
git commit -m "feat(home): add category filter to HomeViewModel"
git push origin feature/phase2-slice-a
```

**စည်းကမ်း ၃ ခု:**

1. **Build မအောင်တဲ့ code ကို ဘယ်တော့မှ push မလုပ်ပါနဲ့** — ၃ ယောက်လုံး ပိတ်မိသွားမယ်
2. **သူများပိုင်တဲ့ ဖိုင် ထိမယ်ဆိုရင် အရင်ပြောပါ** — အထက်က ပိုင်ဆိုင်မှု ဇယား ကြည့်
3. **နေ့တိုင်း အနည်းဆုံး ၁ ခါ push လုပ်ပါ** — ၃ ရက်စာ စုပြီး push ရင် conflict ကြီးမယ်

---

## Phase 2 ပြီးရင် — Phase 3 (Week 3: Player)

Phase 3 မှာလည်း **vertical slice** ပုံစံ ဆက်သုံးမယ်:

| Member | Slice | DB | Model | UI |
|---|---|---|---|---|
| A | Mini Player | — | `PlayerState` | `MiniPlayer` composable |
| B | Playback Engine | `PlaybackService` | `PlayerController` | — (service ပဲ) |
| C | Full Player | `PlayerQueue` | `PlayerViewModel` | `FullPlayerScreen` |

> B ရဲ့ slice မှာ UI မပါဘူး — အစားထိုးအနေနဲ့ B က notification layout နဲ့
> Day 4 integration ကို ဦးဆောင်ရမယ်။

**Phase 3 အတွက် ကြိုပြင်ထားရမယ့် အရာ:**
- `AndroidManifest.xml` မှာ `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_MEDIA_PLAYBACK` permission
- `<service>` tag နဲ့ `MediaSessionService` intent-filter
- Android 13+ အတွက် `POST_NOTIFICATIONS` runtime permission

> **အကြံပြုချက်** — Phase 2 ပြီးတာနဲ့ Phase 3 အတွက် manifest ကို ကြိုထည့်ထားရင်
> Week 3 ရဲ့ ပထမနေ့ တစ်ဝက် သက်သာမယ်။
