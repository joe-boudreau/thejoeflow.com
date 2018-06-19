package com.thejoeflow.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails


class MongoUserDetails(private val email: String, private val password: String, private val authorities: List<String>) : UserDetails {

    private val grantedAuthorities: List<GrantedAuthority> = AuthorityUtils.createAuthorityList(*authorities.toTypedArray())

    override fun getAuthorities() = grantedAuthorities
    override fun getPassword() = password
    override fun getUsername() = email

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}