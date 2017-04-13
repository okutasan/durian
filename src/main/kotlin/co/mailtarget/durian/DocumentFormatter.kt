package co.mailtarget.durian

import co.mailtarget.durian.WebPage
import co.mailtarget.durian.formatter.TemplateLoader
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.io.InputStream

/**
 * used to generated formatted html from content extactor
 *
 * @author masasdani
 */
class DocumentFormatter {

    private val TEMPLATE_FILENAME = "template.html"
    private val htmlTemplate: String

    init {
        val classLoader = javaClass.classLoader
        val inputStream = classLoader.getResourceAsStream(TEMPLATE_FILENAME)
        htmlTemplate = TemplateLoader.read(inputStream)
    }

    /**
     * generate formatted html content from template
     *
     * @param webPage webpage
     * @return String
     */
    fun format(webPage: WebPage): String? {
        if(webPage.content == null) return null
        val document = Jsoup.parse(htmlTemplate)
        setContent(document, webPage.content?.html() ?: "")
        setTitle(document, webPage.title)
        setFavicon(document, webPage.favicon)
        setMainImage(document, webPage.image)
        addPaddingToImgDiv(document)
        return document.html()
    }

    /**
     * set title to template,
     * if content already contain title, remove them
     *
     * @param document document
     * @param title title
     */
    private fun setTitle(document: Document, title: String) {
        val escapedTitle = StringEscapeUtils.escapeHtml4(title).trim { it <= ' ' }
        val titleElements = document.select("h1, h2, h3")
        for (element in titleElements) {
            val escapedElementTitle = StringEscapeUtils.escapeHtml4(element.text()).trim { it <= ' ' }
            if (escapedElementTitle == escapedTitle) {
                element.remove()
            }
        }
        val headerTitleElements = document.getElementsByTag("title")
        for (element in headerTitleElements) {
            element.text(escapedTitle)
        }
        val titleElement = document.getElementById("articleHeader-title")
        titleElement.text(title)
    }

    /**
     * set generated content to template
     *
     * @param document document
     * @param contentHtml contentHtml
     */
    private fun setContent(document: Document, contentHtml: String) {
        val mainContentElement = document.getElementById("mainContent")
        mainContentElement.html(contentHtml)
    }

    /**
     * set main image to template,
     * if the extracted content doesn't have main image inside
     *
     * @param document document
     * @param mainImageUrl mainImageUrl
     */
    private fun setMainImage(document: Document, mainImageUrl: String?) {
        val mainImageElement = document.getElementById("mainImage")
        if (mainImageUrl == null) {
            mainImageElement.parent().remove()
            return
        }
        val contentElement = document.getElementById("mainContent")
        val imgElements = contentElement.getElementsByTag("img")
        val mainImageExist = imgElements.map { it.attr("src") }.none { it.isNullOrEmpty() }
        if (!mainImageExist) {
            mainImageElement.attr("src", mainImageUrl)
        } else {
            mainImageElement.parent().remove()
        }
    }

    /**
     * set favicon of this page
     * 
     * @param document document
     * @param faviconUrl faviconUrl
     */
    private fun setFavicon(document: Document, faviconUrl: String?) {
        val elements = document.select("link[rel=shortcut icon], link[rel=icon]")
        if (faviconUrl == null) elements.remove()
        else {
            for (el in elements) {
                el.attr("href", faviconUrl)
            }
        }
    }

    /**
     * some of content has image containin in div,
     * add a image class to it to do css styling on it
     *
     * @param document document
     */
    private fun addPaddingToImgDiv(document: Document) {
        val contentElement = document.getElementById("mainContent")
        val divElements = contentElement.select("div img")
        for (element in divElements) {
            element.parent().addClass("image")
        }
    }

}
