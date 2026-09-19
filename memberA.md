# Member A — Slice A: Podcast စာရင်း (Week 2)

> **ဒါက မင်းရဲ့ တစ်ပတ်စာ အလုပ် အပြည့်အစုံ**။
> Database → Model → UI ၃ ခုလုံး မင်း ကိုယ်တိုင် ရေးရမယ်။

---

## 📋 Slice A — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **တာဝန်** | Home screen မှာ podcast စာရင်း ပြရန် |
| **အခက်ခဲဆုံး အပိုင်း** | ရှိပြီးသား code ရဲ့ ချို့ယွင်းချက် ၇ ခု ရှာပြီး ပြင်ခြင်း |
| **အရေးကြီးဆုံး** | Home က app ရဲ့ **မျက်နှာစာ** — demo ဖွင့်ရင် ဒါ အရင်မြင်ရမယ် |
| **အပို တာဝန်** | Day 1 မှာ **အမြန်ဆုံး push လုပ်ရမယ်** — B စောင့်နေတယ် |

### မင်း ပိုင်တဲ့ ဖိုင်များ

| ဖိုင် | လက်ရှိ | ဘာလုပ်ရမလဲ |
|---|---|---|
| `data/repository/PodcastRepository.kt` | ⚠️ B နဲ့ မျှသုံး | စစ် + `orderBy` ဆုံးဖြတ် |
| `ui/home/HomeViewModel.kt` | 🟡 ၆၂ ကြောင်း | ချို့ယွင်းချက် ၃ ခု ပြင် |
| `ui/home/HomeScreen.kt` | 🟡 ၁၈၃ ကြောင်း | ချို့ယွင်းချက် ၄ ခု ပြင် |
| `ui/components/PodcastListItem.kt` | 🟡 ၈၃ ကြောင်း | Myanmar font bug ပြင် |

> **မင်းရဲ့ အလုပ်က "အသစ်ရေး" မဟုတ်ဘူး၊ "ပြင်" ပါ** — code က ရှိပြီးသား။
> ဒါပေမဲ့ ပြင်ရမယ့် အချက်တွေက သေချာ နားလည်မှ ပြင်လို့ရမယ်။

### မင့်ကို မှီခိုနေတဲ့ သူများ

```
Member B  →  မင်း PodcastRepository push မလုပ်ရင် B စလို့ မရဘူး  ← Day 1 အရေးကြီး
Member C  →  မင်းရဲ့ HomeViewModel / HomeScreen ထဲ ဝင်ရမယ်      ← Day 3 ပူးပေါင်း
Demo      →  Home ပျက်နေရင် project တစ်ခုလုံး ပျက်သလို မြင်ရမယ်
```

---

## 🚦 Day 0 — စမလုပ်ခင် (၃၀ မိနစ်)

### ၁။ ကိုယ့် code ကို အရင် ဖတ်

```bash
git checkout dev && git pull origin dev
git checkout -b feature/phase2-slice-a
```

ဒီ ၃ ဖိုင်ကို **တစ်ကြောင်းချင်း ဖတ်ပါ** — မဖတ်ဘဲ မပြင်ပါနဲ့:

| ဖိုင် | ဘာကို ရှာဖတ်ရမလဲ |
|---|---|
| `ui/home/HomeViewModel.kt` | `selectCategory()` က ဘာလုပ်လဲ? (အဖြေ — ဘာမှ မလုပ်ဘူး) |
| `ui/home/HomeScreen.kt` | `state.error!!` ဘယ်ကြောင်းမှာလဲ? (၈၆) |
| `data/repository/PodcastRepository.kt` | `getPodcasts()` မှာ `orderBy` ပါလား? (အဖြေ — မပါဘူး) |

### ၂။ App ကို အရင် run ပြီး ဘာဖြစ်နေလဲ ကြည့်

Emulator ဖွင့်ပြီး `./gradlew installDebug` → Home ဘာပေါ်လဲ မှတ်ထားပါ။
**ပြင်ပြီးမှ ဘာပြောင်းသွားလဲ နှိုင်းယှဉ်လို့ရအောင်။**

---

# 🗄️ Day 1 — DATABASE Layer

**ရည်မှန်းချက်** — Repository ကနေ data ထွက်လာပုံ နားလည် + B ကို လမ်းဖွင့်ပေး

---

## Step 1: ⏰ အမြန်ဆုံး လုပ်ရမယ့် အရာ (မနက် ပထမဆုံး)

**B က မင်းကို စောင့်နေတယ်။** `PodcastRepository.kt` ကို ၂ ယောက်လုံး ထိရမယ်။

```
မနက်     A က getPodcasts() ပိုင်း စစ်/ပြင် ပြီး → push
နေ့လယ်   B က pull ဆွဲပြီး getPodcast() ထည့်
```

**မင်းရဲ့ ပိုင်း ၁ နာရီနဲ့ ပြီးအောင် လုပ်ပြီး ချက်ချင်း push လုပ်ပါ။**
ဖိုင်တစ်ခုလုံး မပြင်ပါနဲ့ — `getPodcasts()` function တစ်ခုတည်းပဲ ထိပါ။

---

## Step 2: `getPodcasts()` အလုပ်လုပ်မလုပ် စစ်

ယာယီ log ထည့် — **ပြီးရင် ပြန်ဖြုတ်ပါ**:

