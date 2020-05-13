package com.thejoeflow.controller

import com.ninjasquad.springmockk.MockkBean
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.service.BlogService
import com.thejoeflow.domain.PostType
import com.thejoeflow.config.AppProperties
import com.thejoeflow.service.FlickrService
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest
class HomeControllerTests(@Autowired private val mockMvc: MockMvc) {

    @MockkBean(relaxed = true)
    private lateinit var appProperties: AppProperties
    @MockkBean
    private lateinit var blogService: BlogService
    @MockkBean
    private lateinit var flickrService: FlickrService

    private val reviewPosts = arrayOf(BlogPost(id = 0))
    private val otherPosts = arrayOf(BlogPost(id = 1))
    private val photoUrls = listOf("url1")

    @BeforeEach
    fun setup() {
        every { blogService.getBlogPosts(6, 0, PostType.FICTION_REVIEW, PostType.NON_FICTION_REVIEW) } returns reviewPosts
        every { blogService.getBlogPosts(6, 0, PostType.OTHER) } returns otherPosts
        every { flickrService.getPhotoURLSFromFeed() } returns photoUrls
    }

    @Test
    fun getHome_returnsHome() {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("bookReviews", reviewPosts))
                .andExpect(model().attribute("otherPosts", otherPosts))
                .andExpect(model().attribute("dadFlickrPhotos", photoUrls))
    }

    @Test
    fun getDefault_returnsHome() {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("bookReviews", reviewPosts))
                .andExpect(model().attribute("otherPosts", otherPosts))
                .andExpect(model().attribute("dadFlickrPhotos", photoUrls))
    }
}