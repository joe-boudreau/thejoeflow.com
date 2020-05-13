package com.thejoeflow.controller

import com.ninjasquad.springmockk.MockkBean
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.service.BlogService
import com.thejoeflow.config.AppProperties
import com.thejoeflow.service.FlickrService
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest
class BlogControllerTests(@Autowired private val mockMvc: MockMvc) {

    @MockkBean(relaxed = true)
    private lateinit var appProperties: AppProperties
    @MockkBean
    private lateinit var blogService: BlogService
    @MockkBean
    private lateinit var flickrService: FlickrService

    private val testPosts = arrayOf(BlogPost(id = 0), BlogPost(id = 1), BlogPost(id = 2), BlogPost(id = 3), BlogPost(id = 4))
    private val archiveMap = mapOf(Pair("2020", mapOf(Pair("April", listOf(testPosts[0])),
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
    }

    @Test
    fun getPost_postExists_returnPostView() {

        mockMvc.perform(get("/blog/post").param("id", "1"))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("post"))
                .andExpect(model().attribute("blogPost", testPosts[1]))
                .andExpect { archivePopulated() }
         }

    private fun archivePopulated() {
        model().attribute("yearTotals", yearTotals)
        model().attribute("archive", archiveMap)
    }

    @Test
    fun getPost_postDoesntExist_throwException() {
        assertThrows<Exception> { mockMvc.perform(get("/blog/post").param("id", "123456")) }
    }

    @Test
    fun getBlog_page0_returnBlogViewOfFirstPage() {
        every { blogService.getBlogPosts(3, 0) } returns testPosts.sliceArray(IntRange(0, 2))
        mockMvc.perform(get("/blog")) //implicit first page
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("blog"))
                .andExpect(model().attribute("totalPages", 1)) //zero indexed
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("posts", testPosts.sliceArray(IntRange(0, 2)))) //first 3 posts
                .andExpect { archivePopulated() }
    }

    @Test
    fun getBlog_page1_returnBlogViewOfNextPage() {
        every { blogService.getBlogPosts(3, 3) } returns testPosts.sliceArray(IntRange(3, 4))

        mockMvc.perform(get("/blog/{page}", "1"))
                .andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("blog"))
                .andExpect(model().attribute("totalPages", 1)) //zero indexed
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("posts", testPosts.sliceArray(IntRange(3, 4)))) //next 2 posts
                .andExpect { archivePopulated() }
    }

    @Test
    fun getBlog_page2_throwError() {
        assertThrows<Exception> { mockMvc.perform(get("/blog/{page}", "2")) }
    }

    @Test
    fun getRootUrl_port80_returnRooturl(){
        val request = get("/blog").buildRequest(MockServletContext())
        request.contextPath = "/context/path"
        request.serverPort = 80
        request.scheme = "http"
        val rootUrl = BlogController(blogService).getRootUrl(request)
        assertEquals("http://localhost/context/path", rootUrl)
    }

    @Test
    fun getRootUrl_port443_returnRooturl(){
        val request = get("/blog").buildRequest(MockServletContext())
        request.contextPath = "/context/path"
        request.serverPort = 443
        request.scheme = "https"
        val rootUrl = BlogController(blogService).getRootUrl(request)
        assertEquals("https://localhost/context/path", rootUrl)
    }

    @Test
    fun getRootUrl_nonStandardPort_returnRooturl(){
        val request = get("/blog").buildRequest(MockServletContext())
        request.contextPath = "/context/path"
        request.serverPort = 69
        request.scheme = "https"
        val rootUrl = BlogController(blogService).getRootUrl(request)
        assertEquals("https://localhost:69/context/path", rootUrl)
    }
}