package com.thejoeflow.website

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.blogger.Blogger
import com.google.api.services.blogger.BloggerScopes
import com.google.api.services.blogger.model.Blog
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap
import java.time.ZoneId



@Service
class BlogService(){

        val CLIENT_ID_FILE = "build/resources/main/private/client_secret_983358690299-a0kc29n179c2gat4fkk0nlpj9dc7q30p.apps.googleusercontent.com.json"
        val APP_NAME = "website-blog"
        val BLOG_ID = "3036352493674726765"
        val POST_DIRECTORY = "build/resources/main/posts"

        val mapper = ObjectMapper().registerKotlinModule()

        val blogPostsOrdered = populateBlogCache()

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

        fun getArchive(): Map<String, Map<String, MutableList<BlogPost>>>{

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
                                        year.put(ym[1], mutableListOf<BlogPost>(post))
                                }
                        }
                        else{
                                archive.put(ym[0], mutableMapOf<String, MutableList<BlogPost>>(Pair(ym[1], mutableListOf<BlogPost>(post))))
                        }
                }
                return archive
        }

        fun getYearMonth(date: Date): Array<String> {
                val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                return arrayOf(localDate.year.toString(), getMonthName(localDate.monthValue))
        }

        fun getMonthName(number: Int): String{
                return DateFormatSymbols().months[number-1]
        }

        fun getPostById(id: String): BlogPost? {
                for (blog in blogPostsOrdered){
                        if(blog.id.toString().equals(id)){
                                return blog
                        }
                }
                return null
        }

        fun getBlogPosts(amount: Int, offset: Int): Array<BlogPost>{
                return Array(amount, {i -> blogPostsOrdered[i + offset]})
        }

        fun populateBlogCache(): List<BlogPost> {

                val blogList = mutableListOf<BlogPost>()
                var blogPost: BlogPost
                File(POST_DIRECTORY + "/").walk().forEach {
                        if(it.isFile){
                                blogList.add(mapper.readValue(it))
                        }
                }

                blogList.sortByDescending(BlogPost::published)
                return blogList
        }

        fun parseJSONToBlogPost(jsonPost: String): BlogPost {

                val mapper = ObjectMapper().registerKotlinModule()
                return mapper.readValue(jsonPost)

        }

}
