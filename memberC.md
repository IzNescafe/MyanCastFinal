# Member C — Slice C: Category နှင့် ပုံစံ (Week 2)

> **ဒါက မင်းရဲ့ တစ်ပတ်စာ အလုပ် အပြည့်အစုံ**။
> Database → Model → UI ၃ ခုလုံး မင်း ကိုယ်တိုင် ရေးရမယ်။

---

## 📋 Slice C — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **တာဝန်** | Category filter + ကြာချိန်/ရက်စွဲ ပုံစံ ပြောင်းခြင်း |
| **Code ပမာဏ** | အနည်းဆုံး — ဒါပေမဲ့ **အမှီခိုခံရဆုံး** |
| **⏰ အရေးကြီးဆုံး** | **Day 2 ညနေ အထိ ပြီးရမယ်** — A ရော B ရော မင့် function စောင့်နေတယ် |
| **အထူး အားသာချက်** | မင်းရဲ့ function တွေက pure — **test အရေးလွယ်ဆုံး** |

### မင်း ပိုင်တဲ့ ဖိုင်များ

| ဖိုင် | လက်ရှိ | ဘာလုပ်ရမလဲ |
|---|---|---|
| `ui/home/CategoryFilter.kt` | 🔴 **မရှိသေး** | အသစ် ဆောက် |
| `domain/util/DurationFormatter.kt` | 🔴 **အလွတ်** | အစအဆုံး ရေး |
| `domain/util/DateFormatter.kt` | 🔴 **အလွတ်** | အစအဆုံး ရေး |
| `domain/util/EncodingUtil.kt` | 🔴 **အလွတ်** | ဆုံးဖြတ်ချက် ချ (ရေးစရာ မလိုနိုင်) |
| `ui/components/CategoryChips.kt` | 🟡 ၅၀ ကြောင်း | Font bug ပြင် |
| `ui/components/EpisodeRow.kt` | 🟡 ၈၄ ကြောင်း | Formatter ချိတ် + font bug ပြင် |
| Firebase Console | ⚠️ | Category data သန့်ရှင်းရေး |

### ⚠️ မင့်ကို မှီခိုနေတဲ့ သူများ — ဒါက မင်းရဲ့ အဓိက တာဝန်

```
Member A  →  buildCategories()  +  filterByCategory()
             ဒါတွေ မရှိရင် A ရဲ့ HomeViewModel ပြီးလို့ မရဘူး

Member B  →  formatDurationLabel()  +  formatRelativeDate()
             ဒါတွေ မရှိရင် B ရဲ့ EpisodeRow မှာ "1470 min" လို့ ပေါ်မယ်
```

> **မင်းရဲ့ code ပမာဏ အနည်းဆုံး ဖြစ်ပေမဲ့ deadline အကြပ်ဆုံးပါ။**
> Day 2 ညနေမှာ မပြီးရင် A ရော B ရော Day 3 မှာ ပိတ်မိမယ်။
> **ဒါကြောင့် Day 2 ကို အရင် ဖတ်ပြီး Day 1 ကို မြန်မြန် ပြီးအောင် လုပ်ပါ။**

---

## 🚦 Day 0 — စမလုပ်ခင် (၃၀ မိနစ်)

### ၁။ Firebase Console access ရယူ

မင်း **Editor** role လိုတယ် — category data ပြင်ဖို့။ Owner (MyoThura86) ကို ခိုင်း:

```
Firebase Console → ⚙️ Project settings → Users and permissions
                 → Add member → မင့် Google account → Editor
```

Project: **`myancast-personal`**

### ၂။ Branch ဆောက်

```bash
git checkout dev && git pull origin dev
git checkout -b feature/phase2-slice-c
```

### ၃။ ဒီ ၂ ဖိုင်ကို အရင် ဖတ်

| ဖိုင် | ဘာကို ရှာဖတ်ရမလဲ |
|---|---|
| `ui/theme/Type.kt` | `labelMedium` က ဘယ် font family လဲ? (အဖြေ — **Poppins**) |
| `ui/components/CategoryChips.kt` | ကြောင်း ၄၇ မှာ ဘယ် style သုံးထားလဲ? (အဖြေ — `labelMedium`) |

> ဒီ ၂ ချက်ပေါင်းရင် **bug တစ်ခု** ဖြစ်တယ် — Day 3 မှာ ရှင်းပြမယ်။

---

# 🗄️ Day 1 — DATABASE Layer

**ရည်မှန်းချက်** — Category data သန့်ရှင်းရေး + Zawgyi ဆုံးဖြတ်ချက်
**⏰ ဒီနေ့ကို ၄ နာရီနဲ့ ပြီးအောင် လုပ်ပါ** — Day 2 က ပိုအရေးကြီးတယ်

---

## Step 1: Category Data ကို စစ်

Firebase Console → Firestore → `podcasts` → document ၅ ခု တစ်ခုချင်း ဖွင့်

### ဘာကို ရှာရမလဲ

Category data မှာ ဒီ ပြဿနာ ၄ မျိုး ဖြစ်တတ်တယ်:

| ပြဿနာ | ဥပမာ | ဘာဖြစ်လဲ |
|---|---|---|
| **နောက်က space** | `"သတင်း "` | `"သတင်း" == "သတင်း "` → **false** → filter မရ |
| **ဘာသာစကား ရော** | `"News"` နဲ့ `"သတင်း"` ရော | Chip ၂ ခု ဖြစ်နေမယ် |
| **အလွတ်** | `""` | Chip အလွတ် တစ်ခု ပေါ်မယ် |
| **Field မပါ** | `category` field ကို မထည့်မိ | `""` ဖြစ်မယ် |

