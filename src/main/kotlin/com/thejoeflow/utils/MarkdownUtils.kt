package com.thejoeflow.utils

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

fun parseMarkdownToHtml(md: String): String{
    val parser = Parser.builder().build()
    val document = parser.parse(md)
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(document)
}