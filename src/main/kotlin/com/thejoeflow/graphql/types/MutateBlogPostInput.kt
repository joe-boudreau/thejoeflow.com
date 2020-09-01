package com.thejoeflow.graphql.types

import com.thejoeflow.domain.PostType
import com.thejoeflow.domain.Score
import graphql.schema.GraphQLInputType

data class MutateBlogPostInput(
        val id: String,
        val title: String?,
        val content: String?,
        val type: PostType?,
        val score: Score?,
        val background: String?
): GraphQLInputType {
    override fun getName() = "MutateBlogPostInput"
}