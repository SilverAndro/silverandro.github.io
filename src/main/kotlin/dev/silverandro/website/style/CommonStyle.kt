package dev.silverandro.website.style

import dev.silverandro.website.StyleSheet

object CommonStyle : StyleSheet() {
    override val name = "common"

    override fun getMain(): String {
        return """
            html {
                font-size: 18px;
                font-family: 'Lato', sans-serif;
            }

            a:link, a:visited {
                color: #36A1A1;
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

            body {
                height: auto;
                margin: 0 0 0 0;
                border: solid #BF3FBF;
                border-width: 0 0 0 30px;
                padding: 0 0 15px 30px;
            }

            p {
                overflow-wrap: normal;
            }
            
            hr {
                width: 90%;
                margin: 10px 0 10px;
            }
            
            h1, h2 {
                margin: 20px 0px 10px 0px;
            }

            .inline_code {
                display: inline-block;

                font-size: smaller;
                letter-spacing: 0;
                margin: 0 2px 0 2px;

                font-family: 'JetBrains Mono', monospace;
            }

            .code {
                font-family: 'JetBrains Mono', monospace;
                font-size: 14px;
                line-height: 1.3;
                tab-size: 4;

                padding: 8px 10px 8px;
                width: fit-content;
                min-width: 55%;
                max-width: 100%;
                overflow: scroll;
                
                border: 5px solid #B9B5BC;
                border-radius: 8px;
            }

            .nowrap {
                white-space: nowrap;
            }
            
            .split {
                display: flex;
            }
        """.trimIndent()
    }

    override fun getSmall() = """
        .code {
            font-size: 12px;
        }
    """.trimIndent()

    override fun getMobile() = """
        .split {
            flex-direction: column;
            align-items: center;
        }
    """.trimIndent()
}