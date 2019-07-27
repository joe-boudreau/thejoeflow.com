package com.thejoeflow.config

import org.apache.catalina.connector.Connector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
class BCryptBean{

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}

@Configuration
class WebSecurityConfiguration(private val customerUserDetailsService: CustomerUserDetailsService) : GlobalAuthenticationConfigurerAdapter() {

    @Value("\${server.port.http}")
    private val serverPortHttp: Int = 0

    @Value("\${server.port}")
    private val serverPortHttps: Int = 0

    override fun init(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(customerUserDetailsService)
    }

    @Bean
    fun servletContainer(): ServletWebServerFactory {
        val tomcat = TomcatServletWebServerFactory()
        val httpConnector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
        httpConnector.scheme = "http"
        httpConnector.port = serverPortHttp
        httpConnector.secure = false
        httpConnector.redirectPort = serverPortHttps
        tomcat.addAdditionalTomcatConnectors(httpConnector)
        return tomcat
    }
}

@Configuration
@EnableWebSecurity
class SecurityJavaConfig : WebSecurityConfigurerAdapter() {

    @Value("\${security.require.ssl}")
    private val requireSSL: Boolean = true

    override fun configure(http: HttpSecurity) {
        if(requireSSL){
            http.requiresChannel().anyRequest().requiresSecure()
        }
        http.csrf().disable()
        http.authorizeRequests().antMatchers("/api/*").fullyAuthenticated().and().httpBasic()
        http.authorizeRequests().antMatchers("/actuator/*").fullyAuthenticated().and().httpBasic()
        http.authorizeRequests().antMatchers("/editPost/**").authenticated().and().formLogin()
    }
}
