package com.thejoeflow.controller

import com.thejoeflow.config.AppProperties
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.service.BlogService
import com.thejoeflow.service.synd.SyndicationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

@Controller
class EditPostController(
        private val blogService: BlogService,
        private val syndicationService: SyndicationService,
        private val appProperties: AppProperties
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
        syndicationService.reloadFeeds()
        return "redirect:/editPost/$id?result=updated"
    }

    @PostMapping("/editPost/{id}/background")
    fun uploadNewPostBackground(@PathVariable id: Long, @RequestParam("background") background: MultipartFile): String{
        val post = blogService.getPostById(id) ?: return "error"
        val updated = post.copy(background = savePhoto(background, post.title))
        blogService.saveBlogPost(updated)
        return "redirect:/editPost/$id?result=updated"
    }

    @ModelAttribute("posts")
    fun getAllPosts() = blogService.blogPostsOrdered

    private fun savePhoto(background: MultipartFile, title: String): String {
        val backgroundFilename = title.hashCode().toString()
        // Save the file
        val outputFile = Path.of(appProperties.photoFolder, backgroundFilename).toFile()
        outputFile.outputStream().use { background.inputStream.copyTo(it) }
        return backgroundFilename
    }
}