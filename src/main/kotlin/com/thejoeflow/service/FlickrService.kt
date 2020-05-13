package com.thejoeflow.service

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.thejoeflow.config.AppProperties
import org.springframework.stereotype.Service

@Service
class FlickrService(appProperties: AppProperties){

    val apiKey = appProperties.apiKey
    val sharedSecret = appProperties.sharedSecret

    fun getPhotoURLSFromFeed() : List<String> {

        return try {
            val f = Flickr(apiKey, sharedSecret, REST())
            val recentPhotos = f.photosInterface
                    .getContactsPublicPhotos("57861973@N02", 3, false, false, true)
            recentPhotos.map { photo -> photo.mediumUrl }
        }
        catch(e: Exception){
            emptyList()
        }
    }
}