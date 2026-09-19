ဟုတ်ကဲ့။ **Phase 1 — Setup + Foundations (Week 1)** အတွက် အသေးစိတ် အပြည့်အစုံ ရှင်းပြပါမယ်။ ဒီအဆင့်က အရေးကြီးဆုံးပါ — ဒီအဆင့် ကောင်းရင် ကျန်တဲ့ ၄ ပတ် အလွန်လွယ်သွားမယ်။

---

## Phase 1 — Overview

| အချက် | အသေးစိတ် |
|---|---|
| **ရည်ရွယ်ချက်** | Project အခြေခံ အားလုံး အဆင်သင့် ဖြစ်စေရန် |
| **ကြာချိန်** | ၅ ရက် (Week 1) |
| **အဖွဲ့ဝင်** | ၃ ယောက် အားလုံး ပါဝင် |
| **အဆုံးမှာ ရရမယ့် ရလဒ်** | App run ရင် Bottom Nav ၅ ခုနဲ့ Empty screens ပေါ်လာမယ်။ Firebase ချိတ်ပြီးသား။ Data ၅ podcast၊ ၂၀ episode၊ ၁၀ news ရှိပြီးသား။ |

### Phase 1 မှာ လုပ်မယ့် အလုပ် ၅ ခု

```
၁။ Firebase Project Setup
၂။ Android Studio Project Setup
၃။ Design System (Color, Type, Theme)
၄။ Folder Structure + Dependencies
၅။ Navigation + Empty Screens
```

---

## Day 1 — Firebase Project + Android Studio Setup

### ရက် ၁ ရဲ့ ရည်မှန်းချက်
- Firebase project ဖန်တီးပြီး Android app ချိတ်
- Android Studio project အသစ် ဖွင့်
- Git repo ဖန်တီး

### လုပ်ဆောင်ရမယ့် အဆင့်များ

#### Step 1: Firebase Project ဖန်တီးခြင်း (Member C)

