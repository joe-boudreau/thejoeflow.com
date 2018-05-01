package com.thejoeflow.website

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(@JsonProperty("id") val id: Long,
                    @JsonProperty("title") val title: String,
                    @JsonProperty("content") val content: String,
                    @JsonProperty("published") val published: Date,
                    @JsonProperty("updated") val updated: Date)