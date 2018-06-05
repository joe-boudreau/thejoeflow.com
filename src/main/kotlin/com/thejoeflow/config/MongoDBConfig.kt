package com.thejoeflow.config

import com.mongodb.Mongo
import com.mongodb.MongoClient
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories("org.thejoeflow.repository")
class MongoConfig(): AbstractMongoConfiguration() {

    override fun getDatabaseName(): String  {
        return "flowDB"
    }

    override fun mongoClient(): MongoClient{
        return MongoClient("127.0.0.1", 27017)
    }

    override fun getMappingBasePackage(): String {
        return "com.thejoeflow"
    }
}