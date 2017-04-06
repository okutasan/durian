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
        assertEquals("#3 Weekly Sharing : Why Continuous Delivery ?", webData.title)
    }

}