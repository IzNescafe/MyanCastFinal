# Member B — Slice B: Episode အသေးစိတ် (Week 2)

> **ဒါက မင်းရဲ့ တစ်ပတ်စာ အလုပ် အပြည့်အစုံ**။
> Database → Model → UI ၃ ခုလုံး မင်း ကိုယ်တိုင် ရေးရမယ်။

---

## 📋 Slice B — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **တာဝန်** | Podcast တစ်ခုကို နှိပ်ရင် အသေးစိတ် + episode စာရင်း ပြရန် |
| **အခက်ခဲဆုံး အပိုင်း** | One-shot call နဲ့ real-time Flow ၂ ခုကို ပေါင်းခြင်း (Day 2) |
| **အန္တရာယ် အကြီးဆုံး** | Firestore composite index (Day 1) — မသိရင် တစ်နေကုန် နစ်မယ် |
| **အပို တာဝန်** | Day 4 integration ကို **မင်း ဦးဆောင်ရမယ်** |

### မင်း ပိုင်တဲ့ ဖိုင်များ

| ဖိုင် | လက်ရှိ အခြေအနေ | ဘာလုပ်ရမလဲ |
|---|---|---|
| `data/repository/PodcastRepository.kt` | ⚠️ A နဲ့ မျှသုံး | `getPodcast()` ထပ်ထည့် |
| `ui/details/PodcastDetailViewModel.kt` | 🔴 **အလွတ်** (၁ ကြောင်း) | အစအဆုံး ရေး |
| `ui/details/PodcastDetailScreen.kt` | 🟡 Placeholder ၁၈ ကြောင်း | အစားထိုး ရေး |
| `ui/navigation/MyanCastNavHost.kt` | 🟢 ရှိပြီး | Day 4 မှာ ၁ နေရာ ပြင် |

### မင့်ကို မှီခိုနေတဲ့ သူများ

```
Member C  →  formatDurationLabel(), formatRelativeDate()
             မင်းရဲ့ EpisodeRow မှာ ဒါတွေ သုံးမယ် (C က ပြင်မယ်)

Member A  →  Home ကနေ မင့် screen ကို navigate လုပ်မယ်
             Screen.PodcastDetail.create(id) — route က ပြီးသား

Firebase  →  မင်း index မဆောက်ရင် A ရော C ရော error တွေ့မယ်
             → Day 1 မှာ အမြန်ဆုံး လုပ်ပြီး အဖွဲ့ကို ပြော
```

---

## 🚦 Day 0 — စမလုပ်ခင် (၃၀ မိနစ်)

### ၁။ Firebase Console access ရယူ

မင်း **Editor** role လိုတယ် — index ဆောက်ဖို့။ Owner (MyoThura86) ကို ဒီလို လုပ်ခိုင်း:

```
Firebase Console → ⚙️ Project settings
                 → Users and permissions
                 → Add member → မင့် Google account → Editor
```

Project: **`myancast-personal`**

### ၂။ လက်ရှိ code ကို ဖတ်

ဒီ ၄ ဖိုင်ကို **အရင်ဖတ်ပါ** — မဖတ်ဘဲ မစပါနဲ့:

```bash
git pull origin dev
```

| ဖိုင် | ဘာကြောင့် ဖတ်ရမလဲ |
|---|---|
| `data/firebase/FirestoreExt.kt` | `toObjectWithId()` က မင်း Day 1 မှာ သုံးမယ် |
| `data/repository/PodcastRepository.kt` | မင်း ဒီထဲ function ထပ်ထည့်ရမယ် |
| `domain/model/Episode.kt` + `Podcast.kt` | Field တွေ ဘာရှိလဲ သိထားရမယ် |
| `ui/components/EpisodeRow.kt` | မင်း ဒီ component ကို သုံးမယ် (**မပြင်နဲ့** — C ပိုင်တယ်) |

### ၃။ Branch ဆောက်

```bash
git checkout dev
git pull origin dev
git checkout -b feature/phase2-slice-b
```

---

# 🗄️ Day 1 — DATABASE Layer

**ရည်မှန်းချက်** — Firestore ကနေ episode တွေ တကယ် ထွက်လာအောင် လုပ်

---

## Step 1: Composite Index ကို **ကြိုဆောက်ထား** (၁၅ မိနစ်)

### ဘာကြောင့် လိုလဲ

မင်းရဲ့ query က ဒီလို:

```kotlin
db.collection("episodes")
    .whereEqualTo("podcastId", podcastId)      // ← filter
    .orderBy("publishedAt", Query.Direction.DESCENDING)  // ← sort
```

Firestore က **filter တစ်ခု + sort တစ်ခု (field မတူ)** ဆိုရင် composite index တောင်းတယ်။
မရှိရင် query က **crash မဖြစ်ဘူး၊ error ပဲ ပြန်လာတယ်** — ဒါကြောင့် ရှာရခက်တယ်။

### နည်းလမ်း ၂ မျိုး

**A. Error ကို စောင့်ပြီး link နှိပ် (ရိုးရှင်း)**

App run → Detail ဖွင့် → Logcat မှာ ဒါ ပေါ်လာမယ်:

```
FAILED_PRECONDITION: The query requires an index.
You can create it here: https://console.firebase.google.com/...
```

Link နှိပ် → **Create index** → ၁–၂ မိနစ် စောင့်။

**B. ကြိုဆောက် (အကြံပြု — အချိန် မကုန်ဘူး)**

```
Firebase Console → Firestore Database → Indexes tab → Composite
→ Create index

Collection ID : episodes
Field 1       : podcastId    → Ascending
Field 2       : publishedAt  → Descending
Query scope   : Collection

→ Create
```

