package dev.silverandro.website

import kotlin.properties.Delegates

object G {
    var debug by Delegates.notNull<Boolean>()

    fun d(string: String): Int = string.count { it == '/' }
}