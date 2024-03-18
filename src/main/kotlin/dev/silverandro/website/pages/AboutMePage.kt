package dev.silverandro.website.pages

import dev.silverandro.website.Page
import dev.silverandro.website.StyleSheet
import dev.silverandro.website.components._a
import dev.silverandro.website.components.br2
import dev.silverandro.website.components.email
import dev.silverandro.website.components.inlineCode
import kotlinx.html.BODY
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul

object AboutMePage : Page() {
    override val path = "about"

    override fun additionalStyleSheets(): List<StyleSheet> {
        return listOf(Style)
    }

    override fun BODY.body() {
        h1 { +"Contact and Social Media" }
        +"Email is usually the best way to contact me, however discord is available for more informal discussions or support "
        +"with my projects."
        br2;
        +"Contact:"
        ul {
            li { +"Email: "; email("me@silverandro.dev")}
            li { +"Discord: "; inlineCode("mrs.silver.andro") }
            li { +"Discord Server: "; _a("https://discord.gg/PZAunp345q") }
            li { +"Github: "; _a("https://github.com/SilverAndro") }
        }
        +"Social media:"
        ul {
            li { +"Mastadon: "; _a("https://tech.lgbt/@silverandro") }
            li { +"Tumblr: "; _a("https://www.tumblr.com/blog/silverandro")}
        }
        +"Mods:"
        ul {
            li { +"Modrinth: "; _a("https://modrinth.com/user/SilverAndro") }
        }
    }

    object Style : StyleSheet() {
        override val name = "about_me"

        override fun getMain(): String {
            return ""
        }
    }
}
