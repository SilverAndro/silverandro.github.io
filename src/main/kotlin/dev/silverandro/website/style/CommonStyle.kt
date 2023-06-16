package dev.silverandro.website.style

import dev.silverandro.website.StyleSheet

object CommonStyle : StyleSheet() {
    override val name = "common"

    override fun getStyleSheet(): String {
        return """
            html {
                font-size: 18px;
                font-family: 'Lato', sans-serif;
            }

            p {
                overflow-wrap: normal;
            }

            .inline_code {
                display: inline-block;

                font-size: smaller;
                letter-spacing: 0;
                margin: 0 2px 0 2px;

                font-family: 'JetBrains Mono', monospace;
            }

            .code {
                font-size: 14px;
                line-height: 1.3;

                padding: 10px 20px 20px 20px;
                background-color: #EFEFEF;
                border: 5px solid #9E9E9E;
                border-radius: 8px;

                font-family: 'JetBrains Mono', monospace;
            }

            .nowrap {
                white-space: nowrap;
            }
        """.trimIndent()
    }
}