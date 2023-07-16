package dev.silverandro.website.util

import kotlinx.datetime.LocalDate

val LocalDate.ymdString: String
    get() = "$year-${monthNumber.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"