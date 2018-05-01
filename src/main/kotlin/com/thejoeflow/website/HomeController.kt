package com.thejoeflow.website

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController{

    @GetMapping("/home")
    fun getHome() : String {
        return "index"
    }
}