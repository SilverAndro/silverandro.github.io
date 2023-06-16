package dev.silverandro.website.components

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.SPAN
import kotlinx.html.span

fun FlowOrPhrasingContent.inlineCode(block : SPAN.() -> Unit = {}) {
    span("inline_code") { block() }
}