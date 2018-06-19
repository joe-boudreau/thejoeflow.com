package com.thejoeflow.blog

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class NewPostReceiver {

    @PostMapping("/api/hello")
    fun returnGreeting(@RequestParam(value = "name") name : String) = Greeting(1, "Hello, " + name)
}

data class Greeting(val id: Long, val content: String)