package co.mailtarget.durian

import jdk.nashorn.api.scripting.NashornScriptEngine
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.script.ScriptEngineManager
import java.io.InputStreamReader
import java.io.Reader


/**
 *
 * @author masasdani
 * @since 4/6/17
 */
open class Connection {

    private val CONNECTION_TIMEOUT = 30000
    private val USER_AGENT = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";

    @Throws(Exception::class)
    fun getDocument(url: String): Document {
        return getDocument(url, CONNECTION_TIMEOUT)
    }

    @Throws(Exception::class)
    fun getDocument(url: String, timeout: Int): Document {
        return Jsoup.connect(url).userAgent(USER_AGENT).timeout(timeout).get()
    }

    fun getNashorn(){
        val nashorn: NashornScriptEngine = ScriptEngineManager().getEngineByName("nashorn") as NashornScriptEngine
        nashorn.eval(read("nashorn-polyfill.js"));
        nashorn.eval(read("react.js"));
        nashorn.eval(read("showdown.js"));
        nashorn.eval(read("commentBox.js"));
    }

    fun read(path: String): Reader {
        val `in` = javaClass.classLoader.getResourceAsStream(path)
        return InputStreamReader(`in`)
    }
}