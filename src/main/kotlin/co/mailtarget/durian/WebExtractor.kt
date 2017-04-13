package co.mailtarget.durian

import co.mailtarget.durian.extractor.*
import com.machinepublishers.jbrowserdriver.JBrowserDriver
import com.machinepublishers.jbrowserdriver.Settings
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL
import java.util.logging.Level

/**
 *
 * @author masasdani
 * @since 4/5/17
 */
class WebExtractor(val driver: JBrowserDriver): Connection()  {

    var cleaner: DocumentCleaner = DocumentCleaner()
    var strategy = Strategy.META

    /**
     * attemp to extract web data
     *
     * @param url
     * @return
     */
    fun extract(url: String): WebPage {
        val document = getDocument(url)
        when (strategy) {
            Strategy.CONTENT -> return extractContent(url, document)
            Strategy.HYBRID -> return extractHybrid(url, document)
            else -> return extractMeta(url, document)
        }
    }

    /**
     * attemp to extract web data
     *
     * @param url
     * @return
     */
    fun extract(url: String, html: String): WebPage {
        val document = Jsoup.parse(html)
        when (strategy) {
            Strategy.CONTENT -> return extractContent(url, document)
            Strategy.HYBRID -> return extractHybrid(url, document)
            else -> return extractMeta(url, document)
        }
    }


    /**
     * attemp to extract web data
     *
     * @param url
     * @return
     */
    fun extract(url: String, forceJavascript: Boolean): WebPage {
        if(forceJavascript) {
            driver.get(url)
            val html = driver.pageSource
            return extract(url, html)
        }
        return extract(url)
    }

    private fun extractContent(url: String, document: Document): WebPage {
        val doc = cleaner.clean(document)
        val contentElement = extractContentElement(doc)
        val title = TitleExtractor.getTitleFromContent(doc, document.body())
        val webPage = WebPage(url, title)
        webPage.favicon = FaviconExtractor.getFavicon(document)
        webPage.image = ImageExtractor.getImageFromContent(document, URL(url), webPage.title, contentElement)
        webPage.description = SnippetExtractor.getDescriptionFromContent(document, contentElement)
        webPage.publishedDate = DateExtractor.getDateFromContent(document, contentElement)
        webPage.keywords = KeywordExtractor.getKeywordsFromContent(document, contentElement)
        webPage.content = contentElement
        return webPage
    }

    private fun extractHybrid(url: String, document: Document): WebPage {
        val doc = cleaner.clean(document)
        val contentElement = extractContentElement(doc)
        val title = TitleExtractor.getTitle(doc, document.body())
        val webPage = WebPage(url, title)
        webPage.favicon = FaviconExtractor.getFavicon(document)
        webPage.image = ImageExtractor.getImage(document, URL(url), webPage.title, contentElement)
        webPage.description = SnippetExtractor.getDescription(document, contentElement)
        webPage.publishedDate = DateExtractor.getDate(document, contentElement)
        webPage.keywords = KeywordExtractor.getKeywords(document, contentElement)
        webPage.content = contentElement
        return webPage
    }

    private fun extractMeta(url: String, document: Document): WebPage {
        val title = TitleExtractor.getTitleFromMeta(document)
        val webPage = WebPage(url, title)
        webPage.favicon = FaviconExtractor.getFavicon(document)
        webPage.image = ImageExtractor.getImageFromMeta(document)
        webPage.description = SnippetExtractor.getDescriptionFromMeta(document)
        webPage.publishedDate = DateExtractor.getDateFromMeta(document)
        webPage.keywords = KeywordExtractor.getKeywordsFromMeta(document)
        webPage.content = document.body()
        return webPage
    }

    private fun extractContentElement(document: Document): Element {
        return ContentExtractor.getContentElement(document)
    }

    object Builder {

        private val MAX_PROCESS = 1000
        private val CONNECTION_TIMEOUT = 3000
        private val driverSettingBuilder = Settings.builder()
                .csrf()
                .headless(true)
                .javascript(true)
                .logJavascript(false)
                .ignoreDialogs(true)
                .cache(true)
                .processes(MAX_PROCESS)
                .maxConnections(MAX_PROCESS)
                .connectionReqTimeout(CONNECTION_TIMEOUT)
                .ajaxResourceTimeout(CONNECTION_TIMEOUT.toLong())
                .socketTimeout(CONNECTION_TIMEOUT)
                .connectTimeout(CONNECTION_TIMEOUT)
                .quickRender(true)

        private var strategy = Strategy.META
        private var cleanerOptions: ArrayList<DocumentCleaner.Options> = arrayListOf()

        fun strategy(strategy: Strategy): Builder {
            this.strategy = strategy
            return this
        }

        fun cleanerOptions(options: ArrayList<DocumentCleaner.Options>): Builder {
            this.cleanerOptions = options
            return this
        }

        fun disableLogging(disableLogging: Boolean): Builder {
            if(disableLogging) {
                driverSettingBuilder.loggerLevel(Level.OFF)
            }
            return this
        }

        fun build(): WebExtractor {
            val extractor = WebExtractor(JBrowserDriver(driverSettingBuilder.build()))
            extractor.strategy = this.strategy
            extractor.cleaner.options.addAll(cleanerOptions)
            return extractor
        }
    }

    enum class Strategy {
        META,
        CONTENT,
        HYBRID
    }
}
