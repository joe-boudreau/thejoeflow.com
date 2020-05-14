package com.thejoeflow

import com.thejoeflow.config.AppProperties
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver


@SpringBootApplication
class WebsiteApplication

fun main(args: Array<String>) {
    SpringApplication.run(WebsiteApplication::class.java, *args)
}

@Configuration
class Resources(val appProperties: AppProperties) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/covers/**")
                .addResourceLocations("file:"+appProperties.coverFolder)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(PathResourceResolver())

        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:"+appProperties.photoFolder)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(PathResourceResolver())
    }
}
