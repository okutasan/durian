package co.mailtarget.durian.extractor

import co.mailtarget.durian.content.AddSiblings
import co.mailtarget.durian.content.ScoreInfo
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.ArrayList
import java.util.HashSet
import java.util.LinkedHashMap
import java.util.regex.Pattern

/**
 *
 * @author masasdani
 * @since 4/7/17
 */
object ContentExtractor {

    // Unlikely candidates
    private val UNLIKELY = Pattern.compile("^(com(bx|ment|munity)|dis(qus|cuss)|e(xtra|[-]?mail)|foot|"
            + "header|menu|re(mark|ply)|rss|sh(are|outbox)|sponsor"
            + "a(d|ll|gegate|rchive|ttachment)|(pag(er|ination))|popup|print|"
            + "login|si(debar|gn|ngle))")

    // Most likely positive candidates
    private val POSITIVE = Pattern.compile("(^(body|content|h?entry|main|page|post|text|blog|story|haupt))"
            + "|arti(cle|kel)|instapaper_body")

    // Most likely negative candidates
    private val NEGATIVE = Pattern.compile("nav($|igation)|user|com(ment|bx)|(^com-)|contact|"
            + "foot|masthead|^(me(dia|ta))$|outbrain|promo|related|scroll|(sho(utbox|pping))|"
            + "sidebar|sponsor|tags|tool|widget")


    /**
     * @param doc
     * @return
     */
    fun getContentElement(document: Document): Element {
        //println(document.text())
        var result = getContentByTitle(document)
        if(result == null) result = getArticleByTag(document)
        if (result == null) result = getArticleById(document)
        if (result == null) result = fetchArticleContent(document)
        return result ?: document.body()
    }

    private fun getContentByTitle(document: Document): Element? {
        var titleCandicates = document.getElementsByTag("h1")
        if (titleCandicates.size == 0) {
            titleCandicates = document.getElementsByTag("h2")
            if (titleCandicates.size == 0) {
                titleCandicates = document.getElementsByTag("h3")
            }
        }
        var result: Element? = null
        if (titleCandicates.size > 0) {
            val topElement = getContentTargets(titleCandicates.first(), document)
            result = getMatchesContentByHrOrP(topElement)
        }
        return result
    }

    private fun getArticleByTag(document: Document): Element? {
        return document.getElementsByTag("article")?.first()
    }

    private fun getArticleById(document: Document): Element? {
        return document.getElementById("articleDescription")
    }

    private fun getContentTargets(topElement: Element, document: Document): Elements {
        val siblings = topElement.siblingElements()
        val targets = siblings.select("p, br + br")
        if(targets.size > 3 || topElement.parent() == null) return targets
        return getContentTargets(topElement.parent(), document)
    }

    private fun getMatchesContentByHrOrP(targets: Elements): Element? {
        for (element in targets) {
            if (element.parent().select("p, br+br").size > 3) {
                return element.parent()
            }
        }
        return null
    }

    /**
     * @param document
     * @return
     */
    private fun fetchArticleContent(document: Document): Element? {
        var topNode: Element? = null
        val parentNodes = HashSet<Element>()
        val nodesToCheck = getNodesToCheck(document)
        for (element in nodesToCheck) {
            println("nodetocheck "+element.text())
            if (element.text().length < 25) {
                continue
            }
            println("content "+element.text())
            val contentScore = getElementScore(element)
            ScoreInfo.updateContentScore(element.parent(), contentScore)
            ScoreInfo.updateContentScore(element.parent().parent(),
                    contentScore / 2)
            if (!parentNodes.contains(element.parent())) {
                parentNodes.add(element.parent())
            }
            if (!parentNodes.contains(element.parent().parent())) {
                parentNodes.add(element.parent().parent())
            }
        }

        var topNodeScore = 0.0
        for (e in parentNodes) {
            val score = ScoreInfo.getContentScore(e)
            if (score > topNodeScore) {
                topNode = e
                topNodeScore = score
            }
            if (topNode == null) {
                topNode = e
            }
        }
        if (topNode != null) {
            topNode = AddSiblings.addSiblings(topNode)
        }
        return topNode
    }

