package com.thejoeflow.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("custom")
class AppProperties{
    lateinit var coverFolder: String
    lateinit var apiKey: String
    lateinit var sharedSecret: String
}