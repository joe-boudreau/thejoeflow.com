package com.thejoeflow.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter




@Configuration
class WebSecurityConfiguration(private val customerUserDetailsService: CustomerUserDetailsService) : GlobalAuthenticationConfigurerAdapter() {

    override fun init(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(customerUserDetailsService)
    }
}

@Configuration
@EnableWebSecurity
class SecurityJavaConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.authorizeRequests().antMatchers("/api/*").fullyAuthenticated().and().httpBasic()
        http.authorizeRequests().antMatchers("/editPost/**").authenticated().and().formLogin()
    }
}