ဟုတ်ကဲ့။ အောက်မှာ **MyanCast Personal** Course Project အတွက် **Project Structure + Feature List အပြည့်အစုံ** ကို မြန်မာလို ရှင်းပြထားပါတယ်။ Firebase ကို သုံးမယ်၊ အဖွဲ့ဝင် ၃ ယောက်၊ အချိန် ၅ ပတ်၊ Simple & Clean ဖြစ်အောင် စီစဉ်ထားပါတယ်။

---

## ပထမ — Project အခြေခံ အချက်အလက်

| အချက် | အသေးစိတ် |
|---|---|
| **App Name** | MyanCast Personal |
| **Package** | `com.example.myancast` |
| **Language** | Kotlin 100% |
| **UI** | Jetpack Compose |
| **Architecture** | MVVM + Clean Architecture (၃ လွှာ) |
| **Backend** | Firebase Firestore (read-only) |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 34 |
| **Team** | ၃ ယောက် |
| **Timeline** | ၅ ပတ် (MVP) |

---

## ဒုတိယ — Project Folder Structure (အပြည့်အစုံ)

```
app/
├── src/main/
│   ├── java/com/example/myancast/
│   │   │
│   │   ├── MyanCastApp.kt              ← Application class
│   │   ├── MainActivity.kt             ← Single Activity
│   │   │
│   │   ├── data/                       ← DATA LAYER
│   │   │   ├── firebase/
│   │   │   │   ├── FirebaseSource.kt   ← Firestore instance
│   │   │   │   └── FirestoreExt.kt     ← Flow extension helpers
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── PodcastRepository.kt
│   │   │   │   ├── NewsRepository.kt
│   │   │   │   └── LibraryRepository.kt
│   │   │   │
│   │   │   └── local/
│   │   │       ├── AppDatabase.kt      ← Room (optional)
│   │   │       ├── LibraryDao.kt
│   │   │       └── LibraryEntity.kt
│   │   │
│   │   ├── domain/                     ← DOMAIN LAYER
│   │   │   ├── model/
│   │   │   │   ├── Podcast.kt
│   │   │   │   ├── Episode.kt
│   │   │   │   ├── NewsItem.kt
│   │   │   │   └── PlaybackState.kt
│   │   │   │
│   │   │   └── util/
│   │   │       ├── DurationFormatter.kt
│   │   │       ├── DateFormatter.kt
│   │   │       └── EncodingUtil.kt     ← Zawgyi/Unicode
│   │   │
│   │   ├── player/                     ← MEDIA LAYER
│   │   │   ├── PlaybackService.kt      ← Media3 Foreground Service
│   │   │   ├── PlayerController.kt     ← Singleton controller
│   │   │   └── PlayerQueue.kt
│   │   │
│   │   └── ui/                         ← UI LAYER
│   │       ├── theme/
│   │       │   ├── Color.kt
│   │       │   ├── Type.kt             ← Padauk + Zawgyi font
│   │       │   ├── Shape.kt
│   │       │   └── Theme.kt
│   │       │
│   │       ├── navigation/
│   │       │   ├── MyanCastNavHost.kt
│   │       │   └── Screen.kt           ← Route constants
│   │       │
│   │       ├── components/             ← Reusable UI
│   │       │   ├── PodcastCard.kt
│   │       │   ├── EpisodeRow.kt
│   │       │   ├── NewsCard.kt
│   │       │   ├── MiniPlayer.kt
│   │       │   ├── LoadingView.kt
│   │       │   ├── EmptyView.kt
│   │       │   └── ErrorView.kt
│   │       │
│   │       ├── home/
│   │       │   ├── HomeScreen.kt
│   │       │   └── HomeViewModel.kt
│   │       │
│   │       ├── detail/
│   │       │   ├── PodcastDetailScreen.kt
│   │       │   └── PodcastDetailViewModel.kt
│   │       │
│   │       ├── player/
│   │       │   ├── FullPlayerScreen.kt
│   │       │   └── PlayerViewModel.kt
│   │       │
│   │       ├── news/
│   │       │   ├── NewsScreen.kt
│   │       │   ├── NewsDetailScreen.kt
│   │       │   └── NewsViewModel.kt
│   │       │
│   │       ├── library/
│   │       │   ├── LibraryScreen.kt
│   │       │   └── LibraryViewModel.kt
│   │       │
│   │       ├── search/
│   │       │   ├── SearchScreen.kt
│   │       │   └── SearchViewModel.kt
│   │       │
│   │       └── settings/
│   │           ├── SettingsScreen.kt
│   │           └── SettingsViewModel.kt
│   │
│   └── res/
│       ├── font/
│       │   ├── padauk_regular.ttf
│       │   ├── padauk_bold.ttf
│       │   ├── zawgyi_one.ttf
│       ├── values/
│       │   ├── strings.xml          ← Myanmar default
│       │   └── themes.xml
│       └── drawable/
│           └── ic_launcher.xml
│
├── google-services.json               ← Firebase config
└── build.gradle.kts
```

