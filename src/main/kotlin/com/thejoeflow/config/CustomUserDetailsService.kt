package com.thejoeflow.config

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class CustomerUserDetailsService(private val userRepository: UserRepositoryInterface, private val passwordEncoder: PasswordEncoder) : UserDetailsService {

    fun save(user: MongoUserDetails) {
        var passwordEncoded = passwordEncoder.encode(user.getPassword())
        userRepository.save(MongoUserDetails(user.username, passwordEncoded, user.authorities.map { ga -> ga.authority }))
    }
    override fun loadUserByUsername(email: String): UserDetails? {
        var userFound = userRepository.findByEmail(email)
        return userFound[0]
    }

}
