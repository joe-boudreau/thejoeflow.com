package com.thejoeflow.website

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class HomeController(
        private val blogService: BlogService
) {

    @GetMapping("/home")
    fun getHome() : String {
        return "index"
    }

    @ModelAttribute("posts")
    fun getFirstThreeblogPosts(): Array<BlogPost> {
        return blogService.getBlogPosts(6, 0)
    }


}