    /**
     * @param element
     * @return
     */
    private fun getElementScore(element: Element): Double {
        var contentScore = 0.0
        val scoreTags = ScoreTags.getTagName(element.tagName())
        when (scoreTags) {

            ScoreTags.div -> contentScore += 5.0
            ScoreTags.pre, ScoreTags.td, ScoreTags.blockquote -> contentScore += 3.0
            ScoreTags.address, ScoreTags.ol, ScoreTags.ul,
            ScoreTags.dl, ScoreTags.dd, ScoreTags.dt, ScoreTags.li, ScoreTags.form -> contentScore -= 3.0
            ScoreTags.h1, ScoreTags.h2, ScoreTags.h3, ScoreTags.h4, ScoreTags.h5, ScoreTags.h6, ScoreTags.th -> contentScore -= 5.0

            else -> contentScore += 0
        }

        val weight = getClassWeight(element)
        contentScore += weight
        return contentScore

    }

    /**
     * @param e
     * @return
     */
    private fun getClassWeight(e: Element): Double {
        var weight = 0.0
        if (POSITIVE.matcher(e.className()).find())
            weight += 35.0
        if (POSITIVE.matcher(e.id()).find())
            weight += 40.0
        if (UNLIKELY.matcher(e.className()).find())
            weight -= 20.0
        if (UNLIKELY.matcher(e.id()).find())
            weight -= 20.0
        if (NEGATIVE.matcher(e.className()).find())
            weight -= 50.0
        if (NEGATIVE.matcher(e.id()).find())
            weight -= 50.0

        weight += Math.round(e.ownText().length / 100.0 * 10).toInt().toDouble()
        weight += weightChildNodes(e).toDouble()
        return weight
    }

    /**
     * @param e
     * *
     * @return
     */
    private fun weightChildNodes(e: Element): Int {
        var weight = 0
        var caption: Element? = null
        val headerEls = ArrayList<Element>(5)
        val pEls = ArrayList<Element>(5)

        for (child in e.children()) {
            val ownText = child.ownText()
            val ownTextLength = ownText.length
            if (ownTextLength < 20)
                continue
            if (ownTextLength > 200)
                weight += Math.max(50, ownTextLength / 10)
            if (e.id().contains("caption") || e.className().contains("caption"))
                weight += 30
            if (child.tagName() == "h1" || child.tagName() == "h2") {
                weight += 30
            } else if (child.tagName() == "div" || child.tagName() == "p") {
                weight += calcWeightForChild(child, ownText)
                if (child.tagName() == "p" && ownTextLength > 50)
                    pEls.add(child)
                if (child.className().toLowerCase() == "caption")
                    caption = child
            }
        }

        // use caption and image
        if (caption != null)
            weight += 30
        if (pEls.size >= 2) {
            for (subEl in e.children()) {
                if ("h1;h2;h3;h4;h5;h6".contains(subEl.tagName())) {
                    weight += 20
                    headerEls.add(subEl)
                }
                if ("p".contains(subEl.tagName())) ScoreInfo.updateContentScore(subEl, 30.toDouble())
            }
            weight += 60
        }
        return weight
    }

    /**
     * @param child
     * @param ownText
     * @return
     */
    private fun calcWeightForChild(child: Element, ownText: String): Int {
        val `val`: Int
        val c = 0
        if (c > 5) `val` = -30
        else `val` = Math.round(ownText.length / 25.0).toInt()
        ScoreInfo.updateContentScore(child, `val`.toDouble())
        return `val`
    }

    /**
     * @param doc
     * @return
     */
    private fun getNodesToCheck(doc: Document): Collection<Element> {
        val nodesToCheck = LinkedHashMap<Element, Any?>(64)
        for (element in doc.select("body").select("*")) {
            if ("p;td;h1;h2;pre".contains(element.tagName())) {
                nodesToCheck.put(element, null)
            }
        }
        return nodesToCheck.keys
    }

    enum class ScoreTags {

        div, pre, td, blockquote, address, ol, ul, dl, dd, dt, li, form, h1, h2, h3, h4, h5, h6, th, UNKNOWN;

        companion object {
            fun getTagName(tag: String): ScoreTags {
                try {
                    return valueOf(tag)
                } catch (e: Exception) {
                    return UNKNOWN
                }
            }
        }
    }
}