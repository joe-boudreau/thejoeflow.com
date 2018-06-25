package com.thejoeflow.blog

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(@JsonProperty("id") @Id val id: Long = Random().nextLong(),
                    @JsonProperty("title") var title: String,
                    @JsonProperty("content") var content: String,
                    @JsonProperty("published") var published: Date = Date.from(Instant.now()),
                    @JsonProperty("updated") var updated: Date  = Date.from(Instant.now()),
                    @JsonProperty("type") var type: PostType = PostType.BOOKREVIEW
                    ) {

    @Transient
    private val parsedHtml = Jsoup.parse(content)
    @Transient
    private val defaultImage = "images/default_cover.png"
    @Transient
    val firstImage = parsedHtml.selectFirst("img")?.attr("src") ?: defaultImage
    @Transient
    val first150Chars = parsedHtml.text().substring(0,150) + "..."
}

enum class PostType {
    BOOKREVIEW, POST
}