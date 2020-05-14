package com.thejoeflow.service.synd

import com.rometools.rome.feed.synd.SyndFeedImpl
import java.util.*


class CustomFeedEntry(var publishDate: Date) : SyndFeedImpl() {
    override fun getPublishedDate() = publishDate

    override fun setPublishedDate(publishedDate: Date) {
        this.publishDate = Date(publishedDate.time)
    }
}