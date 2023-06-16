package dev.silverandro.website

abstract class StyleSheet {
    abstract val name: String
    abstract fun getStyleSheet(): String
}