package dev.silverandro.website.components

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.pre
import kotlinx.html.style

enum class Language(val id: String) {
    NONE("nohighlight"),
    JAVA("language-java"),
    KOTLIN("language-kotlin"),
    DIFF("language-diff"),
    GRADE("language-gradle"),
    JSON("language-json"),
}

fun FlowContent.codeBlock(language: Language, content: String, restrictHeight: Int = Int.MAX_VALUE) {
    pre("code") {
        if (restrictHeight != Int.MAX_VALUE) {
            style = "max-height: ${restrictHeight}px"
        }

        code(language.id) {
            style = "height: $restrictHeight%"
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