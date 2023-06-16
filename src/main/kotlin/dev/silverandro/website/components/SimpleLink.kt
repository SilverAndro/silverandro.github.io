package dev.silverandro.website.components

import kotlinx.html.A
import kotlinx.html.FlowOrInteractiveOrPhrasingContent
import kotlinx.html.a

fun FlowOrInteractiveOrPhrasingContent._a(href: String, text: String = href, block: A.()->Unit = {}) {
    a(href) {
        +text
        block()
    }
}