package com.thejoeflow.domain

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
                    @JsonProperty("title") val title: String = "",
                    @JsonProperty("content") val content: String = "",
                    @JsonProperty("published") val published: Date = Date.from(Instant.now()),
                    @JsonProperty("updated") var updated: Date  = Date.from(Instant.now()),
                    @JsonProperty("type") val type: PostType = PostType.OTHER,
                    @JsonProperty("score") val score: Score? = null,
                    @JsonProperty("background") val background: String? = null
) {

    @Transient
    private val parsedHtml = Jsoup.parse(content)
    @Transient
    private val defaultCover = "/images/default_cover.png"
    @Transient
    private val defaultBackground = "/images/postBackgroundImg.jpg"

    @Transient
    val firstImage = parsedHtml.selectFirst("img")?.attr("src") ?: defaultCover
    @Transient
    val first150Chars = getFirstNChars(150)
    @Transient
    val backgroundImg = if (background != null) "/photos/$background" else defaultBackground

    fun getFirstNChars(n: Int): String {
        //Get the content of the first paragraph in the post that isn't part of a quote
        val previewText = parsedHtml.selectFirst("p:not(blockquote p)")?.text()?.take(n)
        return "$previewText..."
    }
}

@Document
data class Score(@JsonProperty("scores") val scores : IntArray = intArrayOf(0, 0, 0),
            @JsonProperty("sandwich") var sandwich : String = "")

enum class PostType {
    //TODO: Remove these deprecated bitches
    BOOKREVIEW, POST, FICTION_REVIEW, NON_FICTION_REVIEW,  OTHER
}