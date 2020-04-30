package com.thejoeflow.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories(basePackages = ["com.thejoeflow.blog", "com.thejoeflow.config"])
class MongoConfig: AbstractMongoClientConfiguration() {

    override fun mongoClient(): MongoClient {
        return MongoClients.create()
    }

    override fun getDatabaseName(): String {
        return "local"
    }

    override fun getMappingBasePackage(): String {
        return "com.thejoeflow"
    }
}