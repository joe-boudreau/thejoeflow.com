package com.thejoeflow.blog

import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepositoryInterface: MongoRepository<BlogPost, Long>, PostRepositoryInterfaceCustom

interface PostRepositoryInterfaceCustom {

    fun findAllBlogPostsSortedByPublishDateDesc(): List<BlogPost>
}