package com.thejoeflow.blog

import com.thejoeflow.utils.parseMarkdownToHtml
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@ResponseStatus
class BlogRestEndpoint(private val blogService: BlogService) {

    @GetMapping("/api/blogpost/{id}")
    fun getBlogPostById(@PathVariable id: Long) : ResponseEntity<BlogPost>{
        val post = blogService.getPostById(id)
        return when(post != null){
            true -> ResponseEntity.ok(post!!)
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

    @PutMapping("/api/blogpost/{id}")
    fun updateBlogPost(@RequestParam(required = false) md: Boolean, @PathVariable id: Long, @RequestBody blogPost: BlogPost): ResponseEntity<String>{

        var blogPostToSave = blogPost.copy(id = id)
        if(md){
            blogPostToSave = blogPostToSave.copy(content = parseMarkdownToHtml(blogPost.content))
        }
        blogService.saveBlogPost(blogPostToSave)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }
}