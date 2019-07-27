package com.thejoeflow.website

import com.thejoeflow.blog.BlogPost
import com.thejoeflow.blog.BlogService
import com.thejoeflow.blog.PostType
import com.thejoeflow.utils.FlickrService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import java.util.*

@Controller
class HomeController(
        private val blogService: BlogService,
        private val flickrService: FlickrService
) {
    private val NUMBER_OF_BG_IMAGES = 10

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

    @ModelAttribute("randNum")
    fun getRandomNumberForBgImage(): Int = Random().nextInt(NUMBER_OF_BG_IMAGES + 1)

    @ModelAttribute("dadFlickrPhotos")
    fun getRecentFlickr(): List<String> = flickrService.getPhotoURLSFromFeed()


}