### Module အရေအတွက် — **၁ ခုတည်း (`app`)**

> Multi-module မခွဲပါနဲ့။ Course project အတွက် `app` module တစ်ခုတည်းက လုံလောက်တယ်။ ခွဲရင် build time ပိုကြာတယ်၊ setup ရှုပ်တယ်။

---

## တတိယ — Layer သုံးခု ဘယ်လိုချိတ်ဆက်လဲ？

```
┌─────────────────────────────────────┐
│  UI LAYER                            │
│  Compose Screen ← ViewModel          │
│  (StateFlow collect)                 │
└────────────────┬────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│  DOMAIN LAYER                        │
│  Model (Podcast, Episode, NewsItem)  │
│  Util (Formatter, EncodingUtil)      │
└────────────────┬────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│  DATA LAYER                          │
│  Repository → FirebaseSource         │
│  Repository → Room DAO (optional)    │
└─────────────────────────────────────┘
```

**စည်းကမ်း —**
- UI က Firebase ကို တိုက်ရိုက် မခေါ်ရဘူး။ Repository ကတစ်ဆင့်ပဲ။
- Repository က `Flow<List<Podcast>>` ပြန်ပေးတယ်။
- ViewModel က `StateFlow` ပြောင်းပြီး Screen ကို ပေးတယ်။
- Screen က `collectAsState()` နဲ့ ဖတ်တယ်။

---

## စတုတ္ထ — Feature List (အပြည့်အစုံ)

### 🟢 MVP Features (မဖြစ်မနေ လုပ်ရမယ်)

| # | Feature | Screen | အသေးစိတ် |
|---|---|---|---|
| 1 | **Podcast List** | Home | Firestore က `podcasts` collection ဖတ်၊ card grid ပြ |
| 2 | **Continue Listening** | Home | နောက်ဆုံး နားထောင်ခဲ့တဲ့ episode ကို hero card ပြ |
| 3 | **Podcast Detail** | Detail | Episode list၊ subscribe button |
| 4 | **Play / Pause** | Player | ExoPlayer နဲ့ audio stream ဖွင့် |
| 5 | **Seek** | Player | 15s back / 30s forward |
| 6 | **Mini Player** | All | Screen အောက်ခြေမှာ persistent bar |
| 7 | **Full Player** | Player | Cover၊ title၊ seek bar၊ controls |
| 8 | **News List** | News | Firestore `news` collection |
| 9 | **News Detail** | News | Headline + body + optional audio |
| 10 | **Subscribe / Unsubscribe** | Detail | Room DB မှာ သိမ်း |
| 11 | **Library** | Library | Subscribed + History tab |
| 12 | **Search** | Search | Title နဲ့ Firestore query |
| 13 | **Zawgyi / Unicode Toggle** | Settings | Global font swap |
| 14 | **Dark / Light Theme** | Settings | Toggle |
| 15 | **Playback Speed** | Player | 0.5x – 2.0x |

