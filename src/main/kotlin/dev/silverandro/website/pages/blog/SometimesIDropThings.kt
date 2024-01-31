package dev.silverandro.website.pages.blog

import dev.silverandro.website.components.br2
import kotlinx.datetime.LocalDate
import kotlinx.html.*

object SometimesIDropThings : BlogPost("sometimes-i-drop-things") {
    override val title = "sometimes i drop things"
    override val description = "a small rant about ui, ux, and mistakes"

    override val publishDate = LocalDate(2024, 1, 22)

    override fun BODY.body() {
        div("content") {
            +"you know how when youre holding something that can break easily, you feel everything tense up because you "
            +"understand that one small slip up is going to destroy it? like when youre holding your grandma's old "
            +"sentimental vase, you "; i { +"have" }; +" to move it somewhere, but hold it slightly the wrong way, or "
            +"god forbid drop it, and youre gonna have a lot of pain?"
            br2
            +"thats how i feel about a lot of chat apps."

            h2 { +"dropping things" }
            +"when i drop stuff its not because i have any motor control issues, i just slip up sometimes, or trip, or a "
            +"million other reasons. i keep a case on my phone because i drop stuff often enough that its a concern for "
            +"the integrity of my devices. of course when you drop a phone, you try to catch it, and thats where a lot of "
            +"my complaints with interfaces are coming from today, sometimes i drop things, and try to catch them."
            br2
            +"for example, discord pretty much forever has had a \"call this user\" button easily accessible from someones "
            +"profile, which is excellent when you want to call someone in app! but i dont call people, so what does the "
            +"button do for me? im sure being able to call someone in 2 taps is great but, sometimes i drop things."
            br
            +"when i go to catch my phone, im not really thinking about what i might be hitting on the screen."
        }
    }
}