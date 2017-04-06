package co.mailtarget.durian.formatter

import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.util.ArrayList

/**
 * @author masasdani
 */
object ParagraphNormalization {

    fun normalizeParagraph(topElement: Element): Element {
        val elements = topElement.getElementsByTag("p")
        for (e in elements) {
            if (e.parent() != null) {

            } else {

            }
        }
        return topElement
    }

    fun normalizeSiblingParagraph(topElement: Element) {
        val pelements = topElement.getElementsByTag("p")
        val siblings = pelements[0].siblingElements()
        val stringBuilder = StringBuilder()
        //	stringBuilder.append("<p>\n");
        val tobeReplace = ArrayList<String>()
        for (i in siblings.indices) {
            val sib = siblings[i]
            if (sib.tag().toString() != "p") {
                stringBuilder.append(sib.outerHtml())
            } else {

            }
        }
        tobeReplace.add(stringBuilder.toString())
        if (tobeReplace.size > 0) {
            val inner = topElement.html()
            inner.replace(stringBuilder.toString().toRegex(), "<p>\n" + stringBuilder.toString() + "\n</p>")
            topElement.html(inner)
        }
    }

}