### 🟡 Nice-to-Have (အချိန်ရှိရင်)

| # | Feature | အသေးစိတ် |
|---|---|---|
| 16 | Sleep Timer | မိနစ် သတ်မှတ်ပြီး auto-pause |
| 17 | Favorites | ♥ နှိပ်ပြီး သိမ်း |
| 18 | Queue | နောက်ဖွင့်မယ့် episode စာရင်း |
| 19 | Shake to Skip | Sensor သုံး |
| 20 | Download | WorkManager နဲ့ offline |
| 21 | Push Notification | FCM နဲ့ အသစ်ထွက်ရင် အသိပေး |

### 🔴 လုံးဝ မလုပ်ပါနဲ့ (Out of Scope)

- ❌ Amplifier / Bluetooth
- ❌ Kiosk Mode / Broadcast
- ❌ Auto Scheduler / AlarmManager
- ❌ User Auth / Login
- ❌ Cloud Functions
- ❌ Payment / Subscription
- ❌ Social / Share / Comment

---

## ပဉ္စမ — Firebase Data Structure

### Collections ၃ ခုပဲ

```
📁 podcasts/{podcastId}
   ├─ title: String
   ├─ description: String
   ├─ coverUrl: String          ← Storage URL သို့မဟုတ် အပြင် URL
   ├─ category: String
   ├─ episodeCount: Number
   └─ updatedAt: Timestamp

📁 episodes/{episodeId}
   ├─ podcastId: String         ← podcast နဲ့ ချိတ်
   ├─ title: String
   ├─ description: String
   ├─ audioUrl: String          ← MP3 URL
   ├─ duration: Number          ← seconds
   └─ publishedAt: Timestamp

📁 news/{newsId}
   ├─ headline: String
   ├─ body: String
   ├─ audioUrl: String?         ← optional
   ├─ category: String
   └─ publishedAt: Timestamp
```

### Firebase Service သုံးမယ့် အနည်းဆုံး

| Service | သုံးမလား |
|---|---|
| Firestore | ✅ |
| Crashlytics | ✅ (optional) |
| Auth | ❌ |
| Storage | ❌ |
| Functions | ❌ |
| FCM | ⚠️ optional |

> **Data တွေကို Firebase Console ကနေ ကိုယ်တိုင် ထည့်ပါ။** App ထဲကနေ data ရေး မလုပ်ပါနဲ့။ Security rule ရေးစရာ မလိုတော့ဘူး။

---

## ဆဋ္ဌမ — Kotlin Data Class များ

```kotlin
// domain/model/Podcast.kt
data class Podcast(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val category: String = "",
    val episodeCount: Int = 0
)

// domain/model/Episode.kt
data class Episode(
    val id: String = "",
    val podcastId: String = "",
    val title: String = "",
    val description: String = "",
    val audioUrl: String = "",
    val duration: Int = 0,
    val publishedAt: com.google.firebase.Timestamp? = null
)

// domain/model/NewsItem.kt
data class NewsItem(
    val id: String = "",
    val headline: String = "",
    val body: String = "",
    val audioUrl: String? = null,
    val category: String = "",
    val publishedAt: com.google.firebase.Timestamp? = null
)
```

**သတိ** — `is` prefix မသုံးပါနဲ့ (Firestore mapping error တက်တယ်)။

---

## သတ္တမ — Repository Code (အဓိက ၃ ခု)

```kotlin
// data/repository/PodcastRepository.kt
class PodcastRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getPodcasts(): Flow<List<Podcast>> = callbackFlow {
        val listener = db.collection("podcasts")
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.toObjects(Podcast::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    fun getEpisodes(podcastId: String): Flow<List<Episode>> = callbackFlow {
        val listener = db.collection("episodes")
            .whereEqualTo("podcastId", podcastId)
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.toObjects(Episode::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }
}
```