```kotlin
// HomeViewModel.loadPodcasts() ထဲမှာ ယာယီ
repo.getPodcasts()
    .onEach { Log.d("SLICE_A", "podcasts = ${it.size}, firstId = ${it.firstOrNull()?.id}") }
    .catch { ... }
    .collect { ... }
```

**မျှော်လင့်ရမယ့် Logcat:**
```
SLICE_A: podcasts = 5, firstId = aBcD1234...
```

| ရလဒ် | ဆိုလိုတာ | ဘာလုပ်ရမလဲ |
|---|---|---|
| `podcasts = 5` | ✅ အားလုံး အဆင်ပြေ | ဆက်လုပ် |
| `podcasts = 0` | Collection name (သို့) rules | Console စစ် |
| `firstId = ` (အလွတ်) | 🔴 ID mapping ပျက်နေတယ် | အောက်က Step 3 ဖတ် |
| `PERMISSION_DENIED` | Firestore rules | `allow read: if true` ဟုတ်လား |

---

## Step 3: ⚠️ `id` က ဘာကြောင့် အရေးကြီးလဲ

Firestore က **document ID ကို field အဖြစ် မပေးဘူး**။ ဒါကြောင့် —

```kotlin
snapshot.toObjects(Podcast::class.java)   // ❌ id က အမြဲ ""
```

Phase 1 မှာ ဒါကို ပြင်ထားပြီးပြီ (`data/firebase/FirestoreExt.kt`):

```kotlin
fun <T : Any> QuerySnapshot.toListWithIds(
    clazz: Class<T>,
    withId: (T, String) -> T
): List<T> = documents.mapNotNull { doc ->
    doc.toObject(clazz)?.let { withId(it, doc.id) }      // ✅ id ထည့်ပေးတယ်
}
```

> **ဘာကြောင့် မင်း သိထားရမလဲ** — `id` က `""` ဖြစ်နေရင်
> podcast နှိပ်တဲ့အခါ `podcast/` ဆိုတဲ့ route ဖြစ်ပြီး **B ရဲ့ screen က crash မယ်**။
> မင်းရဲ့ bug ဖြစ်ပေမဲ့ B ဆီမှာ ပေါ်မယ်။ Day 1 မှာ စစ်ထားပါ။

---

## Step 4: 🤔 ဆုံးဖြတ်ချက် — `orderBy` ပြန်ထည့်မလား?

`phase1.md` ရဲ့ မူလ design မှာ ဒီလို ရေးထားတယ်:

```kotlin
db.collection("podcasts")
    .orderBy("updatedAt", Query.Direction.DESCENDING)   // ← ဒါ ခု မပါတော့ဘူး
```

လက်ရှိ code မှာ **`orderBy` မပါဘူး** — Firestore ရဲ့ default order (document ID) နဲ့ ပြန်လာတယ်။

### ဘာကြောင့် ဖြုတ်ထားလဲ

```
orderBy("updatedAt") သုံးရင် —
updatedAt field မပါတဲ့ document တွေ ရလဒ်ထဲက တိတ်တဆိတ် ပျောက်သွားမယ်။
Error မပြဘူး။ Podcast ၅ ခု ထဲက ၃ ခုပဲ ပေါ်တာမျိုး ဖြစ်မယ်။
```

### နည်းလမ်း ၃ မျိုး

| # | နည်းလမ်း | အားသာချက် | အန္တရာယ် |
|---|---|---|---|
| ၁ | `orderBy` မထည့်ဘူး (လက်ရှိ) | ဘာမှ မပျောက် | အစီအစဉ် ကျပန်း |
| **၂** | **ViewModel မှာ sort** | ဘာမှ မပျောက် + အစီအစဉ် ရ | — |
| ၃ | `orderBy` ပြန်ထည့် | Firestore က တွက်ပေး | Document ပျောက်နိုင် |

**⭐ နည်းလမ်း ၂ ကို အကြံပြုပါတယ်** — podcast ၅ ခုပဲ ရှိတာ၊ client-side sort က
စွမ်းဆောင်ရည် ပြဿနာ မရှိဘူး။ Firestore query ကလည်း index မလိုဘူး။

> နည်းလမ်း ၃ ရွေးမယ်ဆိုရင် **Console မှာ podcast ၅ ခုလုံး `updatedAt` ရှိကြောင်း အရင်စစ်ပါ**။

---

## Step 5: `snapshotFlow` ဘယ်လို အလုပ်လုပ်လဲ (နားလည်ထားရမယ်)

`data/firebase/FirestoreExt.kt` ကို ဖတ်ပြီး ဒီမေးခွန်း ၃ ခုကို ဖြေနိုင်ရမယ်:

```kotlin
fun <T : Any> Query.snapshotFlow(...): Flow<List<T>> = callbackFlow {
    val listener = addSnapshotListener { snapshot, error -> ... trySend(...) }
    awaitClose { listener.remove() }        // ← ဒီကြောင်း ဘာလုပ်တာလဲ?
}
```

| မေးခွန်း | အဖြေ |
|---|---|
| `callbackFlow` ဘာကြောင့် သုံးလဲ? | Callback API (listener) ကို Flow ပြောင်းဖို့ |
| `awaitClose` ဘာလုပ်တာလဲ? | Collector ရပ်ရင် listener ဖြုတ်ပေးတယ် — **memory leak ကာကွယ်** |
| Data ပြောင်းရင် ဘာဖြစ်လဲ? | Listener ပြန်ခေါ်ခံရ → `trySend` → UI ချက်ချင်း ပြောင်း |

