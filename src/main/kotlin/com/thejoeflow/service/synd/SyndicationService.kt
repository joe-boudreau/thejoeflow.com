package com.thejoeflow.service.synd

import com.rometools.rome.feed.WireFeed
import com.rometools.rome.feed.atom.Feed
import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.synd.*
import com.thejoeflow.service.BlogService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

enum class FeedType(val value: String) {
    RSS("rss_2.0"),
    ATOM("atom_1.0")
}

@Service
class SyndicationService(@Value("\${server.port}") private val serverPortHttps: Int = 0,
                         @Value("\${server.hostname}") private val serverHostname: String = "localhost",
                         private val blogService: BlogService) {

    var rssChannel = createWireFeed(FeedType.RSS) as Channel
    var atomFeed = createWireFeed(FeedType.ATOM) as Feed

    fun reloadFeeds() {
        rssChannel = createWireFeed(FeedType.RSS) as Channel
        atomFeed = createWireFeed(FeedType.ATOM) as Feed
    }

    private fun createWireFeed(type: FeedType): WireFeed? {

        val rootUrl = "https://$serverHostname" + if (serverPortHttps != 443) ":$serverPortHttps" else ""

        val feed = if (type == FeedType.RSS) {
            CustomFeedEntry(Date())
        } else {
            SyndFeedImpl()
        }

        feed.feedType = type.value
        feed.title = "The Joe Flow"
        feed.description = "A blog about literature, mostly"
        feed.author = "joe@thejoeflow.com"
        feed.link = rootUrl
        feed.uri = rootUrl

        val link = if (type == FeedType.RSS) "/synd/rss" else "/synd/atom"

        val atomNSModule: AtomNSModule = AtomNSModuleImpl("$rootUrl$link")

        feed.modules.add(atomNSModule)

        feed.entries = generatePostEntries(type, rootUrl)

        return feed.createWireFeed()
    }

    private fun generatePostEntries(type: FeedType, rootUrl: String) : List<SyndEntry> {

        val entries = mutableListOf<SyndEntry>()

        for (blogPost in blogService.getAllBlogPosts()){

            val entry = if (type == FeedType.RSS) {
                CustomSyndEntry(blogPost.published)
            } else {
                SyndEntryImpl()
            }

            entry.title = blogPost.title
            entry.author = "Joe"
            entry.link = "$rootUrl/blog/post?id=${blogPost.id}"
            entry.uri =  "$rootUrl/blog/post?id=${blogPost.id}"
            entry.updatedDate = blogPost.updated

            val categories = mutableListOf<SyndCategory>()
            val category: SyndCategory = SyndCategoryImpl()
            category.name = blogPost.type.name
            categories.add(category)
            entry.categories = categories
            val description: SyndContent = SyndContentImpl()
            description.type = "text/plain"
            description.value = blogPost.first150Chars
            entry.description = description

            entries.add(entry)
        }

        return entries
    }
}