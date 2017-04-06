package co.mailtarget.durian

import java.util.*

/**
 *
 * @author masasdani
 * @since 4/4/17
 */
data class WebPage constructor(var url: String, var title: String) {
    var description: String? = null
    var keywords: String? = null
    var favicon: String? = null
    var images: List<String> = emptyList()
    var publishedDate: Date? = null
    var content: String? = null
    var contentHtml: String? = null
}