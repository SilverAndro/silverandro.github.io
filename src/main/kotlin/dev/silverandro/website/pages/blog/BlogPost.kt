package dev.silverandro.website.pages.blog

import dev.silverandro.website.Page
import dev.silverandro.website.StyleSheet
import dev.silverandro.website.components.hr
import dev.silverandro.website.util.ymdString
import kotlinx.datetime.LocalDate
import kotlinx.html.*

abstract class BlogPost(val slug: String, val needsCodeHighlighting: Boolean = false) : Page() {
    override val path: String
        get() = "blog/$slug"

    abstract val publishDate: LocalDate
    open val updateDate: LocalDate? = null

    override fun additionalStyleSheets(): List<StyleSheet> {
        return listOf(Style)
    }

    override fun HEAD.head() {
        if (needsCodeHighlighting) {
            link("/style/code_tomorrow.css", "stylesheet")
            script(src = "/script/highlight.js") {
                async = true
                attributes["onload"] = "hljs.highlightAll()"
            }
        }
    }

    override fun BODY.preBody() {
        h1("title") { +IntroToOW2Asm.title }
        strong("title") {
            style = "font-size: 16px;"
            +"Published: ${publishDate.ymdString}"
        }
        br
        if (updateDate != null) {
            strong("title") {
                +"Updated: ${updateDate!!.ymdString}"
            }
            br
        }
        p {
            style = "margin: 18px 0 0;"
            +description
        }
        hr
    }

    object Style : StyleSheet() {
        override val name = "blog_post"

        override fun getStyleSheet(): String {
            return """
                body {
                    padding: 20px 0px 15px 20px;
                }
                
                .title {
                    margin: 0px 0px 10px 0px;
                }
                
                .content {
                    width: 80%;
                }
            """.trimIndent()
        }
    }
}