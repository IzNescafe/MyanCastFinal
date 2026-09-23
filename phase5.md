# Phase 5 — Polish + Testing + Presentation (Week 5)

> **အရေးကြီး** — ဒါက **plan guide** ပါ။ Phase 1–4 အပေါ်မှာ ဆက်တည်ဆောက်ပါ။
>
> အခြေခံ — `fullscope.md` (Week 5 + Quality Checklist)၊ `phase2/3/4-checklist.md` ရဲ့
> ကျန်ရှိနေတဲ့ အလုပ်များ၊ နဲ့ 2026-09-24 က `main` ပေါ်က code အခြေအနေ။

---

## Phase 5 — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **ရည်ရွယ်ချက်** | Feature အသစ် **မထည့်တော့ဘူး** — ရှိတာကို ချောအောင်၊ မကျေတာ ရှင်းအောင်၊ demo လုပ်လို့ရအောင် |
| **ကြာချိန်** | Day 0 (၁ နာရီ) + ၅ ရက် |
| **အလုပ်ခွဲဝေပုံ** | Slice မဟုတ်တော့ဘူး — **တာဝန် အလိုက်** (A = UX/တင်ပြ · B = စွမ်းဆောင်ရည်/release · C = quality/data/docs) |
| **အဆုံးမှာ ရရမယ့် ရလဒ်** | Crash မရှိ · warning ၀ · test အားလုံး pass · README ရှိ · demo script ရှိ · APK build ရ · ၃ ယောက်လုံး ရှင်းပြနိုင် |

### ⚠️ Phase 5 ရဲ့ အကြီးမားဆုံး အန္တရာယ် — **feature အသစ် ထည့်ချင်စိတ်**

```
Sleep timer ထည့်ရမလား?  →  ❌ မထည့်ဘူး
Favorites ထည့်ရမလား?    →  ❌ မထည့်ဘူး
Download ထည့်ရမလား?     →  ❌ မထည့်ဘူး
Queue screen?           →  ❌ မထည့်ဘူး
```

> `fullscope.md` ရဲ့ nice-to-have တွေက **"အချိန်ရရင်"** ပါ။ ခု အချိန်က demo အတွက်ပဲ ရှိတယ်။
> Feature အသစ် တစ်ခု ထည့်တာက bug အသစ် ၃ ခု ခေါ်လာတယ် — Week 5 မှာ bug ရှာချိန် မရှိတော့ဘူး။
> **ဘယ်သူမှ feature အသစ် မထည့်ရ** — ဒါက Day 0 မှာ သဘောတူရမယ့် ပထမဆုံး အချက်ပါ။

---

## Phase 5 က ဘာအပေါ်မှာ စတည်လဲ (code ထဲ စစ်ပြီး)

