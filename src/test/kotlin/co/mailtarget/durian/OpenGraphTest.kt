package co.mailtarget.durian

import org.jsoup.Jsoup
import org.junit.Test

class OpenGraphTest {

    private val urlTokopedia = "https://www.tokopedia.com/darjeelingstore/essential-oil-nebulizer-baterai-tanpa-air-mobil-diffuser-aroma-go"

    @Test
    fun tokopediaTest() {
        val document =  Jsoup.parse(html)
        val openGraph = document.openGraphData(urlTokopedia)
        println(openGraph.title)
        println(openGraph.description)
        println(openGraph.image)
        println(openGraph.productPrice)
    }

}

val html = "<html>" +
        "<head>" +
        "<meta data-rh=\"true\" property=\"og:title\" content=\"Jual Essential Oil Nebulizer Baterai Tanpa Air Mobil Diffuser Aroma Go - Kota Bandung - Darjeeling Store | Tokopedia\">\n" +
        "<meta data-rh=\"true\" property=\"og:description\" content=\"Jual Essential Oil Nebulizer Baterai Tanpa Air Mobil Diffuser Aroma Go dengan harga Rp429.000 dari toko online Darjeeling Store, Kota Bandung. Cari produk Pengharum Ruangan lainnya di Tokopedia. Jual beli online aman dan nyaman hanya di Tokopedia.\">\n" +
        "<meta data-rh=\"true\" property=\"og:site_name\" content=\"Tokopedia\">\n" +
        "<meta data-rh=\"true\" property=\"og:url\" content=\"https://www.tokopedia.com/darjeelingstore/essential-oil-nebulizer-baterai-tanpa-air-mobil-diffuser-aroma-go\">\n" +
        "<meta data-rh=\"true\" property=\"og:image\" content=\"https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/5/15154422/15154422_4d584e59-ed5f-432a-8da4-bce40b243acd_750_750\">\n" +
        "<meta data-rh=\"true\" property=\"og:type\" content=\"product\">\n" +
        "<meta data-rh=\"true\" property=\"product:price:amount\" content=\"429000\">\n" +
        "<meta data-rh=\"true\" property=\"product:price:currency\" content=\"Rp\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:card\" content=\"product\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:site\" content=\"@tokopedia\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:creator\" content=\"@tokopedia\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:title\" content=\"Jual Essential Oil Nebulizer Baterai Tanpa Air Mobil Diffuser Aroma Go - Kota Bandung - Darjeeling Store | Tokopedia\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:description\" content=\"Jual Essential Oil Nebulizer Baterai Tanpa Air Mobil Diffuser Aroma Go dengan harga Rp429.000 dari toko online Darjeeling Store, Kota Bandung. Cari produk Pengharum Ruangan lainnya di Tokopedia. Jual beli online aman dan nyaman hanya di Tokopedia.\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:image\" content=\"https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/5/15154422/15154422_4d584e59-ed5f-432a-8da4-bce40b243acd_750_750\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:label1\" content=\"Harga\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:data1\" content=\"Rp429.000\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:label2\" content=\"Lokasi\">\n" +
        "<meta data-rh=\"true\" name=\"twitter:data2\" content=\"Kota Bandung\">\n" +
        "</head>" +
        "</html>"