> **ဒါက Day 4 မှာ မင်း ရှင်းပြရမယ့် အပိုင်းပါ။**

### ✅ Day 1 Deliverable

- [ ] `getPodcasts()` က ၅ ခု ပြန်ပေးကြောင်း Logcat နဲ့ အတည်ပြုပြီး
- [ ] `firstId` က အလွတ် မဟုတ်ကြောင်း စစ်ပြီး
- [ ] `orderBy` ဆုံးဖြတ်ချက် ချပြီး
- [ ] **`PodcastRepository.kt` ကို push ပြီး + B ကို ပြောပြီး** ⏰
- [ ] `snapshotFlow` အလုပ်လုပ်ပုံ ရှင်းပြနိုင်ပြီ

---

# 🧠 Day 2 — MODEL Layer

**ရည်မှန်းချက်** — `HomeViewModel` ရဲ့ ချို့ယွင်းချက် ၃ ခု ပြင် + ဖွဲ့စည်းပုံ ပြန်စီ

---

## လက်ရှိ code ရဲ့ ချို့ယွင်းချက် ၃ ခု

| # | ပြဿနာ | ဘယ်မှာလဲ | ဘာဖြစ်လဲ |
|---|---|---|---|
| 1 | `selectCategory()` က filter မလုပ်ဘူး | ကြောင်း ၆၂–၆၄ | Chip နှိပ်လည်း ဘာမှ မဖြစ် |
| 2 | `categories` က hard-code English | ကြောင်း ၂၀ | Firestore က မြန်မာလို → ဘယ်တော့မှ မကိုက် |
| 3 | `refresh()` က listener ပွားစေတယ် | ကြောင်း ၆၆–၆၉ | Memory leak + data ၂ ဆ |

---

## Step 1: 🐛 ချို့ယွင်းချက် ၁ — Filter မလုပ်တာ

**လက်ရှိ code:**
```kotlin
fun selectCategory(category: String) {
    _state.update { it.copy(selectedCategory = category) }   // ← state ပဲ ပြောင်း
}
```

Chip နှိပ်ရင် `selectedCategory` ပြောင်းတယ်၊ ဒါပေမဲ့ `podcasts` list က **မပြောင်းဘူး**။
→ UI မှာ ဘာမှ မဖြစ်သလို မြင်ရမယ်။

**ဖြေရှင်းနည်း — list ၂ မျိုး ခွဲထားရမယ်:**

```kotlin
data class HomeUiState(
    val allPodcasts     : List<Podcast> = emptyList(),   // ← Firestore ကလာတာ (မပြောင်း)
    val podcasts        : List<Podcast> = emptyList(),   // ← Filter ပြီးသား (UI က ဒါကို ပြ)
    val categories      : List<String>  = emptyList(),
    val selectedCategory: String        = ALL_CATEGORY,
    val trending        : List<Podcast> = emptyList(),
    val lastPlayed      : Episode?      = null,
    val isLoading       : Boolean       = true,
    val error           : String?       = null
)
```

`selectCategory()` က ခု ဒီလို ဖြစ်ရမယ်:
```kotlin
fun selectCategory(category: String) {
    _state.update { s ->
        s.copy(
            selectedCategory = category,
            podcasts = filterByCategory(s.allPodcasts, category)   // ← C ရဲ့ function
        )
    }
}
```

> **`filterByCategory()` က C ရေးမယ်** — C မပြီးသေးရင် ယာယီ ကိုယ့်ဘာသာ ရေးထားပြီး
> Day 3 မှာ C ရဲ့ဟာနဲ့ အစားထိုးပါ။ **၂ ယောက် ၂ ခု မရေးပါနဲ့** — C ကို ပြောပါ။

---

## Step 2: 🐛 ချို့ယွင်းချက် ၂ — Hard-code Category

**လက်ရှိ code:**
```kotlin
val categories: List<String> = listOf("All", "News", "Tech", "Story", "Music")
```

Firestore မှာ ရှိတာက:
```
"သတင်း", "နည်းပညာ", "ဇာတ်လမ်း", "ကျန်းမာရေး", "စီးပွားရေး"
```

→ `"News" == "သတင်း"` ဘယ်တော့မှ မဟုတ်ဘူး → filter လုပ်ရင် **list အလွတ်** ဖြစ်မယ်။

**ဖြေရှင်းနည်း — data ကနေ တွက်ထုတ်:**
```kotlin
categories = buildCategories(list)      // ← C ရဲ့ function
```

> **Hard-code list ကို ဖျက်ပစ်ပါ** — Firestore မှာ category အသစ် ထည့်တိုင်း
> code ပြင်ရမယ်ဆိုရင် design မှားနေပြီ။

---

## Step 3: 🐛 ချို့ယွင်းချက် ၃ — Listener ပွားခြင်း

**လက်ရှိ code:**
```kotlin
fun refresh() {
    _state.update { it.copy(isLoading = true, error = null) }
    loadPodcasts()        // ← အရင် coroutine က ဆက်ပြေးနေတုန်း
}
```

`refresh()` ၃ ခါ နှိပ်ရင် → Firestore listener **၄ ခု** ဖွင့်ထားမယ် →
data ဝင်တိုင်း state ၄ ခါ update ဖြစ်မယ် → memory leak။

