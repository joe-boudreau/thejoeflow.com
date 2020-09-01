package com.thejoeflow.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.util.StringUtils
import java.time.Instant
import java.util.*
import kotlin.math.roundToInt


@Document
@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(@JsonProperty("id") @Id var id: Long = Random().nextLong(),
                    @JsonProperty("title") var title: String = "",
                    @JsonProperty("content") var content: String = "",
                    @JsonProperty("published") var published: Date = Date.from(Instant.now()),
                    @JsonProperty("updated") var updated: Date  = Date.from(Instant.now()),
                    @JsonProperty("type") var type: PostType = PostType.OTHER,
                    @JsonProperty("score") var score: Score = Score(),
                    @JsonProperty("background") var background: String = ""
) {

    @Transient
    private val parsedHtml = Jsoup.parse(content)
    @Transient
    private val defaultCover = "/images/default_cover.png"
    @Transient
    private val defaultBackground = "/images/postBackgroundImg.jpg"

    @Transient
    val firstImage = if (type != PostType.OTHER) {
        parsedHtml.selectFirst("img")?.attr("src") ?: defaultCover
    } else{
        defaultCover
    }

    @Transient
    val numberOfWholeStars = getStars().first
    @Transient
    val numberOfHalfStars = getStars().second

    private fun getStars(): Pair<Int, Int> {
        val r2 = (score.scores.sum()/21.0 * 5 * 2).roundToInt()
        return Pair(r2/2, r2%2)
    }

    @Transient
    val first150Chars = getFirstNChars(150)
    @Transient
    val backgroundImg = if (StringUtils.isEmpty(background)) defaultBackground else "/photos/$background"

    fun getFirstNChars(n: Int): String {
        //Get the content of the first paragraph in the post that isn't part of a quote
        val previewText = parsedHtml.selectFirst("p:not(blockquote p)")?.text()?.take(n)
        return "$previewText..."
    }
}

enum class PostType {
    FICTION_REVIEW, NON_FICTION_REVIEW,  OTHER
}