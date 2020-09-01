package com.thejoeflow.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.service.BlogService

class Query(private val blogService: BlogService) : GraphQLQueryResolver {

    fun blogPost(id: String): BlogPost? {
        return blogService.getPostById(id.toLong())
    }

    fun blogPosts(limit: Int = 10, offset: Int = 0): List<BlogPost> {
        return blogService.getBlogPosts(limit, offset).toList()
    }
}