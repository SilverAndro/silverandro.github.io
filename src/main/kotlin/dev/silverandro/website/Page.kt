package dev.silverandro.website

import dev.silverandro.website.style.CommonStyle
import kotlinx.html.BODY
import kotlinx.html.HEAD

abstract class Page {
    abstract val path: String
    open val title: String = "Silver's Silly Little Website"
    open val description = "Silver's silly site, full of awful web design and a maven"

    open fun styleSheets(): List<StyleSheet> {
        return mutableListOf<StyleSheet>(
            CommonStyle
        ).also { it.addAll(additionalStyleSheets()) }
    }
    open fun additionalStyleSheets(): List<StyleSheet> = emptyList()
    open fun HEAD.head() {}
    open fun BODY.preBody() {}
    abstract fun BODY.body()
    open fun BODY.postBody() {}
}