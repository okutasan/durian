package co.mailtarget.durian.extractor

import org.apache.commons.text.StringEscapeUtils
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * find title on web document, first try get matches meta document title and content,
 * if not found try to select best content title
 *
 * @author masasdani
 * @since 4/6/17
 */
object TitleExtractor : BaseExtractor() {

    private val PIPE = "\\|".toRegex()
    private val DASH = " - ".toRegex()
    private val MDASH = " — ".toRegex()
    private val ARROW = "»".toRegex()
    private val COLON = ":".toRegex()
    private val MOTLEY_REPLACEMENT = arrayOf("&#65533;")
    private val TITLE_REPLACEMENT = arrayOf("&raquo;", "»")
    private val META_TITLE = "title,meta[property~=title$]"

    /**
     * find best title from content,
     * using h1, h2 and get the longest text
     *
     * @param topElement
     * @return
     */
    @JvmOverloads fun getTitleFromContent(document: Document, topElement: Element = document.body()): String {
        val elements = topElement.getElementsByTag("h1")
        val titleCandidates = elements.map { it.text() }
        return findLargestStringOnCollection(titleCandidates)
    }

    /**
     * find cleaned title from meta,
     * if empty or null, find best from element
     *
     * @param document
     * @param topElement
     * @return
     */
    @JvmOverloads fun getTitle(document: Document, topElement: Element = document.body()): String {
        var title = getTitleFromMeta(document)
        if (title.isNullOrEmpty()) {
            title = getTitleFromContent(document, topElement)
        }
        return title
    }

    /**
     * attemps to grab titles from the html pages, lots of sites use different
     * delimiters for titles so we'll try and do our best guess.
     *
     * @param document
     * @return
     */
    fun getTitleFromMeta(document: Document): String {
        var title: String? = extractMeta(document, META_TITLE)
        try {
            val titleElem = document.getElementsByTag("title")
            if (titleElem == null || titleElem.isEmpty())
                return ""

            var titleText = titleElem.first().text()
            if (titleText.isNullOrEmpty())
                return ""

            var usedDelimeter = false

            if (titleText.contains(" | ")) {
                titleText = doTitleSplits(titleText, PIPE)
                usedDelimeter = true
            }
            if (titleText.contains(" — ")) {
                titleText = doTitleSplits(titleText, MDASH)
                usedDelimeter = true
            }
            if (titleText.contains(" - ")) {
                titleText = doTitleSplits(titleText, DASH)
                usedDelimeter = true
            }
            if (!usedDelimeter && titleText.contains(" » ")) {
                titleText = doTitleSplits(titleText, ARROW)
                usedDelimeter = true
            }
            if (!usedDelimeter && titleText.contains(" : ")) {
                titleText = doTitleSplits(titleText, COLON)
            }

            // encode unicode charz
            title = StringEscapeUtils.escapeHtml4(titleText)
            title = replaceAll(title, MOTLEY_REPLACEMENT)
        } catch (e: NullPointerException) {

        }
        return StringEscapeUtils.unescapeHtml4(title)
    }

    /**
     * based on a delimeter in the title take the longest piece or do some
     * custom logic based on the site
     *
     * @param title
     * @param splitter
     * @return
     */
    private fun doTitleSplits(title: String, splitter: Regex): String {
        var largetTextLen = 0
        var largeTextIndex = 0
        val titlePieces = splitter.split(title)
        // take the largest split
        for (i in titlePieces.indices) {
            val current = titlePieces[i]
            if (current.length > largetTextLen) {
                largetTextLen = current.length
                largeTextIndex = i
            }
        }
        return replaceAll(titlePieces[largeTextIndex], TITLE_REPLACEMENT).trim()
    }

    /**
     * find largest text on collection, used to determine best title
     *
     * @param strings
     * @return
     */
    private fun findLargestStringOnCollection(strings: Collection<String>): String {
        var largetTextLen = 0
        var largestString: String? = null
        for (s in strings) {
            if (s.length > largetTextLen) {
                largetTextLen = s.length
                largestString = s.trim { it <= ' ' }
            }
        }
        return largestString ?: ""
    }

    private fun replaceAll(input: String, patterns: Array<String>): String {
        var s = input
        for(pattern in patterns) s = s.replace(pattern, "")
        return s
    }
}
