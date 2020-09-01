package com.thejoeflow.graphql

import com.thejoeflow.service.BlogService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLConfiguration {
    @Bean
    fun query(blogService: BlogService): Query {
        return Query(blogService)
    }

    @Bean
    fun mutation(blogService: BlogService): Mutation {
        return Mutation(blogService)
    }
}