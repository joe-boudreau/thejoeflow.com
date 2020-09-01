package com.thejoeflow.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Score(@JsonProperty("scores") val scores : List<Int> = listOf(0, 0, 0),
                 @JsonProperty("sandwich") var sandwich : String = "")