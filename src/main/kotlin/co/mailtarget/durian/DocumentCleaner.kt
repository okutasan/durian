package co.mailtarget.durian

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.util.ArrayList
import java.util.regex.Pattern

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
class DocumentCleaner
constructor(
        private var strategy: Strategy,
        private var options: ArrayList<Options>
) {

    private val regexRemoveNodes: String = "^side$|combx|retweet|menucontainer|navbar|^comment$|^commentContent$|^comment-body$|PopularQuestions|contact|foot|footer|Footer|footnote|cnn_strycaptiontxt|links|meta$|scroll|shoutbox|sponsor" +
            "|tags|socialnetworking|socialNetworking|cnnStryHghLght|cnn_stryspcvbx|^inset$|pagetools|post-attributes|welcome_form|contentTools2|the_answers" +
            "|communitypromo|runaroundLeft|^subscribe$|vcard|articleheadings|^date$|^print$|popup|tools|socialtools|byline|konafilter|KonaFilter|breadcrumb|^fn$|wp-caption-text|^column c160 left mb max$|^FL$" +
            "|^job_inner_tab_content$|^newsItem newsMagazine$|^newsItem newsOnline$|^float$|^mod-featured-title$|^below$|^quotePeekContainer$" +
            "|^header|header$|^menu|.*trending.*|^ads|_ad$|_ads$|^ad-|promo$|^promo|^survey|^related|related$|^login|login$|^register|register$|^signup|signup$|^search|search$|^notice|^notif|^action|^form" +
            "|^sharing|^share|sharing$|share$|back-to-top|^nav|control|relatedposts|.*_related|switch|^btn|sidebar|bottom|komentar|newsmore|button|sosmed|bacajuga|topiksisip|banner" +
            "|dtk-comment|clearfik|newstag|right_det"

    private val queryNaughtyIDs: String = "[id~=($regexRemoveNodes)]"
    private val queryNaughtyClasses: String = "[class~=($regexRemoveNodes)]"
    private val queryNaughtyNames: String = "[name~=($regexRemoveNodes)]"

    private val divToPElementsPattern = Pattern.compile("<(a|blockquote|dl|div|img|ol|p|pre|table|ul)")
    private val captionPattern = Pattern.compile("^caption$")
    private val googlePattern = Pattern.compile(" google ")
    private val entriesPattern = Pattern.compile("^[^entry-]more.*$")
    private val facebookPattern = Pattern.compile("[^-]facebook")
    private val twitterPattern = Pattern.compile("[^-]twitter")

    private val REPLACE_BRS = "(<br[^>]*>[ \n\r\t]*){2,}".toRegex()

    private val optionDefault = setOf(
            Options.cleanHeaderTag,
            Options.cleanFooterTag,
            Options.cleanFormTag,
            Options.cleanBadTags,
            Options.cleanFooterTag,
            Options.convertFontToSpan,
            Options.removeDropCaps,
            Options.removeScriptsAndStyles
    )

    constructor() : this(Strategy.DEFAULT, arrayListOf())

    constructor(options: ArrayList<Options>) : this(Strategy.CUSTOM, options)

    init {
        if(strategy == Strategy.DEFAULT) {
            options.addAll(optionDefault)
        }
    }

    fun clean(document: Document): Document {
        val docToClean = document

        cleanHeaderTag(docToClean)
        cleanFooterTag(docToClean)
        cleanFormTag(docToClean)
        cleanBadTags(docToClean)

        convertFontToSpan(docToClean)

        removeDropCaps(docToClean)
        removeScriptsAndStyles(docToClean)
        removeStyleSheets(docToClean)
        removeComments(docToClean)

        removeNodesViaRegEx(docToClean, captionPattern)
        removeNodesViaRegEx(docToClean, googlePattern)
        removeNodesViaRegEx(docToClean, entriesPattern)
        removeNodesViaRegEx(docToClean, facebookPattern)
        removeNodesViaRegEx(docToClean, twitterPattern)

        cleanUpSpanTagsInParagraphs(docToClean)
        wrapDoubleBrsParentWithP(docToClean)

        cleanHr(docToClean)
        cleanAside(docToClean)
        cleanCode(docToClean)
        cleanDicClearfix(docToClean)
        removeEmptyParas(docToClean)
        removeEmptyH(docToClean)

        if(options.contains(Options.convertNoScriptToDiv)) convertNoScriptToDiv(docToClean)
        if(options.contains(Options.convertDoubleBrsToP)) convertDoubleBrsToP(docToClean)
        if(options.contains(Options.convertDivsToParagraphs)) convertDivsToParagraphs(docToClean)
        if(options.contains(Options.cleanEmTags)) cleanEmTags(docToClean)

        return docToClean
    }

    private fun convertFontToSpan(docToClean: Document) {
        val fonts = docToClean.getElementsByTag("font")
        for (font in fonts) {
            changeElementTag(docToClean, font, "span")
        }
    }

    private fun removeStyleSheets(docToClean: Document) {
        val stylesheets = docToClean.select("link[rel='stylesheet']")
        stylesheets.remove()
    }

    private fun convertDoubleBrsToP(docToClean: Document) {
        val doubleBrs = docToClean.select("br + br")
        for (br in doubleBrs) {
            // we hope that there's a 'p' up there....
            val parents = br.parents()
            var parent: Element? = parents.firstOrNull { it.tag().name == "p" }
            if (parent == null) {
                parent = br.parent()
                parent!!.wrap("<p></p>")
            }
            // now it's safe to make the change.
            var inner = parent.html()
            if (!inner.startsWith("<p>")) {
                inner = "<p>" + inner
            }
            inner = inner.replace(REPLACE_BRS, "</p><p>")
            parent.html(inner)
        }
    }

    private fun wrapDoubleBrsParentWithP(docToClean: Document) {
        val doubleBrs = docToClean.select("br + br")
        for (br in doubleBrs) {
            // we hope that there's a 'p' up there....
            val parents = br.parents()
            var parent: Element? = parents.firstOrNull { it.tag().name == "p" }
            if (parent == null) {
                parent = br.parent()
                parent!!.wrap("<p></p>")
            }
            // now it's safe to make the change.
            val inner = parent.html()
            parent.html(inner)
        }
    }

    private fun removeComments(docToClean: Document) {
        val childNodes = docToClean.childNodes()
        for (node in childNodes) {
            cleanComments(node)
        }
    }

    private fun convertNoScriptToDiv(docToClean: Document) {
        val noScripts = docToClean.getElementsByTag("noscript")
        for (noScript in noScripts) {
            changeElementTag(docToClean, noScript, "div")
        }
    }

    private fun changeElementTag(docToClean: Document, e: Element, newTag: String): Element {
        val newElement = docToClean.createElement(newTag)
        /* JSoup gives us the live child list, so we need to make a copy. */
        val copyOfChildNodeList = ArrayList<Node>()
        copyOfChildNodeList.addAll(e.childNodes())
        for (n in copyOfChildNodeList) {
            n.remove()
            newElement.appendChild(n)
        }
        e.replaceWith(newElement)
        return e
    }

    private fun cleanComments(node: Node) {
        var i = 0
        while (i < node.childNodes().size) {
            val child = node.childNode(i)
            if (child.nodeName() == "#comment") {
                child.remove()
            } else {
                cleanComments(child)
                i++
            }
        }
    }

    private fun removeEmptyParas(docToClean: Document) {
        val paras = docToClean.select("p")
        paras.filter { para -> para.text().trim { it <= ' ' }.isEmpty() && para.childNodes().size == 0 }
                .forEach { it.remove() }
    }

    private fun removeEmptyH(docToClean: Document) {
        val paras = docToClean.select("h2, h3, h4, h5, h6")
        paras.filter { it.text().isEmpty() && it.childNodes().size == 0 }.forEach { it.remove() }
    }

    /**
     * remove those css drop caps where they put the first letter in big text in
     * the 1st paragraph
     */
    private fun removeDropCaps(document: Document) {
        val items = document.select("span[class~=(dropcap|drop_cap)]")
        for (item in items) {
            val tn = TextNode(item.text(), document.baseUri())
            item.replaceWith(tn)
        }
    }

    private fun cleanBadTags(document: Document) {
        // only select elements WITHIN the body to avoid removing the body itself
        val children = document.body().children()

        val naughtyList = children.select(queryNaughtyIDs)
        for (node in naughtyList) {
            removeNode(node)
        }

        val naughtyList3 = children.select(queryNaughtyClasses)
        for (node in naughtyList3) {
            removeNode(node)
        }

        // starmagazine puts shit on name tags instead of class or id
        val naughtyList5 = children.select(queryNaughtyNames)
        for (node in naughtyList5) {
            removeNode(node)
        }
    }

    private fun removeNode(node: Element?) {
        if (node == null || node.parent() == null) return
        node.remove()
    }

    private fun removeNodesViaRegEx(document: Document, pattern: Pattern) {
        try {
            val naughtyList = document.getElementsByAttributeValueMatching("id", pattern)
            for (node in naughtyList) {
                removeNode(node)
            }
            val naughtyList3 = document.getElementsByAttributeValueMatching("class", pattern)
            for (node in naughtyList3) {
                removeNode(node)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }

    private fun removeScriptsAndStyles(document: Document) {
        val scripts = document.getElementsByTag("script")
        for (item in scripts) {
            item.remove()
        }
        val styles = document.getElementsByTag("style")
        for (style in styles) {
            style.remove()
        }
    }

    private fun convertDivsToParagraphs(document: Document) {
        val divs = document.getElementsByTag("div")
        for (div in divs) {
            try {
                val divToPElementsMatcher = divToPElementsPattern.matcher(div.html().toLowerCase())
                if (!divToPElementsMatcher.find()) {
                    replaceElementsWithPara(document, div)
                } else {
                    val replaceNodes = getReplacementNodes(document, div)
                    for (child in div.children()) {
                        child.remove()
                    }
                    for (node in replaceNodes) {
                        try {
                            div.appendChild(node)
                        } catch (ignored: Exception) {

                        }
                    }
                }
            } catch (ignored: NullPointerException) {

            }
        }
    }

    private fun getReplacementNodes(document: Document, div: Element): ArrayList<Node> {
        val replacementText = StringBuilder()
        val nodesToReturn = ArrayList<Node>()
        val nodesToRemove = ArrayList<Node>()

        for (kid in div.childNodes()) {
            if (kid.nodeName() == "p" && replacementText.isNotEmpty()) {
                // flush the buffer of text
                val newNode = getFlushedBuffer(replacementText, document)
                nodesToReturn.add(newNode)
                replacementText.setLength(0)
                if (kid is Element) {
                    nodesToReturn.add(kid)
                }
            } else if (kid.nodeName() == "#text") {
                val kidTextNode = kid as TextNode
                val kidText = kidTextNode.attr("text")
                if (kidText.isEmpty())
                    continue

                // clean up text from tabs and newlines
                var replaceText = kidText.replace("\n".toRegex(), "\n\n")
                replaceText = replaceText.replace("\t".toRegex(), "")
                replaceText = replaceText.replace("^\\s+$".toRegex(), "")

                if (replaceText.trim().length > 1) {
                    var previousSiblingNode: Node? = kidTextNode.previousSibling()
                    while (previousSiblingNode != null
                            && previousSiblingNode.nodeName() == "a"
                            && previousSiblingNode.attr("grv-usedalready") != "yes") {
                        replacementText.append(" ").append(previousSiblingNode.outerHtml()).append(" ")
                        nodesToRemove.add(previousSiblingNode)
                        previousSiblingNode.attr("grv-usedalready", "yes")
                        if (previousSiblingNode.previousSibling() != null) {
                            previousSiblingNode = previousSiblingNode.previousSibling()
                        } else {
                            previousSiblingNode = null
                        }
                    }
                    // add the text of the node
                    replacementText.append(replaceText)
                    // check the next set of links that might be after text (see
                    // businessinsider2.txt)
                    var nextSiblingNode: Node? = kidTextNode.nextSibling()
                    while (nextSiblingNode != null
                            && nextSiblingNode.nodeName() == "a"
                            && nextSiblingNode.attr("grv-usedalready") != "yes") {
                        replacementText.append(" ").append(nextSiblingNode.outerHtml()).append(" ")
                        nodesToRemove.add(nextSiblingNode)
                        nextSiblingNode.attr("grv-usedalready", "yes")
                        if (nextSiblingNode.nextSibling() != null) {
                            nextSiblingNode = nextSiblingNode.nextSibling()
                        } else {
                            nextSiblingNode = null
                        }
                    }
                }
                nodesToRemove.add(kid)
            } else {
                nodesToReturn.add(kid)
            }
        }
        // flush out anything still remaining
        if (replacementText.isNotEmpty()) {
            val newNode = getFlushedBuffer(replacementText, document)
            nodesToReturn.add(newNode)
            replacementText.setLength(0)
        }
        for (node in nodesToRemove) {
            node.remove()
        }

        return nodesToReturn

    }

    private fun getFlushedBuffer(replacementText: StringBuilder, document: Document): Element {
        val bufferedText = replacementText.toString()
        val newDoc = Document(document.baseUri())
        val newPara = newDoc.createElement("p")
        newPara.html(bufferedText)
        return newPara

    }

    private fun replaceElementsWithPara(doc: Document, div: Element) {
        val newDoc = Document(doc.baseUri())
        val newNode = newDoc.createElement("p")
        newNode.append(div.html())
        div.replaceWith(newNode)

    }

    private fun cleanUpSpanTagsInParagraphs(document: Document) {
        val span = document.getElementsByTag("span")
        for (item in span) {
            if (item.parent().nodeName() == "p") {
                val tn = TextNode(item.text(), document.baseUri())
                item.replaceWith(tn)
            }
        }
    }

    private fun cleanEmTags(document: Document) {
        val ems = document.getElementsByTag("em")
        for (node in ems) {
            // replace the node with a div node
            val images = node.getElementsByTag("img")
            if (images.size != 0) {
                continue
            }
            val tn = TextNode(node.text(), document.baseUri())
            node.replaceWith(tn)
        }
    }

    private fun cleanHeaderTag(document: Document) {
        val elements = document.getElementsByTag("header")
        for (node in elements) {
            node.remove()
        }
    }

    private fun cleanFormTag(document: Document) {
        val elements = document.getElementsByTag("form")
        for (node in elements) {
            node.remove()
        }
    }

    private fun cleanFooterTag(document: Document) {
        val elements = document.getElementsByTag("footer")
        for (node in elements) {
            node.remove()
        }
    }

    private fun cleanHr(document: Document) {
        val elements = document.getElementsByTag("hr")
        for (node in elements) {
            node.remove()
        }
    }

    private fun cleanAside(document: Document) {
        val elements = document.getElementsByTag("aside")
        for (node in elements) {
            node.remove()
        }
    }

    private fun cleanCode(document: Document) {
        val elements = document.getElementsByTag("code")
        for (node in elements) {
            node.remove()
        }
    }

    private fun cleanDicClearfix(document: Document) {
        val elements = document.select("div .clearfix")
        elements.filter { it.text().isEmpty() }.forEach { it.remove() }
    }

    enum class Strategy {
        DEFAULT,
        CUSTOM
    }

    enum class Options {
        convertDoubleBrsToP,
        convertFontToSpan,
        removeStyleSheets,
        wrapDoubleBrsParentWithP,
        removeComments,
        convertNoScriptToDiv,
        cleanComments,
        removeEmptyParas,
        removeEmptyH,
        removeDropCaps,
        cleanBadTags,
        removeScriptsAndStyles,
        convertDivToParagraph,
        convertDivsToParagraphs,
        replaceElementsWithPara,
        cleanUpSpanTagsInParagraphs,
        cleanEmTags,
        cleanHeaderTag,
        cleanFormTag,
        cleanFooterTag,
        cleanHr,
        cleanAside,
        cleanCode,
        cleanDicClearfix,
    }

}