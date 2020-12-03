package com.thejoeflow.mongo

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients


@Configuration
@EnableMongoRepositories(basePackages = ["com.thejoeflow.mongo"])
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