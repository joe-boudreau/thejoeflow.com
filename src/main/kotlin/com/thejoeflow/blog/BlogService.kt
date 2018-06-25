package com.thejoeflow.blog

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.blogger.Blogger
import com.google.api.services.blogger.BloggerScopes
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.DateFormatSymbols
import java.time.ZoneId
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap


@Service
class BlogService(
        private val postRepository: PostRepositoryInterface
) {

        val CLIENT_ID_FILE = "build/resources/main/private/client_secret_983358690299-a0kc29n179c2gat4fkk0nlpj9dc7q30p.apps.googleusercontent.com.json"
        val APP_NAME = "website-blog"
        val BLOG_ID = "3036352493674726765"
        val POST_DIRECTORY = "build/resources/main/posts"

        val mapper = ObjectMapper().registerKotlinModule()

        var blogPostsOrdered = postRepository.findAllBlogPostsSortedByPublishDateDesc()

        val archiveMap = generateArchive()

        fun getPostsFromBlogger() {
                val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
                val jsonFactory = JacksonFactory.getDefaultInstance()
                val scopes = Arrays.asList(BloggerScopes.BLOGGER)

                // load client secrets
                val clientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(FileInputStream(CLIENT_ID_FILE)))

                // set up authorization code flow
                val flow = GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes).build()

                //Configure the Installed App OAuth2 flow.
                val credential = AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")

                val blogger = Blogger.Builder(httpTransport, jsonFactory, credential).setApplicationName(APP_NAME).setHttpRequestInitializer(credential).build()

                // The request action object.
                val blogPostsRetrieve = blogger.posts().list(BLOG_ID)
                blogPostsRetrieve.blogId = BLOG_ID
                blogPostsRetrieve.maxResults = 100


                // Restrict the result content to just the data we need.
                val posts = blogPostsRetrieve.execute()
                val factory = posts.items[0].factory

                var file: File
                var blogPost: BlogPost
                for(post in posts.items) {
                        blogPost = mapper.readValue(factory.toString(post))
                        file = File(POST_DIRECTORY + "/" + post.id + ".json")
                        mapper.writeValue(file, blogPost)

                }
        }

        fun generateArchive(): Map<String, Map<String, List<BlogPost>>>{

                val archive = HashMap<String, MutableMap<String, MutableList<BlogPost>>>()

                var ym: Array<String>
                for(post in blogPostsOrdered){
                        ym = getYearMonth(post.published)
                        var year = archive[ym[0]]
                        if(year != null){
                                val month = year[ym[1]]
                                if(month != null){
                                        month.add(post)
                                }
                                else {
                                        year.put(ym[1], mutableListOf(post))
                                }
                        }
                        else{
                                archive.put(ym[0], mutableMapOf(Pair(ym[1], mutableListOf(post))))
                        }
                }
                return archive
        }

        fun getArchive(): Map<String, Map<String, List<BlogPost>>>{
                return archiveMap
        }

        fun getYearTotals(): Map<String, Int>{
                return archiveMap.entries
                                 .stream()
                                 .collect(Collectors.toMap({(key, _) -> key},
                                                           {(_, value) -> value.entries.flatMap({l -> l.value}).count()}))
        }

        fun getYearMonth(date: Date): Array<String> {
                val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                return arrayOf(localDate.year.toString(), getMonthName(localDate.monthValue))
        }

        fun getMonthName(number: Int): String{
                return DateFormatSymbols().months[number-1]
        }

        fun getPostById(id: Long): BlogPost? {
                return blogPostsOrdered.stream()
                                       .filter({bp -> bp.id == id})
                                       .findFirst()
                                       .orElseThrow { Exception("No blog found!")}
        }

        fun getBlogPosts(amount: Int, offset: Int): Array<BlogPost>{
                var available = Math.min(amount, blogPostsOrdered.size - offset)
                return Array(available, {i -> blogPostsOrdered[i + offset]})
        }

        fun getBlogPosts(amount: Int, offset: Int, type: PostType): Array<BlogPost>{
                var blogPostsOfType = blogPostsOrdered.filter { bp -> bp.type == type }
                var available = Math.min(amount, blogPostsOfType.size)
                return Array(available, {i -> blogPostsOfType[i + offset]})
        }


        fun saveBlogPost(blogPost: BlogPost){
                postRepository.save(blogPost)
                reloadBlogCache()
        }

        private fun reloadBlogCache() {
                blogPostsOrdered = postRepository.findAllBlogPostsSortedByPublishDateDesc()
        }

        fun getTotalNumberOfPosts(): Int = blogPostsOrdered.size
}
