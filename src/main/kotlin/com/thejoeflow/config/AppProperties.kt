package com.thejoeflow.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("custom")
class AppProperties{
    var coverFolder: String = ""
    var photoFolder: String = ""
    var apiKey: String = ""
    var sharedSecret: String = ""
}