**ဖြေရှင်းနည်း:**
```kotlin
private var loadJob: Job? = null

private fun loadPodcasts() {
    loadJob?.cancel()                        // ← အရင်တစ်ခု ရပ်
    loadJob = viewModelScope.launch { ... }
}
```

> **B လည်း ဒီ bug ရှိမယ်** — ၂ ယောက် တူတဲ့ pattern သုံးပါ။

---

## Step 4: ViewModel Factory ထည့်

**လက်ရှိ:**
```kotlin
class HomeViewModel(
    private val repo: PodcastRepository = PodcastRepository()   // ← default parameter
) : ViewModel()
```

ဒါက `viewModel()` နဲ့ အလုပ်လုပ်ပေမဲ့ **Day 5 မှာ test မရေးနိုင်ဘူး** —
fake repository ထည့်လို့ မရဘူး။

```kotlin
class HomeViewModel(private val repo: PodcastRepository) : ViewModel() {
    companion object {
        fun factory(repo: PodcastRepository = PodcastRepository()) = viewModelFactory {
            initializer { HomeViewModel(repo) }
        }
    }
}
```
Screen မှာ: `viewModel(factory = HomeViewModel.factory())`

---

## Step 5: ⚠️ `lastPlayed` ကို မထိပါနဲ့

`HomeUiState` မှာ `lastPlayed: Episode?` ရှိတယ်၊ `HomeScreen` မှာလည်း
`ContinueListeningCard` ပြဖို့ code ရှိတယ် — ဒါပေမဲ့ **ဘယ်တော့မှ set မလုပ်ဘူး**။

```kotlin
state.lastPlayed?.let { episode ->        // ← အမြဲ null → ဘာမှ မပေါ်
    item { ContinueListeningCard(...) }
}
```

> **ဒါ bug မဟုတ်ဘူး** — "Continue Listening" က **Phase 4 (Room)** အလုပ်ပါ။
> `null` ပဲ ထားပြီး ဆက်သွားပါ။ ဒီနေရာမှာ အချိန် မကုန်ပါနဲ့။
> Day 2 မှာ ဒါကို ဖြေရှင်းဖို့ ကြိုးစားရင် တစ်ရက် ကုန်မယ်။

### ✅ Day 2 Deliverable

- [ ] `allPodcasts` / `podcasts` ခွဲထားပြီး
- [ ] `selectCategory()` က တကယ် filter လုပ်
- [ ] Hard-code category list ဖျက်ပြီး
- [ ] `loadJob?.cancel()` ထည့်ပြီး
- [ ] Factory ရေးပြီး
- [ ] `lastPlayed` ကို မထိဘဲ ချန်ထားပြီး

---

# 🎨 Day 3 — UI Layer

**ရည်မှန်းချက်** — `HomeScreen` ရဲ့ ချို့ယွင်းချက် ၄ ခု ပြင် + demo အဆင့် ရောက်အောင် လုပ်

---

## လက်ရှိ code ရဲ့ ချို့ယွင်းချက် ၄ ခု

| # | ပြဿနာ | ကြောင်း | ဘာဖြစ်လဲ |
|---|---|---|---|
| 1 | `LoadingView` / `ErrorView` မှာ padding မပါ | ၈၄, ၈၅ | TopAppBar အောက် ဝင်နေမယ် |
| 2 | `state.error!!` | ၈၆ | Race condition မှာ crash |
| 3 | Empty state မရှိ | — | Data မရှိရင် အဖြူ screen |
| 4 | `onSeeAll = { /* TODO */ }` | ၁၆၁ | နှိပ်လို့ရပေမဲ့ ဘာမှ မဖြစ် |

---

## Step 1: 🐛 Padding မပါတဲ့ ပြဿနာ

**လက်ရှိ:**
```kotlin
) { padding ->
    when {
        state.isLoading -> LoadingView()                    // ❌ padding မပါ
        state.error != null -> ErrorView(state.error!!, vm::refresh)   // ❌ ၂ ခု မှား
        else -> HomeContent(..., modifier = Modifier.padding(padding))  // ✅ ဒါပဲ မှန်
    }
}
```

`Scaffold` က `padding` ပေးတာ **TopAppBar နဲ့ bottom bar ရဲ့ နေရာ** အတွက်။
မသုံးရင် loading spinner က TopAppBar အောက်မှာ ဝင်နေမယ်။

**ပြင်ပြီး:**
```kotlin
state.isLoading -> LoadingView(Modifier.padding(padding))
```

---

## Step 2: 🐛 `!!` ကို ဖယ်ရှားခြင်း

**လက်ရှိ:**
```kotlin
state.error != null -> ErrorView(
    message = state.error!!,      // ❌ smart-cast မရလို့ !! သုံးထားတယ်
    onRetry = vm::refresh
)
```

`state` က `val` မဟုတ်ဘဲ delegate (`by`) ဖြစ်လို့ Kotlin က smart-cast မလုပ်နိုင်ဘူး။
ဒါပေမဲ့ `!!` က **ဖြေရှင်းနည်း မဟုတ်ဘူး** — ဖုံးကွယ်တာပဲ။

**မှန်တဲ့နည်း ၂ မျိုး:**

