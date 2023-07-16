package dev.silverandro.website.components

import kotlinx.html.OL
import kotlinx.html.UL
import kotlinx.html.li

context(UL)
operator fun String.unaryMinus() {
    li {
        +this@unaryMinus
    }
}

context(OL)
operator fun String.unaryMinus() {
    li {
        +this@unaryMinus
    }
}