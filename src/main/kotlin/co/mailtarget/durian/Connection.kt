package co.mailtarget.durian

import com.machinepublishers.jbrowserdriver.JBrowserDriver
import com.machinepublishers.jbrowserdriver.Settings
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
    private val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36";

    @JvmOverloads fun getDocument(url: String, timeout: Int = CONNECTION_TIMEOUT): Document {
        try {
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(timeout)
                    .followRedirects(true)
                    .get()
        }catch (e: Exception) {
            throw DurianException(e.message)
        }
    }

    fun getDocument(url: String, html: String): Document {
        try {
            return Jsoup.parse(html, url)
        }catch (e: Exception) {
            throw DurianException(e.message)
        }
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