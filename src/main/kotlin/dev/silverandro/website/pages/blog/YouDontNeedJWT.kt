package dev.silverandro.website.pages.blog

import dev.silverandro.website.components.Language
import dev.silverandro.website.components._a
import dev.silverandro.website.components.br2
import dev.silverandro.website.components.codeBlock
import kotlinx.datetime.LocalDate
import kotlinx.html.BODY
import kotlinx.html.div
import kotlinx.html.h2

object YouDontNeedJWT : BlogPost("you-dont-need-jwt", needsCodeHighlighting = true) {
    override val title = "You Don't Need JWTs and They Are Also Bad"
    override val description = "A rant on how JWTs benefit no one and are another case of no one understanding what they're actually building"
    override val publishDate = LocalDate(2025, 2, 23)

    override fun BODY.body() {
        div("content") {
            +"Json Web Tokens, or \"JWTs\" are things defined in "; _a("https://datatracker.ietf.org/doc/html/rfc7519", "RFC-7519");
            +" that are supposed to be \"URL-safe means of representing claims to be transferred between two parties\", which in actuality, means "
            +"that they're now the Definitive Way to manage to web sessions. They also suck at literally any task and continue the web dev pattern "
            +"of reinventing the wheel because the other options were very slight too hard in javascript."
            br2
            +"If you can't tell, this is going to be a rant about why JWTs are bad and probably don't actually do what you think they're good for. "
            +"Although they can, technically, hold any data, ill mostly be arguing from the perspective of user sessions since that seems to be "
            +"the most common usecase, and all the major criticisms still apply to other uses. This post is, mostly, going to follow the structure of "
            _a("https://jwt.io/introduction"); +"'s explanation of what JWTs are and how they're useful, but instead arguing against each point."

            h2 { +"The header is too bulky for no reason" }
            +"The example header given for a JWT is:"
            codeBlock(Language.JSON, content = """
                {
                  "alg": "HS256",
                  "typ": "JWT"
                }
            """.trimIndent())
            +"Even when minimized, this is still a lot of extra data. Why do we need a whole JSON object for this? Well because its apparently important that "
            +"We allow people to shove whatever data they want inside the header for, reasons"
        }
    }
}