Status က `Building` → `Enabled` ဖြစ်ရင် ပြီးပြီ။

> **📣 ပြီးတာနဲ့ အဖွဲ့ကို ပြောပါ** — "index ဆောက်ပြီးပြီ" လို့။
> A နဲ့ C လည်း ဒီ error တွေ့နိုင်တယ်။

---

## Step 2: ⚠️ တိတ်တဆိတ် ပျောက်နေတဲ့ Episode ပြဿနာ

### ဒါက Firestore ရဲ့ အန္တရာယ်အရှိဆုံး အပြုအမူ

```
orderBy("publishedAt") သုံးရင် —
publishedAt field မပါတဲ့ document တွေဟာ ရလဒ်ထဲက လုံးဝ ပျောက်သွားမယ်။
Error မပြဘူး။ Warning မပြဘူး။ တိတ်တဆိတ် ပျောက်တာ။
```

**စမ်းကြည့်ရမယ့် အရာ:**

1. Firebase Console → `episodes` collection
2. Document ၂၀ ခုလုံး ဖွင့်ပြီး `publishedAt` **ရှိမရှိ** တစ်ခုချင်း စစ်
3. Type က `timestamp` ဟုတ်မဟုတ် စစ် (`string` ဆိုရင် မရဘူး)

**ဥပမာ** — episode ၄ ခု ထည့်ထားပေမဲ့ ၃ ခုပဲ ပေါ်ရင် → တစ်ခုမှာ `publishedAt` မပါဘူး။

> **Senior tip** — Development အဆင့်မှာ `orderBy` ကို ယာယီ ဖြုတ်ပြီး စမ်းပါ။
> ဖြုတ်တာနဲ့ ၄ ခု ပေါ်လာရင် → ပြဿနာက index မဟုတ်ဘူး၊ **data** ပဲ။
> ဒီနည်းက ၁ မိနစ်နဲ့ ပြဿနာ ၂ မျိုးကို ခွဲခြားပေးတယ်။

---

## Step 3: `getPodcast(id)` ရေးခြင်း

### အရင်ဆုံး — A ကို စောင့်ပါ

```bash
# A က getPodcasts() ပိုင်း ပြီးပြီလား မေး → ပြီးရင်
git pull origin dev
```

> **Conflict မဖြစ်အောင်** — function အသစ်ကို **ဖိုင်ရဲ့ အောက်ဆုံး**၊
> `private companion object` ရဲ့ **အပေါ်မှာ** ထည့်ပါ။ အလယ်မှာ မထည့်ပါနဲ့။

### Dependency ဆုံးဖြတ်ချက်

`document(id).get()` က `Task<DocumentSnapshot>` ပြန်ပေးတယ် — coroutine မဟုတ်ဘူး။
`suspend` function အဖြစ် သုံးဖို့ နည်း ၃ မျိုး:

| နည်းလမ်း | Dependency | ခက်ခဲမှု | အကြံပြုချက် |
|---|---|---|---|
| **၁။ `.await()`** | `kotlinx-coroutines-play-services` ထည့်ရ | လွယ် | ⭐ **ဒါကို သုံး** |
| ၂။ `suspendCancellableCoroutine` | မလို | အလယ်အလတ် | Dependency မထည့်ချင်ရင် |
| ၃။ `snapshotFlow` နဲ့ပဲ ဆက်သုံး | မလို | လွယ် | Real-time မလိုတဲ့ data မို့ အလဟဿ |

**နည်းလမ်း ၁ — `libs.versions.toml` မှာ ထည့်ရမယ့် အရာ:**

```toml
[libraries]
kotlinx-coroutines-play-services = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-play-services", version.ref = "coroutines" }
```

`app/build.gradle.kts` မှာ:
```kotlin
implementation(libs.kotlinx.coroutines.play.services)
```

> `coroutines = "1.9.0"` က catalog မှာ ရှိပြီးသား — version အသစ် မထည့်ရဘူး။

> **⚠️ `libs.versions.toml` က အဖွဲ့တစ်ခုလုံး မျှသုံးတဲ့ ဖိုင်** —
> ထည့်ပြီးတာနဲ့ **ချက်ချင်း push** ပြီး အဖွဲ့ကို ပြောပါ။

### Function ရေးနည်း

```kotlin
suspend fun getPodcast(id: String): Podcast? {
    val snapshot = db.collection(COLLECTION_PODCASTS)
        .document(id)
        .get()
        .await()

    return snapshot.toObjectWithId(Podcast::class.java) { podcast, docId ->
        podcast.copy(id = docId)
    }
}
```

**အဓိက အချက် ၃ ခု:**

| အချက် | ဘာကြောင့်လဲ |
|---|---|
| `Podcast?` (nullable) ပြန်ပေး | Document မရှိရင် `null` — exception မပစ်နဲ့ |
| `toObjectWithId()` သုံး | Phase 1 မှာ ရေးထားပြီးသား — document ID ထည့်ပေးတယ် |
| `get()` သုံး (`addSnapshotListener` မဟုတ်) | Podcast အချက်အလက်က မပြောင်းလဲဘူး — listener အလဟဿ |

> **`toObject()` ချည်း သုံးရင် `id` က အမြဲ `""` ဖြစ်မယ်** —
> Firestore က document ID ကို field အဖြစ် မပေးဘူး။ ဒါကြောင့် `toObjectWithId()` ရှိတာ။

---

## Step 4: တကယ် အလုပ်လုပ်မလုပ် စစ်

