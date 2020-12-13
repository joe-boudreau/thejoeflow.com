package com.thejoeflow.service

import com.thejoeflow.domain.BlogPost
import com.thejoeflow.domain.PostType
import com.thejoeflow.mongo.posts.PostRepositoryInterface
import org.springframework.stereotype.Service
import java.text.DateFormatSymbols
import java.time.Instant
import java.time.ZoneId
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.LinkedHashMap


@Service
class BlogService(
        private val postRepository: PostRepositoryInterface,
) {
        private var blogPostsOrdered = postRepository.findAllBlogPostsSortedByPublishDateDesc()
        private var archiveMap = generateArchive()
        private var titlePostMap = generateTitlePostMap()

        fun getArchive(): Map<String, Map<String, List<BlogPost>>> {
                return archiveMap
        }

        fun getYearTotals(): Map<String, Int> {
                return archiveMap.entries
                                 .stream()
                                 .collect(Collectors.toMap(
                                         {(key, _) -> key},
                                         {(_, value) -> value.entries.flatMap { l -> l.value}.count()}))
        }

        fun getYearMonth(date: Date): Array<String> {
                val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                return arrayOf(localDate.year.toString(), getMonthName(localDate.monthValue))
        }

        fun getMonthName(number: Int): String {
                return DateFormatSymbols().months[number-1]
        }

        fun getPostById(id: Long): BlogPost? {
                return blogPostsOrdered.stream()
                                        .filter { bp -> bp.id == id}
                                        .findFirst()
                                        .orElseThrow { Exception("No blog found!")}
        }

        fun getPostByTitle(title: String): BlogPost? {
                return titlePostMap[title]
        }

        fun getBlogPosts(amount: Int, offset: Int): Array<BlogPost> {
                val available = (blogPostsOrdered.size - offset).coerceAtMost(amount)
                return Array(available) { i -> blogPostsOrdered[i + offset]}
        }

        fun getBlogPosts(amount: Int, offset: Int, vararg types: PostType): Array<BlogPost> {
                val blogPostsOfType = blogPostsOrdered.filter { bp -> types.contains(bp.type) }
                val available = blogPostsOfType.size.coerceAtMost(amount)
                return Array(available) { i -> blogPostsOfType[i + offset]}
        }

        fun getAllBlogPosts(): List<BlogPost> {
                return blogPostsOrdered
        }



        fun saveBlogPost(blogPost: BlogPost) {
                blogPost.updated = Date.from(Instant.now())
                postRepository.save(blogPost)
                reloadBlogCache()
        }

        fun deleteBlogPost(id: Long) {
                postRepository.deleteById(id)
                reloadBlogCache()
        }

        fun getTotalNumberOfPosts(): Int = blogPostsOrdered.size

        private fun reloadBlogCache() {
                blogPostsOrdered = postRepository.findAllBlogPostsSortedByPublishDateDesc()
                archiveMap = generateArchive()
                titlePostMap = generateTitlePostMap()
        }

        private fun generateTitlePostMap(): Map<String, BlogPost> {
                return blogPostsOrdered.stream().collect(Collectors.toMap(BlogPost::urlTitle) { bp -> bp })
        }

        private fun generateArchive(): Map<String, Map<String, List<BlogPost>>> {

                val archive = LinkedHashMap<String, MutableMap<String, MutableList<BlogPost>>>()

                var ym: Array<String>
                for(post in blogPostsOrdered){
                        ym = getYearMonth(post.published)
                        val year = archive[ym[0]]
                        if(year != null){
                                val month = year[ym[1]]
                                if(month != null){
                                        month.add(post)
                                }
                                else {
                                        year[ym[1]] = mutableListOf(post)
                                }
                        }
                        else{
                                archive[ym[0]] = mutableMapOf(Pair(ym[1], mutableListOf(post)))
                        }
                }
                return archive
        }
}
