package com.thejoeflow.mongo

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

class MongoUserDetails(private val email: String, private val password: String, private val authorities: List<String>) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> = AuthorityUtils.createAuthorityList(*authorities.toTypedArray())
    override fun getPassword() = password
    override fun getUsername() = email

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}