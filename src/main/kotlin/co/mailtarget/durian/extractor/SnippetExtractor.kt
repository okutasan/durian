package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
object SnippetExtractor: BaseExtractor() {

    private val MAX_SNIPPET_LENGHT = 200
    private val META_DESCRIPTION = "meta[name~=description$],meta[property~=description$]"

    fun getDescriptionFromMeta(document: Document): String? {
        return extractMeta(document, META_DESCRIPTION)
    }

    @JvmOverloads fun getDescriptionFromContent(document: Document, contentElement: Element = document.body()): String? {
        var snippet = contentElement.text().substring(MAX_SNIPPET_LENGHT)
        snippet = snippet.replace(" [^ ]+$", "")
        return "$snippet ..."
    }

    @JvmOverloads fun getDescription(document: Document, contentElement: Element = document.body()): String? {
        val desc = getDescriptionFromMeta(document)
        if(desc.isNullOrEmpty()) {
            return getDescriptionFromContent(document, contentElement)
        }
        return desc
    }

}