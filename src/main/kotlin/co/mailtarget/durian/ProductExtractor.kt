package co.mailtarget.durian

import co.mailtarget.durian.extractor.TitleExtractor
import org.jsoup.nodes.Document

class ProductExtractor: Connection() {

    fun extract(url: String, html: String): ProductPage {
        val document: Document = if (html.isNotEmpty()) {
            getDocument(url, html)
        } else {
            getDocument(url)
        }
        return extractProduct(url, document)
    }

    private fun extractProduct(url: String, document: Document): ProductPage {
        val openGraph = document.openGraphData(url)
        val title = openGraph.title ?: TitleExtractor.getTitle(document, document.body())
        val page = ProductPage(url, title)
        page.description = openGraph.description
        page.price = openGraph.productPrice
        page.image = openGraph.image
        return page
    }
}