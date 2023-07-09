package dev.silverandro.website.components

import kotlinx.html.A
import kotlinx.html.FlowOrInteractiveOrPhrasingContent
import kotlinx.html.a

fun FlowOrInteractiveOrPhrasingContent.email(email: String, text: String = email, block: A.()->Unit = {}) {
    a("mailto:$email") {
        +text
        block()
    }
}