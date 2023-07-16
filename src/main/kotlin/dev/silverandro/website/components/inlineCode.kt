package dev.silverandro.website.components

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.span

fun FlowOrPhrasingContent.inlineCode(c: String) {
    span("inline_code") { +c }
}