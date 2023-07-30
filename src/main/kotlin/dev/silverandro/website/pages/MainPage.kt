package dev.silverandro.website.pages

import dev.silverandro.website.Page
import dev.silverandro.website.StyleSheet
import dev.silverandro.website.components._a
import dev.silverandro.website.components.inlineCode
import dev.silverandro.website.components.noWrap
import dev.silverandro.website.pages.blog.BlogPost
import dev.silverandro.website.util.px
import dev.silverandro.website.util.ymdString
import kotlinx.html.*

object MainPage : Page() {
    override val path = "index"

    override fun additionalStyleSheets(): List<StyleSheet> {
        return listOf(Style)
    }

    override fun HEAD.head() {
        link(
            href="https://tech.lgbt/@silverandro",
            rel = "me"
        )
    }

    override fun BODY.body() {
        div("opening") {
            h1 { +"welcome to my page! "; noWrap { +"\uD83D\uDC95\uD83C\uDFF3\uFE0F\u200D⚧\uFE0F" } }
        }
        div("showcase") {
            div { img("Silver's profile picture", "https://avatars.githubusercontent.com/SilverAndro?size=200") {
                id = "profile_picture"
                width = 200.px
                height = 200.px
            } }
            div {
                id = "bio"
                article {
                    +"hi there! im silver [it/she], im a kotlin/jvm programmer who mostly makes minecraft mods for quilt."
                    br
                    +"i dont have much here right now other than my blog though (and a private s3 backed file host)."
                    br; br
                    +"if i have friend requests or dms disabled on discord, feel free to join my sever and request to dm from there."
                    br
                    +"check out "; _a("/about", "my about page"); +" for more info about me and contact info."
                }
            }
        }
        hr {  }
        h2 { +"blog entries:" }
        div {
            id = "blog-list"
            ul {
                //blogEntry(IntroToOW2Asm)
            }
        }
    }

    object Style : StyleSheet() {
        override val name = "main_page"

        override fun getStyleSheet(): String {
            return """
                h1 {
                    margin: 0 2px 2px 2px;
                    font-size: 40px;
                    text-align: center;
                }

                .showcase {
                    display: flex;
                    justify-content: center;
                }

                #profile_picture {
                    border: 6px solid black;
                    border-radius: 50%;
                    margin: 10px 30px 10px 10px;

                    max-width: 100%;
                    height: auto;
                }

                #bio {
                    font-size: 22px;
                    line-height: 1.3;
                    width: 50%;

                    display: flex;
                    align-items: center;
                }

                /* Shrink to recover a few characters of space */
                @media screen and (max-width: 700px) {
                    body {
                        border-width: 0 0 0 16px;
                    }

                    #profile_picture {
                        width: 160px;
                    }
                }

                /* Shift to mobile layout */
                @media screen and (max-width: 530px) {
                    body {
                        border-width: 0 0 0 8px;
                    }

                    h1 {
                        font-size: 35px;
                    }

                    .showcase {
                        flex-direction: column;
                        align-items: center;
                        justify-content: center;
                    }

                    #profile_picture {
                        width: 130px;
                    }

                    #bio {
                        width: 80%;
                    }
                }
            """.trimIndent()
        }
    }

    private fun UL.blogEntry(post: BlogPost) {
        li {
            _a("/" + post.path.removePrefix("/"), post.title)
            +" - published "
            inlineCode(post.publishDate.ymdString)
            if (post.updateDate != null) {
                +", updated "
                inlineCode(post.updateDate!!.ymdString)
            }
        }
    }
}