package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
object ImageExtractor: BaseExtractor() {

    private val META_IMAGE = "meta[property~=image$]"
    private val BAD_IMAGE = ".html|.ico|button|twitter.jpg|facebook.jpg|digg.jpg|digg.png|delicious.png|facebook.png|reddit.jpg|doubleclick|diggthis|diggThis|adserver|/ads/|ec.atdmt.com" +
            "|mediaplex.com|adsatt|view.atdmt|reuters_fb_share.jpg" +
            "|twitter-login.png|google-plus.png" +
            "|gif$|gif\\?"
    private val JUNK_IMAGE = "d-logo-blue-100x100.png|WSJ_profile_lg.gif|dealbook75.gif|t_wb_75.gif|fivethirtyeight75.gif|current_issue.jpg|thecaucus75.gif"

    @JvmOverloads fun getImages(document: Document, contentElement: Element = document.body()): Set<String> {
        val images = HashSet<String>()

        return images
    }

    @JvmOverloads fun getImagesFromContent(document: Document, contentElement: Element = document.body()): Set<String> {
        val images = HashSet<String>()

        return images
    }

    fun getImagesFromMeta(document: Document): Set<String> {
        val images = HashSet<String>()
        extractMetas(document, META_IMAGE).filterNotTo(images) { it.matches(JUNK_IMAGE.toRegex()) }
        return images
    }
}