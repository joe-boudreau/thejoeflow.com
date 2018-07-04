package com.thejoeflow.utils

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import org.springframework.stereotype.Service

@Service
class FlickrService(){

    val apiKey = "57cd4c90c180344f68322adc76feb1d1"
    val sharedSecret = "01c4143064408629"

    fun getPhotoURLSFromFeed() : List<String> {

        val f = Flickr(apiKey, sharedSecret, REST())
        val recentPhotos = f.photosInterface.getContactsPublicPhotos("57861973@N02", 3, false, false, true)
        return recentPhotos.map { photo -> photo.mediumUrl }
    }
}