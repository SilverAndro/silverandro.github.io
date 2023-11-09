package dev.silverandro.website.components

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div

inline fun FlowContent.split(justify: Justify, crossinline leftSide: DIV.() -> Unit, crossinline rightSide: DIV.() -> Unit) {
    div("split") {
        this.attributes.set("style", "justify-content: ${justify.css}")
        div { leftSide() }
        div { rightSide() }
    }
}

enum class Justify(val css: String) {
    CENTER("center"),
    SPACE_BETWEEN("space-between"),
    SPACE_AROUND("space-around")
}