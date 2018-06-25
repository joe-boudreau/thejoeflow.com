package com.thejoeflow.blog

import com.thejoeflow.utils.parseMarkdownToHtml
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@ResponseStatus
class BlogRestEndpoint(private val blogService: BlogService) {

    @PostMapping("/api/blogpost")
    fun saveNewBlogPost(@RequestBody blogPost: BlogPost): ResponseEntity<String>{
        blogPost.content = parseMarkdownToHtml(blogPost.content)
        blogService.saveBlogPost(blogPost)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/api/blogpost/{id}")
    fun getBlogPostById(@PathVariable id: Long) : ResponseEntity<BlogPost>{
        val post = blogService.getPostById(id)
        return when(post != null){
            true -> ResponseEntity.ok(post!!)
            false -> ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/api/blogpost/{id}")
    fun updateBlogPost(@PathVariable id: Long, @RequestBody blogPost: BlogPost): ResponseEntity<String>{
        blogPost.content = parseMarkdownToHtml(blogPost.content)
        blogService.saveBlogPost(blogPost)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }
}