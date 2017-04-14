package co.mailtarget.durian

import com.machinepublishers.jbrowserdriver.JBrowserDriver
import com.machinepublishers.jbrowserdriver.Settings
import com.sun.org.apache.xpath.internal.operations.Bool
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.WebDriver
import java.util.logging.Level

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
open class Connection {

    val MAX_PROCESS = 1000
    val CONNECTION_TIMEOUT = 30000
    private val USER_AGENT = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";

    @Throws(Exception::class)
    @JvmOverloads fun getDocument(url: String, timeout: Int = CONNECTION_TIMEOUT): Document {
        return Jsoup.connect(url).userAgent(USER_AGENT).timeout(timeout).get()
    }

    @Throws(Exception::class)
    fun getDocument(url: String, html: String): Document {
        return Jsoup.parse(html, url)
    }

    fun getWebDriver(logging: Boolean): WebDriver {
        val settingBuilder = Settings.builder()
                .csrf()
                .headless(true)
                .javascript(true)
                .logJavascript(false)
                .ignoreDialogs(true)
                .cache(true)
                .processes(MAX_PROCESS)
                .maxConnections(MAX_PROCESS)
                .connectionReqTimeout(CONNECTION_TIMEOUT)
                .ajaxResourceTimeout(CONNECTION_TIMEOUT.toLong())
                .socketTimeout(CONNECTION_TIMEOUT)
                .connectTimeout(CONNECTION_TIMEOUT)
                .quickRender(true)
                .loggerLevel(if(logging) Level.WARNING else Level.OFF)
        return JBrowserDriver(settingBuilder.build())

    }
}