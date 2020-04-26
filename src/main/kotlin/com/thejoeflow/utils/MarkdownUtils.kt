package com.thejoeflow.utils

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader

fun parseMarkdownToHtml(md: String): String{
    val parser = Parser.builder().build()
    val document = parser.parse(md)
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(document)
}

fun parseMarkdownToHtml(md: MultipartFile): String{
    val parser = Parser.builder().build()
    val document = parser.parseReader(InputStreamReader(md.inputStream))
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(document)
}

