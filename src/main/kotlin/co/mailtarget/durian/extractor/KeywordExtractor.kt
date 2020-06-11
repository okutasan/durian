package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document

/**
 *
 * @author masasdani
 * @since 4/7/17
 */
object KeywordExtractor : BaseExtractor() {

    private const val META_KEYWORDS = "meta[name~=keywords]"

    fun getKeywordsFromMeta(document: Document): List<String> {
        val keywordsTag = extractMeta(document, META_KEYWORDS)
        return if(keywordsTag.isNullOrEmpty()) emptyList()
        else {
            val keywords = ArrayList<String>()
            keywordsTag.split(",").mapTo(keywords) { it.trim() }
            keywords
        }
    }

    private fun getKeywordsFromContent(document: Document): List<String> {
        return emptyList()
    }

    fun getKeywords(document: Document): List<String> {
        val keywords = getKeywordsFromMeta(document)
        if(keywords.isEmpty()) return getKeywordsFromContent(document)
        return keywords
    }

}