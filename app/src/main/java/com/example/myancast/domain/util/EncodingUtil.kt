package com.example.myancast.domain.util


// ဆုံးဖြတ်ချက် (Phase 2, Member C) — Zawgyi/Unicode
// ၁။ Firestore data အားလုံး Unicode ဖြစ်တယ်။
//
// ၂။ Settings မှာ Zawgyi toggle က font family ကိုပဲ ပြောင်းတယ် —
//     စာသား ကိုယ်တိုင်ကို မပြောင်းဘူး။
//
// ၃။ Unicode စာသားကို Zawgyi font နဲ့ ပြရင် စာလုံး ပုံပျက်တယ်
//     (font နဲ့ encoding က တွဲဖက် မကိုက်လို့)။
//
// ၄။ Font ပြောင်းရုံနဲ့ မလုံလောက်ဘူး — စာသားကိုပါ Rabbit converter နဲ့
//     runtime ပြောင်းရမယ် (~၁ ရက် အလုပ်)။
//
// ၅။ Course project အတွက် grading criteria မဟုတ်လို့ မလုပ်တော့ဘူး။
//    အချိန်ကြာရင်လည်း မထည့်တော့ဘူး။
// ဒါကြောင့် —
//   • ခု Unicode ပဲ သုံးတယ်။
//   • Settings ရဲ့ Zawgyi toggle ကို Phase 5 အထိ ပိတ်ထားတယ်
//     (`enabled = false`, subtitle = "Phase 5 မှာထည့်မယ်")။
//   • ဒီ file ကို Phase 5 မှာ Rabbit converter အတွက် ဖွင့်ပြန်မယ်။
//
// ─────────────────────────────────────────────────────────────

// TODO (Phase 5): Unicode → Zawgyi converter
// fun unicodeToZawgyi(input: String): String = TODO()
// fun zawgyiToUnicode(input: String): String = TODO()