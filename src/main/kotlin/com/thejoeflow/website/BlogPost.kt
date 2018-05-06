package com.thejoeflow.website

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(@JsonProperty("id") val id: Long,
                    @JsonProperty("title") val title: String,
                    @JsonProperty("content") val content: String,
                    @JsonProperty("published") val published: Date,
                    @JsonProperty("updated") val updated: Date) {

    private val htmlImageRegex = Regex("<img.*src=\\\"(.*?)\\\"")
    private val defaultImage = "/default_cover.jpg"
    val firstImage = htmlImageRegex.find(content, 0)?.groups?.get(1)?.value ?: defaultImage
    val first150Chars = Jsoup.parse(content).text().substring(0,150) + "..."
}