### ဘာလုပ်ရမလဲ

1. Podcast ၅ ခုလုံးရဲ့ `category` ကို **မြန်မာလို တစ်ပုံစံတည်း** ဖြစ်အောင် ပြင်
2. Space မပါစေနဲ့ — Console မှာ ရွေးပြီး ကြည့်ရင် သိသာတယ်
3. `category` field မပါတဲ့ document ရှိရင် ထည့်

**Phase 1 မှာ သတ်မှတ်ထားတဲ့ category ၅ မျိုး:**
```
သတင်း · နည်းပညာ · ဇာတ်လမ်း · ကျန်းမာရေး · စီးပွားရေး
```

4. **စာရွက်မှာ ချရေးထားပါ** — Day 2 မှာ test ရေးတဲ့အခါ လိုမယ်

> **Senior tip** — Data cleanup ကို **တစ်ထိုင်တည်း ပြီးအောင်** လုပ်ပါ။
> App က real-time listener သုံးထားတော့ မင်း Console မှာ ပြင်တိုင်း
> A နဲ့ B ရဲ့ ဖုန်းမှာ **ချက်ချင်း ပြောင်းသွားမယ်**။ ဖြည်းဖြည်းချင်း ပြင်နေရင်
> သူတို့က "data ကျပန်း ပြောင်းနေတယ်" ထင်ပြီး bug ရှာနေမယ်။
> **ပြီးတာနဲ့ group chat မှာ "category cleanup ပြီးပြီ" လို့ ပြောပါ။**

---

## Step 2: 🤔 Zawgyi ဆုံးဖြတ်ချက် (မင်း ဆုံးဖြတ်ရမယ်)

### လက်ရှိ အခြေအနေ

Phase 1 မှာ Settings screen မှာ Zawgyi toggle ရှိတယ်၊ `Type.kt` မှာ
`ZawgyiFamily` နဲ့ `PadaukFamily` ခွဲထားတယ်။ **ဒါပေမဲ့ —**

```
ခု လုပ်နေတာ    →  font ပဲ ပြောင်းတယ်
လိုအပ်တာ      →  စာသား ကိုယ်တိုင်လည်း ပြောင်းရမယ်
```

Firestore data က **Unicode** ဖြစ်တယ်။ Unicode စာသားကို Zawgyi font နဲ့ ပြရင်
**စာလုံး ပုံပျက်မယ်** (ဖတ်လို့ မရတော့ဘူး)။

### နည်းလမ်း ၂ မျိုး

| # | နည်းလမ်း | အလုပ်ပမာဏ | ရလဒ် |
|---|---|---|---|
| **၁** | **Unicode ပဲ သုံး** — toggle ကို ဖျောက် (သို့) "Phase 5" လို့ မှတ်ထား | **၅ မိနစ်** | Zawgyi feature မရှိ |
| ၂ | Rabbit converter library ထည့်ပြီး စာသား တကယ် ပြောင်း | **၁ ရက်** | Zawgyi အလုပ်လုပ် |

**⭐ နည်းလမ်း ၁ ကို အကြံပြုပါတယ်။** အကြောင်းရင်း ၃ ခု:

1. မင်းမှာ Day 2 က အရေးကြီးတယ် — A နဲ့ B က စောင့်နေတယ်
2. Course project အတွက် Zawgyi က grading criteria မဟုတ်ဘူး
3. ၁ ရက် ကုန်ပြီး ပြီးမပြီး မသေချာဘူး

**လုပ်ရမယ့် အရာ (၅ မိနစ်):**
`ui/settings/SettingsScreen.kt` မှာ Zawgyi toggle ရဲ့ subtitle ကို ပြောင်း:
```
"ဇော်ဂျီ သုံးနေသည်"  →  "Phase 5 တွင် ထည့်သွင်းမည်"
```
(သို့) toggle ကို `enabled = false` ထား။

> **ဆုံးဖြတ်ချက်ကို ဘယ်လိုပဲ ချချ — group chat မှာ ပြောပါ + `EncodingUtil.kt` ထဲမှာ
> comment ရေးထားပါ။** နောက်လာမယ့်သူ ဘာကြောင့် ဒီလို ဖြစ်နေလဲ သိအောင်။

### ✅ Day 1 Deliverable

- [ ] Firebase Console Editor access ရပြီး
- [ ] Podcast ၅ ခုလုံး `category` တစ်ပုံစံတည်း ဖြစ်ပြီး (space မပါ)
- [ ] Category စာရင်းကို ချရေးထားပြီး
- [ ] **Group chat မှာ "cleanup ပြီးပြီ" ပြောပြီး** 📣
- [ ] Zawgyi ဆုံးဖြတ်ချက် ချပြီး + comment ရေးပြီး

---

# 🧠 Day 2 — MODEL Layer ⭐ မင်းရဲ့ အရေးကြီးဆုံး နေ့

**ရည်မှန်းချက်** — Pure function ၃ ခု ရေး
**⏰ ဒီနေ့ ညနေ မပြီးရင် A နဲ့ B Day 3 မှာ ပိတ်မိမယ်**

---

## 💡 ဒီနေ့ Test အရင် ရေးပါ (TDD)

