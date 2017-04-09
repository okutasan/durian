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
                .cleanerOptions(arrayListOf())
                .build()
        assertNotNull(extractor.cleaner)
        assertNotNull(extractor.cleaner.options)
        assertEquals(WebExtractor.Strategy.META, extractor.strategy)
    }

    @Test
    fun extractorText() {
        val extractor = WebExtractor.Builder
                .strategy(WebExtractor.Strategy.META)
                .build()
        val webData = extractor.extractWebData(url)
        assert(!webData.title.isNullOrEmpty())
        println(webData.title)
        assert(!webData.image.isNullOrEmpty())
        println("images: ${webData.image}")
        assert(!webData.favicon.isNullOrEmpty())
        println("favicon: ${webData.favicon}")
        assert(!webData.description.isNullOrEmpty())
        println("desc: ${webData.description}")
        assert(!webData.publishedDate.isNullOrEmpty())
        println("date: ${webData.publishedDate}")
    }

}