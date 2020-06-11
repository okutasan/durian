package co.mailtarget.durian

import org.jsoup.Jsoup
import org.jsoup.nodes.Document


data class OpenGraph(
        val url: String, var title: String? = null, var description: String? = null,
        var image: String? = null, var type: String = "website", var video: String? = null,
        var productPrice: String? = null
)

fun metaSelector(property: String) = "meta[property~=$property]"

fun Document.getMetaProperty(name: String, attr: String = "content"): String? {
    val prop = select(metaSelector(name)) ?: return null
    return prop.attr(attr)
}

fun Document.openGraphData(url: String): OpenGraph {
    val openGraph = OpenGraph(url)
    openGraph.title = getMetaProperty("og:title")
    openGraph.description = getMetaProperty("og:description")
    openGraph.image = getMetaProperty("og:image")
    openGraph.type = getMetaProperty("og:type") ?: "website"
    val price = "${getMetaProperty("product:price:currency")} ${getMetaProperty("product:price:amount")}"
    openGraph.productPrice = price.trim()
    openGraph.video = getMetaProperty("og:video")
    return openGraph
}