ယာယီ log ထည့်ပြီး စစ်ပါ — **ပြီးရင် ပြန်ဖြုတ်ပါ**:

```kotlin
// PodcastDetailScreen (placeholder) ထဲမှာ ယာယီ
LaunchedEffect(podcastId) {
    val repo = PodcastRepository()
    Log.d("SLICE_B", "podcastId = $podcastId")
    Log.d("SLICE_B", "podcast = ${repo.getPodcast(podcastId)}")
    repo.getEpisodes(podcastId).collect {
        Log.d("SLICE_B", "episodes = ${it.size}")
    }
}
```

**မျှော်လင့်ရမယ့် Logcat:**
```
SLICE_B: podcastId = aBcD1234...        ← "" ဖြစ်နေရင် navigation ပြဿနာ
SLICE_B: podcast = Podcast(id=aBcD...)  ← null ဖြစ်နေရင် ID မှား
SLICE_B: episodes = 4                   ← 0 ဖြစ်နေရင် index (သို့) publishedAt
```

### ✅ Day 1 Deliverable

- [ ] Firebase Console Editor access ရပြီး
- [ ] Composite index `Enabled` ဖြစ်ပြီး
- [ ] Episode ၂၀ ခုလုံး `publishedAt` ရှိကြောင်း စစ်ပြီး
- [ ] `getPodcast(id)` ရေးပြီး
- [ ] Logcat မှာ `episodes = 4` ထွက်ပြီး
- [ ] `libs.versions.toml` ပြောင်းလဲမှု push ပြီး + အဖွဲ့ကို ပြောပြီး

---

# 🧠 Day 2 — MODEL Layer

**ရည်မှန်းချက်** — `PodcastDetailViewModel` ရေး
**ဒီနေ့က တစ်ပတ်လုံးရဲ့ အခက်ခဲဆုံး နေ့ပါ**

---

## Step 1: State ပုံစံ

```kotlin
data class PodcastDetailUiState(
    val podcast  : Podcast?      = null,
    val episodes : List<Episode> = emptyList(),
    val isLoading: Boolean       = true,
    val error    : String?       = null
)
```

> **`StateFlow` ၄ ခု သီးခြား မလုပ်ပါနဲ့** — `data class` တစ်ခုတည်း ဖြစ်ရမယ်။
> သီးခြား လုပ်ရင် UI က တစ်ခါတည်း update မဖြစ်ဘဲ မျက်စိလည်စရာ အလယ်အလတ် အခြေအနေတွေ ပေါ်မယ်။

---

## Step 2: ⭐ အဓိက ပြဿနာ — မတူတဲ့ Async ၂ မျိုး ပေါင်းခြင်း

မင်း data ၂ မျိုး လိုတယ်၊ ဒါပေမဲ့ **သဘာဝ မတူဘူး**:

```
getPodcast(id)     →  suspend fun  →  တစ်ခါ ခေါ် → တစ်ခါ ပြန် → ပြီး
getEpisodes(id)    →  Flow         →  ခေါ်ထား → ပြောင်းတိုင်း ထပ်ပို့ → ဘယ်တော့မှ မပြီး
```

### နည်းလမ်း ၃ မျိုး

| # | နည်းလမ်း | အားသာချက် | အားနည်းချက် |
|---|---|---|---|
| **၁** | Coroutine တစ်ခုထဲမှာ အစဉ်လိုက် | ရိုးရှင်း၊ ဖတ်ရလွယ် | Podcast load နေစဉ် episode မစဘူး |
| ၂ | `combine()` ၂ ခု ပေါင်း | ပိုမြန်၊ reactive | `getPodcast` ကို Flow ပြောင်းရ |
| ၃ | `launch` ၂ ခု ခွဲ | အမြန်ဆုံး | State race condition ဖြစ်နိုင် |

**Course project အတွက် → နည်းလမ်း ၁**

---

## Step 3: 🪤 ထောင်ချောက် ၃ ခု (ဒါတွေ မသိရင် နစ်မယ်)

### ထောင်ချောက် ၁ — `collect` က ဘယ်တော့မှ မပြီးဘူး

```kotlin
viewModelScope.launch {
    repo.getEpisodes(id).collect { ... }
    Log.d("TAG", "ပြီးပြီ")      // ❌ ဒီကြောင်း ဘယ်တော့မှ မရောက်ဘူး
}
```

`collect` က flow ပိတ်မှ ပြန်လာတယ်။ `callbackFlow` က ဘယ်တော့မှ မပိတ်ဘူး။
→ **`collect` အောက်မှာ ဘာမှ မရေးပါနဲ့**။

### ထောင်ချောက် ၂ — `try/catch` က Cancellation ကို မျိုချမယ်

```kotlin
try {
    repo.getEpisodes(id).collect { ... }
} catch (e: Exception) {           // ❌ CancellationException ပါ ဖမ်းမိမယ်
    _state.update { it.copy(error = e.message) }
}
```

`viewModelScope` ပိတ်တဲ့အခါ `CancellationException` ပစ်တယ်။ ဒါကို ဖမ်းလိုက်ရင်
screen ပိတ်သွားပြီးမှ "error" ဆိုပြီး ပြနေမယ် — **coroutine cancellation ပျက်သွားမယ်**။

**✅ မှန်တဲ့နည်း — Flow အတွက် `.catch {}` operator သုံး:**

```kotlin
repo.getEpisodes(id)
    .catch { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
    .collect { eps -> _state.update { it.copy(episodes = eps, isLoading = false) } }
```

`.catch {}` က `CancellationException` ကို **အလိုအလျောက် လွှတ်ပေးတယ်**။

