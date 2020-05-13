package com.thejoeflow.mongo

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepositoryInterface: MongoRepository<MongoUserDetails, Long> {

    fun findByEmail(email: String): List<MongoUserDetails>
}