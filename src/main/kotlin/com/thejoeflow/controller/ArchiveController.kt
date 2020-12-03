package com.thejoeflow.controller

import com.thejoeflow.service.BlogService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class ArchiveController(
        private val blogService: BlogService
) {
    @GetMapping("/blog/archive")
    fun getArchive() = "archive"

    @ModelAttribute("posts")
    fun getAllPosts() = blogService.getAllBlogPosts()
}