```kotlin
// နည်း ၁ — local val ခံ (အရှင်းဆုံး)
val error = state.error
when {
    state.isLoading -> LoadingView(Modifier.padding(padding))
    error != null   -> ErrorView(error, vm::refresh, Modifier.padding(padding))
    ...
}

// နည်း ၂ — let သုံး
state.error?.let { msg -> ErrorView(msg, vm::refresh, ...) }
```

> **Code review မှာ `!!` တွေ့ရင် အမြဲ မေးပါ** — "null ဖြစ်ရင် ဘာဖြစ်မလဲ?"

---

## Step 3: 🐛 Empty State ထည့်ခြင်း

Firestore မှာ data မရှိရင် (သို့) filter လုပ်လို့ ရလဒ် မရှိရင် **အဖြူ screen** ဖြစ်မယ်။

**`when` ရဲ့ အစီအစဉ် အရေးကြီးတယ်:**

```kotlin
when {
    state.isLoading          -> LoadingView(Modifier.padding(padding))
    error != null            -> ErrorView(error, vm::refresh, Modifier.padding(padding))
    state.podcasts.isEmpty() -> EmptyView("Podcast မရှိသေးပါ", Modifier.padding(padding))
    else                     -> HomeContent(...)
}
```

> **⚠️ `isLoading` ကို အရင် စစ်ပါ** — မဟုတ်ရင် ပထမဆုံး frame မှာ `podcasts` က
> အလွတ် ဖြစ်နေလို့ "မရှိသေးပါ" ဆိုတာ တစ်ချက် လျှပ်ပြပြီး ပျောက်သွားမယ်။
> User က "app ပျက်နေတယ်" ထင်မယ်။

**Category filter နဲ့ ခွဲခြားပါ:**

| အခြေအနေ | ပြရမယ့် စာသား |
|---|---|
| Firestore မှာ လုံးဝ မရှိ | "Podcast မရှိသေးပါ" |
| Filter လုပ်လို့ မရှိ | "ဒီအမျိုးအစားမှာ မရှိသေးပါ" |

---

## Step 4: 🐛 `onSeeAll` — လုပ်နိုင်တာ လုပ်၊ မလုပ်နိုင်ရင် ဖျောက်

```kotlin
SectionHeader(title = "Trending Now", onSeeAll = { /* TODO */ })
```

နှိပ်လို့ရနေပေမဲ့ ဘာမှ မဖြစ်ဘူး — **user အတွက် ပျက်နေသလို ခံစားရမယ်**။

`SectionHeader` ရဲ့ signature က `onSeeAll: (() -> Unit)? = null` —
**null ပေးလိုက်ရင် ခလုတ် လုံးဝ မပေါ်ဘူး**။

| ရွေးချယ်မှု | Code |
|---|---|
| ⭐ ဖျောက် (အကြံပြု) | `SectionHeader(title = "...")` — `onSeeAll` မပေးနဲ့ |
| Search ကို ပို့ | `onSeeAll = { onSearchClick() }` — parameter ထပ်ထည့်ရမယ် |

> **Demo မှာ နှိပ်လို့ရပြီး ဘာမှ မဖြစ်တဲ့ ခလုတ်က အမှတ် ဖြတ်စရာ** —
> မလုပ်နိုင်သေးရင် မပြတာ ပိုကောင်းတယ်။

---

## Step 5: ⚠️ Myanmar စာလုံး ပေါ်မှာ Poppins သုံးမိတဲ့ bug

`ui/components/PodcastListItem.kt` ကြောင်း ၇၈:

```kotlin
Text(
    text = "${podcast.episodeCount} episodes • ${podcast.category}",
    style = MaterialTheme.typography.labelSmall,      // ← Poppins!
)
```

**ပြဿနာ ၂ ခု:**

| # | ပြဿနာ | ဘာဖြစ်လဲ |
|---|---|---|
| 1 | `labelSmall` = **Poppins** (`Type.kt` ကြည့်) | Poppins မှာ **မြန်မာ glyph မရှိဘူး** → `podcast.category` ("သတင်း") က fallback font နဲ့ ပေါ်မယ် |
| 2 | `"episodes"` က **အင်္ဂလိပ် hard-code** | မြန်မာ app မှာ အင်္ဂလိပ် ရောနေမယ် |

**ဖြေရှင်းနည်း:**
```kotlin
Text(
    text = "${podcast.episodeCount} ပိုင်း • ${podcast.category}",
    style = MaterialTheme.typography.bodySmall,       // ← Padauk/Zawgyi family
)
```

> **ဒါက codebase တစ်ခုလုံး ရှိတဲ့ pattern ပါ** — `Type.kt` မှာ `label*` style ၃ ခုက
> Poppins၊ ကျန်တာက Myanmar family။ **မြန်မာစာ ပါရင် `label*` မသုံးရဘူး**။
> Phase 1 မှာ bottom nav ကို ဒီအတွက် `bodySmall` ပြောင်းထားပြီးပြီ။
> **C ရဲ့ `CategoryChips` နဲ့ `EpisodeRow` မှာလည်း တူတူ ရှိတယ် — C ကို ပြောပါ။**

---

## Step 6: အင်္ဂလိပ် စာသားတွေ မြန်မာ ပြောင်း

`HomeScreen` မှာ မြန်မာနဲ့ အင်္ဂလိပ် ရောနေတယ်:

