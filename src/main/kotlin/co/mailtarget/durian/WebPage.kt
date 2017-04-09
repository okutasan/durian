package co.mailtarget.durian

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
    var content: String? = null
    var contentHtml: String? = null
}