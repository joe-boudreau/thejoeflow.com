package com.thejoeflow.controller

import com.ninjasquad.springmockk.MockkBean
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.service.BlogService
import com.thejoeflow.config.AppProperties
import com.thejoeflow.service.FlickrService
import io.mockk.*
import org.hamcrest.CoreMatchers.any
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest
class EditPostControllerTests(@Autowired private val mockMvc: MockMvc) {

    @MockkBean(relaxed = true)
    private lateinit var appProperties: AppProperties

    @MockkBean
    private lateinit var blogService: BlogService

    @MockkBean
    private lateinit var flickrService: FlickrService

    private val testPosts = arrayOf(BlogPost(id = 0), BlogPost(id = 1), BlogPost(id = 2), BlogPost(id = 3), BlogPost(id = 4))
    private val archiveMap = mapOf(
            Pair("2020", mapOf(Pair("April", listOf(testPosts[0])),
                    Pair("February", listOf(testPosts[1])))),
            Pair("2019", mapOf(Pair("October", listOf(testPosts[2])),
                    Pair("June", listOf(testPosts[3], testPosts[4])))))
    private val yearTotals = mapOf(Pair("2020", 2), Pair("2019", 3))

    @BeforeEach
    fun setup() {
        every { blogService.getArchive() } returns archiveMap
        every { blogService.getYearTotals() } returns yearTotals
        every { blogService.getPostById(range(0L, testPosts.size.toLong())) } answers { testPosts[(firstArg() as Long).toInt()] }
        every { blogService.getPostById(not(range(0L, testPosts.size.toLong()))) } throws Exception("404 Not Found!")
        every { blogService.getTotalNumberOfPosts() } returns testPosts.size
        every { blogService.blogPostsOrdered } returns testPosts.asList()
        every { blogService.saveBlogPost(any()) } just runs
    }

    @Test
    @WithMockUser(value = "test", roles = ["ADMIN"])
    fun get_returnEmptyPost() {
        mockMvc.perform(get("/editPost"))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("editPost"))
                .andExpect(model().attribute("post", any(BlogPost::class.java)))
                .andExpect(model().attribute("posts", testPosts.asList()))
    }

    @Test
    @WithMockUser(value = "test", roles = ["ADMIN"])
    fun get_providedId_returnPostWithId() {
        mockMvc.perform(get("/editPost/{id}", 1))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("editPost"))
                .andExpect(model().attribute("post", testPosts[1]))
                .andExpect(model().attribute("posts", testPosts.asList()))
    }

    @Test
    @WithMockUser(value = "test", roles = ["ADMIN"])
    fun updateExistingPost_updateTimeChanged_postSaved() {
        val oldPost = testPosts[0]
        val oldUpdatedAt = oldPost.updated
        val newContent = "NEW CONTENT!"
        mockMvc.perform(post("/editPost/{id}", 0)
                .param("id", oldPost.id.toString())
                .param("title", oldPost.title)
                .param("content", newContent)
                .param("published", oldPost.published.toString())
                .param("updated", oldPost.updated.toString()))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrlTemplate("/editPost/{id}?result=updated", 0))

        val postSlot = slot<BlogPost>()
        verify(exactly = 1) { blogService.saveBlogPost(capture(postSlot)) }
        val savedPost = postSlot.captured
        assertEquals(0, savedPost.id)
        assertEquals(newContent, savedPost.content)
        assertTrue(oldUpdatedAt < savedPost.updated)
    }
}