| ကြောင်း | လက်ရှိ | ပြောင်းရမယ် |
|---|---|---|
| ၁၁၄ | `"မင်္ဂလာပါ 👋"` | ✅ မှန်ပြီး |
| ၁၄၁ | `"Categories"` | `"အမျိုးအစား"` |
| ၁၆၀ | `"Trending Now"` | `"ရေပန်းစားနေသော"` |
| ၁၈၂ | `"All Podcasts"` | `"Podcast အားလုံး"` |

---

## Step 7: `@Preview` ထည့်

```kotlin
@Preview(showBackground = true, backgroundColor = 0xFF0E0D0B)
@Composable
private fun HomeContentPreview() {
    MyanCastTheme {
        HomeContent(
            state = HomeUiState(podcasts = fakePodcasts, categories = listOf("အားလုံး", "သတင်း")),
            onCategorySelect = {},
            onPodcastClick = {}
        )
    }
}
```

| ✅ လုပ်ရမယ် | ❌ မလုပ်ရ |
|---|---|
| `HomeContent` (private) ကို preview | `HomeScreen` ကို preview — ViewModel လိုလို့ crash |
| `MyanCastTheme { }` နဲ့ ပတ် | ပတ်မထား → font/color မမှန် |
| Dark background ပေး | Default အဖြူ → စာ မမြင်ရ |

> **Preview က မင်းရဲ့ အချိန်ကို အများဆုံး သက်သာစေမယ်** —
> Layout ပြင်တိုင်း emulator run စရာ မလိုတော့ဘူး။

### ✅ Day 3 Deliverable

- [ ] Loading / Error / Empty ၃ မျိုးလုံး padding ပါပြီး
- [ ] `!!` တစ်ခုမှ မကျန်
- [ ] Empty state ၂ မျိုး (data မရှိ / filter မရှိ) ခွဲထားပြီး
- [ ] `onSeeAll` ဖျောက် (သို့) အလုပ်လုပ်အောင် လုပ်ပြီး
- [ ] `PodcastListItem` Myanmar font ပြင်ပြီး
- [ ] အင်္ဂလိပ် စာသား ၃ ခု မြန်မာ ပြောင်းပြီး
- [ ] `@Preview` အလုပ်လုပ်

---

# 🔗 Day 4 — INTEGRATION

**မင်း အရင်ဆုံး merge ဖြစ်မယ်** — ဒါကြောင့် **မင်း အသင့် ဖြစ်နေရမယ်**။

---

## Step 1: မင်း အရင် merge

```bash
git checkout dev && git pull origin dev
git merge feature/phase2-slice-a
./gradlew assembleDebug            # ← build မအောင်ရင် ကျန် ၂ ယောက် ပိတ်မိမယ်
```

> **A → C → B အစီအစဉ်** — မင်းက ပထမဆုံး။ မင်းရဲ့ code build မအောင်ရင်
> C ရော B ရော merge လုပ်လို့ မရဘူး။ **Day 3 ညနေမှာ build စစ်ထားပါ။**

---

## Step 2: C နဲ့ ပူးတွဲ ချိတ်ခြင်း (၃၀ မိနစ်)

C က မင်းရဲ့ ဖိုင် ၂ ခုထဲ ဝင်ရမယ်:

| ဖိုင် | C ဘာထည့်မလဲ |
|---|---|
| `HomeViewModel.kt` | `buildCategories()` + `filterByCategory()` ခေါ် |
| `HomeScreen.kt` | `CategoryChips` နေရာ အတည်ပြု |

**အတူထိုင်ပြီး လုပ်ပါ** — chat နဲ့ မလုပ်ပါနဲ့။ ၃၀ မိနစ်နဲ့ ပြီးတယ်။

---

## Step 3: Edge Case စစ် (မင်း တာဝန်ယူရမယ့် အပိုင်း)

| စမ်းရမယ့် အရာ | မျှော်လင့်ရမယ့် ရလဒ် |
|---|---|
| Internet ဖြုတ်ပြီး app ဖွင့် | Error view + retry ခလုတ် |
| Internet ပြန်ဖွင့်ပြီး retry | Data ပြန်ရ — **၂ ဆ မဖြစ်ရ** |
| Retry ၅ ခါ ဆက်တိုက် နှိပ် | Podcast ၅ ခုပဲ ရှိရမယ် (၂၅ ခု မဟုတ်) |
| Console မှာ podcast အသစ် ထည့် | **ချက်ချင်း ပေါ်လာရမယ်** |
| Category chip အားလုံး နှိပ်စမ်း | List မှန်မှန် ပြောင်း |
| Rotate | State မပျောက် |

> **"Retry ၅ ခါ" test က မင်းရဲ့ `loadJob?.cancel()` မှန်မမှန် စစ်တာ** —
> ၂၅ ခု ပေါ်လာရင် cancel မလုပ်ရသေးဘူး။

---

## Step 4: Data Flow ရှင်းပြနိုင်အောင် လေ့ကျင့်

```
Firestore "podcasts" collection
        ↓  addSnapshotListener  ← real-time
PodcastRepository.getPodcasts()
        ↓  toListWithIds() → document ID ကို model ထဲ ထည့်  ← မင်း Day 1 မှာ စစ်ခဲ့တာ
Flow<List<Podcast>>
        ↓  viewModelScope.launch { .catch{}.collect{} }
HomeViewModel._state  →  allPodcasts
        ↓  filterByCategory()  ← C ရဲ့ function
HomeUiState.podcasts
        ↓  collectAsStateWithLifecycle()
HomeScreen  →  LazyColumn  →  PodcastListItem
        ↓  နှိပ်လိုက်တယ်
Screen.PodcastDetail.create(podcast.id)
        ↓
B ရဲ့ screen
```