| အခြေအနေ | ဘာ |
|---|---|
| ✅ | MVP feature **၁၃ / ၁၅** ပြီး (`phase4-checklist.md` section 7) |
| ✅ | Bottom nav ၅ ခုလုံး တကယ့် screen — `PlaceholderScreen` တစ်ခုမှ မကျန် |
| ✅ | Unit test **၁၀၉ ခု** pass · build warning ၀ ခု |
| ✅ | Contract ပုံစံ (interface + fake) ၂ ပတ်ဆက် အလုပ်ဖြစ် |
| ⬜ | **Device test ၂၆ ခု** ကျန် (Phase 3 ၁၁ + Phase 4 ၁၅) |
| ⬜ | **Cross-review ၄ ခု** ကျန် (Phase 3: A→C, B→A · Phase 4: A→B, B→C) |
| ⬜ | A ရဲ့ code မှာ finding ၆ ချက် (`phase4-checklist.md` 6.1) |
| ⬜ | Zawgyi toggle — ဆုံးဖြတ်ချက် မချရသေး (MVP feature #13) |
| ⬜ | Firestore `duration` data မကိုက် — demo မှာ မြင်ရမယ် |
| ⬜ | `README.md` မရှိ · `proguard-rules.pro` မရှိ |
| ⬜ | Dead code — `MainActivity` comment block · `FirebaseSource.kt` · `SettingsViewModel.kt` |
| ⬜ | App icon က default Android icon · `versionName = "1.0"` |

---

## ⭐ Day 0 — Debt Triage (၃ ယောက်လုံး၊ ၁ နာရီ)

### ဘာကြောင့် လိုလဲ

Phase 3/4 မှာ **contract** က အလုပ်ဖြစ်ခဲ့တယ် — ဘာလုပ်မလဲ အရင် သဘောတူလို့။
Phase 5 မှာ contract မလိုတော့ဘူး (feature အသစ် မရှိ) — ဒါပေမဲ့ **ကျန်နေတဲ့ အလုပ်က ၄၀ ခုကျော်** ရှိတယ်။
အားလုံး လုပ်ဖို့ အချိန် မရှိဘူး။ ဒါကြောင့် Day 0 မှာ **တစ်ခုချင်း ဆုံးဖြတ်**ရမယ်:

```
🔴 FIX   — demo/အမှတ်ကို ထိခိုက်တယ် → ဒီအတ ပြင်ရမယ်
🟡 DROP  — ထိခိုက်မှု နည်း → Phase 5 မလုပ်ဘူး၊ README မှာ "known limitation" လို့ ရေး
⚪ KEEP  — အခြေအနေအတိုင်း သဘောတူပြီး ထား (ဆုံးဖြတ်ချက်ကို ရေးထား)
```

### Day 0 မှာ ဆုံးဖြတ်ရမယ့် ၈ ခု

| # | ကိစ္စ | အကြံပြု |
|---|---|---|
| 1 | Feature အသစ် ထည့်မလား | **❌ မထည့်ဘူး** (အထက်က အကြောင်းရင်း) |
| 2 | **Zawgyi toggle** (MVP #13) | ⚪ **KEEP** — Unicode ပဲ သုံး၊ toggle disable · README + slide မှာ ဆုံးဖြတ်ချက် ရှင်းပြ (Rabbit ~၁ ရက်) |
| 3 | **Firestore `duration` မကိုက်** | 🔴 **FIX** — demo podcast ၁ ခုရဲ့ episode ၄ ခု အနည်းဆုံး ပြင် (၁၀ မိနစ်) |
| 4 | A ရဲ့ finding ၆ ချက် | 🔴 **FIX** — ၂၀ မိနစ်ပဲ · `android.icu` import က domain purity ချိုးတယ် |
| 5 | Release build (minify) | ⚪ **KEEP `isMinifyEnabled = false`** — R8 က Firestore data class ဖြတ်မိရင် demo ပျက်မယ် · `proguard-rules.pro` ဖိုင်သာ ဖန်တီးထား |
| 6 | App icon | 🔴 **FIX** (A) — default icon က demo မှာ အမှတ် ဖြတ်စရာ · Image Asset Studio နဲ့ ၁၅ မိနစ် |
| 7 | Process death ပြီး mini player အလွတ် | ⚪ **KEEP** — README မှာ known limitation |
| 8 | History ၂၀ limit | ⚪ **KEEP** — course project scale မှာ ပြဿနာ မရှိ |

### Day 0 ရလဒ် — Bug/Debt Board တစ်ခု

Group chat (သို့) စာရွက်မှာ ဒီပုံစံနဲ့ ရေး — Day 1–3 မှာ ဒီ board ကိုပဲ ဖြတ်သွားမယ်:

```
🔴 FIX (Day 1–2)
  [ ] A — finding ၆ ချက် (PlaybackProgress import, exportSchema, coverURL, toSet, % test name, history limit)
  [ ] A — app icon + strings
  [ ] B — device test 1-8 (Phase 4) + 1,2,3,5 (Phase 3)
  [ ] C — Firestore duration data + dead code ဖျက် + README
  ...
🟡 DROP  (README မှာ ရေး)
  [ ] Sleep timer / Favorites / Queue / Download / FCM
  [ ] Zawgyi text conversion
⚪ KEEP  (ဆုံးဖြတ်ချက် မှတ်ထား)
  [ ] minify off · process death mini player · history limit
```

### ✅ Day 0 Deliverable
- [ ] "Feature အသစ် မထည့်ဘူး" ၃ ယောက်လုံး သဘောတူ
- [ ] ၈ ခု ဆုံးဖြတ်ပြီး — FIX/DROP/KEEP ခွဲပြီး
- [ ] Bug/Debt board ရေးပြီး (ဘယ်သူ ဘာလုပ်မလဲ ရှင်းပြီး)
- [ ] Demo လမ်းကြောင်း ရွေးပြီး (Day 4 ကြည့်)

---

## ⭐ တာဝန် ခွဲဝေမှု

#### 🅰️ A — UX Polish + Presentation

| အလုပ် | ဖိုင်/နေရာ |
|---|---|
| Finding ၆ ချက် ပြင် | `PlaybackProgress.kt` · `AppDatabase.kt` · `LibraryEntity.kt` · `RoomLibraryRepository.kt` · `LibraryDao.kt` · `RoomLibraryRepositoryTest.kt` |
| App icon + app name | `res/mipmap-*` (Image Asset Studio) · `strings.xml` |
| Empty/Error state audit — screen ၉ ခုလုံး | `ui/**` |
| Dark ↔ Light — screen တိုင်း ဖတ်လို့ရမလား | `ui/theme/` |
| Slide + demo script ရေး | `docs/` (သို့) Google Slides |
| Device test 1–4 (Library) + 13–16 (Phase 3 mini player) | — |

#### 🅱️ B — Performance + Release

| အလုပ် | ဖိုင်/နေရာ |
|---|---|
| Device test 1–8 (News + play) + Phase 3 ရဲ့ 1,2,3,5 (background/lock screen/ဖုန်းဝင်) | — |
| Startup time + scroll jank စစ် (Logcat `Choreographer` skipped frames) | — |
| `proguard-rules.pro` ဖန်တီး (minify မဖွင့်ရင်လည်း ဖိုင် ရှိရမယ်) | `app/proguard-rules.pro` |
| `assembleRelease` အလုပ်လုပ်မလုပ် စစ် (unsigned APK ရရင် ရပြီ) | `app/build.gradle.kts` |
| Firestore listener leak စစ် — screen ပိတ်ရင် ရပ်လား | `player/` · `data/` |
| Process death / notification behaviour မှတ်တမ်း (README အတွက်) | — |

#### 🅲 C — Quality + Data + Docs

| အလုပ် | ဖိုင်/နေရာ |
|---|---|
| **Firestore `duration` data ပြင်** (အနည်းဆုံး demo podcast) | Firebase Console |
| Dead code ဖျက် — `MainActivity` block · `FirebaseSource.kt` · `SettingsViewModel.kt` | `app/src/main/` |
| **`README.md` ရေး** (အောက်မှာ ပုံစံ ပါ) | `README.md` |
| Device test 9–15 (Search + ဆက်နားထောင်ရန်) + Phase 3 ရဲ့ 7–12 | — |
| Test ကျန်တာ ဖြည့် — `SettingsScreen` · `EpisodeRow` · `PlaybackState` | `app/src/test/` |
| Zawgyi ဆုံးဖြတ်ချက်ကို README + `EncodingUtil.kt` မှာ အတည်ပြု | `domain/util/EncodingUtil.kt` |

---

# Day 1 — 🔴 DEBT BURN-DOWN

> **ရည်မှန်းချက်** — Day 0 board ရဲ့ 🔴 FIX အားလုံး ပြီးအောင်။ **Code ပြင်တာ ဒီနေ့ပဲ**။

### 🔰 ၃ ယောက်လုံး — မနက် ၃၀ မိနစ်

```powershell
git checkout main; git pull origin main
.\gradlew clean assembleDebug     # ★ clean နဲ့ — warning အားလုံး ပြန်ပေါ်မယ်
.\gradlew testDebugUnitTest
```

> **`clean` ဘာကြောင့် လိုလဲ** — Gradle က task တွေ `UP-TO-DATE` ဖြစ်နေရင် warning ပြန်မပြဘူး။
> Phase 4 မှာ A ရဲ့ KSP warning ၂ ခုကို ဒီအတွက် မမြင်ခဲ့ဘူး။

Warning စာရင်းကို ရေးချပြီး **၀ ဖြစ်အောင်** ၃ ယောက် ခွဲပြီး ပြင်ပါ။

### 🅰️ A — finding ၆ ချက် + icon
### 🅱️ B — `proguard-rules.pro` + `assembleRelease`
### 🅲 C — data + dead code + README အစ

### ✅ Day 1 Deliverable
- [ ] `clean assembleDebug` မှာ warning **၀ ခု**
- [ ] Test အားလုံး pass
- [ ] App icon အသစ် ပေါ်
- [ ] Firestore `duration` ပြင်ပြီး (`EpisodeRow` မှာ အချိန် မှန်ပြီလား device မှာ ကြည့်)

---

# Day 2 — 🐛 BUG HUNT (device ပေါ်မှာ)

> **ရည်မှန်းချက်** — device test **၂၆ ခု** ပြီးအောင်။ ၃ ယောက်လုံး **ဖုန်းအစစ်** နဲ့။

### စည်းကမ်း ၃ ခု

| # | စည်းကမ်း | ဘာကြောင့် |
|---|---|---|
| 1 | တွေ့တဲ့ bug တိုင်း **board မှာ ရေး** — ချက်ချင်း မပြင်နဲ့ | ပြင်ရင်း အသစ် ပေါက်တာ ကွာသွားမယ် |
| 2 | Bug တစ်ခု = **ပိုင်ရှင် ၁ ယောက်** | ၂ ယောက် တစ်ဖိုင် ပြင်ရင် conflict |
| 3 | 🔴 (demo ပျက်) / 🟡 (မလှ) / ⚪ (မမြင်သာ) ခွဲ | 🔴 ပဲ Day 3 မှာ ပြင်မယ် |

### စမ်းရမယ့် စာရင်း

| ရင်းမြစ် | အရေအတွက် | ဘယ်သူ |
|---|---|---|
| `phase3-checklist.md` section 6.1 | ၁၁ (background/lock screen/ဖုန်းဝင်/mini player) | B ၄ · A ၄ · all ၃ |
| `phase4-checklist.md` section 6.2 | ၁၅ (Library/News/Search/ဆက်နားထောင်ရန်) | A ၄ · B ၄ · C ၂ · all ၁ |

### ထပ်ဖြည့် စမ်းရမယ့် အခြေအနေ ၆ ခု (Phase 5 အသစ်)

| # | စမ်းချက် | မျှော်လင့်ရမယ့် ရလဒ် |
|---|---|---|
| 1 | Airplane mode နဲ့ app ဖွင့် (cold start) | Screen ၅ ခုလုံး crash မဖြစ် · error/empty ပြ |
| 2 | Firestore data အလွတ် (Console မှာ collection ရှင်း — မစမ်းပါနဲ့၊ filter နဲ့ တူတူ) | Empty state ပြ |
| 3 | Bottom nav ၅ ခုကို မြန်မြန် ၂၀ ခါ နှိပ် | Crash မဖြစ် · back stack မပွား |
| 4 | Back ကို screen တိုင်းကနေ နှိပ် → app ထွက် | Crash မဖြစ် |
| 5 | Battery saver / Doze mode မှာ background playback | ရပ်သွားရင် README မှာ မှတ် |
| 6 | Device ၂ မျိုး (API မတူ) | ၂ ခုလုံး run ရ |

### ✅ Day 2 Deliverable
- [ ] Device test ၂၆ + အသစ် ၆ = **၃၂ ခု** စစ်ပြီး
- [ ] Bug board မှာ 🔴/🟡/⚪ ခွဲပြီး
- [ ] Device ၂ မျိုးမှာ run ပြီး

---

# Day 3 — 🔧 FIX + PERFORMANCE + REVIEW

> **ရည်မှန်းချက်** — Day 2 ရဲ့ 🔴 bug အားလုံး ပြင် + cross-review ပြီးအောင်

### Step 1: 🔴 Bug ပြင် (မနက်)
- ပိုင်ရှင်အလိုက် ပြင် · ပြင်ပြီးရင် **test တစ်ခု ထပ်ရေး** (နောက်တစ်ခါ မဖြစ်အောင်)
- ပြင်လို့ မရတာ ရှိရင် 🟡 ပြောင်းပြီး README ကို ရွှေ့

### Step 2: စွမ်းဆောင်ရည် (B ဦးဆောင်၊ ၁ နာရီ)

| စစ်ချက် | ဘယ်လို | မျှော်လင့်ချက် |
|---|---|---|
| Cold start | App ပိတ်ပြီး ဖွင့် → ပထမ screen ပေါ်ချိန် | ၂ စက္ကန့် အောက် |
| Scroll jank | Home/News မှာ မြန်မြန် scroll → Logcat `Choreographer: Skipped` | Frame skip ၅၀ အောက် |
| Listener leak | Screen ၅ ခု ဝင်/ထွက် ၁၀ ခါ → memory profiler | မတက်သွားရ |
| APK size | `assembleRelease` → `app-release-unsigned.apk` | မှတ်ထားရုံ (slide အတွက်) |

### Step 3: Cross-review ကျန်တာ ၄ ခု (နေ့လယ်)

```
Phase 3 ကျန် — A → C  ·  B → A
Phase 4 ကျန် — A → B  ·  B → C
```

**Review မှာ စစ်ရမယ့် အချက် (Phase 1–4 စည်းကမ်း စုစည်း):**
- [ ] `!!` မရှိ · ViewModel ထဲ `android.*` / Media3 / Room type မရှိ
- [ ] `loadJob?.cancel()` ပါ · `collectAsStateWithLifecycle()` သုံး
- [ ] မြန်မာစာအပေါ် `label*` (Poppins) မရှိ
- [ ] Empty / Error / Loading ၃ မျိုး ပါ · full-screen empty က "data လုံးဝ မရှိ" မှာပဲ
- [ ] Route က `Screen.kt` ကနေပဲ · hard-code string မရှိ
- [ ] Pure logic က top-level function (test လုပ်လို့ရ)
- [ ] `@Preview` က private stateless content ပေါ်မှာပဲ

### ✅ Day 3 Deliverable
- [ ] 🔴 bug အားလုံး ပြင်ပြီး (သို့) 🟡 ပြောင်းပြီး README မှာ ရေးပြီး
- [ ] စွမ်းဆောင်ရည် ၄ ချက် မှတ်ပြီး
- [ ] Cross-review ၄ ခု ပြီး → **၃ ယောက်လုံး တစ်ယောက်စီရဲ့ code ဖတ်ဖူးပြီ**
- [ ] `main` မှာ build + test အားလုံး pass

---

# Day 4 — 📊 PRESENTATION အဆင်သင့်

> **ရည်မှန်းချက်** — Slide + demo script + README ပြီးအောင်၊ ပြီးရင် ၁ ခါ ပြန်လေ့ကျင့်

### Step 1: Demo လမ်းကြောင်း (၅ မိနစ်) — ၃ ယောက်လုံး အတူ ရွေး

```
၁။ Home          → podcast ၅ ခု · category chip နှိပ်ပြ · "ဆက်နားထောင်ရန်" card
၂။ Detail        → episode list · "သိမ်း" နှိပ် (Library မှာ ပေါ်မယ်)
၃။ Episode ဖွင့် → Mini player ပေါ်လာ → Full Player
၄။ Full Player   → seek ဆွဲ · ၃၀ စက္ကန့် ကျော် · speed 1.5x (အသံ ပြောင်းတာ ကြားပြ)
၅။ Home ပြန်     → mini player ဆက်ပေါ်နေ · app ပိတ်ပြ → notification ကနေ ထိန်းပြ
၆။ News          → featured + list · detail → audio ဖွင့်ပြ
၇။ Search        → "နည်း" ရိုက်ပြ
၈။ Library       → သိမ်းထားတာ + မှတ်တမ်း (progress bar)
၉။ Settings      → Dark → Light ပြောင်းပြ
၁၀။ ဆက်နားထောင်ရန် card နှိပ် → ရပ်ထားတဲ့ နေရာက ဆက်ဖွင့်ပြ ⭐ (အသန့်ဆုံး အပိုင်း)
```

> **သတိ ၃ ခု** — ① Wi-Fi/data ရှိကြောင်း အရင် စစ်ပါ ② Demo မတိုင်မီ app ကို တစ်ခါ run ထားပါ
> (Firestore cache ဝင်ပြီး ပိုမြန်မယ်) ③ Volume ½ လောက် ကြိုတင် ထားပါ

### Step 2: Slide ဖွဲ့စည်းပုံ (A ဦးဆောင်၊ slide ၁၀–၁၂)

| # | Slide | ပါရမယ့် အချက် |
|---|---|---|
| 1 | Title | MyanCast Personal · အဖွဲ့ဝင် ၃ ယောက် · ၅ ပတ် |
| 2 | ပြဿနာ/ရည်ရွယ်ချက် | မြန်မာလို podcast + သတင်း app |
| 3 | Feature ၁၃ ခု | Screenshot ၅ ခုနဲ့ |
| 4 | Architecture | UI → Domain → Data · MVVM · DI framework မသုံး |
| 5 | Tech stack | Kotlin · Compose · Firestore · Media3 · Room · KSP |
| 6 | **Contract ပုံစံ** ⭐ | Interface + fake → ၃ ယောက် ပြိုင်တူ လုပ်နိုင်တာ (ဒီ project ရဲ့ အကောင်းဆုံး ဆုံးဖြတ်ချက်) |
| 7 | Vertical slice | Phase 2 ကနေ ပုံစံ ပြောင်းလိုက်တာ + ဘာကြောင့်လဲ |
| 8 | Test | ၁၀၉ ခု · pure function ကို ဘာကြောင့် ခွဲရလဲ |
| 9 | ခက်ခဲခဲ့တာ ၃ ခု | Media3 background playback · `play()`+`seekTo()` bug · Myanmar font (Poppins glyph) |
| 10 | Known limitation | Zawgyi · process death · nice-to-have မလုပ်ရတာ |
| 11 | Demo | (live) |
| 12 | Q&A | — |

### Step 3: `README.md` (C ဦးဆောင်)

```markdown
# MyanCast Personal
မြန်မာလို podcast + သတင်း app — Kotlin · Jetpack Compose · Firebase · Media3 · Room

## Screenshot
(Home · Full Player · Library · News · Search)

## Feature
- Podcast စာရင်း + အမျိုးအစား filter · Episode အသေးစိတ်
- Background playback (notification + lock screen) · 15s/30s · speed 0.5–2x
- Mini player (screen တိုင်း) · Full player (seek + speed)
- သတင်း စာရင်း + အသေးစိတ် (+ audio) · Subscribe · မှတ်တမ်း · ရှာဖွေ
- ဆက်နားထောင်ရန် (ရပ်ထားတဲ့ နေရာက ဆက်)
- Dark / Light

## Architecture
UI (Compose + ViewModel) → Domain (model + pure util) → Data (Firestore + Room)
- DI framework မသုံး — `MyanCastApp` မှာ singleton ၂ ခု (`playerController`, `libraryRepository`)
- Repository နဲ့ PlayerController က interface — test မှာ fake ထည့်
- Firestore read-only · Room မှာ subscribe + history

## Build လုပ်ရန်
1. Firebase project ဖန်တီး → `app/google-services.json` ထည့်
2. Firestore collection ၃ ခု: `podcasts`, `episodes`, `news` (schema — `fullscope.md`)
3. Composite index: `episodes` (podcastId ↑, publishedAt ↓)
4. `./gradlew installDebug`

## Test
`./gradlew testDebugUnitTest` — ၁၀၉ ခု

## Known limitation
- Zawgyi toggle — Unicode ပဲ သုံးတယ် (စာသား ပြောင်းလဲမှုက Rabbit converter လိုတယ် ~၁ ရက်)
- App ကို system က သတ်ပြီးရင် mini player အလွတ် — "ဆက်နားထောင်ရန်" ကနေ ပြန်ဖွင့်ရမယ်
- Download · sleep timer · queue screen · FCM — မလုပ်ဖြစ် (scope အပြင်)

## အဖွဲ့
A — Home/Library/Mini player · B — Player engine/News · C — Formatter/Full player/Search+History
```

### Step 4: ၁ ခါ ပြန်လေ့ကျင့် (ညနေ)
- Slide ၁၂ + demo ၅ မိနစ် → **အချိန် တိုင်း**
- တစ်ယောက်စီ ဘယ် slide ပြောမလဲ ခွဲ (A = 1–4, 11 · B = 5, 9 · C = 6–8, 10)

### ✅ Day 4 Deliverable
- [ ] Demo လမ်းကြောင်း ၁၀ ခု စီစဉ်ပြီး · ဖုန်းမှာ ၁ ခါ လုပ်ပြပြီး
- [ ] Slide ၁၀–၁၂ ပြီး
- [ ] `README.md` ပြီး + screenshot ၅ ခု
- [ ] တစ်ယောက်စီ ဘာပြောမလဲ ခွဲပြီး

---

# Day 5 — 🎬 FINAL REHEARSAL + SUBMIT

### Step 1: နောက်ဆုံး စစ်ဆေးမှု (မနက်)

```powershell
git checkout main; git pull origin main
.\gradlew clean assembleDebug       # warning ၀
.\gradlew testDebugUnitTest         # အားလုံး pass
.\gradlew installDebug              # device ၂ မျိုးမှာ
.\gradlew assembleRelease           # APK ထွက်လား
```

### Step 2: `fullscope.md` ရဲ့ Quality Checklist

| # | စစ်ရမယ့် အချက် | ☐ |
|---|---|---|
| 1 | App crash မဖြစ်ဘဲ run နိုင် | ☐ |
| 2 | Bottom nav ၅ ခု အလုပ်လုပ် | ☐ |
| 3 | Podcast list Firestore ကနေ ပေါ် | ☐ |
| 4 | Episode ဖွင့်နားထောင်နိုင် | ☐ |
| 5 | Mini player screen အားလုံးမှာ ပေါ် | ☐ |
| 6 | Zawgyi/Unicode toggle — **ဆုံးဖြတ်ချက် ရှင်းပြနိုင်** (Day 0 #2) | ☐ |
| 7 | News list နဲ့ detail ပေါ် | ☐ |
| 8 | Subscribe/unsubscribe အလုပ်လုပ် | ☐ |
| 9 | Search အလုပ်လုပ် | ☐ |
| 10 | Dark/Light theme toggle | ☐ |
| 11 | Unit test ၅ ခုအထက် | ☐ (၁၀၉) |
| 12 | README ရေးပြီး | ☐ |

### Step 3: ပြန်လေ့ကျင့် ၂ ခါ (နေ့လယ်)
- ပထမခါ — အစအဆုံး · အချိန် တိုင်း
- ဒုတိယခါ — **အင်တာနက် ပိတ်ပြီး** စမ်း (demo ချိန် Wi-Fi ပျက်ရင် ဘာပြောမလဲ ပြင်ဆင်)

### Step 4: မေးခွန်း ပြင်ဆင်မှု — ဖြေနိုင်ရမယ့် ၈ ခု

| မေးခွန်း | ဖြေရမယ့် အချက် |
|---|---|
| "Architecture ဘာကြောင့် ဒီလို ခွဲလဲ" | UI → Domain → Data · test လုပ်လို့ရအောင် |
| "DI framework ဘာကြောင့် မသုံးလဲ" | Course project scale · `MyanCastApp` မှာ singleton ၂ ခုပဲ လို |
| "Background playback ဘယ်လို လုပ်လဲ" | `MediaSessionService` + `MediaController` + foreground service type |
| "Test ဘယ်လို ရေးလဲ" | Interface + fake · pure function ခွဲ · `StandardTestDispatcher` |
| "အဖွဲ့ ၃ ယောက် ဘယ်လို ခွဲလဲ" | Vertical slice + Day 0 contract (Phase 2 ကနေ) |
| "အခက်ခဲဆုံး bug" | `play()` ပြီးမှ `seekTo()` — service မချိတ်ရသေးရင် ပျောက်တာ |
| "Myanmar font ပြဿနာ" | Poppins မှာ glyph မရှိ → `label*` မသုံးရ ဆိုတဲ့ စည်းကမ်း |
| "နောက်ထပ် ဘာ ထည့်မလဲ" | Download · sleep timer · queue · Zawgyi converter |

### Step 5: တင်ပြ + submit
- [ ] Code `main` မှာ push ပြီး · `git log` သန့်
- [ ] APK (သို့) repo link submit
- [ ] Slide + README ပါ

### ✅ Day 5 Deliverable
- [ ] Quality checklist ၁၂ ခု ✅
- [ ] ပြန်လေ့ကျင့် ၂ ခါ ပြီး
- [ ] Submit ပြီး 🎉

---

## Phase 5 — Troubleshooting

| ပြဿနာ | ဖြေရှင်းနည်း |
|---|---|
| `assembleRelease` မှာ `proguard-rules.pro` မတွေ့ | ဖိုင် အလွတ် ဖန်တီးလိုက်ရုံ (minify off မို့ content မလို) |
| Minify ဖွင့်ပြီး Firestore data class အလွတ် ဖြစ် | R8 က constructor ဖြတ်လိုက်တာ → `-keep class com.example.myancast.domain.model.** { *; }` · (သို့) minify ပိတ်ထား |
| Demo ချိန် data မပေါ် | Wi-Fi စစ် · Firestore rules (`allow read: if true`) သက်တမ်း ကုန်သွားလား |
| Demo ချိန် အသံ မထွက် | Volume · Bluetooth ချိတ်နေလား · အခြား app က audio focus ယူထားလား |
| Emulator မှာ notification မပေါ် | API 33+ မှာ permission · ဖုန်းအစစ်မှာ စမ်းပါ |
| Cold start နှေး | Firestore cache မရှိသေးလို့ — demo မတိုင်မီ တစ်ခါ run ထားပါ |
| `clean` ပြီးရင် build ၂၀ မိနစ် | Robolectric + KSP — demo မတိုင်မီ `clean` မလုပ်ပါနဲ့ |

---

## Phase 5 — Git Workflow

```
main
  ├── fix/phase5-a-review-findings      (A)
  ├── fix/phase5-b-release-config       (B)
  └── fix/phase5-c-docs-and-data        (C)
```

**စည်းကမ်း ၃ ခု:**
1. **Feature branch မဖွင့်ရ** — `fix/` ပဲ (Phase 5 မှာ feature အသစ် မရှိ)
2. Day 3 ညနေ နောက်ဆုံး merge — Day 4/5 မှာ **code မထိရ** (demo ပျက်နိုင်)
3. `git add app/src` ပဲ — `.idea/`, `.kotlin/` မပါအောင်

---

## 🏁 ၅ ပတ် ပြန်ကြည့်

| Phase | ရလဒ် | သင်ခန်းစာ |
|---|---|---|
| 1 | Setup · theme · navigation · Firestore data | Folder + design system အရင် ချရင် နောက်ပိုင်း လွယ်တယ် |
| 2 | Home + Detail (Firestore → UI) | Layer အလိုက် ခွဲတာ ပြောင်းပြီး **vertical slice** သုံးလိုက်တာ အောင်မြင်တယ် · pure function က test လွယ်တယ် |
| 3 | Player (service + mini + full) | **Day 0 contract** က ၃ ယောက် ပြိုင်တူ လုပ်နိုင်စေတယ် — တစ်ပတ်လုံး မပြောင်းရ |
| 4 | News · Library · Search · ဆက်နားထောင်ရန် | Contract ကို **additive** ပြောင်းတာ ရတယ် (default value နဲ့) |
| 5 | Polish · test · presentation | Feature အသစ် မထည့်တာ အရေးကြီးဆုံး ဆုံးဖြတ်ချက် |

> **၅ ပတ်လုံးရဲ့ အကောင်းဆုံး ဆုံးဖြတ်ချက် ၃ ခု** — ① Vertical slice (Phase 2)
> ② Day 0 contract + fake (Phase 3) ③ Pure function ခွဲထားတာ (Phase 2 ကနေ ဆက်)
> ဒီ ၃ ခုက slide #6, #7 မှာ ပြောရမယ့် အဓိက အချက်တွေပါ။
