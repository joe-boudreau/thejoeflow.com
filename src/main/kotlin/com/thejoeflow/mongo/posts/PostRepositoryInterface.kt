package com.thejoeflow.mongo.posts

import com.thejoeflow.domain.BlogPost
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepositoryInterface: MongoRepository<BlogPost, Long>, PostRepositoryInterfaceCustom

interface PostRepositoryInterfaceCustom {

    fun findAllBlogPostsSortedByPublishDateDesc(): List<BlogPost>
}