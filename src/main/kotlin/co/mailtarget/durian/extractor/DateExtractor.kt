package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
object DateExtractor: BaseExtractor() {

    private const val META_DATE = "meta[name~=date$],meta[property~=date$],meta[name~=time$],meta[property~=time$]"

    fun getDateFromMeta(document: Document): String? {
        return extractMeta(document, META_DATE)
    }

    private fun getDateFromContent(document: Document): String? {
        return ""
    }

    fun getDate(document: Document, contentElement: Element = document.body()): String? {
        val desc = getDateFromMeta(document)
        if(desc.isNullOrEmpty()) {
            return getDateFromContent(document)
        }
        return desc
    }

}