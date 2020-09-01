package com.thejoeflow.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.thejoeflow.domain.BlogPost
import com.thejoeflow.graphql.types.MutateBlogPostInput
import com.thejoeflow.graphql.types.NewBlogPostInput
import com.thejoeflow.service.BlogService

class Mutation(private val blogService: BlogService) : GraphQLMutationResolver {

    fun saveBlogPost(blogPost: NewBlogPostInput): BlogPost {
        val bp = createNewBlogPost(blogPost)
        blogService.saveBlogPost(bp)
        return bp
    }

    fun updateBlogPost(blogPost: MutateBlogPostInput): BlogPost {
        val bp = updateExistingBlogPost(blogPost)
        blogService.saveBlogPost(bp)
        return bp
    }

    fun deleteBlogPost(id: String): Boolean {
        try {
            blogService.deleteBlogPost(id.toLong())
        }
        catch (e: Exception){
            return false
        }
        return true
    }

    private fun createNewBlogPost(bp: NewBlogPostInput): BlogPost {
        val newBp = BlogPost(title=bp.title, content=bp.content, type=bp.type, score=bp.score)
        if (bp.id != null){
            newBp.id = bp.id.toLong()
        }
        if (bp.background != null) {
            newBp.background = bp.background;
        }
        return newBp
    }

    private fun updateExistingBlogPost(bp: MutateBlogPostInput): BlogPost {
        val oldBp = blogService.getPostById(bp.id.toLong()) ?: throw PostNotFoundException()

        oldBp.title = bp.title ?: oldBp.title
        oldBp.content = bp.content ?: oldBp.content
        oldBp.score = bp.score ?: oldBp.score
        oldBp.type = bp.type ?: oldBp.type

        return oldBp
    }
}

class PostNotFoundException : Exception()
