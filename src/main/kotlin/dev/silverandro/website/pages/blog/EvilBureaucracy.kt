package dev.silverandro.website.pages.blog

import kotlinx.datetime.LocalDate
import kotlinx.html.BODY
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3

object EvilBureaucracy : BlogPost("bureaucracies-are-evil") {
    override val title = "bureaucracies are evil"
    override val description = "any inclusive community must avoid bureaucracy as much as possible"
    override val publishDate = LocalDate(2023, 12, 11)

    override fun BODY.body() {
        h1 { +"what is a bureaucracy anyways" }
        h2 { +"why are they \"evil\"" }
        h2 { +"alternatives" }
        h2 { +"group cohesion" }
        h3 { +"dunbar's number" }
        h1 { +"but surely my bureaucracy will be good or necessary" }
        h2 { +"people end up abstracted" }
        h2 { +"you exist within the system too" }
        h2 { +"no a meta-bureaucracy will not solve this" }
    }
}