package com.thejoeflow.website

import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepositoryInterface: MongoRepository<BlogPost, Long>, PostRepositoryInterfaceCustom

interface PostRepositoryInterfaceCustom {

    fun findAllPostsSortedByPublishDateDesc(): List<BlogPost>
}