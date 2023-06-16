package dev.silverandro.website

import dev.silverandro.website.components.metaProperty
import dev.silverandro.website.pages.AboutMePage
import dev.silverandro.website.pages.MainPage
import dev.silverandro.website.style.CommonStyle
import dev.silverandro.website.style.LinksStyle
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.absolute

fun main(args: Array<String>) {
    val debug = args.isNotEmpty() && args[0] == "debug"

    val allStyleSheets = arrayOf(
        CommonStyle,
        LinksStyle,
        MainPage.Style,
        AboutMePage.Style,
    )

    val pages = arrayOf(
        MainPage,
        AboutMePage,
    )

    val root = Path("web_output/").absolute()
    root.toFile().mkdirs()

    Files.copy(Path("favicon.ico"), root.resolve("favicon.ico"), StandardCopyOption.REPLACE_EXISTING)

    root.resolve("style").toFile().mkdirs()
    allStyleSheets.forEach {
        val output = root.resolve("style/${it.name}.css").toFile()
        println("Creating css file ${it.name}.css")
        output.createNewFile()
        output.outputStream().bufferedWriter().use { writer ->
            writer.write(it.getStyleSheet())
        }
    }

    pages.forEach {
        val pagePath = it.path.removePrefix("/")
        val output = root.resolve("${pagePath}.html").toFile()
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
                metaProperty("og:url", "https://www.silverandro.dev/$pagePath")
                metaProperty("og:image", "https://www.silverandro.dev/site_image.png")
                metaProperty("og:description", it.description)
                meta("theme-color", "#BF3FBF")

                // Favicon
                link("/favicon.ico", "icon", "image/x-icon")

                // Fonts
                link("https://fonts.googleapis.com", "preconnect")
                link("https://fonts.gstatic.com", "preconnect") {
                    attributes["crossorigin"] = ""
                }
                link("https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@600&family=Lato&display=swap", "stylesheet")

                // CSS
                it.styleSheets().forEach {
                    if (!allStyleSheets.contains(it)) throw IllegalStateException("Attempting to use unknown stylesheet \"${it.name}\"")
                    link("/style/${it.name}.css", "stylesheet")
                }

                // Anything else
                methods.find { it.name == "head" }!!.invoke(it, this)
            }

            methods.find { it.name == "preBody" }!!.invoke(it, this)

            body {
                methods.find { it.name == "body" }!!.invoke(it, this)
            }

            methods.find { it.name == "postBody" }!!.invoke(it, this)
        }

        output.outputStream().bufferedWriter().use { writer ->
            writer.write("<!DOCTYPE html>\n")
            writer.write(result)
        }
    }
}