မင်းရဲ့ function တွေက **pure function** — input ပေး → output ရ၊ Android မလို၊
Firebase မလို၊ emulator မလို။ **ဒါကြောင့် test ရေးရ အလွယ်ဆုံးပါ။**

```
ပုံမှန် နည်း   :  function ရေး → app run → emulator စောင့် → စမ်း → ပြင် → ထပ်စမ်း
TDD နည်း      :  test ရေး → function ရေး → ./gradlew test → ၂ စက္ကန့်နဲ့ အဖြေ
```

> **မင်းက အဖွဲ့ထဲမှာ emulator မလိုဘဲ အလုပ်လုပ်နိုင်တဲ့ တစ်ယောက်တည်းပါ။**
> ဒီအခွင့်အရေးကို သုံးပါ — တစ်နေ့စာ အချိန် သက်သာမယ်။

---

## Step 1: `ui/home/CategoryFilter.kt` (ဖိုင်အသစ်)

### Function ၂ ခု — `class` မလိုဘူး

```kotlin
const val ALL_CATEGORY = "အားလုံး"

fun buildCategories(podcasts: List<Podcast>): List<String>
fun filterByCategory(podcasts: List<Podcast>, category: String): List<Podcast>
```

### `buildCategories()` လမ်းညွှန်

```
listOf(ALL_CATEGORY) + podcasts.map { it.category }
                                .filter { it.isNotBlank() }    ← အလွတ် ဖယ်
                                .distinct()                     ← ထပ်တာ ဖယ်
                                .sorted()                       ← အက္ခရာစဉ်
```

**Test ရေးရမယ့် အခြေအနေ:**

| Input | မျှော်လင့်ရမယ့် Output |
|---|---|
| Podcast ၃ ခု — `["သတင်း", "နည်းပညာ", "သတင်း"]` | `["အားလုံး", "နည်းပညာ", "သတင်း"]` |
| Podcast ၂ ခု — `["သတင်း", ""]` | `["အားလုံး", "သတင်း"]` |
| Podcast ၀ ခု | `["အားလုံး"]` |

### `filterByCategory()` လမ်းညွှန်

```
if (category == ALL_CATEGORY) podcasts
else podcasts.filter { it.category == category }
```

**Test ရေးရမယ့် အခြေအနေ:**

| Input | မျှော်လင့်ရမယ့် Output |
|---|---|
| `("အားလုံး")` | အကုန်လုံး ပြန်ရ |
| `("သတင်း")` | သတင်း ၂ ခုပဲ |
| `("မရှိတဲ့ category")` | အလွတ် list (crash မဖြစ်ရ) |

### ⚠️ `ALL_CATEGORY` ကို `const val` လုပ်ပါ

```kotlin
// ❌ မလုပ်ရ — A ရဲ့ code မှာလည်း "အားလုံး" လို့ ရိုက်ထားမယ်
if (category == "အားလုံး") ...

// ✅ လုပ်ရမယ်
const val ALL_CATEGORY = "အားလုံး"
```

> **A က ဒါကို default state အဖြစ် သုံးရမယ်** (`selectedCategory = ALL_CATEGORY`)။
> ၂ နေရာ hard-code ရေးရင် တစ်နေရာ ပြင်ဖို့ မေ့သွားပြီး **filter က တိတ်တဆိတ် ပျက်မယ်** —
> error မပြဘူး၊ list အလွတ် ဖြစ်နေရုံပဲ။ ဒါက ရှာရအခက်ဆုံး bug မျိုးပါ။
> **A ကို `ALL_CATEGORY` သုံးဖို့ ပြောပါ။**

---

## Step 2: `domain/util/DurationFormatter.kt`

### Function ၂ ခု

```kotlin
fun formatDuration(seconds: Int): String        // "24:30"      — progress bar အတွက်
fun formatDurationLabel(seconds: Int): String   // "၂၄ မိနစ်"   — list row အတွက်
```

### `formatDuration()` — Test table

| Input | Output | ဘာကို စစ်တာလဲ |
|---|---|---|
| `1470` | `"24:30"` | ပုံမှန် |
| `3725` | `"1:02:05"` | နာရီ ပါလာရင် |
| `45` | `"0:45"` | ၁ မိနစ် အောက် |
| `0` | `"0:00"` | သုည |
| `-5` | `"0:00"` | **အနုတ် — crash မဖြစ်ရ** |
| `86400` | `"24:00:00"` | ၁ ရက်ပြည့် |

**လမ်းညွှန်:**
```
seconds < 0 ဆိုရင် → "0:00" ပြန်ပေး (အရင်ဆုံး စစ်)

h = seconds / 3600
m = (seconds % 3600) / 60
s = seconds % 60

h > 0 ဆိုရင်  →  "$h:${m.pad()}:${s.pad()}"
မဟုတ်ရင်      →  "$m:${s.pad()}"

pad() = toString().padStart(2, '0')
```

> **နာရီကို pad မလုပ်ပါနဲ့** — `"01:02:05"` မဟုတ်ဘဲ `"1:02:05"` ဖြစ်ရမယ်။
> မိနစ်ကလည်း နာရီ မပါရင် pad မလုပ်ဘူး — `"0:45"` မှန်တယ်၊ `"00:45"` မမှန်ဘူး။

### `formatDurationLabel()` — မြန်မာ ဂဏန်း လိုတယ်

