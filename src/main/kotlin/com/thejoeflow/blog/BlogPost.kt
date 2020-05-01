package com.thejoeflow.blog

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*
import kotlin.math.min


@Document
@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(@JsonProperty("id") @Id val id: Long = Random().nextLong(),
                    @JsonProperty("title") val title: String = "",
                    @JsonProperty("content") val content: String = "",
                    @JsonProperty("published") val published: Date = Date.from(Instant.now()),
                    @JsonProperty("updated") var updated: Date  = Date.from(Instant.now()),
                    @JsonProperty("type") val type: PostType = PostType.OTHER,
                    @JsonProperty("score") val score: Score = Score(intArrayOf(0 , 0, 0), "")
                    ) {

    @Transient
    private val parsedHtml = Jsoup.parse(content)
    @Transient
    private val defaultImage = "images/default_cover.png"
    @Transient
    val firstImage = parsedHtml.selectFirst("img")?.attr("src") ?: defaultImage
    @Transient
    val first150Chars = getFirstChars()

    private fun getFirstChars(): String {
        val length = min(parsedHtml.text().length, 150)
        return parsedHtml.text().substring(0, length) + "..."
    }
}

@Document
data class Score(@JsonProperty("scores") val scores : IntArray,
            @JsonProperty("sandwich") var sandwich : String)

enum class PostType {
    //TODO: Remove these deprecated bitches
    BOOKREVIEW, POST, FICTION_REVIEW, NON_FICTION_REVIEW,  OTHER
}