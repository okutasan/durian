package co.mailtarget.durian

import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/4/17
 */
data class WebPage constructor(var url: String, var title: String) {
    var description: String? = null
    var favicon: String? = null
    var image: String? = null
    var keywords: List<String> = emptyList()
    var publishedDate: String? = null
    var content: Element? = null
}

data class ProductPage constructor(var url: String, var title: String) {
    var description: String? = null
    var price: String? = null
    var image: String? = null
}