```
1470  →  "၂၄ မိနစ်"
3725  →  "၁ နာရီ ၂ မိနစ်"
45    →  "၁ မိနစ် အောက်"
0     →  ""              ← ဘာမှ မပြဘူး
```

**⚠️ မြန်မာ ဂဏန်း ပြောင်းဖို့ function တစ်ခု လိုတယ်:**

```
ASCII    0 1 2 3 4 5 6 7 8 9
Myanmar  ၀ ၁ ၂ ၃ ၄ ၅ ၆ ၇ ၈ ၉
```

```kotlin
fun Int.toMyanmarDigits(): String =
    toString().map { c ->
        if (c in '0'..'9') '၀' + (c - '0') else c
    }.joinToString("")
```

> **ဒီ function က မင်းရဲ့ `DateFormatter` မှာလည်း လိုမယ်** —
> `domain/util/` ထဲမှာ ဘုံနေရာ တစ်ခုမှာ ထားပါ။

---

## Step 3: `domain/util/DateFormatter.kt`

```kotlin
fun formatRelativeDate(timestamp: Timestamp?): String
```

### Test table

| Input | Output |
|---|---|
| `null` | `""` — **crash မဖြစ်ရ** |
| ၃၀ စက္ကန့် အရင်က | `"ခုလေးတင်"` |
| ၅ မိနစ် အရင်က | `"၅ မိနစ် အရင်က"` |
| ၂ နာရီ အရင်က | `"၂ နာရီ အရင်က"` |
| ၁ ရက် အရင်က | `"မနေ့က"` |
| ၅ ရက် အရင်က | `"၅ ရက် အရင်က"` |
| ၄၅ ရက် အရင်က | `"၁၅ စက်တင်ဘာ ၂၀၂၅"` |
| **အနာဂတ် ရက်စွဲ** | `"ခုလေးတင်"` — **crash မဖြစ်ရ** |

**လမ်းညွှန်:**
```
timestamp == null  →  "" ပြန်ပေး

diff = System.currentTimeMillis() - timestamp.toDate().time
diff < 0           →  "ခုလေးတင်"  (device နာရီ မမှန်တာ ဖြစ်နိုင်)
diff < 1 မိနစ်     →  "ခုလေးတင်"
diff < 1 နာရီ      →  "${မိနစ်.toMyanmarDigits()} မိနစ် အရင်က"
diff < 24 နာရီ     →  "${နာရီ.toMyanmarDigits()} နာရီ အရင်က"
diff < 48 နာရီ     →  "မနေ့က"
diff < 30 ရက်      →  "${ရက်.toMyanmarDigits()} ရက် အရင်က"
ကျန်               →  ရက်စွဲ အပြည့်အစုံ
```

### ⚠️ `Locale("my")` ကို မယုံပါနဲ့

```kotlin
SimpleDateFormat("d MMMM yyyy", Locale("my"))    // ⚠️ device ပေါ် မူတည်တယ်
```

Device အားလုံးမှာ မြန်မာ locale data မရှိဘူး — API level နဲ့ ထုတ်လုပ်သူပေါ် မူတည်ပြီး
`"15 September 2025"` လို့ အင်္ဂလိပ်လို ထွက်နိုင်တယ်။

**ပိုစိတ်ချရတဲ့နည်း — လကို ကိုယ်တိုင် သတ်မှတ်:**

```kotlin
private val MYANMAR_MONTHS = arrayOf(
    "ဇန်နဝါရီ", "ဖေဖော်ဝါရီ", "မတ်", "ဧပြီ", "မေ", "ဇွန်",
    "ဇူလိုင်", "သြဂုတ်", "စက်တင်ဘာ", "အောက်တိုဘာ", "နိုဝင်ဘာ", "ဒီဇင်ဘာ"
)
```

`Calendar` နဲ့ `MONTH`, `DAY_OF_MONTH`, `YEAR` ဆွဲထုတ်ပြီး ကိုယ်တိုင် ပေါင်းပါ။
ဒါဆို **device တိုင်းမှာ တူညီစွာ ပေါ်မယ်**။

---

## Step 4: ⏰ ပြီးတာနဲ့ ချက်ချင်း push

```bash
./gradlew testDebugUnitTest      # test အရင် pass ဖြစ်ပါစေ
git add . && git commit -m "feat(util): add category filter and formatters"
git push origin feature/phase2-slice-c
```

**📣 Group chat မှာ ပြောပါ:**
```
formatter တွေ ပြီးပြီ — branch: feature/phase2-slice-c
A: buildCategories(), filterByCategory(), ALL_CATEGORY သုံးလို့ရပြီ
B: formatDurationLabel(), formatRelativeDate() သုံးလို့ရပြီ
```

### ✅ Day 2 Deliverable

- [ ] `CategoryFilter.kt` — function ၂ ခု + `ALL_CATEGORY`
- [ ] `DurationFormatter.kt` — function ၂ ခု + မြန်မာ ဂဏန်း
- [ ] `DateFormatter.kt` — `null` ကို ကိုင်တွယ် + အနာဂတ် ရက်စွဲ ကိုင်တွယ်
- [ ] Unit test ၆ ခု pass
- [ ] **Push ပြီး + group chat မှာ ပြောပြီး** ⏰
- [ ] A ကို `ALL_CATEGORY` သုံးဖို့ ပြောပြီး

---

# 🎨 Day 3 — UI Layer

**ရည်မှန်းချက်** — Component ၂ ခု ချိတ် + **font bug ၂ ခု** ပြင်

---

