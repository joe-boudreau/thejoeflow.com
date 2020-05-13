package com.thejoeflow.controller

import com.thejoeflow.service.BlogService
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.domain.PostType
import com.thejoeflow.service.FlickrService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class HomeController(
        private val blogService: BlogService,
        private val flickrService: FlickrService
) {

    @GetMapping("/home")
    fun getHome() : String {
        return "home"
    }

    @GetMapping("/")
    fun getHomeDefault() : String {
        return "home"
    }

    @ModelAttribute("bookReviews")
    fun getLatestSixBookReviews(): Array<BlogPost> = blogService.getBlogPosts(6, 0, PostType.FICTION_REVIEW, PostType.NON_FICTION_REVIEW)

    @ModelAttribute("otherPosts")
    fun getLatestSixOtherPosts(): Array<BlogPost> = blogService.getBlogPosts(6, 0, PostType.OTHER)

    @ModelAttribute("dadFlickrPhotos")
    fun getRecentFlickr(): List<String> = flickrService.getPhotoURLSFromFeed()


}