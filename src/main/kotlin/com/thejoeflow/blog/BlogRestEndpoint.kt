package com.thejoeflow.blog

import com.thejoeflow.utils.parseMarkdownToHtml
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


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

    @PostMapping("/api/blogpost", consumes = ["multipart/form-data"])
    fun saveNewBlogPost(@RequestParam title: String,
                        @RequestParam type: PostType,
                        @RequestParam content: MultipartFile,
                        @RequestParam score: Score): ResponseEntity<String>{


        var blogPostToSave = BlogPost(title = title, type = type, content = parseMarkdownToHtml(content), score = score)

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

    @DeleteMapping("/api/blogpost/{id}")
    fun deleteBlogPost( @PathVariable id: Long): ResponseEntity<String>{
        blogService.deleteBlogPost(id)
        return ResponseEntity(HttpStatus.OK)
    }
}