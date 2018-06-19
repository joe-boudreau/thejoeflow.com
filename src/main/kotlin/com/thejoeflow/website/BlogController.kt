package com.thejoeflow.website

import com.thejoeflow.blog.BlogPost
import com.thejoeflow.blog.BlogService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable

@Controller
class BlogController(
        private val blogService: BlogService
) {

    @GetMapping("/blog/{id}")
    fun getPost(model: Model, @PathVariable("id") id: Long): String {
        model.addAttribute("blogPost", blogService.getPostById(id.toString()))
        return "post"
    }

    @GetMapping("/blog")
    fun getBlog(): String = "blog"

    @ModelAttribute("posts")
    fun blogPosts(): Array<BlogPost> {
        return blogService.getBlogPosts(3, 0)
    }

    @ModelAttribute("archive")
    fun getArchiveInfo(): Map<String, Map<String, List<BlogPost>>>{
        return blogService.getArchive()
    }

    @ModelAttribute("yearTotals")
    fun getYearTotals(): Map<String, Int>{
        return blogService.getYearTotals()
    }
}