## Step 1: 🐛 Poppins မှာ မြန်မာ glyph မရှိတဲ့ bug

### ပြဿနာ

`ui/theme/Type.kt` မှာ style တွေက ဒီလို ခွဲထားတယ်:

| Style အုပ်စု | Font family | မြန်မာစာ ရလား |
|---|---|---|
| `display*`, `headline*`, `title*`, `body*` | Padauk / Zawgyi | ✅ ရ |
| **`labelLarge`, `labelMedium`, `labelSmall`** | **Poppins** | ❌ **မရ** |

Poppins က Latin font — **မြန်မာ glyph လုံးဝ မရှိဘူး**။ မြန်မာစာ ထည့်ရင်
system fallback font နဲ့ ပေါ်မယ် → font ရော၊ အရွယ်အစား ကွဲ၊ line-height မမှန်။

### bug ၂ နေရာ — ၂ ခုလုံး မင်း ပိုင်တယ်

**၁။ `ui/components/CategoryChips.kt` ကြောင်း ၄၇:**
```kotlin
Text(
    text = category,                                    // ← "သတင်း" — မြန်မာစာ!
    style = MaterialTheme.typography.labelMedium        // ❌ Poppins
)
```
**ပြင်:** `style = MaterialTheme.typography.bodyMedium`

**၂။ `ui/components/EpisodeRow.kt` ကြောင်း ၆၅:**
```kotlin
Text(
    text = "${episode.duration / 60} min",              // ← ခု အင်္ဂလိပ် မို့ အဆင်ပြေ
    style = MaterialTheme.typography.labelSmall         // ❌ ဒါပေမဲ့ Step 2 မှာ...
)
```
မင်း ဒါကို `"၂၄ မိနစ်"` လို့ ပြောင်းလိုက်တာနဲ့ **မြန်မာစာ ဖြစ်သွားမယ်** →
style ကိုလည်း `bodySmall` ပြောင်းရမယ်။

> **စည်းကမ်း — မြန်မာစာ ပါတဲ့ `Text` မှာ `label*` style မသုံးရ**
> Phase 1 မှာ bottom nav ကို ဒီအတွက် `bodySmall` ပြောင်းထားပြီးပြီ
> (`MyanCastNavHost.kt` မှာ comment ပါတယ်)။
> **A ရဲ့ `PodcastListItem.kt` မှာလည်း တူတူ ရှိတယ် — A ကို ပြောပါ။**

---

## Step 2: `EpisodeRow` မှာ Formatter ချိတ်

**လက်ရှိ (ကြောင်း ၆၃–၆၇):**
```kotlin
Text(
    text = "${episode.duration / 60} min",
    style = MaterialTheme.typography.labelSmall,
    color = TextLo
)
```

**ပြဿနာ ၃ ခု:**
| # | ပြဿနာ |
|---|---|
| 1 | `duration = 0` ဆိုရင် `"0 min"` လို့ ပေါ်မယ် — ရုပ်ဆိုးတယ် |
| 2 | `duration = 45` (၄၅ စက္ကန့်) ဆိုရင်လည်း `"0 min"` |
| 3 | အင်္ဂလိပ် `"min"` က မြန်မာ app နဲ့ မလိုက်ဘူး |

**ပြင်ပြီး:**
```kotlin
val label = formatDurationLabel(episode.duration)
val date  = formatRelativeDate(episode.publishedAt)

// ၂ ခုလုံး ရှိမှ "·" ခံမယ်
val meta = listOf(label, date).filter { it.isNotBlank() }.joinToString(" · ")

if (meta.isNotBlank()) {
    Text(
        text = meta,
        style = MaterialTheme.typography.bodySmall,     // ← Myanmar family
        color = TextLo
    )
}
```

> **`duration = 0` ဆိုရင် လုံးဝ မပြပါနဲ့** — `formatDurationLabel(0)` က `""` ပြန်ပေးတယ်။
> `"— · ၂ ရက် အရင်က"` လိုမျိုး နေရာလွတ် မကျန်အောင် `filter { it.isNotBlank() }` သုံးပါ။
> **ဒါက B ရဲ့ screen မှာ ပေါ်မယ့် အရာ — B ကို ပြပါ။**

---

## Step 3: `CategoryChips` ကို `HomeScreen` မှာ ချိတ် (A နဲ့ အတူတူ)

> **⚠️ `HomeViewModel.kt` နဲ့ `HomeScreen.kt` က A ပိုင်တဲ့ ဖိုင်။**
> A push လုပ်ပြီးမှ pull ဆွဲပြီး ထည့်ပါ။ **A နဲ့ အတူထိုင်ပြီး လုပ်ပါ** — ၃၀ မိနစ်ပဲ ကြာတယ်။

**`HomeViewModel` မှာ ထည့်ရမယ့် အရာ ၃ နေရာ:**

```kotlin
// ၁။ Data ဝင်လာချိန်
.collect { list ->
    _state.update {
        it.copy(
            allPodcasts = list,
            podcasts    = filterByCategory(list, it.selectedCategory),   // ← မင်းရဲ့
            categories  = buildCategories(list),                          // ← မင်းရဲ့
            isLoading   = false
        )
    }
}

// ၂။ Chip နှိပ်ချိန်
fun selectCategory(category: String) {
    _state.update { s ->
        s.copy(
            selectedCategory = category,
            podcasts = filterByCategory(s.allPodcasts, category)          // ← မင်းရဲ့
        )
    }
}

// ၃။ Default state
val selectedCategory: String = ALL_CATEGORY                                // ← မင်းရဲ့
```

