package com.thejoeflow.mongo.posts

import com.thejoeflow.domain.BlogPost
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query

class PostRepositoryInterfaceImpl(private val mongoTemplate: MongoTemplate) : PostRepositoryInterfaceCustom {

    override fun findAllBlogPostsSortedByPublishDateDesc(): List<BlogPost> {
        return mongoTemplate.find(Query().with(Sort.by(Order(Direction.DESC, "published"))), BlogPost::class.java)
    }
}