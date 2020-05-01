package com.thejoeflow.blog

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thejoeflow.utils.parseMarkdownToHtml
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader


@RestController
@ResponseStatus
class BlogRestEndpoint(private val blogService: BlogService) {

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
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/api/blogpost", consumes = ["multipart/form-data"])
    fun saveNewBlogPost(@RequestParam title: String,
                        @RequestParam type: PostType,
                        @RequestParam content: MultipartFile,
                        @RequestParam("score") scoreJSON: String?,
                        @RequestParam(required = false) md: Boolean): ResponseEntity<String>{


        var stringContent = InputStreamReader(content.inputStream).use(InputStreamReader::readText)
        if (md){
            stringContent = parseMarkdownToHtml(stringContent)
        }

        if (type == PostType.OTHER){
            blogService.saveBlogPost(BlogPost(title = title, type = type, content = stringContent))
        }
        else{
            if (scoreJSON == null){
                return ResponseEntity(HttpStatus.BAD_REQUEST)
            }
            val score = jacksonObjectMapper().readValue<Score>(scoreJSON)
            blogService.saveBlogPost(BlogPost(title = title, type = type, content = stringContent, score = score))
        }
        return ResponseEntity(HttpStatus.CREATED)
    }



    @PutMapping("/api/blogpost/{id}")
    fun updateBlogPost(@RequestParam(required = false) md: Boolean, @PathVariable id: Long, @RequestBody blogPost: BlogPost): ResponseEntity<String>{
        var blogPostToSave = blogPost.copy(id = id)
        if(md){
            blogPostToSave = blogPostToSave.copy(content = parseMarkdownToHtml(blogPost.content))
        }
        blogService.saveBlogPost(blogPostToSave)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @DeleteMapping("/api/blogpost/{id}")
    fun deleteBlogPost( @PathVariable id: Long): ResponseEntity<String>{
        blogService.deleteBlogPost(id)
        return ResponseEntity(HttpStatus.OK)
    }
}