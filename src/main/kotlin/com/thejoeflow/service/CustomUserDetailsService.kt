package com.thejoeflow.service

import com.thejoeflow.mongo.MongoUserDetails
import com.thejoeflow.mongo.UserRepositoryInterface
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class CustomerUserDetailsService(private val userRepository: UserRepositoryInterface, private val passwordEncoder: PasswordEncoder) : UserDetailsService {

    fun save(user: MongoUserDetails) {
        val passwordEncoded = passwordEncoder.encode(user.password)
        userRepository.save(MongoUserDetails(user.username, passwordEncoded, user.authorities.map { ga -> ga.authority }))
    }
    override fun loadUserByUsername(email: String): UserDetails? {
        val userFound = userRepository.findByEmail(email)
        return userFound[0]
    }

}
