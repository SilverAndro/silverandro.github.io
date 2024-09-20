package dev.silverandro.website.components

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.rp
import kotlinx.html.rt
import kotlinx.html.ruby

fun FlowOrPhrasingContent._ruby(base: String, ruby: String) {
    ruby { +base; rp { +"(" }; rt { +ruby }; rp { +")" } }
}