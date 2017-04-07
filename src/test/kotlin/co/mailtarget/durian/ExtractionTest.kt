package co.mailtarget.durian

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
class ExtractionTest {

    private val url = "https://news.detik.com/berita/d-3466341/polisi-pemilik-yang-aniaya-anjing-di-kebon-jeruk-bisa-dipidana?_ga=1.98411999.19828648.1488992470"

    @Test
    fun builderTest() {
        val extractor = WebExtractor.Builder
                .strategy(WebExtractor.Strategy.META)
                .withCleaner()
                .build()
        assertNotNull(extractor.cleaner)
        assertEquals(WebExtractor.Strategy.META, extractor.strategy)
    }

    @Test
    fun extractorText() {
        val extractor = WebExtractor.Builder
                .strategy(WebExtractor.Strategy.META)
                .withCleaner()
                .build()
        val webData = extractor.extractWebData(url)
        assert(!webData.title.isNullOrEmpty())
        println(webData.title)
        assert(webData.images.isNotEmpty())
        println("images: ${webData.images.joinToString()}")
        assert(!webData.favicon.isNullOrEmpty())
        println("favicon: ${webData.favicon}")
        assert(!webData.description.isNullOrEmpty())
        println("desc: ${webData.description}")
        assert(!webData.publishedDate.isNullOrEmpty())
        println("date: ${webData.publishedDate}")
    }

}