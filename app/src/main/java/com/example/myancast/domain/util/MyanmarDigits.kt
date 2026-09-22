package com.example.myancast.domain.util

/** 0-9 ကို ၀-၉ ပြောင်း — "24" → "၂၄" */
fun Long.toMyanmarDigits(): String =
    toString().map {
        c -> if (c in '0'..'9') '၀' + (c - '0') else c
    }.joinToString("")

fun Int.toMyanmarDigits(): String = toLong().toMyanmarDigits()