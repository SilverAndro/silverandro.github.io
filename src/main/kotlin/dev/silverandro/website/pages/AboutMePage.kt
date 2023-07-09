package dev.silverandro.website.pages

import dev.silverandro.website.Page
import dev.silverandro.website.StyleSheet
import dev.silverandro.website.components._a
import dev.silverandro.website.components.email
import dev.silverandro.website.components.inlineCode
import kotlinx.html.BODY
import kotlinx.html.li
import kotlinx.html.ul

object AboutMePage : Page() {
    override val path = "about"

    override fun additionalStyleSheets(): List<StyleSheet> {
        return listOf(Style)
    }

    override fun BODY.body() {
        +"whoops! you found some unfinshed content, try again later?"
        ul {
            li { +"Github: "; _a("https://github.com/SilverAndro") }
            li { +"Twitter: "; _a("https://twitter.com/SilverAndro") }
            li { +"Discord: "; inlineCode { +"mrs.silver.andro" } }
            li { +"Discord Server: "; inlineCode { +"PZAunp345q" } }
            li { +"Modrinth: "; _a("https://modrinth.com/user/SilverAndro") }
            li { +"Curseforge: "; _a("https://curseforge.com/members/silverthelesbian/projects") }
            li { +"Email: "; email("NiAndromedae@proton.me")}
        }
    }

    object Style : StyleSheet() {
        override val name = "about_me"

        override fun getStyleSheet(): String {
            return ""
        }
    }
}