**`HomeScreen` မှာ `CategoryChips` က ရှိပြီးသား** (ကြောင်း ၁၄၇–၁၅၄) —
state ကနေ တကယ့် data ရောက်လာရုံပဲ လိုတယ်:
```kotlin
CategoryChips(
    categories = state.categories,        // ← ခု တကယ့် data ရောက်မယ်
    selected   = state.selectedCategory,
    onSelect   = onCategorySelect
)
```

---

## Step 4: စမ်းသပ်

| စမ်းရမယ့် အရာ | မျှော်လင့်ရမယ့် ရလဒ် |
|---|---|
| App ဖွင့် | Chip ၆ ခု ပေါ် (`အားလုံး` + category ၅ ခု) |
| `"သတင်း"` နှိပ် | သတင်း podcast ပဲ ကျန် |
| `"အားလုံး"` ပြန်နှိပ် | ၅ ခုလုံး ပြန်ပေါ် |
| Chip စာလုံး | **Padauk နဲ့ ပေါ်ရမယ်** (ပုံပျက် မဖြစ်ရ) |
| Episode row | `"၂၄ မိနစ် · ၂ ရက် အရင်က"` |
| `duration = 0` episode | ကြာချိန် မပေါ် (crash မဖြစ်ရ) |

### ✅ Day 3 Deliverable

- [ ] `CategoryChips` font bug ပြင်ပြီး (`labelMedium` → `bodyMedium`)
- [ ] `EpisodeRow` font bug ပြင်ပြီး (`labelSmall` → `bodySmall`)
- [ ] `EpisodeRow` မှာ formatter ၂ ခု ချိတ်ပြီး
- [ ] `duration = 0` ဆိုရင် မပြဘူး
- [ ] A နဲ့ အတူတူ `HomeViewModel` ချိတ်ပြီး
- [ ] Chip နှိပ်ရင် list တကယ် ပြောင်း
- [ ] A ကို `PodcastListItem` font bug အကြောင်း ပြောပြီး

---

# 🔗 Day 4 — INTEGRATION

**မင်း ဒုတိယ merge ဖြစ်မယ်** (A → **C** → B)

---

## Step 1: A ပြီးမှ merge

```bash
# A merge ပြီးပြီလား အရင်မေး
git checkout dev && git pull origin dev
git merge feature/phase2-slice-c
./gradlew assembleDebug
```

> **ဘာကြောင့် A ပြီးမှလဲ** — မင်းက A ရဲ့ `HomeViewModel.kt` နဲ့ `HomeScreen.kt` ကို
> ထိထားတယ်။ A မ merge ရသေးဘဲ မင်း merge ရင် conflict ကြီးမယ်။
> **Conflict ဖြစ်ရင် A နဲ့ အတူတူ ဖြေရှင်းပါ** — တစ်ယောက်တည်း မလုပ်ပါနဲ့။

---

## Step 2: Edge Case စစ် (မင်း တာဝန်ယူရမယ့် အပိုင်း)

| စမ်းရမယ့် အရာ | မျှော်လင့်ရမယ့် ရလဒ် |
|---|---|
| Category chip ၆ ခုလုံး တစ်ခုချင်း နှိပ် | မှန်မှန် filter ဖြစ် |
| Chip နှိပ်ပြီး rotate | ရွေးထားတဲ့ chip မပျောက်ရ |
| Console မှာ category အသစ် ထည့် | Chip အသစ် **ချက်ချင်း ပေါ်လာရမယ်** |
| Console မှာ category ဖျက် | Chip ပျောက်ရမယ် + crash မဖြစ်ရ |
| `duration = 0` episode | ကြာချိန် မပေါ် |
| `publishedAt` မပါတဲ့ episode | ရက်စွဲ မပေါ် (crash မဖြစ်ရ) |
| ဖုန်းရဲ့ နာရီကို အနာဂတ် ပြောင်းပြီး စမ်း | `"ခုလေးတင်"` — crash မဖြစ်ရ |

> **နောက်ဆုံး ၃ ခုက မင်းရဲ့ null/edge handling စစ်တာ။**
> Emulator မှာ Settings → Date & time ကနေ ပြောင်းလို့ရတယ်။

---

## Step 3: Data Flow ရှင်းပြနိုင်အောင် လေ့ကျင့်

```
Firebase Console  →  မင်း category ကို "သတင်း" လို့ ပြင်
        ↓  real-time listener
PodcastRepository.getPodcasts()
        ↓
HomeViewModel  →  allPodcasts
        ↓  buildCategories()      ← မင်းရဲ့ function
HomeUiState.categories = ["အားလုံး", "ကျန်းမာရေး", "စီးပွားရေး", ...]
        ↓
CategoryChips  →  chip ၆ ခု ပေါ်
        ↓  user က "သတင်း" နှိပ်
HomeViewModel.selectCategory("သတင်း")
        ↓  filterByCategory()     ← မင်းရဲ့ function
HomeUiState.podcasts = [သတင်း podcast တွေ ချည်း]
        ↓
PodcastListItem  →  မျက်လုံးနဲ့ မြင်ရတာ
```

### ✅ Day 4 Deliverable

