package dev.silverandro.website.components

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div

inline fun FlowContent.split(crossinline leftSide: DIV.() -> Unit, crossinline rightSide: DIV.() -> Unit) {
    div("split") {
        div { leftSide() }
        div { rightSide() }
    }
}