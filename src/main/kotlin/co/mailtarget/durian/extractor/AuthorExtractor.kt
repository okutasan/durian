package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/7/17
 */
object AuthorExtractor : BaseExtractor() {

    private const val META_PUBLISHER = ""

    fun getPublisher(document: Document): String? {
        return extractMeta(document, META_PUBLISHER)
    }

}