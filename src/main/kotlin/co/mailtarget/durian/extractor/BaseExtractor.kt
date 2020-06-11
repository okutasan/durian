package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
open class BaseExtractor {

    protected fun extractMeta(document: Document, selector: String, attr: String = "content"): String? {
        return document.select(selector).attr(attr)
    }

    protected fun extractMetas(document: Document, selector: String, attr: String = "content"): List<String> {
        val datas = ArrayList<String>()
        document.select(selector).mapTo(datas) { it.attr(attr) }
        return datas
    }
}