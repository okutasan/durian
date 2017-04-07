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

    private val url = "https://blog.mailtarget.co/3-weekly-sharing-continuous-delivery/"

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
        println(webData.images.joinToString())
        assert(!webData.favicon.isNullOrEmpty())
        println(webData.favicon)
        assert(!webData.description.isNullOrEmpty())
        println(webData.description)
        assert(!webData.publishedDate.isNullOrEmpty())
        println(webData.publishedDate)
    }

}