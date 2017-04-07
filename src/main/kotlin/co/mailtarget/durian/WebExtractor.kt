package co.mailtarget.durian

import co.mailtarget.durian.extractor.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/5/17
 */
class WebExtractor: Connection()  {

    var cleaner: DocumentCleaner? = null
    var strategy = Strategy.META

    /**
     * attemp to extract web data
     *
     * @param url
     * @return
     */
    fun extractWebData(url: String): WebPage {
        val document = getDocument(url)
        when (strategy) {
            Strategy.CONTENT -> return extractContent(url, document)
            Strategy.HYBRID -> return extractHybrid(url, document)
            else -> return extractMeta(url, document)
        }
    }

    private fun extractContent(url: String, document: Document): WebPage {
        val doc = cleaner?.clean(document) ?: document
        val contentElement = extractContentElement(doc)
        val title = TitleExtractor.getTitleFromContent(doc, contentElement)
        val webPage = WebPage(url, title)
        return webPage
    }

    private fun extractHybrid(url: String, document: Document): WebPage {
        val doc = cleaner?.clean(document) ?: document
        val contentElement = extractContentElement(doc)
        val title = TitleExtractor.getTitle(doc, contentElement)
        val webPage = WebPage(url, title)
        return webPage
    }

    private fun extractMeta(url: String, document: Document): WebPage {
        val title = TitleExtractor.getTitleFromMeta(document)
        val webPage = WebPage(url, title)
        webPage.favicon = FaviconExtractor.getFavicon(document)
        webPage.images = ArrayList(ImageExtractor.getImagesFromMeta(document))
        webPage.description = SnippetExtractor.getDescriptionFromMeta(document)
        webPage.publishedDate = DateExtractor.getDateFromMeta(document)
        webPage.keywords = KeywordExtractor.getKeywordsFromMeta(document)
        return webPage
    }

    private fun extractContentElement(document: Document): Element {
        return document.body()
    }

    object Builder {

        private val extractor = WebExtractor()

        fun strategy(strategy: Strategy): Builder {
            extractor.strategy = strategy
            return this
        }

        fun withCleaner(): Builder {
            if(extractor.cleaner == null) extractor.cleaner = DocumentCleaner()
            return this
        }

        fun build(): WebExtractor {
            return extractor
        }
    }

    enum class Strategy {
        META,
        CONTENT,
        HYBRID
    }
}
