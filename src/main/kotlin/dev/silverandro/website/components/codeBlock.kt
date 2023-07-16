package dev.silverandro.website.components

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.pre

enum class Language(val id: String) {
    NONE("nohighlight"),
    JAVA("language-java"),
    KOTLIN("language-kotlin"),
    DIFF("language-diff")
}

fun FlowContent.codeBlock(language: Language, content: String) {
    pre("code") {
        code(language.id) {
            val split = content.split("\n")

            split.forEach {
                val tabbed = it
                    .replace("    ", "\t")
                +tabbed
                +"\n"
            }
        }
    }
}