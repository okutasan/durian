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
        val content = contentElement.text()
        if(content.length > MAX_SNIPPET_LENGHT) {
            var snippet = content.substring(0, MAX_SNIPPET_LENGHT)
            snippet = snippet.replace("\\w+$".toRegex(), "")
            return snippet
        }
        return content
    }

    @JvmOverloads fun getDescription(document: Document, contentElement: Element = document.body()): String? {
        val desc = getDescriptionFromMeta(document)
        if(desc == null || desc.length < MAX_SNIPPET_LENGHT) {
            return getDescriptionFromContent(document, contentElement)
        }
        return desc
    }

}