**Suspend call အတွက်တော့ `try/catch` ရပေမဲ့ cancellation ကို ပြန်ပစ်ရမယ်:**

```kotlin
val podcast = try {
    repo.getPodcast(id)
} catch (e: CancellationException) {
    throw e                        // ← ဒီကြောင်း မမေ့နဲ့
} catch (e: Exception) {
    _state.update { it.copy(isLoading = false, error = e.message) }
    return@launch
}
```

### ထောင်ချောက် ၃ — Job ပွားခြင်း

`retry()` ခေါ်တိုင်း `launch` အသစ် လုပ်ရင် listener တွေ ပုံနေမယ် (A ရဲ့ bug နဲ့ အတူတူ)။

```kotlin
private var loadJob: Job? = null

private fun load() {
    loadJob?.cancel()              // ← အရင်တစ်ခု ရပ်
    loadJob = viewModelScope.launch { ... }
}
```

---

## Step 4: ViewModel ဖွဲ့စည်းပုံ

```kotlin
class PodcastDetailViewModel(
    private val podcastId: String,
    private val repo: PodcastRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PodcastDetailUiState())
    val state: StateFlow<PodcastDetailUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init { load() }

    fun retry() {
        _state.update { it.copy(isLoading = true, error = null) }
        load()
    }

    private fun load() {
        // ၁။ loadJob?.cancel()
        // ၂။ loadJob = viewModelScope.launch {
        //      ၃။ getPodcast(id) ခေါ် — try/catch + CancellationException rethrow
        //      ၄။ null ဆိုရင် error = "Podcast ရှာမတွေ့ပါ" → return@launch
        //      ၅။ state ထဲ podcast ထည့် (isLoading က true ပဲ ထား — episode မရသေးလို့)
        //      ၆။ getEpisodes(id).catch{}.collect{} — episode ဝင်မှ isLoading = false
        //    }
    }

    companion object {
        fun factory(
            podcastId: String,
            repo: PodcastRepository = PodcastRepository()
        ) = viewModelFactory {
            initializer { PodcastDetailViewModel(podcastId, repo) }
        }
    }
}
```

### Error message ကို ခွဲခြားပါ

```
Podcast က null          →  "Podcast ရှာမတွေ့ပါ"       (data ပြဿနာ — retry အလဟဿ)
Network exception       →  "အင်တာနက် ချိတ်ဆက်မှု မရပါ"  (retry သုံးလို့ရ)
FAILED_PRECONDITION     →  "Index မပြင်ဆင်ရသေးပါ"      (dev အတွက်ပဲ)
```

> **Senior tip** — "ရှာမတွေ့ပါ" မှာ retry ခလုတ် **မပြပါနဲ့**။ ဘယ်နှစ်ခါ နှိပ်နှိပ် မရဘူး။
> `error` အပြင် `canRetry: Boolean` ဆိုတာ state ထဲ ထပ်ထည့်လိုက်ပါ။

---

## Step 5: ⚠️ Episode မှာ Cover မရှိတဲ့ ပြဿနာ

`EpisodeRow` က `episode.coverUrl` ကို သုံးတယ်:

```kotlin
AsyncImage(model = episode.coverUrl, ...)
```

**ဒါပေမဲ့** — `phase1.md` ရဲ့ episode schema မှာ `coverUrl` **မပါဘူး**:

```javascript
{ podcastId, title, description, audioUrl, duration, publishedAt }
```

→ `episode.coverUrl` က အမြဲ `""` → ပုံ မပေါ်ဘဲ **အကွက်လွတ် ၅၆dp** ကျန်နေမယ်။

### ဖြေရှင်းနည်း ၂ မျိုး

| နည်းလမ်း | အလုပ်ပမာဏ | အကြံပြုချက် |
|---|---|---|
| Firestore မှာ episode ၂၀ ခုလုံးကို `coverUrl` ထပ်ထည့် | ၂၀ ခါ ရိုက် | ❌ |
| **ViewModel မှာ podcast ရဲ့ cover ကို episode တွေဆီ ဖြည့်ပေး** | ၂ ကြောင်း | ⭐ **ဒါကို သုံး** |

```kotlin
.collect { eps ->
    val cover = _state.value.podcast?.coverUrl.orEmpty()
    val withCover = eps.map { ep ->
        if (ep.coverUrl.isBlank()) ep.copy(coverUrl = cover) else ep
    }
    _state.update { it.copy(episodes = withCover, isLoading = false) }
}
```

> Podcast တစ်ခုရဲ့ episode အားလုံးက cover တူတာ သဘာဝကျတယ် — data ထပ်သိမ်းစရာ မလိုဘူး။
> **ဒါက မင်း ဆုံးဖြတ်ရမယ့် အရာ** — screen က မင်း ပိုင်တယ်။

### ✅ Day 2 Deliverable

- [ ] `PodcastDetailUiState` ရေးပြီး
- [ ] `PodcastDetailViewModel` ရေးပြီး
- [ ] `.catch {}` operator သုံးထားပြီး (`try/catch` around collect မဟုတ်)
- [ ] `CancellationException` ကို rethrow လုပ်ထားပြီး
- [ ] `loadJob?.cancel()` ထည့်ပြီး
- [ ] Factory ရေးပြီး
- [ ] Cover fallback ထည့်ပြီး

---

# 🎨 Day 3 — UI Layer

**ရည်မှန်းချက်** — `PodcastDetailScreen` ရေး

---

## Step 1: Signature ကို **မပြောင်းပါနဲ့**

Phase 1 မှာ ရေးထားတဲ့ placeholder ရဲ့ signature က NavHost နဲ့ ကိုက်ပြီးသား:

