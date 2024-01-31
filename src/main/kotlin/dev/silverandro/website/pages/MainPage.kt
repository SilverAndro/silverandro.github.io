package dev.silverandro.website.pages

import dev.silverandro.website.Page
import dev.silverandro.website.StyleSheet
import dev.silverandro.website.components.*
import dev.silverandro.website.pages.blog.AoTM
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
        split(Justify.CENTER, {
            img("Silver's profile picture", "https://avatars.githubusercontent.com/SilverAndro?size=200") {
                id = "profile_picture"
                width = 200.px
                height = 200.px
            }},
            {
            id = "bio"
            article {
                +"hi there! im silver [it/she], im a kotlin/jvm programmer who mostly makes minecraft mods for quilt "
                +"and has been slowly branching out into other projects."
                br
                +"i dont have much here right now other than my blog and "; _a("https://maven.silverandro.dev", "my personal maven"); +"."
                br2
                +"if i have friend requests or dms disabled on discord, feel free to join my sever and request to dm from there."
                br
                +"check out "; _a("/about", "my about page"); +" for more info about me and contact info."
                br
                i { +"i promise ill publish blog posts someday" }
            }}
        )
        hr {  }
        split(Justify.SPACE_AROUND, {
            h2 { +"blog entries:" }
            div {
                id = "blog-list"
                ul {
                    //blogEntry(IntroToOW2Asm)
                    //blogEntry(EvilBureaucracy)
                    //blogEntry(SometimesIDropThings)
                    blogEntry(AoTM)
                }
            }},
            {
            id = "project-list"
            h2 { +"notable projects" }
            div {
                ul {
                    projectEntry(
                        "s3 maven rust lambda", "https://github.com/SilverAndro/s3-maven-rust-lambda",
                        "the aws lambda function that implements my maven"
                    )
                    projectEntry(
                        "broadsword", "https://github.com/SilverAndro/broadsword",
                        "a jvm class file remapper thats focused on speed rather than functionality (probably getting a rewrite soon-ish" +
                                " to be way better and more dynamic)"
                    )
                }
            }}
        )
    }

    object Style : StyleSheet() {
        override val name = "main_page"

        override fun getMain(): String {
            return """
                h1 {
                    margin: 0px 2px 2px 2px;
                    font-size: 40px;
                    text-align: center;
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
                
                #project-list {
                    width: 50%;
                }
            """.trimIndent()
        }

        override fun getSmall() = """
            body {
                border-width: 0px 0px 0px 16px;
            }

            #profile_picture {
                width: 160px;
            }
        """.trimIndent()

        override fun getMobile() = """
            body {
                border-width: 0px 0px 0px 8px;
            }

            h1 {
                font-size: 35px;
            }

            #profile_picture {
                width: 130px;
            }

            #bio {
                width: 80%;
            }
        """.trimIndent()
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

    private fun UL.projectEntry(name: String, link: String, description: String) {
        li {
            _a(link, name)
            +" - $description"
        }
    }
}