1. [Firebase Console](https://console.firebase.google.com) သွား
2. **"Add project"** နှိပ်
3. Project name: `MyanCast-Personal`
4. Google Analytics: **Enable** (optional ဒါပေမဲ့ ထားလိုက်ပါ)
5. Project ဖန်တီးပြီးရင် **"Add app"** → Android ရွေး
6. Package name ထည့်: `com.example.myancast`
7. App nickname: `MyanCast Android`
8. SHA-1: **မလိုဘူး** (Auth မသုံးလို့)
9. **`google-services.json`** ကို ဒေါင်းလုဒ်ဆွဲ

> **သတိ** — `google-services.json` ကို Git မတင်ပါနဲ့။ `.gitignore` ထဲ ထည့်ပါ။ ဒါပေမဲ့ Course project အတွက် တင်လည်း ရပါတယ် (security အနည်းဆုံးပဲ ရှိတယ်)။

#### Step 2: Android Studio Project ဖန်တီးခြင်း (Member A)

1. Android Studio → **New Project**
2. Template: **Empty Activity (Compose)**
3. Name: `MyanCast`
4. Package: `com.example.myancast`
5. Language: **Kotlin**
6. Minimum SDK: **API 24 (Android 7.0)**
7. Build configuration language: **Kotlin DSL**

#### Step 3: `google-services.json` ထည့်ခြင်း

ဒေါင်းလုဒ်ဆွဲထားတဲ့ file ကို ဒီနေရာမှာ ထည့်ပါ:

```
MyanCast/
└── app/
    └── google-services.json   ← ဒီမှာ
```

#### Step 4: Git Repository Setup (All)

```bash
# Terminal မှာ
cd MyanCast
git init
git add .
git commit -m "Initial commit: Android Studio template"

# GitHub မှာ repo အသစ် ဖန်တီး → ပြီးရင်
git remote add origin https://github.com/yourname/myancast.git
git branch -M main
git push -u origin main

# Dev branch ဖန်တီး
git checkout -b dev
git push -u origin dev
```

#### Step 5: `.gitignore` ထည့်ခြင်း

```
# .gitignore
*.iml
.gradle/
local.properties
.idea/
.DS_Store
build/
captures/
.externalNativeBuild
.cxx
google-services.json    ← optional (Course project အတွက် တင်လည်း ရ)
```

### Day 1 ရဲ့ Deliverable

- ✅ Firebase Console မှာ project ရှိပြီး
- ✅ Android Studio project run လို့ရပြီး
- ✅ `google-services.json` ထည့်ပြီး
- ✅ Git repo + `main` + `dev` branch ရှိပြီး

---

## Day 2 — Design System + Folder Structure

### ရက် ၂ ရဲ့ ရည်မှန်းချက်
- Design tokens (Color, Type, Theme) ရေး
- Folder structure ဖန်တီး
- Font file ထည့်

### Step 1: Font ထည့်ခြင်း (Member A)

Padauk နဲ့ Zawgyi-One font ဖိုင် ရှာပါ:

```
app/src/main/res/font/
├── padauk_regular.ttf
├── padauk_bold.ttf
└── zawgyi_one.ttf
```

**ဒေါင်းလုဒ်ဆွဲရမယ့် နေရာ:**
- Padauk: [Google Fonts](https://fonts.google.com/specimen/Padauk) သို့မဟုတ် [Padauk GitHub](https://github.com/khmertype/Padauk)
- Zawgyi-One: [Zawgyi GitHub](https://github.com/ngwestar/zawgyi-tools)

### Step 2: Color.kt ရေးခြင်း

```kotlin
// ui/theme/Color.kt
package com.example.myancast.ui.theme

import androidx.compose.ui.graphics.Color

// Dark Theme
val BgDark        = Color(0xFF0E0D0B)
val SurfaceDark   = Color(0xFF1A1814)
val Surface2Dark  = Color(0xFF262219)
val OutlineDark   = Color(0xFF332E24)

val GoldPrimary   = Color(0xFFF0A93B)
val GoldDark      = Color(0xFFC9821C)
val TealSecondary = Color(0xFF3FBFB0)
val LiveRed       = Color(0xFFE5484D)
val OkGreen       = Color(0xFF34C77B)

val TextHi        = Color(0xFFF7F3EA)
val TextLo        = Color(0xFF9E968A)

// Light Theme (optional)
val BgLight       = Color(0xFFFDF9F2)
val SurfaceLight  = Color(0xFFFFFFFF)
```

### Step 3: Type.kt ရေးခြင်း (Zawgyi/Unicode swap ပါဝင်)

```kotlin
// ui/theme/Type.kt
package com.example.myancast.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myancast.R

val PadaukFamily = FontFamily(
    Font(R.font.padauk_regular, FontWeight.Normal),
    Font(R.font.padauk_bold,    FontWeight.Bold)
)

val ZawgyiFamily = FontFamily(
    Font(R.font.zawgyi_one, FontWeight.Normal),
    Font(R.font.zawgyi_one, FontWeight.Bold)
)

fun myanCastTypography(zawgyi: Boolean): Typography {
    val mm = if (zawgyi) ZawgyiFamily else PadaukFamily
    return Typography(
        displayMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 32.sp, lineHeight = 45.sp
        ),
        titleLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Bold,
            fontSize = 22.sp, lineHeight = 35.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Normal,
            fontSize = 16.sp, lineHeight = 29.sp   // line-height 1.8
        ),
        labelMedium = TextStyle(
            fontFamily = mm, fontWeight = FontWeight.Normal,
            fontSize = 13.sp, lineHeight = 21.sp
        )
    )
}
```

### Step 4: Theme.kt ရေးခြင်း

```kotlin
// ui/theme/Theme.kt
package com.example.myancast.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

data class AppConfig(
    val zawgyi: Boolean = false,
    val darkMode: Boolean = true
)

val LocalAppConfig = staticCompositionLocalOf { AppConfig() }

private val DarkColors = darkColorScheme(
    primary        = GoldPrimary,
    onPrimary      = Color(0xFF1A1408),
    secondary      = TealSecondary,
    background     = BgDark,
    surface        = SurfaceDark,
    surfaceVariant = Surface2Dark,
    outline        = OutlineDark,
    onBackground   = TextHi,
    onSurface      = TextHi
)

@Composable
fun MyanCastTheme(
    config: AppConfig = AppConfig(),
    content: @Composable () -> Unit
) {
    val colors = if (config.darkMode) DarkColors else lightColorScheme()
    val typography = myanCastTypography(config.zawgyi)

    CompositionLocalProvider(LocalAppConfig provides config) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content
        )
    }
}
```

### Step 5: Folder Structure ဖန်တီးခြင်း

Android Studio မှာ ဒီ folder တွေ ဖန်တီးပါ:

```
com.example.myancast/
├── data/
│   ├── firebase/
│   ├── repository/
│   └── local/
├── domain/
│   ├── model/
│   └── util/
├── player/
└── ui/
    ├── theme/       ← ဒီနေ့ ပြီးသွားပြီ
    ├── navigation/
    ├── components/
    ├── home/
    ├── detail/
    ├── player/
    ├── news/
    ├── library/
    ├── search/
    └── settings/
```

> **Tips** — Folder အလွတ် ဖန်တီးရင် `.gitkeep` file ထည့်ထားပါ။ Git က empty folder ကို track မလုပ်ဘူး။

### Step 6: MainActivity ပြင်ခြင်း

```kotlin
// MainActivity.kt
package com.example.myancast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myancast.ui.theme.AppConfig
import com.example.myancast.ui.theme.MyanCastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyanCastTheme(config = AppConfig(zawgyi = false, darkMode = true)) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navigation က နက်ဖန် ထည့်မယ်
                }
            }
        }
    }
}
```

### Day 2 ရဲ့ Deliverable

- ✅ Font ၃ ခု ထည့်ပြီး
- ✅ Color.kt, Type.kt, Theme.kt ရေးပြီး
- ✅ Folder structure အားလုံး ဖန်တီးပြီး
- ✅ MainActivity run လို့ရပြီး (background color ပေါ်လာ)

---

## Day 3 — Firebase Data Setup

### ရက် ၃ ရဲ့ ရည်မှန်းချက်
- Firestore collection ၃ ခု ဖန်တီး
- Data ၅ podcast၊ ၂၀ episode၊ ၁၀ news ထည့်

### Step 1: Firestore Database ဖန်တီးခြင်း (Member A)

1. Firebase Console → **Firestore Database** → **Create database**
2. Location: `asia-southeast1` (Singapore — မြန်မာနဲ့ အနီးဆုံး)
3. Security rules: **Start in test mode**
4. Create

> **Test mode** က ၃၀ ရက် အခမဲ့။ Course project အတွက် လုံလောက်တယ်။ ပြီးရင် rule ပြင်လို့ရတယ်။

### Step 2: Security Rules ပြင်ခြင်း

```javascript
// Firestore Rules
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Read-only for everyone, no writes
    match /{document=**} {
      allow read: if true;
      allow write: if false;
    }
  }
}
```

> **ဒီ rule က** — App ထဲကနေ ဖတ်လို့ရ၊ ရေးလို့မရ။ Data ကို Console ကနေပဲ ထည့်ရမယ်။

### Step 3: `podcasts` Collection ဖန်တီးခြင်း

Firebase Console → Firestore → **Start collection**

Collection ID: `podcasts`

Document ၅ ခု ထည့်ပါ (Auto-ID သုံး):

```javascript
// Podcast 1
{
  "title": "မနက်ခင်း သတင်းလွှာ",
  "description": "ဒေသခံ သတင်းနှင့် ရာသီဥတု အခြေအနေ",
  "coverUrl": "https://picsum.photos/seed/news1/400",
  "category": "သတင်း",
  "episodeCount": 4,
  "updatedAt": <timestamp>
}

// Podcast 2
{
  "title": "နည်းပညာ စကားဝိုင်း",
  "description": "နည်းပညာ အကြောင်း နေ့စဉ် ဆွေးနွေးချက်",
  "coverUrl": "https://picsum.photos/seed/tech1/400",
  "category": "နည်းပညာ",
  "episodeCount": 4,
  "updatedAt": <timestamp>
}

// Podcast 3
{
  "title": "ဇာတ်လမ်းည",
  "description": "မြန်မာ ရိုးရာ ဇာတ်လမ်းများ",
  "coverUrl": "https://picsum.photos/seed/story1/400",
  "category": "ဇာတ်လမ်း",
  "episodeCount": 4,
  "updatedAt": <timestamp>
}

// Podcast 4
{
  "title": "ကျန်းမာရေး နေ့စဉ်",
  "description": "ကျန်းမာရေး အကြံပြုချက်များ",
  "coverUrl": "https://picsum.photos/seed/health1/400",
  "category": "ကျန်းမာရေး",
  "episodeCount": 4,
  "updatedAt": <timestamp>
}

// Podcast 5
{
  "title": "စီးပွားရေး ရှုထောင့်",
  "description": "စီးပွားရေး သတင်းနှင့် သုံးသပ်ချက်",
  "coverUrl": "https://picsum.photos/seed/biz1/400",
  "category": "စီးပွားရေး",
  "episodeCount": 4,
  "updatedAt": <timestamp>
}
```

> **Cover URL** — `picsum.photos` က random image ပေးတယ်။ Course project အတွက် အလွန်အဆင်ပြေတယ်။

### Step 4: `episodes` Collection ဖန်တီးခြင်း

Collection ID: `episodes`

Document ၂၀ ခု ထည့်ပါ (podcast တစ်ခုကို ၄ ခု):

```javascript
{
  "podcastId": "<podcast1_doc_id>",   ← Podcast 1 ရဲ့ ID ကို copy ကူး
  "title": "အပိုင်း ၁၂၈ · မနက်ခင်း သတင်း",
  "description": "ဒီနေ့ သတင်း အကျဉ်း",
  "audioUrl": "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
  "duration": 1470,                    ← seconds (၂၄:၃၀)
  "publishedAt": <timestamp>
}
```

**အခမဲ့ MP3 URL များ (Test အတွက်):**

```
https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3
https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3
https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3
...  (Song-16 အထိ ရှိ)
```

### Step 5: `news` Collection ဖန်တီးခြင်း

Collection ID: `news`

Document ၁၀ ခု ထည့်ပါ:

```javascript
{
  "headline": "ရာသီဥတု အခြေအနေ — မိုးသည်းထန်နိုင်",
  "body": "ယနေ့ ညနေပိုင်းမှာ မိုးသည်းထန်စွာ ရွာနိုင်ပါတယ်...",
  "audioUrl": "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
  "category": "ရာသီဥတု",
  "publishedAt": <timestamp>
}
```

### Step 6: Data ထည့်တဲ့အခါ သတိထားရမယ့် အချက်

| အချက် | အသေးစိတ် |
|---|---|
| **Field type** | Console မှာ type ကို မှန်အောင် ရွေးပါ (string, number, timestamp) |
| **Timestamp** | `timestamp` type ကို ရွေးပြီး ရက်စွဲ ထည့် |
| **Document ID** | Auto-ID သုံးပါ (custom မလိုဘူး) |
| **podcastId** | Episode ထည့်တဲ့အခါ Podcast ရဲ့ document ID ကို copy ကူးထည့်ပါ |

### Day 3 ရဲ့ Deliverable

- ✅ Firestore collection ၃ ခု ရှိပြီး
- ✅ Data ၅ + ၂၀ + ၁၀ = ၃၅ document ရှိပြီး
- ✅ Security rules ပြင်ပြီး (read-only)

---

## Day 4 — Dependencies + Repository Skeleton

### ရက် ၄ ရဲ့ ရည်မှန်းချက်
- Gradle dependency အားလုံး ထည့်
- Data class ၃ ခု ရေး
- Repository skeleton ၃ ခု ရေး

### Step 1: Root `build.gradle.kts` ပြင်ခြင်း

```kotlin
// build.gradle.kts (Project level)
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}
```

### Step 2: App `build.gradle.kts` ပြင်ခြင်း

```kotlin
// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")   ← Firebase
    id("com.google.devtools.ksp")          ← Room
}

android {
    namespace = "com.example.myancast"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myancast"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Activity + Lifecycle
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.5")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("androidx.media3:media3-session:1.4.1")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Coil (image loading)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("io.mockk:mockk:1.13.12")
}
```

> **သတိ** — Version တွေ update ဖြစ်နိုင်တယ်။ Android Studio က အလိုအလျောက် suggest လုပ်တာကို လိုက်နာပါ။

### Step 3: Data Class ရေးခြင်း (Member B)

```kotlin
// domain/model/Podcast.kt
package com.example.myancast.domain.model

data class Podcast(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val category: String = "",
    val episodeCount: Int = 0
)
```

```kotlin
// domain/model/Episode.kt
package com.example.myancast.domain.model

import com.google.firebase.Timestamp

data class Episode(
    val id: String = "",
    val podcastId: String = "",
    val title: String = "",
    val description: String = "",
    val audioUrl: String = "",
    val duration: Int = 0,
    val publishedAt: Timestamp? = null
)
```

```kotlin
// domain/model/NewsItem.kt
package com.example.myancast.domain.model

import com.google.firebase.Timestamp

data class NewsItem(
    val id: String = "",
    val headline: String = "",
    val body: String = "",
    val audioUrl: String? = null,
    val category: String = "",
    val publishedAt: Timestamp? = null
)
```

### Step 4: Firestore Extension ရေးခြင်း (Member B)

```kotlin
// data/firebase/FirestoreExt.kt
package com.example.myancast.data.firebase

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Firestore Query ကို Flow အနေနဲ့ ပြောင်းပေးတယ်
 */
fun <T : Any> Query.snapshotFlow(clazz: Class<T>): Flow<List<T>> = callbackFlow {
    val listener = addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        val items = snapshot?.toObjects(clazz) ?: emptyList()
        trySend(items)
    }
    awaitClose { listener.remove() }
}

/**
 * QuerySnapshot ကို List ပြောင်း
 */
fun <T : Any> QuerySnapshot.toList(clazz: Class<T>): List<T> =
    toObjects(clazz)
```

### Step 5: Repository Skeleton ရေးခြင်း

```kotlin
// data/repository/PodcastRepository.kt
package com.example.myancast.data.repository

import com.example.myancast.data.firebase.snapshotFlow
import com.example.myancast.domain.model.Episode
import com.example.myancast.domain.model.Podcast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

class PodcastRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getPodcasts(): Flow<List<Podcast>> =
        db.collection("podcasts")
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .snapshotFlow(Podcast::class.java)

    fun getEpisodes(podcastId: String): Flow<List<Episode>> =
        db.collection("episodes")
            .whereEqualTo("podcastId", podcastId)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .snapshotFlow(Episode::class.java)
}
```

```kotlin
// data/repository/NewsRepository.kt
package com.example.myancast.data.repository

import com.example.myancast.data.firebase.snapshotFlow
import com.example.myancast.domain.model.NewsItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getNews(): Flow<List<NewsItem>> =
        db.collection("news")
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .limit(50)
            .snapshotFlow(NewsItem::class.java)
}
```

> **LibraryRepository** — Room လိုတဲ့အတွက် Week 4 မှ ရေးမယ်။ ဒီနေ့ skeleton ပဲ ထားပါ။

### Step 6: Test ခြင်း

MainActivity မှာ ရိုးရိုး test ရေးပြီး data ရလား စစ်ပါ:

```kotlin
// MainActivity.kt — test အတွက် ယာယီ
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyanCastTheme {
                val repo = remember { PodcastRepository() }
                val podcasts by repo.getPodcasts()
                    .collectAsState(initial = emptyList())

                Column {
                    Text("Podcasts: ${podcasts.size}")
                    podcasts.forEach { Text(it.title) }
                }
            }
        }
    }
}
```

App run ရင် "Podcasts: 5" ဆိုပြီး ပေါ်ရမယ်။ ပေါ်ရင် Firebase ချိတ်ဆက်မှု အောင်မြင်ပြီ။

### Day 4 ရဲ့ Deliverable

- ✅ Dependency အားလုံး ထည့်ပြီး
- ✅ Data class ၃ ခု ရေးပြီး
- ✅ Repository ၂ ခု ရေးပြီး (Podcast, News)
- ✅ Firebase ကနေ data ဆွဲလို့ရပြီ

---

## Day 5 — Navigation + Empty Screens

### ရက် ၅ ရဲ့ ရည်မှန်းချက်
- Bottom Nav ၅ ခု ဆောက်
- Screen ၉ ခု ရဲ့ Empty composable ရေး
- Navigation ချိတ်

### Step 1: Screen Route Constants

```kotlin
// ui/navigation/Screen.kt
package com.example.myancast.ui.navigation

sealed class Screen(val route: String) {
    data object Home       : Screen("home")
    data object News       : Screen("news")
    data object Search     : Screen("search")
    data object Library    : Screen("library")
    data object Settings   : Screen("settings")

    data object PodcastDetail : Screen("podcast/{podcastId}") {
        fun create(id: String) = "podcast/$id"
    }
    data object NewsDetail : Screen("news/{newsId}") {
        fun create(id: String) = "news/$id"
    }
    data object Player     : Screen("player")
}
```

### Step 2: Bottom Nav Item Model

```kotlin
// ui/navigation/BottomNavItem.kt
package com.example.myancast.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route,     "ပင်မ",       Icons.Filled.Home),
    BottomNavItem(Screen.News.route,     "သတင်း",      Icons.Outlined.Newspaper),
    BottomNavItem(Screen.Search.route,   "ရှာဖွေ",     Icons.Filled.Search),
    BottomNavItem(Screen.Library.route,  "စာကြည့်တိုက်", Icons.Outlined.Bookmarks),
    BottomNavItem(Screen.Settings.route, "ဆက်တင်",     Icons.Filled.Settings)
)
```

### Step 3: Empty Screens ရေးခြင်း

```kotlin
// ui/home/HomeScreen.kt
package com.example.myancast.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Screen", fontSize = 20.sp)
    }
}
```

ဒီ pattern ကို screen ၉ ခုအတွက် ရေးပါ:
- `HomeScreen.kt` (ui/home/)
- `PodcastDetailScreen.kt` (ui/detail/)
- `NewsScreen.kt` (ui/news/)
- `NewsDetailScreen.kt` (ui/news/)
- `SearchScreen.kt` (ui/search/)
- `LibraryScreen.kt` (ui/library/)
- `SettingsScreen.kt` (ui/settings/)
- `FullPlayerScreen.kt` (ui/player/)

### Step 4: Main NavHost ရေးခြင်း

```kotlin
// ui/navigation/MyanCastNavHost.kt
package com.example.myancast.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myancast.ui.detail.PodcastDetailScreen
import com.example.myancast.ui.home.HomeScreen
import com.example.myancast.ui.library.LibraryScreen
import com.example.myancast.ui.news.NewsDetailScreen
import com.example.myancast.ui.news.NewsScreen
import com.example.myancast.ui.player.FullPlayerScreen
import com.example.myancast.ui.search.SearchScreen
import com.example.myancast.ui.settings.SettingsScreen
import com.example.myancast.ui.theme.GoldPrimary

@Composable
fun MyanCastNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Bottom nav ပေါ်မလား ဆုံးဖြတ်
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                MyanCastBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route)     { HomeScreen() }
            composable(Screen.News.route)     { NewsScreen() }
            composable(Screen.Search.route)   { SearchScreen() }
            composable(Screen.Library.route)  { LibraryScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }

            composable(Screen.PodcastDetail.route) {
                PodcastDetailScreen()
            }
            composable(Screen.NewsDetail.route) {
                NewsDetailScreen()
            }
            composable(Screen.Player.route) {
                FullPlayerScreen()
            }
        }
    }
}

@Composable
private fun MyanCastBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GoldPrimary,
                    selectedTextColor = GoldPrimary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
```

> **သတိ** — `0.dp` အတွက် `import androidx.compose.ui.unit.dp` ထည့်ပါ။

### Step 5: MainActivity ပြင်ခြင်း

```kotlin
// MainActivity.kt
package com.example.myancast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myancast.ui.navigation.MyanCastNavHost
import com.example.myancast.ui.theme.AppConfig
import com.example.myancast.ui.theme.MyanCastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyanCastTheme(
                config = AppConfig(zawgyi = false, darkMode = true)
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyanCastNavHost()
                }
            }
        }
    }
}
```

### Step 6: App run ပြီး စစ်ဆေးခြင်း

App run ရင် ဒီအချက်တွေ ဖြစ်ရမယ်:
- ✅ Bottom Nav ၅ ခု ပေါ်လာ
- ✅ Icon နဲ့ label တွေ မြန်မာလို ပေါ်လာ
- ✅ Tab နှိပ်ရင် Screen ပြောင်း
- ✅ Home tab က default

### Day 5 ရဲ့ Deliverable

- ✅ Bottom Nav ၅ ခု အလုပ်လုပ်
- ✅ Screen ၉ ခု navigation ချိတ်ပြီး
- ✅ Empty screens အားလုံး run လို့ရပြီး

---

## Phase 1 — Final Checklist

| # | စစ်ရမယ့် အချက် | ပြီး/မပြီး |
|---|---|---|
| 1 | Firebase project ဖန်တီးပြီး | ☐ |
| 2 | `google-services.json` ထည့်ပြီး | ☐ |
| 3 | Firestore database ဖန်တီးပြီး | ☐ |
| 4 | Security rules read-only ပြင်ပြီး | ☐ |
| 5 | Podcast ၅ ခု၊ Episode ၂၀ ခု၊ News ၁၀ ခု ထည့်ပြီး | ☐ |
| 6 | Android Studio project run လို့ရပြီး | ☐ |
| 7 | Dependency အားလုံး ထည့်ပြီး | ☐ |
| 8 | Font ၃ ခု (Padauk×2, Zawgyi×1) ထည့်ပြီး | ☐ |
| 9 | Color, Type, Theme ရေးပြီး | ☐ |
| 10 | Folder structure ဖန်တီးပြီး | ☐ |
| 11 | Data class ၃ ခု ရေးပြီး | ☐ |
| 12 | Repository ၂ ခု ရေးပြီး | ☐ |
| 13 | Firebase data ဆွဲလို့ရပြီး | ☐ |
| 14 | Bottom Nav ၅ ခု အလုပ်လုပ်ပြီး | ☐ |
| 15 | Screen ၉ ခု navigation ချိတ်ပြီး | ☐ |
| 16 | Git repo + branch အားလုံး push ပြီး | ☐ |

---

## Phase 1 — ဖြစ်နိုင်တဲ့ ပြဿနာများနဲ့ ဖြေရှင်းနည်း

| ပြဿနာ | ဖြေရှင်းနည်း |
|---|---|
| `google-services.json` မတွေ့ | `app/` folder ထဲမှာ ရှိလား စစ် |
| Firebase dependency error | Root `build.gradle.kts` မှာ `google-services` plugin ထည့်ပြီးလား စစ် |
| Firestore permission denied | Security rules test mode ဖြစ်လား စစ် |
| Font မပေါ် | `res/font/` folder name မှန်လား၊ file name lowercase လား စစ် |
| Navigation crash | Route string တွေ တူလား စစ် |
| Data mapping error | Field name တွေ data class နဲ့ တူလား စစ် |
| Zawgyi မပေါ် | `zawgyi_one.ttf` က `res/font/` ထဲ ရှိလား စစ် |

---

## Phase 1 — Git Workflow

```
main (production)
  ↑
dev (integration)
  ↑
feature/memberA-setup
feature/memberB-repo
feature/memberC-firebase
```

**တစ်ရက်တစ်ခါ:**

```bash
git checkout dev
git pull origin dev
git checkout -b feature/your-task

# ... အလုပ် လုပ် ...

git add .
git commit -m "feat: add HomeScreen navigation"
git push origin feature/your-task

# GitHub မှာ PR ဖွင့် → dev ကို merge
```

---

## Phase 1 — ပြီးရင် ဘာဆက်လုပ်မလဲ？

Phase 1 ပြီးရင် **Phase 2 (Week 2)** ကို စပါ:

- Home Screen UI အပြည့်အစုံ
- Podcast Detail Screen
- Episode list
- Repository → ViewModel → UI ချိတ်ဆက်

**အရေးကြီးဆုံး** — Phase 1 မှာ App run လို့ရပြီး Firebase data ဆွဲလို့ရပြီဆိုရင် သင်တို့ အဖွဲ့ အောင်မြင်ဖို့ အလွန်နီးပါပြီ။ ကျန်တဲ့ phase တွေက ဒီအပေါ်မှာ ဆက်တည်ဆောက်ရုံပဲ။

လိုအပ်ရင် **Phase 2 အသေးစိတ်** သို့မဟုတ် **Home Screen code အပြည့်အစုံ** ကို ဆက်ရှင်းပြပေးနိုင်ပါတယ်။