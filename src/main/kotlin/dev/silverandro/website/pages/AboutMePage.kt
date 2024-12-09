package dev.silverandro.website.pages

import dev.silverandro.website.Page
import dev.silverandro.website.StyleSheet
import dev.silverandro.website.components._a
import dev.silverandro.website.components.br2
import dev.silverandro.website.components.email
import dev.silverandro.website.components.inlineCode
import kotlinx.html.*

object AboutMePage : Page() {
    override val path = "about"

    override fun additionalStyleSheets(): List<StyleSheet> {
        return listOf(Style)
    }

    override fun BODY.body() {
        h1 { +"Contact and Social Media" }
        +"Email is usually the best way to contact me, however discord is available for more informal discussions or support "
        +"with my projects."
        br
        +"I am in the EST time zone so expect responses around mid-later day then."
        br2;
        +"Contact:"
        ul {
            li { +"Email: "; email("me@silverandro.dev")}
            li { +"Github: "; _a("https://github.com/SilverAndro") }
            li { +"Discord: "; inlineCode("mrs.silver.andro") }
        }
        +"Social media:"
        ul {
            li { +"BlueSky: "; _a("https://bsky.app/profile/silverandro.dev")}
            li { +"Tumblr: "; _a("https://www.tumblr.com/blog/silverandro")}
            li { +"Discord Server: "; _a("https://discord.gg/PZAunp345q")}
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