### ✅ Day 4 Deliverable

- [ ] မင်း အရင်ဆုံး merge ပြီး build အောင်
- [ ] C နဲ့ ချိတ်ပြီး — chip နှိပ်ရင် list ပြောင်း
- [ ] Edge case ၆ ခု စစ်ပြီး
- [ ] Data flow ရှင်းပြနိုင်ပြီ

---

# 🔄 Day 5 — TEST + REVIEW

---

## Step 1: Repository Interface ခွဲ (၃ ယောက်လုံး — မနက်)

```kotlin
interface PodcastRepository {
    fun getPodcasts(): Flow<List<Podcast>>
    fun getEpisodes(podcastId: String): Flow<List<Episode>>
    suspend fun getPodcast(id: String): Podcast?
}

class FirestorePodcastRepository(...) : PodcastRepository { ... }
```

> ၃ ယောက်လုံးရဲ့ code ကို ထိတယ် — **အတူတူ လုပ်ပါ**၊ ၃၀ မိနစ်နဲ့ ပြီးတယ်။

---

## Step 2: `MainDispatcherRule`

`viewModelScope` က `Dispatchers.Main` သုံးတယ် — unit test မှာ မရှိလို့ crash မယ်။

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(dispatcher)
    override fun finished(description: Description) = Dispatchers.resetMain()
}
```

> **B လည်း ဒီ rule လိုတယ်** — တစ်ယောက်ပဲ ရေးပြီး မျှသုံးပါ။ B နဲ့ ညှိပါ။

---

## Step 3: Test ၂ ခု ရေး

### Test 1 — စတင်ချိန် loading
```
ပေး  : FakeRepository (data မပို့သေး)
လုပ် : ViewModel ဆောက်
စစ်  : state.value.isLoading == true
```

### Test 2 — Data ဝင်ပြီး
```
ပေး  : FakeRepository → podcast ၅ ခု
လုပ် : ViewModel ဆောက် → advanceUntilIdle()
စစ်  : isLoading == false
      allPodcasts.size == 5
      podcasts.size == 5        ← filter က "အားလုံး" မို့ အကုန် ကျန်ရမယ်
      error == null
