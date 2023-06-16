package dev.silverandro.website.style

import dev.silverandro.website.StyleSheet

object LinksStyle : StyleSheet() {
    override val name = "links"

    override fun getStyleSheet(): String {
        return """
            a:link, a:visited {
                color: #3FBFBF;
            }

            a:active {
                color: red;
            }

            /* Disable the link coloring if the user has custom colors setup */
            @media screen and (prefers-contrast:more) or (prefers-contrast:custom) {
                a:link, a:visited {
                    color: revert;
                }
            }
        """.trimIndent()
    }
}