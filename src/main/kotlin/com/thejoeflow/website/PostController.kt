package com.thejoeflow.website

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable

@Controller
class PostController(
        private val blogService: BlogService
) {

    @GetMapping("/blog/{id}")
    fun getPost(model: Model, @PathVariable("id") id: String): String {
        model.addAttribute("post", blogService.getPostById(id))
        return "post"
    }

    @ModelAttribute("archive")
    fun getArchiveInfo(): Map<String, Map<String, MutableList<BlogPost>>>{
        return blogService.getArchive()
    }
}