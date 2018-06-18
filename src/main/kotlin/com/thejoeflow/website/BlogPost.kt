package com.thejoeflow.website

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(@JsonProperty("id") @Id val id: Long,
                    @JsonProperty("title") val title: String,
                    @JsonProperty("content") val content: String,
                    @JsonProperty("published") val published: Date,
                    @JsonProperty("updated") val updated: Date) {

    @Transient
    private val parsedHtml = Jsoup.parse(content)
    @Transient
    private val defaultImage = "/default_cover.jpg"
    @Transient
    val firstImage = parsedHtml.selectFirst("img")?.attr("src") ?: defaultImage
    @Transient
    val first150Chars = parsedHtml.text().substring(0,150) + "..."
}