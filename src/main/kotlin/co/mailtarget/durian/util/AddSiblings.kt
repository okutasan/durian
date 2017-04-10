package co.mailtarget.durian.util

import org.jsoup.nodes.Element
import java.util.regex.Pattern

/**
 *
 * @author masasdani
 * @since 4/10/17
 */
object AddSiblings {

    val VIDEOS = Pattern.compile("http:\\/\\/(www\\.)?(youtube|vimeo|player\\.vimeo)\\.com")

    /**
     * adds any siblings that may have a decent score to this node
     * @param node
     * @return
     */
    fun addSiblings(node: Element): Element {
        val baselineScoreForSiblingParagraphs = getBaselineScoreForSiblings(node)
        var currentSibling: Element? = node.previousElementSibling()
        while (currentSibling != null) {
            if (currentSibling.tagName() == "p") {
                node.child(0).before(currentSibling.outerHtml())
                currentSibling = currentSibling.previousElementSibling()
                continue
            }

            // check for a paraph embedded in a containing element
            var insertedSiblings = 0
            val potentialParagraphs = currentSibling.getElementsByTag("p")
            if (potentialParagraphs.first() == null) {
                currentSibling = currentSibling.previousElementSibling()
                continue
            }
            for (firstParagraph in potentialParagraphs) {
                val wordStats = StopWords.getStopWordCount(firstParagraph.text())
                val paragraphScore = wordStats.stopWordCount
                if ((baselineScoreForSiblingParagraphs * .30).toFloat() < paragraphScore) {
                    node.child(insertedSiblings).before("<p>" + firstParagraph.html() + "<p>")
                    insertedSiblings++
                }
            }
            currentSibling = currentSibling.previousElementSibling()
        }

        val nextSibling = node.nextElementSibling()
        if (nextSibling != null) {
            //Elements iframeElements = nextSibling.getElementsByTag("iframe");
            val iframeElements = nextSibling.select("iframe|object")
            if (iframeElements.size > 0) {
                for (iframe in iframeElements) {
                    if (iframe.tagName() == "iframe") {
                        val srcAttribute = iframe.attr("src")
                        if (!(srcAttribute.isNullOrEmpty())) {
                            if (VIDEOS.matcher(srcAttribute).find()) {
                                node.appendElement("p").appendChild(iframe)
                            }
                        }
                    }
                    if (iframe.tagName() == "object") {
                        val embedElements = iframe.getElementsByTag("embed")
                        for (embedElement in embedElements) {
                            val embedSrc = embedElement.attr("src")
                            if (VIDEOS.matcher(embedSrc).find()) {
                                node.appendElement("p").appendChild(iframe)
                            }

                        }
                    }

                }
            }

            val baseLineScoreForNextSibling = getBaselineScoreForSiblings(nextSibling)
            val nextParaSiblings = nextSibling.getElementsByTag("p")
            if (nextParaSiblings.size > 0) {
                for (nextPara in nextParaSiblings) {
                    val nextParaWordStats = StopWords.getStopWordCount(nextPara.text())
                    val nextParaScore = nextParaWordStats.stopWordCount
                    if ((baseLineScoreForNextSibling * .30).toFloat() < nextParaScore) {
                        //node.appendElement("p").text(nextPara.text());
                        node.appendElement("p").html(nextPara.html())
                    }
                }
            }
        }


        return node
    }

    /**
     * we could have long articles that have tons of paragraphs so if we tried
     * to calculate the base score against the total text score of those
     * paragraphs it would be unfair. So we need to normalize the score based on
     * the average scoring of the paragraphs within the top node. For example if
     * our total score of 10 paragraphs was 1000 but each had an average value
     * of 100 then 100 should be our base.

     * @param topNode
     * *
     * @return
     */
    private fun getBaselineScoreForSiblings(topNode: Element): Int {
        var base = 100000
        var numberOfParagraphs = 0
        var scoreOfParagraphs = 0

        val nodesToCheck = topNode.getElementsByTag("p")

        for (node in nodesToCheck) {
            val nodeText = node.text()
            val wordStats = StopWords.getStopWordCount(nodeText)
            val highLinkDensity = isHighLinkDensity(node)

            if (wordStats.stopWordCount > 2 && !highLinkDensity) {
                numberOfParagraphs++
                scoreOfParagraphs += wordStats.stopWordCount
            }

        }

        if (numberOfParagraphs > 0) {
            base = scoreOfParagraphs / numberOfParagraphs
        }
        return base
    }

    /**
     * checks the density of links within a node, is there not much text and
     * most of it contains links? if so it's no good

     * @param e
     * *
     * @return
     */
    private fun isHighLinkDensity(e: Element): Boolean {
        val links = e.getElementsByTag("a")
        if (links.size == 0) {
            return false
        }

        val text = e.text().trim { it <= ' ' }
        val words = text.split(" ")
        val numberOfWords = words.size.toFloat()

        // let's loop through all the links and calculate the number of words
        // that make up the links
        val sb = StringBuilder()
        for (link in links) {
            sb.append(link.text())
        }
        val linkText = sb.toString()
        val linkWords = linkText.split(" ")
        val numberOfLinkWords = linkWords.size.toFloat()

        val numberOfLinks = links.size.toFloat()

        val linkDivisor = numberOfLinkWords / numberOfWords
        val score = linkDivisor * numberOfLinks

        if (score > 1) {
            return true
        }
        return false
    }

}
