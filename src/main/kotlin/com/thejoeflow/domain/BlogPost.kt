package com.thejoeflow.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.boot.web.servlet.server.Encoding
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.util.StringUtils
import org.springframework.web.util.UriUtils
import java.time.Instant
import java.util.*
import kotlin.math.roundToInt

const val defaultCover = "/images/default_cover.png"
const val defaultBackground = "/images/postBackgroundImg.jpg"

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(
        @JsonProperty("id") @Id val id: Long = Random().nextLong(),
        @JsonProperty("title") val title: String = "",
        @JsonProperty("content") val content: String = "",
        @JsonProperty("published") val published: Date = Date.from(Instant.now()),
        @JsonProperty("updated") var updated: Date  = Date.from(Instant.now()),
        @JsonProperty("type") val type: PostType = PostType.OTHER,
        @JsonProperty("score") val score: Score = Score(),
        @JsonProperty("background") val background: String = ""
) {
    @Transient
    private val parsedHtml = Jsoup.parse(content)

    @Transient
    val urlTitle = createUrlTitle()
    @Transient
    val firstImage = getCoverImage()
    @Transient
    val first150Chars = getFirstNChars(150)
    @Transient
    val backgroundImg = if (StringUtils.hasLength(background)) "/photos/$background" else defaultBackground
    @Transient
    val numberOfWholeStars = getStars().first
    @Transient
    val numberOfHalfStars = getStars().second

    private fun getCoverImage(): String {
        if (type == PostType.OTHER) {
            return defaultCover
        }
        var coverImg: Element? = parsedHtml.selectFirst("img.book-cover")
        coverImg = coverImg ?: parsedHtml.selectFirst("img")

        return coverImg?.attr("src") ?: defaultCover
    }

    private fun getStars(): Pair<Int, Int> {
        val r2 = (score.scores.sum()/21.0 * 5 * 2).roundToInt()
        return Pair(r2/2, r2%2)
    }

    private fun createUrlTitle(): String {
        val normalized = title.toLowerCase().replace(Regex("""\s+"""), "-")
        return UriUtils.encodePathSegment(normalized, Encoding.DEFAULT_CHARSET)
    }

    fun getFirstNChars(n: Int): String {
        //Get the content of the first paragraph in the post that isn't part of a quote
        val previewText = parsedHtml.selectFirst("p:not(blockquote p)")?.text()?.take(n)
        return "$previewText..."
    }
}

@Document
data class Score(
        @JsonProperty("scores") val scores : IntArray = intArrayOf(0, 0, 0),
        @JsonProperty("sandwich") var sandwich : String = ""
)

enum class PostType {
    FICTION_REVIEW,
    NON_FICTION_REVIEW,
    OTHER
}