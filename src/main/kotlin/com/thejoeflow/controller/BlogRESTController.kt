package com.thejoeflow.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thejoeflow.config.AppProperties
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.domain.PostType
import com.thejoeflow.domain.Score
import com.thejoeflow.service.BlogService
import com.thejoeflow.service.synd.SyndicationService
import com.thejoeflow.utils.parseMarkdownToHtml
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.nio.file.Path


@RestController
@ResponseStatus
class BlogRESTController(
        private val blogService: BlogService,
        private val syndicationService: SyndicationService,
        private val appProperties: AppProperties
) {

    @GetMapping("/api/blogpost")
    fun getBlogPosts(@RequestParam(required = false) removeContent: Boolean) : ResponseEntity<List<BlogPost>>{
        var posts = blogService.blogPostsOrdered
        if (removeContent){
            posts = posts.map { bp -> bp.copy(content = "")}
        }
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/api/blogpost/{id}")
    fun getBlogPostById(@PathVariable id: Long) : ResponseEntity<BlogPost>{
        val post = blogService.getPostById(id)
        return when(post != null){
            true -> ResponseEntity.ok(post)
            false -> ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/api/blogpost")
    fun saveNewBlogPost(@RequestParam(required = false) md: Boolean, @RequestBody blogPost: BlogPost): ResponseEntity<String>{

        var blogPostToSave = blogPost
        if(md){
            blogPostToSave = blogPost.copy(content = parseMarkdownToHtml(blogPost.content))
        }
        blogService.saveBlogPost(blogPostToSave)
        syndicationService.reloadFeeds()
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/api/blogpost", consumes = ["multipart/form-data"])
    fun saveNewBlogPost(@RequestParam title: String,
                        @RequestParam type: PostType,
                        @RequestParam content: MultipartFile,
                        @RequestParam(required = false) md: Boolean,
                        @RequestParam("score") scoreJSON: String?,
                        @RequestParam background: MultipartFile?): ResponseEntity<String>{


        // Post content
        var stringContent = InputStreamReader(content.inputStream).use(InputStreamReader::readText)
        if (md){
            stringContent = parseMarkdownToHtml(stringContent)
        }

        // Score - if it's a book review
        var score =
        if (type != PostType.OTHER) {
            if (scoreJSON == null){
                return ResponseEntity("Book review posts require a Score parameter", HttpStatus.BAD_REQUEST)
            }
            jacksonObjectMapper().readValue<Score>(scoreJSON)
        }
        else {
            Score()
        }

        // Post background image
        val backgroundFilename: String = if (background != null) savePhoto(background, title) else ""

        blogService.saveBlogPost(BlogPost(
                title = title,
                type = type,
                content = stringContent,
                score = score,
                background = backgroundFilename))

        syndicationService.reloadFeeds()
        return ResponseEntity(HttpStatus.CREATED)
    }


    @PutMapping("/api/blogpost/{id}")
    fun updateBlogPost(@RequestParam(required = false) md: Boolean, @PathVariable id: Long, @RequestBody blogPost: BlogPost): ResponseEntity<String>{
        var blogPostToSave = blogPost.copy(id = id)
        if(md){
            blogPostToSave = blogPostToSave.copy(content = parseMarkdownToHtml(blogPost.content))
        }
        blogService.saveBlogPost(blogPostToSave)
        syndicationService.reloadFeeds()
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @PutMapping("/api/blogpost/{id}", consumes = ["multipart/form-data"])
    fun updateBlogPostBackground(@PathVariable id: Long, @RequestParam background: MultipartFile): ResponseEntity<String>{
        val post = blogService.getPostById(id) ?: return ResponseEntity("Post with ID: $id not found", HttpStatus.NOT_FOUND)
        val updated = post.copy(background = savePhoto(background, post.title))
        blogService.saveBlogPost(updated)
        syndicationService.reloadFeeds()
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @DeleteMapping("/api/blogpost/{id}")
    fun deleteBlogPost( @PathVariable id: Long): ResponseEntity<String>{
        blogService.deleteBlogPost(id)
        return ResponseEntity(HttpStatus.OK)
    }

    private fun savePhoto(background: MultipartFile, title: String): String {
        val backgroundFilename = title.hashCode().toString()
        // Save the file
        val outputFile = Path.of(appProperties.photoFolder, backgroundFilename).toFile()
        outputFile.outputStream().use { background.inputStream.copyTo(it) }
        return backgroundFilename
    }
}