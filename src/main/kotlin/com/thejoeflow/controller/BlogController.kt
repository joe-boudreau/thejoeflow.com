package com.thejoeflow.controller

import com.thejoeflow.domain.BlogPost
import com.thejoeflow.service.BlogService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import kotlin.math.ceil
import kotlin.math.roundToInt


@Controller
class BlogController(
        private val blogService: BlogService
) {

    @GetMapping("/blog/post")
    fun getPostById(model: Model, @RequestParam id: Long): String {
        model.addAttribute("blogPost", blogService.getPostById(id))
        return "post"
    }

    @GetMapping("/blog/post/{title}")
    fun getPostByTitle(model: Model, @PathVariable title: String): String {
        model.addAttribute("blogPost", blogService.getPostByTitle(title))
        return "post"
    }

    @GetMapping("/blog")
    fun getBlogFirstPage(model: Model): String = getBlog(0 ,model)

    @GetMapping("/blog/{page}")
    fun getBlog(@PathVariable("page") page: Int = 0, model: Model): String {
        model.addAttribute("totalPages", totalNumberOfPages())
        model.addAttribute("currentPage", page)
        model.addAttribute("posts", blogService.getBlogPosts(3, page * 3))
        return "blog"
    }

    private fun totalNumberOfPages() = ceil(blogService.getTotalNumberOfPosts() / 3.0).roundToInt() - 1

    @ModelAttribute("archive")
    fun getArchiveInfo(): Map<String, Map<String, List<BlogPost>>>{
        return blogService.getArchive()
    }

    @ModelAttribute("yearTotals")
    fun getYearTotals(): Map<String, Int>{
        return blogService.getYearTotals()
    }

    @ModelAttribute("rootUrl")
    fun getRootUrl(request: HttpServletRequest): String{
        val serverPort = if (request.serverPort == 80 || request.serverPort == 443) "" else ":" + request.serverPort
        return request.scheme + "://" + request.serverName + serverPort + request.contextPath
    }
}