```kotlin
@Composable
fun PodcastDetailScreen(
    podcastId: String,
    onBack: () -> Unit,
    onEpisodeClick: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

→ ဒီ signature အတိုင်း ထားရင် **Day 4 မှာ NavHost ကို ၁ ကြောင်းပဲ ပြင်ရမယ်**။

---

## Step 2: Layout

```
┌──────────────────────────────┐
│ ←                            │  TopAppBar (transparent)
├──────────────────────────────┤
│        [Cover 200dp]         │  AsyncImage, RoundedCornerShape(16.dp)
│        Podcast Title         │  displaySmall, TextHi
│        သတင်း · ၄ ပိုင်း        │  bodySmall, TextLo
│   [▶ အားလုံး ဖွင့်] [+ သိမ်း] │  Button + OutlinedButton
│  Description...              │  bodyMedium, maxLines = 3
├──────────────────────────────┤
│  ပိုင်းများ (၄)               │  SectionHeader(title = ...)
│  ┌────────────────────────┐  │
│  │ 1  Episode title       │  │  EpisodeRow(index = 1, ...)
│  │ 2  Episode title       │  │
│  └────────────────────────┘  │
└──────────────────────────────┘
```

**ပြန်သုံးရမယ့် component (အသစ် မရေးနဲ့):**

| Component | Signature |
|---|---|
| `EpisodeRow` | `(index: Int, episode: Episode, isPlaying: Boolean = false, onPlay: () -> Unit, modifier)` |
| `SectionHeader` | `(title: String, onSeeAll: (() -> Unit)? = null, modifier)` |
| `LoadingView` / `ErrorView` / `EmptyView` | `(... , modifier: Modifier = Modifier)` |

> **`EpisodeRow` က `index` ကို `1` ကနေ စရမယ်** — `itemsIndexed` သုံးရင် `index + 1` ပေးပါ။

---

## Step 3: State handling — အစီအစဉ် အရေးကြီးတယ်

```kotlin
when {
    state.isLoading            -> LoadingView(Modifier.padding(padding))
    state.error != null        -> ErrorView(state.error, vm::retry, Modifier.padding(padding))
    state.podcast == null      -> EmptyView("Podcast ရှာမတွေ့ပါ", Modifier.padding(padding))
    else                       -> DetailContent(state, ..., Modifier.padding(padding))
}
```

**A ရဲ့ HomeScreen bug ၂ ခုကို မလုပ်မိပါစေနဲ့:**

| ❌ မလုပ်ရ | ✅ လုပ်ရမယ် |
|---|---|
| `LoadingView()` — padding မပါ | `LoadingView(Modifier.padding(padding))` |
| `state.error!!` | `state.error?.let { }` (သို့) local val ခံ |

> **Episode အလွတ်ဖြစ်တာနဲ့ podcast မရှိတာ မတူဘူး** —
> Podcast ရှိပြီး episode မရှိရင် **header တွေ ပြရမယ်**၊ episode နေရာမှာပဲ
> "ပိုင်း မရှိသေးပါ" ပြရမယ်။ Screen တစ်ခုလုံး EmptyView မလုပ်ပါနဲ့။

---

## Step 4: LazyColumn ဖွဲ့စည်းပုံ

```kotlin
LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 96.dp)   // ← Phase 3 MiniPlayer နေရာ
) {
    item { PodcastHeader(podcast) }
    item { ActionButtons(...) }
    item { SectionHeader("ပိုင်းများ (${state.episodes.size})") }

    if (state.episodes.isEmpty()) {
        item { EmptyView("ပိုင်း မရှိသေးပါ") }
    } else {
        itemsIndexed(state.episodes) { i, ep ->
            EpisodeRow(index = i + 1, episode = ep, onPlay = { onEpisodeClick(ep.id) })
        }
    }
}
```

> **`bottom = 96.dp` မဖျက်ပါနဲ့** — Phase 3 မှာ MiniPlayer ဝင်လာမယ်။
> မချန်ထားရင် နောက်ဆုံး episode က MiniPlayer အောက် ဝင်နေမယ်။

---

## Step 5: `@Preview`

```kotlin
@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun DetailContentPreview() {
    MyanCastTheme {
        DetailContent(state = PodcastDetailUiState(podcast = fakePodcast, episodes = fakeEps), ...)
    }
}
```

| ✅ | ❌ |
|---|---|
| `DetailContent` (private, state ကို parameter ယူ) ကို preview | `PodcastDetailScreen` ကို preview (ViewModel လိုလို့ crash) |
| `MyanCastTheme { }` နဲ့ ပတ် | ပတ်မထား (font/color မမှန်) |

> **ဒါက မင်းအတွက် အကြီးဆုံး အချိန်ကုန်သက်သာမှု** — Preview ရှိရင်
> emulator run စရာ မလိုဘဲ layout ပြင်လို့ရမယ်။

### ✅ Day 3 Deliverable

- [ ] `PodcastDetailScreen` ရေးပြီး (signature မပြောင်း)
- [ ] Cover / title / category / description ပေါ်
- [ ] Episode ၄ ခု `EpisodeRow` နဲ့ ပေါ်
- [ ] Loading / Error / Empty ၃ မျိုးလုံး padding ပါပြီး
- [ ] `!!` တစ်ခုမှ မရှိ
- [ ] `@Preview` အလုပ်လုပ်

---

# 🔗 Day 4 — INTEGRATION (မင်း ဦးဆောင်)

**ရည်မှန်းချက်** — Slice ၃ ခု ပေါင်းပြီး အစအဆုံး အလုပ်လုပ်အောင် လုပ်

---

## Step 1: Merge အစီအစဉ် (မင်း ကြီးကြပ်ရမယ်)

```bash
git checkout dev && git pull origin dev

