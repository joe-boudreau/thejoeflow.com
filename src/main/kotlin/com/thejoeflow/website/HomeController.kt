package com.thejoeflow.website

import com.thejoeflow.blog.BlogPost
import com.thejoeflow.blog.BlogService
import com.thejoeflow.blog.PostType
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

    @ModelAttribute("bookReviews")
    fun getLatestSixBookReviews(): Array<BlogPost> {
        return blogService.getBlogPosts(6, 0, PostType.BOOKREVIEW)
    }

    @ModelAttribute("otherPosts")
    fun getLatestSixOtherPosts(): Array<BlogPost> {
        return blogService.getBlogPosts(6, 0, PostType.POST)
    }


}