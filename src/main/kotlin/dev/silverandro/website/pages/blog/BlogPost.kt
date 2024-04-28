package dev.silverandro.website.pages.blog

import dev.silverandro.website.G
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
            if (G.debug) {
                link("../style/code_tomorrow.css", "stylesheet")
                script(src = "../script/highlight.js") {
                    async = true
                    attributes["onload"] = "hljs.highlightAll()"
                }
            } else {
                link("/style/code_tomorrow.css", "stylesheet")
                script(src = "/script/highlight.js") {
                    async = true
                    attributes["onload"] = "hljs.highlightAll()"
                }
            }
        }
    }

    override fun BODY.preBody() {
        h1("title") { +this@BlogPost.title }
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

        override fun getMain(): String {
            return """
                body {
                    padding: 20px 0px 15px 20px;
                }
                
                .title {
                    margin: 0px 0px 10px 0px;
                }
                
                h2 {
                    margin: 28px 0 10px 0;
                }
                
                h3 {
                    margin: 20px 0 10px 0;
                }
                
                .content {
                    width: 90%;
                    max-width: 55em;
                }
            """.trimIndent()
        }

        override fun getSmall(): String {
            return """
                .content {
                    width: 80%;
                }
            """.trimIndent()
        }

        override fun getMobile(): String {
            return """
                .content {
                    width: 97%;
                }
            """.trimIndent()
        }
    }
}