package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document

/**
 *
 * @author masasdani
 * @since 4/7/17
 */
object FaviconExtractor : BaseExtractor() {

    private val META_FAVICON = "link[rel~=icon$]"

    fun getFavicon(document: Document): String? {
        return extractMeta(document, META_FAVICON, "href")
    }

}