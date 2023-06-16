package dev.silverandro.website.components

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.SPAN
import kotlinx.html.span

fun FlowOrPhrasingContent.noWrap(block : SPAN.() -> Unit = {}) {
    span("noWrap") { block() }
}