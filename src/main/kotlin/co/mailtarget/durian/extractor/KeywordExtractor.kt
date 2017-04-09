package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/7/17
 */
object KeywordExtractor : BaseExtractor() {

    private val META_KEYWORDS = "meta[name~=keywords]"

    fun getKeywordsFromMeta(document: Document): List<String> {
        val keywordsTag = extractMeta(document, META_KEYWORDS)
        if(keywordsTag.isNullOrEmpty()) return emptyList()
        else {
            val keywords = ArrayList<String>()
            keywordsTag!!.split(",").mapTo(keywords) { it.trim() }
            return keywords
        }
    }

    @JvmOverloads fun getKeywordsFromContent(document: Document, contentElement: Element = document.body()): List<String> {
        return emptyList()
    }

    @JvmOverloads fun getKeywords(document: Document, contentElement: Element = document.body()): List<String> {
        val keywords = getKeywordsFromMeta(document)
        if(keywords.isEmpty()) return getKeywordsFromContent(document, contentElement)
        return keywords
    }

}