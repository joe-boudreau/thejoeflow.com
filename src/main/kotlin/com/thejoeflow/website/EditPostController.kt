package com.thejoeflow.website

import com.thejoeflow.blog.BlogPost
import com.thejoeflow.blog.BlogService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class EditPostController(
        private val blogService: BlogService
) {

    @GetMapping("/editPost")
    fun get(model: Model): String{
        model.addAttribute("post", BlogPost())
        return "editPost"
    }

    @GetMapping("/editPost/{id}")
    fun get(@PathVariable id: Long, model: Model): String{
        model.addAttribute("post", blogService.getPostById(id))
        return "editPost"
    }

    @PostMapping("/editPost/{id}")
    fun updateExistingPost(@PathVariable id: Long, @ModelAttribute blogPost: BlogPost): String{
        blogService.saveBlogPost(blogPost)
        return "redirect:/editPost/" + id +"?result=updated"
    }

    @ModelAttribute("posts")
    fun getAllPosts(): List<BlogPost>{
        return blogService.blogPostsOrdered
    }
}