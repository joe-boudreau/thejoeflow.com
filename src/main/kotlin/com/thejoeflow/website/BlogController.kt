package com.thejoeflow.website

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class BlogController(
        private val blogService: BlogService
) {

    @GetMapping("/blog")
    fun getPost(): String {
        return "blog"
    }

    @ModelAttribute("posts")
    fun blogPosts(): Array<BlogPost> {
        return blogService.getBlogPosts(3, 0)
    }

    @ModelAttribute("archive")
    fun getArchiveInfo(): Map<String, Map<String, MutableList<BlogPost>>>{
        return blogService.getArchive()
    }
}