git merge feature/phase2-slice-a   &&  ./gradlew assembleDebug   # A အရင်
git merge feature/phase2-slice-c   &&  ./gradlew assembleDebug   # ပြီးမှ C
git merge feature/phase2-slice-b   &&  ./gradlew assembleDebug   # နောက်ဆုံး မင်း
```

> **A → C → B အစီအစဉ် ဘာကြောင့်လဲ** — C က A ရဲ့ `HomeViewModel`/`HomeScreen` ကို ထိထားတယ်။
> C အရင် merge ရင် A ရဲ့ အပြောင်းအလဲ မပါသေးလို့ conflict ကြီးမယ်။
> **Conflict ဖြစ်ရင် ၃ ယောက်လုံး အတူတူ ဖြေရှင်းပါ** — တစ်ယောက်တည်း မလုပ်ပါနဲ့။

---

## Step 2: NavHost ပြင် (၁ နေရာပဲ)

`ui/navigation/MyanCastNavHost.kt` — placeholder import ကို ဖြုတ်ပြီး
တကယ့် screen ကို ညွှန်းလိုက်ရုံပါပဲ။ **Signature တူထားရင် ကျန်တာ ဘာမှ မပြင်ရဘူး**:

```kotlin
composable(
    route = Screen.PodcastDetail.route,
    arguments = listOf(navArgument(Screen.ARG_PODCAST_ID) { type = NavType.StringType })
) { entry ->
    PodcastDetailScreen(
        podcastId = entry.arguments?.getString(Screen.ARG_PODCAST_ID).orEmpty(),
        onBack = { navController.popBackStack() },
        onEpisodeClick = { navController.navigate(Screen.Player.route) }
    )
}
```

> **Route string အသစ် မရေးပါနဲ့** — `Screen.PodcastDetail.create(id)` ကိုပဲ သုံးပါ။
> `"podcast/$id"` လို့ လက်နဲ့ ရိုက်ရင် တစ်နေရာ ပြင်ဖို့ မေ့သွားမယ်။

---

## Step 3: အစအဆုံး လမ်းကြောင်း ရှင်းပြခြင်း

**ဒါက Phase 2 ရဲ့ အဓိက ရည်ရွယ်ချက်ပါ** — မင်း ဒီ လမ်းကြောင်းကို
စာမကြည့်ဘဲ ရှင်းပြနိုင်ရမယ်:

```
Firestore "episodes" collection
        ↓  whereEqualTo("podcastId") + orderBy("publishedAt")  ← composite index လိုတယ်
PodcastRepository.getEpisodes()
        ↓  snapshotFlow() → callbackFlow → addSnapshotListener  ← real-time
        ↓  toListWithIds() → document ID ကို model ထဲ ထည့်
Flow<List<Episode>>
        ↓  viewModelScope.launch { .catch{}.collect{} }
PodcastDetailViewModel._state
        ↓  cover fallback ဖြည့်
PodcastDetailUiState
        ↓  collectAsStateWithLifecycle()
PodcastDetailScreen
        ↓  itemsIndexed
EpisodeRow(index, episode)
        ↓  C ရဲ့ formatDurationLabel(episode.duration)
"၂၄ မိနစ်"  ← မျက်လုံးနဲ့ မြင်ရတာ
```

---

## Step 4: Edge Case စစ် (မင်း တာဝန်ယူရမယ့် အပိုင်း)

| စမ်းရမယ့် အရာ | မျှော်လင့်ရမယ့် ရလဒ် |
|---|---|
| Podcast မရှိတဲ့ ID နဲ့ ဖွင့် | "ရှာမတွေ့ပါ" — crash မဖြစ်ရ |
| Episode မရှိတဲ့ podcast | Header ပေါ် + "ပိုင်း မရှိသေးပါ" |
| Internet ဖြုတ်ပြီး Detail ဖွင့် | Error + retry — retry နှိပ်ရင် တကယ် ပြန်ကြိုး |
| Detail ဖွင့်ပြီး ချက်ချင်း back | Crash မဖြစ်ရ (cancellation မှန်ရမယ်) |
| Back မြန်မြန် ၅ ချက် | Crash မဖြစ်ရ |
| Detail ဖွင့်ထားစဉ် Console မှာ episode ထပ်ထည့် | **ချက်ချင်း ပေါ်လာရမယ်** (real-time) |
| Rotate | State မပျောက်ရ |
| Detail မှာ bottom nav | **မပေါ်ရ** (Phase 1 မှာ စီစဉ်ပြီး) |

> **နောက်ဆုံး ၂ ခုက မင်းရဲ့ cancellation code မှန်မမှန် စစ်တာ** —
> back မြန်မြန် နှိပ်ရင် crash ဖြစ်တယ်ဆိုရင် `try/catch` က
> `CancellationException` ကို မျိုချနေတယ် (Day 2 ထောင်ချောက် ၂)။

### ✅ Day 4 Deliverable

- [ ] Slice ၃ ခု merge ပြီး build အောင်
- [ ] NavHost ချိတ်ပြီး
- [ ] Home → Detail → Back အလုပ်လုပ်
- [ ] Edge case ၈ ခုလုံး စစ်ပြီး
- [ ] Data flow ကို ရှင်းပြနိုင်ပြီ

---

# 🔄 Day 5 — TEST + REVIEW

---

## Step 1: Repository ကို Interface ခွဲ (၃ ယောက်လုံး အတူတူ — မနက်)

လက်ရှိ `PodcastRepository` က `class` — **fake လုပ်လို့ မရဘူး** → test မရေးနိုင်ဘူး။

```kotlin
interface PodcastRepository {
    fun getPodcasts(): Flow<List<Podcast>>
    fun getEpisodes(podcastId: String): Flow<List<Episode>>
    suspend fun getPodcast(id: String): Podcast?
}

class FirestorePodcastRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : PodcastRepository { ... }
```

> **၃ ယောက်လုံးရဲ့ code ကို ထိတဲ့ refactor** — တစ်ယောက်တည်း မလုပ်ပါနဲ့။
> ၃ ယောက် အတူထိုင်ပြီး ၃၀ မိနစ်နဲ့ ပြီးတယ်။

---

## Step 2: Test Setup — `viewModelScope` ပြဿနာ

`viewModelScope` က `Dispatchers.Main` သုံးတယ် — unit test မှာ **မရှိဘူး**
→ `IllegalStateException` ပစ်မယ်။

**JUnit Rule တစ်ခု ဆောက်ပါ** (`app/src/test/java/.../MainDispatcherRule.kt`):

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(dispatcher)
    override fun finished(description: Description) = Dispatchers.resetMain()
}
```

Test မှာ: `@get:Rule val mainDispatcherRule = MainDispatcherRule()`

> ဒီ rule က A ရော မင်းရော လိုတယ် — **တစ်ယောက်ပဲ ရေးပြီး မျှသုံးပါ**။

---

## Step 3: Test ၂ ခု ရေး

### Test 1 — အောင်မြင်တဲ့ load

```
ပေး   : FakeRepository → podcast ၁ ခု + episode ၂ ခု
လုပ်   : ViewModel ဆောက် → advanceUntilIdle()
စစ်    : isLoading == false
        podcast != null
        episodes.size == 2
        error == null
```

### Test 2 — Podcast ရှာမတွေ့

```
ပေး   : FakeRepository → getPodcast() က null ပြန်
လုပ်   : ViewModel ဆောက် → advanceUntilIdle()
စစ်    : error != null
        isLoading == false
        podcast == null
```

**Fake ရေးနည်း:**
```kotlin
class FakePodcastRepository(
    private val podcast: Podcast? = null,
    private val episodes: List<Episode> = emptyList()
) : PodcastRepository {
    override suspend fun getPodcast(id: String) = podcast
    override fun getEpisodes(podcastId: String) = flowOf(episodes)
    override fun getPodcasts() = flowOf(emptyList<Podcast>())
}
```

> **`turbine` library မရှိဘူး** — `advanceUntilIdle()` ခေါ်ပြီး
> `vm.state.value` ကို တိုက်ရိုက် ဖတ်ပါ။ လုံလောက်တယ်။

---

## Step 4: Cross-Review — မင်း C ရဲ့ code ကို review

```
A  →  B ရဲ့ code review        (မင်းရဲ့ဟာကို A ဖတ်မယ်)
B  →  C ရဲ့ code review        (မင်း C ရဲ့ဟာ ဖတ်ရမယ်)
C  →  A ရဲ့ code review
```

**C ရဲ့ code မှာ စစ်ရမယ့် အချက်:**

- [ ] `formatDuration(0)` နဲ့ `(-5)` က crash မဖြစ်ဘူးလား
- [ ] `formatRelativeDate(null)` က `""` ပြန်ပေးလား (crash မဖြစ်ဘူးလား)
- [ ] `buildCategories()` က အလွတ် category ကို ဖယ်ထားလား
- [ ] `"အားလုံး"` က `const val` လား၊ hard-code ၂ နေရာ ရှိလား
- [ ] `EpisodeRow` မှာ `duration == 0` ဆိုရင် ကြာချိန် မပြဘူးလား

> **Review ရဲ့ မေးခွန်း** — "ဒီ code က ဘာလုပ်တာလဲ **ငါ ရှင်းပြနိုင်လား**?"
> မရနိုင်ရင် မေးပါ။ ဒါက ရည်ရွယ်ချက်ပါ။

### ✅ Day 5 Deliverable

- [ ] Repository interface ခွဲပြီး (၃ ယောက်လုံး)
- [ ] `MainDispatcherRule` ရှိပြီး
- [ ] Test ၂ ခု pass
- [ ] C ရဲ့ code review ပြီး
- [ ] `dev` ကို merge ပြီး

---

# ✅ Slice B — Final Checklist

| # | Layer | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|---|
| 1 | DB | Firebase Console Editor access ရပြီး | ☐ |
| 2 | DB | Composite index `Enabled` | ☐ |
| 3 | DB | Episode ၂၀ ခုလုံး `publishedAt` ရှိ | ☐ |
| 4 | DB | `getPodcast(id)` ရေးပြီး — `null` ကို ကိုင်တွယ် | ☐ |
| 5 | DB | `libs.versions.toml` push ပြီး + အဖွဲ့ကို ပြောပြီး | ☐ |
| 6 | Model | `PodcastDetailUiState` — `data class` တစ်ခုတည်း | ☐ |
| 7 | Model | `.catch {}` operator သုံး (try/catch around collect မဟုတ်) | ☐ |
| 8 | Model | `CancellationException` rethrow ပါ | ☐ |
| 9 | Model | `loadJob?.cancel()` ပါ | ☐ |
| 10 | Model | Factory ရေးပြီး | ☐ |
| 11 | Model | Cover fallback ပါ | ☐ |
| 12 | UI | Signature မပြောင်း | ☐ |
| 13 | UI | Loading / Error / Empty ၃ မျိုး padding ပါ | ☐ |
| 14 | UI | Episode မရှိရင် header တော့ ပေါ် | ☐ |
| 15 | UI | `bottom = 96.dp` မဖျက် | ☐ |
| 16 | UI | `!!` တစ်ခုမှ မရှိ | ☐ |
| 17 | UI | `@Preview` အလုပ်လုပ် | ☐ |
| 18 | Integration | NavHost ချိတ်ပြီး — route hard-code မရှိ | ☐ |
| 19 | Integration | Edge case ၈ ခု စစ်ပြီး | ☐ |
| 20 | Test | Test ၂ ခု pass | ☐ |

