package com.thejoeflow.service.synd

import com.rometools.rome.feed.module.Module
import com.rometools.rome.io.ModuleGenerator
import org.jdom2.Element
import org.jdom2.Namespace

val ATOM_NS: Namespace = Namespace.getNamespace("atom", AtomURI)
val NAMESPACES: Set<Namespace> = emptySet()

class AtomNSModuleGenerator : ModuleGenerator {

    override fun getNamespaceUri() = AtomURI

    override fun getNamespaces() = NAMESPACES

    override fun generate(module: Module, element: Element) {

        val atomFeed: Boolean = element.name == "feed"
        val atomNSModule = module as AtomNSModule

        val atomLink: Element
        if (atomFeed) {
            atomLink = Element("link", element.namespace)
            atomLink.setAttribute("type", "application/atom+xml")
        } else {
            var root: Element = element
            while (root.parent != null && root.parent is Element) {
                root = element.parent as Element
            }
            root.addNamespaceDeclaration(ATOM_NS)
            atomLink = Element("link", ATOM_NS)
            atomLink.setAttribute("type", "application/rss+xml")
        }

        atomLink.setAttribute("href", atomNSModule.link)
        atomLink.setAttribute("rel", "self")
        element.addContent(0, atomLink)
    }
}