```

**Fake:**
```kotlin
class FakePodcastRepository(
    private val podcasts: List<Podcast> = emptyList()
) : PodcastRepository {
    override fun getPodcasts() = flowOf(podcasts)
    override fun getEpisodes(podcastId: String) = flowOf(emptyList<Episode>())
    override suspend fun getPodcast(id: String) = podcasts.firstOrNull { it.id == id }
}
```

---

## Step 4: Cross-Review — မင်း B ရဲ့ code ကို review

```
A  →  B ရဲ့ code review        ← မင်း
B  →  C ရဲ့ code review
C  →  A ရဲ့ code review        ← မင်းရဲ့ဟာကို C ဖတ်မယ်
```

**B ရဲ့ code မှာ စစ်ရမယ့် အချက်:**

- [ ] `collect` အောက်မှာ code ရှိလား (ရှိရင် ဘယ်တော့မှ မရောက်ဘူး)
- [ ] `try/catch (Exception)` က flow ကို ပတ်ထားလား (`.catch {}` သုံးရမယ်)
- [ ] `CancellationException` ကို rethrow လုပ်လား
- [ ] `loadJob?.cancel()` ပါလား
- [ ] `getPodcast()` က `null` ပြန်ရင် ဘယ်လို ကိုင်တွယ်လဲ
- [ ] Episode cover fallback ပါလား

### ✅ Day 5 Deliverable

- [ ] Repository interface ခွဲပြီး
- [ ] Test ၂ ခု pass
- [ ] B ရဲ့ code review ပြီး
- [ ] `dev` ကို merge ပြီး

---

# ✅ Slice A — Final Checklist

| # | Layer | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|---|
| 1 | DB | `getPodcasts()` က ၅ ခု ပြန်ပေး | ☐ |
| 2 | DB | `id` က အလွတ် မဟုတ် | ☐ |
| 3 | DB | `orderBy` ဆုံးဖြတ်ချက် ချပြီး | ☐ |
| 4 | DB | **Day 1 မှာ push ပြီး + B ကို ပြောပြီး** ⏰ | ☐ |
| 5 | Model | `allPodcasts` / `podcasts` ခွဲထား | ☐ |
| 6 | Model | `selectCategory()` က တကယ် filter လုပ် | ☐ |
| 7 | Model | Hard-code category list ဖျက်ပြီး | ☐ |
| 8 | Model | `loadJob?.cancel()` ပါ | ☐ |
| 9 | Model | Factory ရေးပြီး | ☐ |
| 10 | Model | `lastPlayed` ကို မထိဘဲ ချန်ထား | ☐ |
| 11 | UI | Loading / Error / Empty padding ပါ | ☐ |
| 12 | UI | `!!` တစ်ခုမှ မကျန် | ☐ |
| 13 | UI | Empty state ၂ မျိုး ခွဲထား | ☐ |
| 14 | UI | `onSeeAll` ဖြေရှင်းပြီး | ☐ |
| 15 | UI | `PodcastListItem` Myanmar font ပြင်ပြီး | ☐ |
| 16 | UI | အင်္ဂလိပ် စာသား ၃ ခု ပြောင်းပြီး | ☐ |
| 17 | UI | `@Preview` အလုပ်လုပ် | ☐ |
| 18 | Integration | အရင်ဆုံး merge ပြီး build အောင် | ☐ |
| 19 | Integration | Edge case ၆ ခု စစ်ပြီး | ☐ |
| 20 | Test | Test ၂ ခု pass | ☐ |

---

# 🔧 Troubleshooting

| Error / လက္ခဏာ | Layer | ဖြေရှင်းနည်း |
|---|---|---|
| `podcasts = 0` | DB | Collection name `podcasts` လား / rules `allow read: if true` လား |
| `PERMISSION_DENIED` | DB | Firestore rules — test mode သက်တမ်း ကုန်သွားလား |
| `firstId` အလွတ် | DB | `toObjects()` သုံးနေလား — `toListWithIds()` သုံးရမယ် |
| Podcast ၃ ခုပဲ ပေါ် (၅ ခု ရှိပေမဲ့) | DB | `orderBy` ထည့်ထားလား → field မပါတဲ့ doc ပျောက်တယ် |
| Chip နှိပ်လည်း ဘာမှ မဖြစ် | Model | `selectCategory()` မှာ filter မပါသေး |
| Chip က အင်္ဂလိပ်လို ပေါ် | Model | Hard-code list မဖျက်ရသေး |
| Retry တိုင်း data ၂ ဆ | Model | `loadJob?.cancel()` မထည့်ရသေး |
| Loading spinner က TopAppBar အောက် | UI | `Modifier.padding(padding)` မပေးရသေး |
| `NullPointerException` at HomeScreen | UI | `!!` သုံးနေတယ် |
| Data မရှိရင် အဖြူ screen | UI | `EmptyView` branch မထည့်ရသေး |
| "မရှိသေးပါ" တစ်ချက် လျှပ်ပြ | UI | `isLoading` ကို အရင် မစစ်ရသေး |
| မြန်မာစာ ပုံပျက် / ကျဉ်း | UI | `label*` style သုံးနေလား → `body*` ပြောင်း |
| `IllegalStateException: Main dispatcher` | Test | `MainDispatcherRule` မထည့်ရသေး |

---

# 🎯 Senior-level မှတ်ချက် ၅ ခု

**၁။ `!!` က ဖြေရှင်းနည်း မဟုတ်ဘူး — ရွှေ့ဆိုင်းတာပဲ**
Compiler က "ဒါ null ဖြစ်နိုင်တယ်" လို့ ပြောတာကို `!!` နဲ့ ပိတ်ပစ်တာက
crash ကို build time ကနေ demo time ကို ရွှေ့လိုက်တာပါ။

**၂။ Hard-code list က design အမှား၏ လက္ခဏာ**
`listOf("All", "News", "Tech")` ဆိုတာ "data က ဘယ်တော့မှ မပြောင်းဘူး" လို့
ယူဆထားတာ။ Firestore သုံးတဲ့ အကြောင်းရင်းက **data ပြောင်းနိုင်လို့**။
ဒီ ၂ ခု ဆန့်ကျင်နေတယ်။

**၃။ State အစီအစဉ်က user ရဲ့ ခံစားမှုကို ဆုံးဖြတ်တယ်**
`isLoading` ကို `isEmpty` ရဲ့ အောက်မှာ စစ်လိုက်ရုံနဲ့ app က ပျက်နေသလို ဖြစ်သွားမယ်။
Code က မှန်နေပေမဲ့ user အတွက် bug ပါပဲ။

**၄။ Font family က စာလုံးပေါ် မူတည်တယ် — style နာမည်ပေါ် မဟုတ်ဘူး**
`labelSmall` ဆိုတာ "သေးတဲ့ label" လို့ မထင်ပါနဲ့ — ဒီ project မှာ ဒါက **Poppins** ပါ။
မြန်မာစာ ထည့်ရင် glyph မရှိဘူး။ `Type.kt` ကို တစ်ခါ ဖတ်ထားပါ။

**၅။ မင်းက ပထမဆုံး merge ဖြစ်တယ် — မင်း build က အားလုံးရဲ့ အခြေခံ**
Day 3 ညနေ `./gradlew assembleDebug` မအောင်ဘဲ အိပ်မသွားပါနဲ့။
မင်း ပိတ်မိရင် ၃ ယောက်လုံး ပိတ်မိမယ်။

---

## 📅 တစ်ပတ်စာ အကျဉ်းချုပ်

| နေ့ | Layer | အဓိက အလုပ် | အန္တရာယ် |
|---|---|---|---|
| ၁ | 🗄️ DB | `getPodcasts()` စစ် + **မြန်မြန် push** | B ကို ပိတ်ဆို့ထားမိမယ် |
| ၂ | 🧠 Model | ချို့ယွင်းချက် ၃ ခု ပြင် | `lastPlayed` မှာ အချိန် မကုန်စေနဲ့ |
| ၃ | 🎨 UI | ချို့ယွင်းချက် ၄ ခု + font + Preview | Day 3 ညနေ build စစ် |
| ၄ | 🔗 Integration | **ပထမဆုံး merge** + C နဲ့ ပူးတွဲ | မင်း ပျက်ရင် အားလုံး ပျက် |
| ၅ | 🔄 Test | Interface + test ၂ ခု + B ကို review | Main dispatcher rule |