```kotlin
// data/repository/NewsRepository.kt
class NewsRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getNews(): Flow<List<NewsItem>> = callbackFlow {
        val listener = db.collection("news")
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.toObjects(NewsItem::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }
}
```

```kotlin
// data/repository/LibraryRepository.kt  (Room)
class LibraryRepository(private val dao: LibraryDao) {
    fun getSubscribed(): Flow<List<String>> = dao.getSubscribedIds()
    suspend fun subscribe(id: String) = dao.insert(LibraryEntity(id, "sub"))
    suspend fun unsubscribe(id: String) = dao.delete(id)
}
```

---

## အဋ္ဌမ — Navigation Structure

**Bottom Nav ၅ ခု**

```
① Home         ──→ Podcast Detail ──→ Full Player
② News         ──→ News Detail
③ Search       ──→ Podcast Detail
④ Library      ──→ Podcast Detail
⑤ Settings
```

**Route Constants**

```kotlin
// ui/navigation/Screen.kt
sealed class Screen(val route: String) {
    data object Home       : Screen("home")
    data object News       : Screen("news")
    data object Search     : Screen("search")
    data object Library    : Screen("library")
    data object Settings   : Screen("settings")
    data object PodcastDetail : Screen("podcast/{id}") {
        fun create(id: String) = "podcast/$id"
    }
    data object NewsDetail : Screen("news/{id}") {
        fun create(id: String) = "news/$id"
    }
    data object Player     : Screen("player")
}
```

---

## နဝမ — Team Roles (၃ ယောက်)

| Member | အဓိက တာဝန် | ဒုတိယ တာဝန် |
|---|---|---|
| **A** | UI အားလုံး (Compose) | Navigation + Theme + Firebase Console data ထည့် |
| **B** | Player + ExoPlayer + Media3 | Repository (Podcast) + ViewModel |
| **C** | News + Library + Room | Repository (News, Library) + Testing |

**စည်းကမ်း —**
- Git branch: `main` → `dev` → `feature/{name}`
- PR တစ်ခုကို အနည်းဆုံး ၁ ယောက် review
- Code မတူတဲ့ naming convention မသုံးရဘူး (Member A က သတ်မှတ်)

---

## ဒသမ — ၅ ပတ် Timeline (အသေးစိတ်)

### Week 1 — Setup + Foundations
| ရက် | လုပ်ဆောင်ချက် | Owner |
|---|---|---|
| ၁ | Firebase project ဖန်တီး၊ `google-services.json` ထည့် | C |
| ၁ | Android Studio project ဖွင့်၊ Compose သုံး | A |
| ၂ | Design system (Color, Type, Theme) ရေး | A |
| ၂ | Folder structure ဖန်တီး၊ Git setup | All |
| ၃ | Firebase Console မှာ data ၅ podcast၊ ၂၀ episode၊ ၁၀ news ထည့် | A |
| ၄ | Firestore Dependency + Repository skeleton | B, C |
| ၅ | Navigation setup + Empty screens | A |

**Deliverable** — App run ရင် Bottom Nav နဲ့ Empty screens ပေါ်လာ

---

### Week 2 — Home + Detail
| ရက် | လုပ်ဆောင်ချက် | Owner |
|---|---|---|
| ၁-၂ | PodcastRepository + HomeViewModel | B |
| ၂-၃ | HomeScreen (Continue Listening, Podcast list) | A |
| ၃-၄ | PodcastDetailScreen + ViewModel | A, B |
| ၄-၅ | EpisodeRow component + Loading/Empty views | A |

**Deliverable** — Podcast list နဲ့ episode list ပြနိုင်ပြီ

---

### Week 3 — Player
| ရက် | လုပ်ဆောင်ချက် | Owner |
|---|---|---|
| ၁ | Media3 ExoPlayer dependency + PlaybackService | B |
| ၂ | PlayerController singleton | B |
| ၃ | MiniPlayer composable | A |
| ၄ | FullPlayerScreen (cover, seek, controls) | A |
| ၅ | Playback state → ViewModel → UI ချိတ် | B, A |

