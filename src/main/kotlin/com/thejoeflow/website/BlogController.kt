package com.thejoeflow.website

import com.thejoeflow.blog.BlogPost
import com.thejoeflow.blog.BlogService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest


@Controller
class BlogController(
        private val blogService: BlogService
) {

    @GetMapping("/blog/post")
    fun getPost(model: Model, @RequestParam("id") id: Long): String {
        model.addAttribute("blogPost", blogService.getPostById(id))
        return "post"
    }

    @GetMapping("/blog")
    fun getBlogFirstPage(model: Model): String = getBlog(0 ,model)

    @GetMapping("/blog/{page}")
    fun getBlog(@PathVariable("page") page: Int = 0, model: Model): String {
        model.addAttribute("totalPages", totalNumberOfPages())
        model.addAttribute("currentPage", page)
        model.addAttribute("posts", getThreeBlogPosts(page * 3))
        return "blog"
    }

    private fun totalNumberOfPages() = Math.round(Math.ceil(blogService.getTotalNumberOfPosts() / 3.0)) - 1

    fun getThreeBlogPosts(offset: Int): Array<BlogPost> {
        return blogService.getBlogPosts(3, offset)
    }

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