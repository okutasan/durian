package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/7/17
 */
object PublisherExtractor : BaseExtractor() {

    private val META_PUBLISHER = ""

    fun getPublisher(document: Document): String? {
        return extractMeta(document, META_PUBLISHER)
    }

}