- [ ] A ပြီးမှ merge ပြီး build အောင်
- [ ] Edge case ၇ ခု စစ်ပြီး
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
```

> မင်းရဲ့ code က repository မသုံးဘူး (pure function ချည်းပဲ) — ဒါပေမဲ့
> **၃ ယောက်လုံး နားလည်ထားရမယ့် refactor** မို့ ပါဝင်ပါ။

---

## Step 2: Test ကို ဖြည့်စွက်

Day 2 မှာ ရေးထားပြီးသား ဆိုရင် ဒီနေ့ **edge case ထပ်ဖြည့်ပါ**:

| Test | ဘာစစ်မလဲ |
|---|---|
| `formatDuration(1470)` | `"24:30"` |
| `formatDuration(3725)` | `"1:02:05"` |
| `formatDuration(0)` | `"0:00"` |
| `formatDuration(-5)` | `"0:00"` — crash မဖြစ်ရ |
| `formatRelativeDate(null)` | `""` — crash မဖြစ်ရ |
| `buildCategories(emptyList())` | `["အားလုံး"]` |
| `buildCategories(category အလွတ် ပါ)` | အလွတ် မပါရ |
| `filterByCategory(မရှိတဲ့ category)` | အလွတ် list |

> **မင်းရဲ့ test တွေက emulator မလိုဘူး** — `./gradlew testDebugUnitTest` ၂ စက္ကန့်နဲ့ ပြီးတယ်။
> A နဲ့ B က `MainDispatcherRule` လိုတယ်၊ မင်း မလိုဘူး။

---

## Step 3: Cross-Review — မင်း A ရဲ့ code ကို review

```
A  →  B ရဲ့ code review
B  →  C ရဲ့ code review        ← မင်းရဲ့ဟာကို B ဖတ်မယ်
C  →  A ရဲ့ code review        ← မင်း
```

**A ရဲ့ code မှာ စစ်ရမယ့် အချက်:**

- [ ] `!!` တစ်ခုမှ မကျန်ဘူးလား (`HomeScreen` ကြောင်း ၈၆ မှာ ရှိခဲ့တယ်)
- [ ] `LoadingView` / `ErrorView` မှာ `Modifier.padding(padding)` ပါလား
- [ ] `EmptyView` branch ထည့်ပြီးလား
- [ ] `when` မှာ `isLoading` ကို **အရင်ဆုံး** စစ်လား
- [ ] `loadJob?.cancel()` ပါလား
- [ ] Hard-code category list ဖျက်ပြီးလား
- [ ] **`ALL_CATEGORY` const ကို သုံးလား၊ `"အားလုံး"` လို့ ရိုက်ထားလား** ⚠️
- [ ] `PodcastListItem` မှာ `label*` style သုံးနေသေးလား

> **နောက်ဆုံး ၂ ခုက မင်းနဲ့ တိုက်ရိုက် ဆိုင်တယ်** — A က `"အားလုံး"` လို့
> လက်နဲ့ ရိုက်ထားရင် filter က တိတ်တဆိတ် ပျက်နေမယ်။

### ✅ Day 5 Deliverable

- [ ] Repository interface ခွဲပြီး (၃ ယောက်လုံး)
- [ ] Test ၈ ခု pass
- [ ] A ရဲ့ code review ပြီး
- [ ] `dev` ကို merge ပြီး

---

# ✅ Slice C — Final Checklist

| # | Layer | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|---|
| 1 | DB | Firebase Console Editor access ရပြီး | ☐ |
| 2 | DB | Category ၅ မျိုး တစ်ပုံစံတည်း (space မပါ) | ☐ |
| 3 | DB | Cleanup ပြီးကြောင်း အဖွဲ့ကို ပြောပြီး | ☐ |
| 4 | DB | Zawgyi ဆုံးဖြတ်ချက် ချပြီး + comment ရေးပြီး | ☐ |
| 5 | Model | `ALL_CATEGORY` က `const val` | ☐ |
| 6 | Model | `buildCategories()` — အလွတ် ဖယ်၊ ထပ်တာ ဖယ် | ☐ |
| 7 | Model | `filterByCategory()` — မရှိတဲ့ category မှာ crash မဖြစ် | ☐ |
| 8 | Model | `formatDuration(-5)` က crash မဖြစ် | ☐ |
| 9 | Model | မြန်မာ ဂဏန်း ပြောင်း function ရှိ | ☐ |
| 10 | Model | `formatRelativeDate(null)` က `""` | ☐ |
| 11 | Model | အနာဂတ် ရက်စွဲ မှာ crash မဖြစ် | ☐ |
| 12 | Model | လ နာမည် hard-code (Locale မမှီခို) | ☐ |
| 13 | Model | **Day 2 ညနေ push ပြီး + ပြောပြီး** ⏰ | ☐ |
| 14 | UI | `CategoryChips` → `bodyMedium` | ☐ |
| 15 | UI | `EpisodeRow` → `bodySmall` | ☐ |
| 16 | UI | `duration = 0` ဆိုရင် မပြ | ☐ |
| 17 | UI | A နဲ့ အတူတူ `HomeViewModel` ချိတ်ပြီး | ☐ |
| 18 | UI | A ကို `PodcastListItem` font bug ပြောပြီး | ☐ |
| 19 | Integration | Edge case ၇ ခု စစ်ပြီး | ☐ |
| 20 | Test | Test ၈ ခု pass | ☐ |

---

# 🔧 Troubleshooting

| Error / လက္ခဏာ | Layer | ဖြေရှင်းနည်း |
|---|---|---|
| Chip နှိပ်လည်း list မပြောင်း | Model | A က `filterByCategory()` ခေါ်ပြီးလား |
| Chip နှိပ်ရင် list အလွတ် | Model | `"အားလုံး"` hard-code ၂ နေရာ ရှိလား — `ALL_CATEGORY` သုံး |
| Category မှာ space ပါနေ | DB | Console မှာ field ရွေးကြည့် — နောက်က space သိသာတယ် |
| Chip ၂ ခု တူနေ | DB | `"သတင်း"` နဲ့ `"သတင်း "` ၂ ခု ရှိနေတယ် |
| Chip အလွတ် တစ်ခု ပေါ် | Model | `filter { it.isNotBlank() }` မထည့်ရသေး |
| မြန်မာစာ ပုံပျက် / ကျဉ်း | UI | `label*` style သုံးနေတယ် → `body*` ပြောင်း |
| ဂဏန်းက `24` လို့ ပေါ် (`၂၄` မဟုတ်) | Model | `toMyanmarDigits()` မခေါ်ရသေး |
| လ နာမည် အင်္ဂလိပ်လို ပေါ် | Model | `Locale("my")` မယုံရဘူး — array hard-code |
| `"0 min"` လို့ ပေါ် | UI | `duration = 0` ကို မစစ်ရသေး |
| `"— · ၂ ရက်"` နေရာလွတ် | UI | `filter { it.isNotBlank() }` မသုံးရသေး |
| `publishedAt` null မှာ crash | Model | `Timestamp?` nullable လက်ခံလား |
| Test မှာ မြန်မာစာ မတူဘူး | Test | ဖိုင် encoding UTF-8 ဟုတ်လား |

---

# 🎯 Senior-level မှတ်ချက် ၅ ခု

**၁။ မင်းရဲ့ code ပမာဏ အနည်းဆုံး ဖြစ်ပေမဲ့ အန္တရာယ် အများဆုံး**
`formatDuration()` က episode တိုင်းမှာ ခေါ်ခံရတယ်။ `buildCategories()` က
Home ဖွင့်တိုင်း ခေါ်ခံရတယ်။ မင်း crash တစ်ခု ထည့်လိုက်ရင် **screen တိုင်းမှာ**
ပေါ်မယ်။ ဒါကြောင့် edge case (`0`, `-5`, `null`) တွေ အရေးကြီးတာ။

**၂။ Pure function က အဖွဲ့ရဲ့ အကောင်းဆုံး လက်ဆောင်**
မင်းရဲ့ function တွေမှာ Android မလို၊ Firebase မလို၊ state မလို — input → output ပဲ။
ဒါကြောင့် **emulator မလိုဘဲ ၂ စက္ကန့်နဲ့ test လုပ်လို့ရတယ်**။
A နဲ့ B က emulator စောင့်နေရချိန်မှာ မင်း ၅၀ ခါ စမ်းပြီးသွားပြီ။

**၃။ Hard-code string က ၂ နေရာ ရှိတာနဲ့ bug ဖြစ်ပြီ**
`"အားလုံး"` ကို မင်းက `CategoryFilter` မှာ၊ A က `HomeUiState` မှာ သီးခြား ရိုက်ရင် —
တစ်နေ့ တစ်ယောက်က `"အားလုံး "` (space ပါ) ရိုက်မိမယ်။ Compile အောင်မယ်။
Test pass မယ်။ **App မှာပဲ filter က တိတ်တဆိတ် ပျက်နေမယ်။**

**၄။ Locale ကို မယုံပါနဲ့ — device တိုင်း မတူဘူး**
`Locale("my")` က Samsung မှာ မြန်မာလို၊ Pixel emulator မှာ အင်္ဂလိပ်လို ထွက်နိုင်တယ်။
Demo ချိန်မှာ ဘယ် device သုံးမလဲ မသိဘူး။ **ကိုယ်တိုင် သတ်မှတ်ထားတာ စိတ်ချရတယ်။**

**၅။ Data cleanup က code အလုပ် မဟုတ်ပေမဲ့ code bug ဖြစ်စေတယ်**
`"သတင်း "` (နောက်က space တစ်ခု) က filter တစ်ခုလုံး ပျက်စေတယ်။
Code ဘက်က ဘယ်လောက် ကောင်းကောင်း data မှားရင် ပျက်တာပဲ။
**Day 1 ကို ပေါ့ပေါ့ မတွေးပါနဲ့။**

---

## 📅 တစ်ပတ်စာ အကျဉ်းချုပ်

| နေ့ | Layer | အဓိက အလုပ် | အန္တရာယ် |
|---|---|---|---|
| ၁ | 🗄️ DB | Category cleanup + Zawgyi ဆုံးဖြတ် | Cleanup လုပ်နေစဉ် အဖွဲ့ကို ပြောပါ |
| ၂ | 🧠 Model | **Function ၃ ခု — ညနေ ပြီးရမယ်** ⏰ | မပြီးရင် A နဲ့ B ပိတ်မိမယ် |
| ၃ | 🎨 UI | Font bug ၂ ခု + formatter ချိတ် | `HomeScreen` က A ပိုင် — အတူလုပ် |
| ၄ | 🔗 Integration | A ပြီးမှ merge | Conflict ဆို A နဲ့ အတူဖြေရှင်း |
| ၅ | 🔄 Test | Edge case test + A ကို review | `ALL_CATEGORY` သုံးမသုံး စစ် |
