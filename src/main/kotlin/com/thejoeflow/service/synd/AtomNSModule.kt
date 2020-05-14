package com.thejoeflow.service.synd

import com.rometools.rome.feed.CopyFrom
import com.rometools.rome.feed.module.Module
import com.rometools.rome.feed.module.ModuleImpl

const val AtomURI = "http://www.w3.org/2005/Atom"

interface AtomNSModule : Module {
    var link: String
}

class AtomNSModuleImpl(override var link: String = "") : ModuleImpl(AtomNSModule::class.java, AtomURI), AtomNSModule {

    override fun getInterface(): Class<out CopyFrom> {
        return AtomNSModule::class.java
    }

    override fun copyFrom(obj: CopyFrom) {
        (obj as AtomNSModule).link = this.link
    }
}