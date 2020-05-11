package com.thejoeflow.utils

import com.thejoeflow.blog.BlogPost
import org.springframework.stereotype.Component

@Component
class ViewUtils {

    fun updatedLater(blogPost: BlogPost): Boolean{
        // updated at least a day after publication
        return blogPost.updated.time - blogPost.published.time > 1000*60*60*24
    }
}