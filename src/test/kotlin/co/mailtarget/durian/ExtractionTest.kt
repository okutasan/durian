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

    private val url = "https://blog.mailtarget.co/haruskah-ucapkan-selamat-tinggal-pada-inactive-subscriber/"

    @Test
    fun builderTest() {
        val extractor = buildExtractor(WebExtractor.Strategy.META, arrayListOf())
        assertEquals(WebExtractor.Strategy.META, extractor.strategy)
    }

    private fun buildExtractor(strategy: WebExtractor.Strategy, options: ArrayList<DocumentCleaner.Options>): WebExtractor {
        val extractor = WebExtractor.Builder
                .strategy(strategy)
                .cleanerOptions(options)
                .build()
        assertNotNull(extractor.cleaner)
        assert(!extractor.cleaner.options.isEmpty())
        return extractor
    }

    @Test
    fun metaExtractorTest(){
        extractorTest(WebExtractor.Strategy.META, arrayListOf())
    }

    @Test
    fun contentExtractorTest(){
        extractorTest(WebExtractor.Strategy.CONTENT, arrayListOf())
    }

    @Test
    fun hybridExtractorTest(){
        extractorTest(WebExtractor.Strategy.HYBRID, arrayListOf())
    }

    private fun extractorTest(strategy: WebExtractor.Strategy, options: ArrayList<DocumentCleaner.Options>) {
        val extractor = buildExtractor(strategy, options)
        val webData = extractor.extract(url)
        assert(!webData.title.isNullOrEmpty())
        println(webData.title)
        assert(!webData.image.isNullOrEmpty())
        println("images: ${webData.image}")
        assert(!webData.favicon.isNullOrEmpty())
        println("favicon: ${webData.favicon}")
        assert(!webData.description.isNullOrEmpty())
        println("desc: ${webData.description}")
        val content = webData.content?.text()
        assert(!content.isNullOrEmpty())
        println("content: $content")
    }

    @Test
    fun formatterTest() {
        val extractor = buildExtractor(WebExtractor.Strategy.HYBRID, arrayListOf())
        val webPage = extractor.extract(url)
        val formmattedDocument = DocumentFormatter().format(webPage)
        assert(!formmattedDocument.isNullOrEmpty())
        println(formmattedDocument)
    }


}