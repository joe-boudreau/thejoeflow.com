package com.thejoeflow.service.synd

import com.rometools.rome.feed.synd.SyndEntryImpl
import java.util.*

class CustomSyndEntry(var publishDate: Date) : SyndEntryImpl() {
    override fun getPublishedDate() = publishDate

    override fun setPublishedDate(publishedDate: Date) {
        this.publishDate = Date(publishedDate.time)
    }
}