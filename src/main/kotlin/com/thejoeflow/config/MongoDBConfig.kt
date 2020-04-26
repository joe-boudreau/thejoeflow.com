package com.thejoeflow.config

import com.mongodb.MongoClient
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories(basePackages = ["com.thejoeflow.blog", "com.thejoeflow.config"])
class MongoConfig: AbstractMongoConfiguration() {

    override fun getDatabaseName(): String  {
        return "local"
    }

    override fun mongoClient(): MongoClient{
        return MongoClient("127.0.0.1", 27017)
    }

    override fun getMappingBasePackage(): String {
        return "com.thejoeflow"
    }
}