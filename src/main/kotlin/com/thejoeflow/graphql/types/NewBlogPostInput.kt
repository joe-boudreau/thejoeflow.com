package com.thejoeflow.graphql.types

import com.thejoeflow.domain.PostType
import com.thejoeflow.domain.Score

data class NewBlogPostInput (
        val id: String?,
        val title: String,
        val content: String,
        val type: PostType,
        val score: Score,
        val background: String?
)