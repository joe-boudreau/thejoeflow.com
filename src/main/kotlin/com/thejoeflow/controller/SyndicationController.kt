package com.thejoeflow.controller

import com.rometools.rome.feed.atom.Feed
import com.rometools.rome.feed.rss.Channel
import com.thejoeflow.service.synd.SyndicationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class SyndicationController(private val syndService: SyndicationService) {

    @GetMapping("/synd/rss")
    fun getRssChannel(): Channel = syndService.rssChannel

    @GetMapping("/synd/atom")
    fun getAtomFeed(): Feed = syndService.atomFeed
}