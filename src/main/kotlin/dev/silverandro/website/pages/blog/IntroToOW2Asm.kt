package dev.silverandro.website.pages.blog

import dev.silverandro.website.components.*
import dev.silverandro.website.util.embedFile
import kotlinx.datetime.LocalDate
import kotlinx.html.*

object IntroToOW2Asm : BlogPost("an-intro-to-ow2-asm", true) {
    override val title: String
        get() = "an (im)practical introduction to ow2 asm"
    override val description: String
        get() = "an introduction to ow2 asm, with examples from my experience with the library"

    override val publishDate = LocalDate(2023, 7, 15)

    override fun BODY.body() {
        div("content") {
            +"please note that this post will not be a thorough introduction to "
            _a("https://docs.oracle.com/javase/specs/jvms/se20/html/jvms-6.html", "jvm bytecode")
            +" as it is focused on using ow2 asm. i recommend checking out the documentation and trying other parts yourself (either with"
            +" asm or a project like "
            _a("https://github.com/roscopeco/jasm", "jasm")
            +")"

            h1 { +"whats so special about ow2 asm?" }
            _a("https://asm.ow2.io/", "ow2 asm")
            +" (or just asm for the rest of this post) is \"an all purpose Java bytecode manipulation and analysis framework.\" "
            +"this means it can read class files, create classes from scratch, and write it all out to either be written to disk or loaded live."
            br; br
            +"The main reason to use asm is that its "
            i { +"fast" }
            +", outpacing many other libraries that also work on class files. This speed comes at a cost though, youre working a "
            +"lot closer to the raw class file. asm, by and large, does not verify anything you do, and will happily output invalid class files "
            +"(other than some cases that we'll get to later)."

            h2 { +"a warning about this post" }
            +"asm uses a visitor pattern basically everywhere, as its the expected way to interface with the library. "
            +"however i "; strong { +"do not " }; +"use the visitor api when working with asm. i find it:"
            ul {
                - "clunky"
                - "hard to use"
                - "destroys control flow"
                - "various other issues"
            }
            +"so this tutorial will instead work in an procedural manner. youre welcome to use the visitor pattern yourself, but you wont find instructions here."

            h2 { +"setting the stage" }
            +"the first few examples here are going to use this calculator code that ive intentionally left Not Great™."
            br
            codeBlock(Language.JAVA, embedFile("tutorials/asm/Calculator.java"))

            +"this works pretty well, putting in a simple calculation gives us the correct result"
            codeBlock(Language.NONE, """
                enter the calculation:
                5 + 6 + 10 - 2 * 3
                result: 57
            """.trimIndent())
            +"so lets try poking at this a bit with asm! "
            br;br
            +"im going to assume for the rest of this tutorial that youve compiled the calculator into "; inlineCode { +"Calculator.class" }
            +", if you need a copy of it, just compile the calculator, rename the output "; inlineCode { +".jar" }; +" to a "; inlineCode { +".zip" }
            +" and extract the file."

            h2 { +"reading some basic info from a class file" }
        }
    }
}