package dev.silverandro.website

import dev.silverandro.website.style.CommonStyle
import dev.silverandro.website.style.LinksStyle
import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.HTML

abstract class Page {
    abstract val path: String
    open val title: String = "Silver's Silly Little Website"
    open val description = "Silver's silly site, full of awful web design and a maven"

    open fun styleSheets(): List<StyleSheet> {
        return mutableListOf(
            CommonStyle,
            LinksStyle
        ).also { it.addAll(additionalStyleSheets()) }
    }
    open fun additionalStyleSheets(): List<StyleSheet> = emptyList()
    open fun HEAD.head() {}
    open fun HTML.preBody() {}
    abstract fun BODY.body()
    open fun HTML.postBody() {}
}