**Deliverable** — Podcast ဖွင့်နားထောင်လို့ရပြီ

---

### Week 4 — News + Library + Settings
| ရက် | လုပ်ဆောင်ချက် | Owner |
|---|---|---|
| ၁ | NewsRepository + NewsScreen | C |
| ၂ | NewsDetailScreen | C, A |
| ၃ | Room DB (LibraryEntity, DAO) | C |
| ၄ | LibraryScreen (Subscribe + History tab) | A, C |
| ၅ | SettingsScreen + Zawgyi/Unicode toggle | A |
| ၅ | SearchScreen | B |

**Deliverable** — Feature အားလုံး အခြေခံပြီး

---

### Week 5 — Polish + Testing + Presentation
| ရက် | လုပ်ဆောင်ချက် | Owner |
|---|---|---|
| ၁ | Bug fix, error handling, empty states | All |
| ၂ | Unit test (ViewModel, Repository) | C |
| ၃ | Performance (startup, scroll) | B |
| ၄ | Presentation slides + demo script | A |
| ၅ | Final rehearsal + submit | All |

**Deliverable** — Course project အဆင်သင့်

---

## ဧကာဒသမ — Dependency List (`build.gradle.kts`)

```kotlin
dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.4.0")
    implementation("androidx.media3:media3-ui:1.4.0")
    implementation("androidx.media3:media3-session:1.4.0")

    // Room (Library only)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Image loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("io.mockk:mockk:1.13.10")
}
```

**မသုံးတာ** — Hilt, Retrofit, Ktor, Koin, DataStore (Course project အတွက် မလို)

---

## ဒွါဒသမ — Quality Checklist (အဆုံးမှာ စစ်ရန်)

| စစ်ရမယ့် အချက် | ပြီး/မပြီး |
|---|---|
| App crash မဖြစ်ဘဲ run နိုင် | ☐ |
| Bottom nav ၅ ခု အလုပ်လုပ် | ☐ |
| Podcast list Firestore ကနေ ပေါ် | ☐ |
| Episode ဖွင့်နားထောင်နိုင် | ☐ |
| Mini player screen အားလုံးမှာ ပေါ် | ☐ |
| Zawgyi/Unicode toggle အလုပ်လုပ် | ☐ |
| News list နဲ့ detail ပေါ် | ☐ |
| Subscribe/unsubscribe အလုပ်လုပ် | ☐ |
| Search အလုပ်လုပ် | ☐ |
| Dark/Light theme toggle | ☐ |
| Unit test ၅ ခုအထက် | ☐ |
| README ရေး | ☐ |

---

## အနှစ်ချုပ်

| အပိုင်း | အဖြေ |
|---|---|
| **Module** | `app` တစ်ခုတည်း |
| **Layer** | UI → Domain → Data |
| **Firebase** | Firestore only (read-only) |
| **Collections** | `podcasts`, `episodes`, `news` (၃ ခု) |
| **Feature** | MVP ၁၅ ခု၊ Nice ၆ ခု၊ Out-of-scope ၇ ခု |
| **Team** | A=UI၊ B=Player၊ C=News+Library |
| **Timeline** | ၅ ပတ် (၁ ပတ် = ၁ phase) |
| **Dependency** | Compose + Firestore + Media3 + Room + Coil |

**အရေးကြီးဆုံး ၃ ချက်**
1. **ရိုးရှင်းအောင် ထား** — Hilt၊ Retrofit၊ Ktor မသုံးပါနဲ့။
2. **Firestore ကို read-only** — Console ကနေပဲ data ထည့်ပါ။
3. **MVP အရင်ပြီးအောင်** — Nice-to-have တွေကို နောက်ဆုံးမှ ထည့်ပါ။

လိုအပ်ရင် **Home Screen code အပြည့်အစုံ** သို့မဟုတ် **ExoPlayer service code** ကို မြန်မာလို ရှင်းပြပေးနိုင်ပါတယ်။