---

# 🔧 Troubleshooting — မင်း တွေ့နိုင်တဲ့ Error

| Error / လက္ခဏာ | Layer | ဖြေရှင်းနည်း |
|---|---|---|
| `FAILED_PRECONDITION: requires an index` | DB | Logcat က link နှိပ် → Create index → ၂ မိနစ် စောင့် |
| Episode `0` ခု (podcast တော့ ရ) | DB | `orderBy` ယာယီ ဖြုတ်စမ်း → ရရင် `publishedAt` missing |
| Episode အချို့ပဲ ပေါ် | DB | ပျောက်နေတဲ့ document မှာ `publishedAt` မပါဘူး |
| `podcast == null` အမြဲ | DB | `podcastId` က `""` လား Logcat စစ် → navigation ပြဿနာ |
| `Unresolved reference: await` | DB | `kotlinx-coroutines-play-services` မထည့်ရသေး |
| `IllegalStateException: Module with Main dispatcher` | Test | `MainDispatcherRule` မထည့်ရသေး |
| Back နှိပ်ရင် crash | Model | `try/catch` က `CancellationException` မျိုချနေတယ် |
| Screen ပိတ်ပြီးမှ error ပြ | Model | အပေါ်နဲ့ အတူတူ — `.catch {}` operator သုံး |
| Retry နှိပ်တိုင်း data ၂ ဆ | Model | `loadJob?.cancel()` မထည့်ရသေး |
| Episode cover အကွက်လွတ် | Model | Cover fallback မထည့်ရသေး (Day 2 Step 5) |
| Loading spinner က TopAppBar အောက် | UI | `Modifier.padding(padding)` မပေးရသေး |
| နောက်ဆုံး episode မမြင်ရ | UI | `contentPadding` bottom မထည့်ရသေး |
| Preview မှာ crash | UI | `PodcastDetailScreen` ကို preview လုပ်နေတယ် — `DetailContent` ကို လုပ် |

---

# 🎯 Senior-level မှတ်ချက် ၅ ခု

**၁။ Data ပြဿနာနဲ့ Code ပြဿနာ ခွဲခြားတတ်ပါ**
Episode မပေါ်ရင် code ကို အရင် မထိပါနဲ့။ `orderBy` ဖြုတ်စမ်းတာ ၁ မိနစ်ပဲ ကြာပြီး
ပြဿနာ ၂ မျိုးကို ချက်ချင်း ခွဲပေးတယ်။

**၂။ Nullable ကို တလေးတစား ကိုင်တွယ်ပါ**
`getPodcast()` က `Podcast?` ပြန်ပေးတာ မတော်တဆ မဟုတ်ဘူး — "မရှိ" ဆိုတာ
မျှော်လင့်ထားတဲ့ အခြေအနေ ဖြစ်လို့။ `!!` နဲ့ ဖျောက်ပစ်ရင် demo ချိန်မှာ crash မယ်။

**၃။ Cancellation က ချွင်းချက် မဟုတ်ဘူး — ပုံမှန် ဖြစ်စဉ်ပါ**
User back နှိပ်တိုင်း coroutine cancel ဖြစ်တယ်။ `catch (e: Exception)` က
ဒါကို မျိုချပြီး error ပြမယ်။ ဒါကြောင့် `.catch {}` operator ရှိတာ။

**၄။ State တစ်ခုတည်း၊ အမှန်တရား တစ်ခုတည်း**
`isLoading`, `error`, `podcast`, `episodes` ကို `StateFlow` သီးခြား လုပ်ရင်
"loading ပြီးပြီ ဒါပေမဲ့ data မရှိသေးဘူး" ဆိုတဲ့ ဖြစ်မရိုးမဖြစ်စဉ် အခြေအနေ ပေါ်မယ်။

**၅။ Signature ကို တည်ငြိမ်အောင် ထားပါ**
Placeholder ရဲ့ signature အတိုင်း ထားရင် Day 4 မှာ ၁ ကြောင်းပဲ ပြင်ရမယ်။
ပြောင်းရင် NavHost၊ A ရဲ့ navigation၊ Preview အားလုံး လိုက်ပြင်ရမယ်။

---

## 📅 တစ်ပတ်စာ အကျဉ်းချုပ်

| နေ့ | Layer | အဓိက အလုပ် | အန္တရာယ် |
|---|---|---|---|
| ၁ | 🗄️ DB | Index + `getPodcast()` | Index မဆောက်ရင် အားလုံး ပိတ်မိမယ် |
| ၂ | 🧠 Model | ViewModel + async ၂ မျိုး ပေါင်း | ထောင်ချောက် ၃ ခု |
| ၃ | 🎨 UI | `PodcastDetailScreen` | Signature မပြောင်းမိစေနဲ့ |
| ၄ | 🔗 Integration | Merge + NavHost (**မင်း ဦးဆောင်**) | Merge အစီအစဉ် A → C → B |
| ၅ | 🔄 Test | Interface ခွဲ + test ၂ ခု + review | Main dispatcher rule |
