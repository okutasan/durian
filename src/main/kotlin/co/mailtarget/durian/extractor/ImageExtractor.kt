package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
object ImageExtractor: BaseExtractor() {

    private val META_IMAGE = "meta[property~=image$],meta[name~=image$],meta[name~=Image$]"
    private val META_IMAGE_LINK = "link[rel~=image_src]"
    private val BAD_IMAGE = ".html|.ico|/favicon|button|digg.jpg|digg.png|delicious.png|reddit.jpg|doubleclick|diggthis|diggThis|adserver|/ads/|ec.atdmt.com" +
            "|mediaplex.com|adsatt|view.atdmt|reuters_fb_share.jpg" +
            "|twitter-login.png|google-plus.png" +
            "|gif$|gif\\?"
    private val JUNK_IMAGE = "d-logo-blue-100x100.png|WSJ_profile_lg.gif|dealbook75.gif|t_wb_75.gif|fivethirtyeight75.gif|current_issue.jpg|thecaucus75.gif"

    private val badImageMacther: Matcher = Pattern.compile(BAD_IMAGE).matcher("");
    private val junkImageMacther: Matcher = Pattern.compile(JUNK_IMAGE).matcher("");

    @JvmOverloads fun getImage(document: Document, url: URL, title: String, contentElement: Element = document.body()): String? {
        val image = getImageFromMeta(document)
        if(image.isNullOrEmpty()) return findBestImageURL(document, contentElement, url, title)
        return image
    }

    @JvmOverloads fun getImageFromContent(document: Document, url: URL, title: String, contentElement: Element = document.body()): String? {
        return findBestImageURL(document, contentElement, url, title)
    }

    fun getImageFromMeta(document: Document): String? {
        for (image in extractMetas(document, META_IMAGE)) {
            if(!image.contains(JUNK_IMAGE.toRegex()) && !image.contains(BAD_IMAGE.toRegex())) {
                return image
            }
        }
        for (image in extractMetas(document, META_IMAGE_LINK, "href")) {
            if(!image.contains(JUNK_IMAGE.toRegex()) && !image.contains(BAD_IMAGE.toRegex())) {
                return image
            }
        }
        return null
    }

    /**
     * @param topElement
     * @param document
     * @return
     */
    fun findBestImageURL(document: Document, topElement: Element, contextUrl: URL, title: String): String? {
        try {
            var bestImage: String? = null
            var element = topElement
            element = filterBadImages(element)
            val imgElements = element.getElementsByTag("img")
            val imageCandidates = ArrayList<String>()
            for (imgElement in imgElements) {
                var imgUrl = imgElement.attr("src")
                try {
                    imgUrl = URL(contextUrl, imgUrl).toString()
                } catch (e: MalformedURLException) {

                }
                imgElement.attr("src", imgUrl)
                imageCandidates.add(imgUrl)
            }

            // Setting the best image , in case still you need the top image.
            if (bestImage == null) {
                bestImage = getImageAltMatchTitle(document, contextUrl, title)
            }
            if (bestImage == null) {
                bestImage = getImageAfterTitle(document, contextUrl)
            }

            if ((bestImage.isNullOrEmpty())) {
                if (imageCandidates.size > 0) {
                    bestImage = imageCandidates[0]
                }
            }
            return bestImage
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * remove bad image in element
     *
     * @param topNode
     * @return
     */
    private fun filterBadImages(topNode: Element): Element {
        val topNodeImages = topNode.select("img")
        if (topNodeImages.size > 0) {
            for (imageElement in topNodeImages) {
                val imgSrc = imageElement.attr("src")
                if (imgSrc.isNullOrEmpty()) continue
                badImageMacther.reset(imgSrc)
                if (badImageMacther.find()) {
                    imageElement.parent()?.remove()
                }
            }
        }
        return topNode
    }

    /**
     * @param title
     * @return
     */
    private fun getImageAltMatchTitle(document: Document, contextUrl: URL, title: String): String? {
        if (!title.isNullOrEmpty()) {
            val imgElements = document.body().select("img[alt=$title]")
            if (imgElements.isNotEmpty()) {
                val imgSrc = imgElements[0].attr("src")
                try {
                    val imgUrl = URL(contextUrl, imgSrc).toString()
                    return imgUrl
                } catch (e: MalformedURLException) {
                    return null
                }
            }
        }
        return null
    }

    /**
     * @return
     */
    private fun getImageAfterTitle(document: Document, contextUrl: URL): String? {
        val h1Elements = document.body().getElementsByTag("h1")
        for (element in h1Elements) {
            val imgElements = element.parent().getElementsByTag("img")
            for (imgElement in imgElements) {
                val imgSrc = imgElement.attr("src")
                try {
                    val imgUrl = URL(contextUrl, imgSrc).toString()
                    return imgUrl
                } catch (e: MalformedURLException) {
                    return null
                }
            }
        }
        return null
    }
}