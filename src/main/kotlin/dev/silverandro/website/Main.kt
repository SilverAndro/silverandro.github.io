package dev.silverandro.website

import dev.silverandro.website.components.metaProperty
import dev.silverandro.website.pages.AboutMePage
import dev.silverandro.website.pages.MainPage
import dev.silverandro.website.pages.blog.*
import dev.silverandro.website.style.CommonStyle
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.absolute

fun main(args: Array<String>) {
    val debug = args.isNotEmpty() && args[0] == "debug"

    G.debug = debug

    val allStyleSheets = arrayOf(
        CommonStyle,
        MainPage.Style,
        AboutMePage.Style,
        BlogPost.Style,
    )

    val pages = arrayOf(
        // Main Pages
        MainPage,
        AboutMePage,

        // Blog Posts
        IntroToOW2Asm,
        EvilBureaucracy,
        SometimesIDropThings,
        AoTM
    )

    val resources = mapOf(
        "meta/robots.txt" to "robots.txt",
        "meta/favicon.ico" to "favicon.ico",
        "meta/site_image.png" to "site_image.png",
        "content/highlight.min.js" to "script/highlight.js",
        "content/tomorrow.min.css" to "style/code_tomorrow.css",

        "well_known/discord" to ".well-known/discord",
    )

    val root = Path("web_output/").absolute()
    root.toFile().mkdirs()

    resources.forEach { (resource, target) ->
        val stream = MainPage.javaClass.getResourceAsStream("/$resource")
        require(stream != null) { "Failed to locate raw resource $resource" }

        val targetFile = root.resolve(target)
        targetFile.parent.toFile().mkdirs()

        println("Copying raw resource $resource to $target")
        Files.copy(stream, root.resolve(target), StandardCopyOption.REPLACE_EXISTING)
        stream.close()
    }

    root.resolve("style").toFile().mkdirs()
    allStyleSheets.forEach {
        val output = root.resolve("style/${it.name}.css").toFile()
        println("Creating css file ${it.name}.css")
        output.parentFile.mkdirs()
        output.createNewFile()

        val sheet = it.assemble()
        val minimized = sheet.lines()
            .filter { it.isNotBlank() }
            .joinToString(separator = "")
            .replace(" 0px", " 0")
            .replace("  ", "")
            .replace(": ", ":")
            .replace(") ", ")")
            .replace(" {", "{")
            .replace(", ", ",")
            .replace("/\\*.+?\\*/".toRegex(), "")
        output.outputStream().bufferedWriter().use { writer ->
            writer.write(if (debug) sheet else minimized)
        }
    }

    pages.forEach {
        val pagePath = it.path.removePrefix("/")
        val output = root.resolve("${pagePath}.html").toFile()
        output.parentFile.mkdirs()
        println("Creating html file ${pagePath}.html")
        output.createNewFile()

        val methods = it::class.java.methods
        val result = createHTML(prettyPrint = debug).html {
            lang = "en"
            head {
                // Page title
                title(it.title)

                // Common setup
                meta(charset = "utf-8")
                meta("viewport", "width=device-width, initial-scale=1.0")

                // Meta properties
                metaProperty("og:title", it.title)
                if (pagePath != "index") {
                    metaProperty("og:url", "https://www.silverandro.dev/$pagePath")
                } else {
                    metaProperty("og:url", "https://www.silverandro.dev/")
                }
                metaProperty("og:image", "https://www.silverandro.dev/site_image.png")
                metaProperty("og:description", it.description)
                meta("description", it.description)
                meta("theme-color", "#BF3FBF")

                // Favicon
                link("/favicon.ico", "icon", "image/x-icon")

                // Fonts
                link("https://fonts.googleapis.com", "preconnect")
                link("https://fonts.gstatic.com", "preconnect") {
                    attributes["crossorigin"] = ""
                }
                link("https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@600&family=Lato&display=swap") {
                    attributes["as"] = "style"
                }

                // CSS
                it.styleSheets().forEach {
                    if (!allStyleSheets.contains(it)) throw IllegalStateException("Attempting to use unknown stylesheet \"${it.name}\"")
                    if (debug) {
                        val pre = buildString { repeat(G.d(pagePath)) { append("../") } }
                        link("${pre}style/${it.name}.css", "stylesheet")
                    } else {
                        link("/style/${it.name}.css", "stylesheet")
                    }
                }

                // Anything else
                methods.find { it.name == "head" }!!.invoke(it, this)
            }

            body {
                methods.find { it.name == "preBody" }!!.invoke(it, this)
                methods.find { it.name == "body" }!!.invoke(it, this)
                methods.find { it.name == "postBody" }!!.invoke(it, this)
            }
        }

        output.outputStream().bufferedWriter().use { writer ->
            writer.write("<!DOCTYPE html>\n")
            writer.write(result)
        }
    }
}
